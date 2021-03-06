package br.com.qcarona.threads;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import br.com.qcarona.controller.ControllerDados;
import br.com.qcarona.exception.JaExisteAmizadeException;
import br.com.qcarona.model.Carona;
import br.com.qcarona.model.Protocolo;
import br.com.qcarona.model.Usuario;
import br.com.qcarona.model.dao.CaronaAndamentoDAO;
import br.com.qcarona.model.dao.CaronaDAO;
import br.com.qcarona.model.dao.CidadeDAO;
import br.com.qcarona.model.dao.SolicitacaoDAO;
import br.com.qcarona.model.dao.UsuarioDAO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

public class ThreadServidorConexao extends Thread {

    private Socket cliente;//socket do cliente
    private ServerSocket server;//socket do servidor
    private JTextArea textField;//para atualizar a interface
    private ControllerDados controller;//instancia do controller

    public ThreadServidorConexao(ServerSocket server, JTextArea textField, Socket cliente) {//recebe o socket server e o textArea
        this.server = server;
        this.cliente = cliente;
        this.textField = textField;
        try {
            controller = ControllerDados.getInstance();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //Inicia thread do cliente aceitando clientes

            //ObjectInputStream para receber o nome do arquivo
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());//cria um objeto de entrada
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());//cria um objeto de saida
            String pack = null;
            Object object = entrada.readObject();
            System.out.println("Novo recebdido");
            String recebido;
            if ((object != null) && (object instanceof String)) {
                pack = (String) object;

                String informacoes[] = pack.split(Pattern.quote("|"));
                //System.out.println(cliente.getRemoteSocketAddress().toString() + " enviou " + pack);
                int opcao = Integer.parseInt(informacoes[0]);//recebe a opcao que o cliente mandou
                String s = "erro";//string de log com erro
                switch (opcao) {
                    case Protocolo.Solicitacao.FAZER_LOGIN://realizar login
                        String email = informacoes[1].trim();//recebe as informa��es para cadastro
                        String senha = informacoes[2].trim();

                        String result = controller.realizaLogin(email, senha);
                        if (result.equals(Protocolo.Notificacao.USUARIO_NAO_CADASTRADO + "") || result.equals("2")) {
                            s = "Tentativa falha de realizar login em: " + email;
                        } else {
                            s = "Novao usuario realizando login: " + email + " com sucesso\n";//string de log
                        }
                        saida.writeObject(result);
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.CADASTRA_USUARIO://cadastrar
                        String nomeCad = informacoes[1].trim();
                        String sobrenomeCad = informacoes[2].trim();
                        String emailCad = informacoes[3].trim();
                        String senhaCad = informacoes[4].trim();
                        String dataCad = informacoes[5].trim();
                        String telCad = informacoes[6].trim();
                        String cepCad = informacoes[7].trim();
                        boolean b = controller.cadastra(nomeCad, sobrenomeCad, emailCad, senhaCad, dataCad, telCad, cepCad);
                        if (b) {
                            s = "Novo cadastro realizado em: " + emailCad;
                            saida.writeObject("103");
                        } else {
                            s = "Tentativa falha de cadastrar: " + emailCad;
                            saida.writeObject("100");
                        }
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.BUSCAR_USUARIO_EMAIL:
                        if (informacoes.length > 1 && informacoes[1] != null) {
                            String emailBusca = informacoes[1].trim();
                            Usuario user = controller.buscarUsuario(emailBusca);
                            if (user != null) {
                                String envio = Protocolo.Notificacao.RETORNO_BUSCA_EMAIL + "|" + user.getId() + "|" + user.getNome() + "|" + user.getSobreNome();
                                saida.writeObject(envio);
                            } else {
                                saida.writeObject(Protocolo.Notificacao.RETORNO_BUSCA_EMAIL + "|ERRO");
                            }
                        } else {
                            saida.writeObject(Protocolo.Notificacao.RETORNO_BUSCA_EMAIL + "|ERRO");
                        }
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.SOLICITAR_AMIZADE:
                        if (informacoes[1] != null) {
                            UsuarioDAO userDAO = new UsuarioDAO();
                            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
                            Usuario user1 = userDAO.buscarUsuarioEmail(informacoes[1].trim());
                            Usuario user2 = userDAO.buscarUsuarioID(Integer.parseInt(informacoes[2].trim()));
                            try {
                                boolean resulta = solicitacaoDAO.inserirAmizade(user1, user2);
                                if (resulta) {
                                    String envio = Protocolo.Notificacao.SOLICITACAO_AMIZ_ENVIADA + "|";
                                    saida.writeObject(envio);
                                }
                            } catch (JaExisteAmizadeException e) {
                                String envio = Protocolo.Notificacao.JA_EXISTE_SOLICITACAO_AMIZ + "|";
                                saida.writeObject(envio);
                            }
                        }
                        break;
                    case Protocolo.Solicitacao.CIDADES_DISPONIVEIS:
                        CidadeDAO cidadeDAO = new CidadeDAO();
                        List<String> cidades = cidadeDAO.buscarCidadesDisponiveis();
                        String retorno = new String();
                        for (String cidade : cidades) {
                            retorno += cidade + ";";
                        }
                        String envio = Protocolo.Notificacao.RETORNO_CIDADES_DISPONIVEIS + "|" + retorno;
                        saida.writeObject(envio);
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.CARONAS_DISPONIVEIS:
                        CaronaDAO caronaDAO = new CaronaDAO();
                        UsuarioDAO usuarioDAO = new UsuarioDAO();

                        String cidadeOrigem = informacoes[1];
                        String cidadeDestino = informacoes[2];
                        List<Carona> caronas = caronaDAO.buscarCaronasDiponiveis(cidadeOrigem.trim(), cidadeDestino.trim());
                        String forEnvio = Protocolo.Notificacao.CARONA_DISPONIVEL + "|";
                        String caronasString = "";
                        for (Carona carona : caronas) {
                            caronasString += carona.getIdCarona() + "," + usuarioDAO.buscarUsuarioID(carona.getIdUsuarioOfertante()).getNome() + "," + carona.getHorarioCarona() + ";";
                        }
                        saida.writeObject(forEnvio + caronasString);
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.SUBMETER_CARONA:
                        String id = informacoes[1].trim();
                        String cidadeOrigemT = informacoes[2].trim();
                        String cidadeDestinoT = informacoes[3].trim();
                        String data = informacoes[4].trim();
                        String hora = informacoes[5].trim();
                        CaronaDAO caronaDAO1 = new CaronaDAO();
                        caronaDAO1.inserirCarona(id, cidadeOrigemT, cidadeDestinoT, data, hora);
                        String envio1 = Protocolo.Notificacao.RESPOSTA_SUBMETER_CARONA + "|";
                        saida.writeObject(envio1);
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.CONFIRMAR_CARONA:
                        CaronaDAO caronaDAO2 = new CaronaDAO();
                        CaronaAndamentoDAO caronaAndamentoDAO = new CaronaAndamentoDAO();

                        int idAproveitador = Integer.parseInt(informacoes[1].trim());
                        int idCarona = Integer.parseInt(informacoes[2].trim());
                        Carona c = caronaDAO2.buscarCaronasPorID(idCarona);
                        if (c == null) {
                            saida.writeObject("|ERRO");
                            saida.flush();
                            break;
                        }
                        int idOfertante = c.getIdUsuarioOfertante();

                        caronaAndamentoDAO.insertCaronaAndamento(idOfertante, idAproveitador, idCarona);
                        String envio2 = Protocolo.Notificacao.RESPOSTA_CONFIRMAR_CARONA + "|";
                        saida.writeObject(envio2);
                        saida.flush();
                        break;
                       
                    case Protocolo.Solicitacao.CARONAS_ANDAMENTO:
                        int idUsuario = Integer.parseInt(informacoes[1].trim());
                        CaronaAndamentoDAO caronaAndamentoDAO1 = new CaronaAndamentoDAO();
                        UsuarioDAO usuarioDAO1 = new UsuarioDAO();
                        List<String> resul = caronaAndamentoDAO1.buscarCaronasAndamentoIDUSuario(idUsuario);
                        String resp = Protocolo.Notificacao.RESPOSTA_CARONAS_ANDAMENTOS+"|";
                        String texto = "";
                        for (String string : resul) {
                            String[] a = string.split(",");
                            texto += a[0] + "," + usuarioDAO1.buscarUsuarioID(idUsuario).getNome()+";";
                        }
                        saida.writeObject(resp + texto);
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.EDITAR_PERFIL:
                        String nomeEdit = informacoes[1].trim();
                        String sobrenomeEdit = informacoes[2].trim();
                        String emailEdit = informacoes[3].trim();
                        String senhaEdit = informacoes[4].trim();
                        String dataEdit = informacoes[5].trim();
                        String telEdit = informacoes[6].trim();
                        String idEdit = informacoes[7].trim();
                        boolean b2 = controller.editar(nomeEdit, sobrenomeEdit, emailEdit, senhaEdit, dataEdit, telEdit, idEdit);
                        if (b2) {
                            s = "Nova edi��o realizada em: " + emailEdit;
                            saida.writeObject("103");
                        } else {
                            s = "Tentativa falha de editar: " + emailEdit;
                            saida.writeObject("100");
                        }
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.OBTEM_PERFIL:
                        String idObtido = informacoes[1].trim();
                        String resultObter = controller.obterPerfil(idObtido);
                        if (resultObter.equals(Protocolo.Notificacao.USUARIO_NAO_CADASTRADO + "")) {
                            s = "Erro ao tentar obter perfil";
                        } else {
                            s = "Solicita��o de obter perfil concluida";
                        }
                        /*if (b2) {
                            s = "Nova Obten��o de perfil realizada em: " + idObtido;
                            saida.writeObject(resultObter);
                        } else {
                            s = "Tentativa falha de editar: " + emailEdit;
                            saida.writeObject("100");
                        }*/
                        saida.writeObject(resultObter);
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.BUSCAR_AMIGOS:
                        if (informacoes.length > 1 && informacoes[1] != null) {
                            String idBuscaAmigosS = informacoes[1].trim();
                            int idBuscaAmigos = Integer.parseInt(idBuscaAmigosS);
                            String resultBA = controller.buscarAmigos(idBuscaAmigos);
                            if (resultBA != null) {
                                if (resultBA.equals("")) {
                                    s = "Nova busca de amigos realiza, sem amigos";
                                    saida.writeObject(Protocolo.Notificacao.RETORNO_BUSCA_EMAIL + "|ERRO");
                                } else {
                                    String envioBA = Protocolo.Notificacao.RETORNO_BUSCA_AMIGOS + "|" + resultBA;
                                    s = "Nova busca de amigos realizada";
                                    saida.writeObject(envioBA);
                                }
                            } else {
                                s = "ERRO FATAL ao buscar amigos";
                                saida.writeObject(Protocolo.Notificacao.RETORNO_BUSCA_EMAIL + "|ERRO");
                            }
                        } else {
                            saida.writeObject(Protocolo.Notificacao.RETORNO_BUSCA_EMAIL + "|ERRO");
                        }
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.DESFAZ_AMIGO:
                        String idprinc = informacoes[1].trim();
                        String idsec = informacoes[2].trim();
                        boolean bDesfaz = controller.obterDesfazAmigo(idprinc, idsec);
                        String resultDesfaz;
                        if (bDesfaz) {
                            resultDesfaz = Protocolo.Notificacao.OPERACAO_CONCLUIDA + "|" + idsec;
                        } else {
                            resultDesfaz = Protocolo.Notificacao.OPERACAO_NAO_CONCLUIDA + "|" + idsec;
                        }
                        if (resultDesfaz.equals(Protocolo.Notificacao.OPERACAO_CONCLUIDA + "")) {
                            s = "Nova solicita��o de amizade desfeita.";
                        } else {
                            s = "ERRO ao desfazer solicita��o de amizade";
                        }
                        saida.writeObject(resultDesfaz);
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.BUSCA_SOLICITACAO_AMIZADE:
                        if (informacoes.length > 1 && informacoes[1] != null) {
                            String idBuscaSolicitacaoS = informacoes[1].trim();
                            int idBuscaSolicitacao = Integer.parseInt(idBuscaSolicitacaoS);
                            String resultBSA = controller.buscarSolicitacaoAmigos(idBuscaSolicitacao);
                            if (resultBSA != null) {
                                if (resultBSA.equals("")) {
                                    s = "Nova busca de solicitacoes de amigos realiza, sem amigos";
                                    saida.writeObject(Protocolo.Notificacao.OPERACAO_NAO_CONCLUIDA + "|ERRO");
                                } else {
                                    String envioSA = Protocolo.Notificacao.RETORNO_BUSCA_AMIGOS + "|" + resultBSA;
                                    s = "Nova busca de solicitacoes de amigos realizada";
                                    saida.writeObject(envioSA);
                                }
                            } else {
                                s = "ERRO FATAL ao buscar solicitacao amizade";
                                saida.writeObject(Protocolo.Notificacao.OPERACAO_NAO_CONCLUIDA + "|ERRO");
                            }
                        } else {
                            saida.writeObject(Protocolo.Notificacao.OPERACAO_NAO_CONCLUIDA + "|ERRO");
                        }
                        saida.flush();
                        break;
                    case Protocolo.Solicitacao.ACEITA_SOLICITACAO:
                        if (informacoes.length > 1 && informacoes[1] != null) {

                            String idSolicitadoS = informacoes[1].trim();
                            String idAmigoS = informacoes[2].trim();
                            String idSolicitacaoS = informacoes[3].trim();
                            int idSolicitado = Integer.parseInt(idSolicitadoS);
                            int idAmigo = Integer.parseInt(idAmigoS);
                            int idSolicicao = Integer.parseInt(idSolicitacaoS);
                            boolean bACEITA = controller.aceitaAmigo(idSolicitado, idAmigo, idSolicicao);
                            if (bACEITA) {
                                s = "Nova busca de solicitacoes de amigos realiza, sem amigos";
                                saida.writeObject(Protocolo.Notificacao.OPERACAO_CONCLUIDA + "|OK");
                            } else {
                                String envioAS = Protocolo.Notificacao.JA_EXISTE_SOLICITACAO_AMIZ + "|" + "ERRO";
                                s = "Nova busca de solicitacoes de amigos realizada";
                                saida.writeObject(envioAS);
                            }

                        } else {
                            saida.writeObject(Protocolo.Notificacao.OPERACAO_NAO_CONCLUIDA + "|ERRO");
                        }
                        saida.flush();
                        break;
                }
                System.out.println("\nCliente atendido com sucesso: " + s + cliente.getRemoteSocketAddress().toString());
                textField.setText(textField.getText() + "\nCliente atendido com sucesso: " + s + cliente.getRemoteSocketAddress().toString());//coloca o log no textArea
            } else {
                System.out.println("\nObjeto recebiddo n�o corresponde " + cliente.getRemoteSocketAddress().toString());
                textField.setText(textField.getText() + "\nObjeto recebiddo n�o corresponde " + cliente.getRemoteSocketAddress().toString());//coloca o log no textArea

            }
            entrada.close();//finaliza a entrada
            saida.close();//finaliza a saida
            cliente.close();//fecha o cliente
        } catch (SocketException e) {
            System.out.println("Filanizou o atendimento.");
            textField.setText(textField.getText() + "\nAtendimento foi finalizado.");//caso alguma exce��o desconheciada seja lan�ada ela encerra a thread e � exibida
            try {
                cliente.close();   //finaliza o cliente
            } catch (Exception ec) {
                textField.setText(textField.getText() + "\nErro fatal cliente n�o finalizado: " + ec.getMessage());//cliente n�o foi finalizado
            }
        } catch (Exception e) {//caso alguma exce��o seja lan�ada
            e.printStackTrace();
            System.out.println("Excecao ocorrida na thread: " + e);
            textField.setText(textField.getText() + "\nExcecao ocorrida na thread: " + e.getMessage());//caso alguma exce��o desconheciada seja lan�ada ela encerra a thread e � exibida
            try {
                cliente.close();   //finaliza o cliente
            } catch (Exception ec) {
                textField.setText(textField.getText() + "\nErro fatal cliente n�o finalizado: " + ec.getMessage());//cliente n�o foi finalizado
            }
        }
    }
}
