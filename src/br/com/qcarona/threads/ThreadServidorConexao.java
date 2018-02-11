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
import br.com.qcarona.model.Protocolo;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class ThreadServidorConexao extends Thread{
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
				case 0://realizar login
					String email = informacoes[1];//recebe as informaï¿½ï¿½es para cadastro
					String senha = informacoes[2];

					String result = controller.realizaLogin(email, senha);
					if(result.equals(Protocolo.Notificacao.USUARIO_NAO_CADASTRADO+"") || result.equals("2")){
						s = "Tentativa falha de realizar login em: " + email;
					}else{
						s = "Novao usuario realizando login: " + email + " com sucesso\n";//string de log
					}
					saida.writeObject(result);
					saida.flush();
					break;
				case 1://cadastrar
					String nomeCad = informacoes[1];
					String sobrenomeCad = informacoes[2];
					String emailCad = informacoes[3];
					String senhaCad = informacoes[4];
					String dataCad = informacoes[5];
					String telCad = informacoes[6];
					String cepCad = informacoes[7];
					boolean b = controller.cadastra(nomeCad, sobrenomeCad, emailCad, senhaCad, dataCad, telCad, cepCad);
					if(b){
						s = "Novo cadastro realizado em: " + emailCad;
						saida.writeObject("103");
					}else{
						s = "Tentativa falha de cadastrar: " + emailCad;
						saida.writeObject("100");
					}
					saida.flush();
					break;
				}
				System.out.println("\nCliente atendido com sucesso: " + s + cliente.getRemoteSocketAddress().toString());
				textField.setText(textField.getText() + "\nCliente atendido com sucesso: " + s + cliente.getRemoteSocketAddress().toString());//coloca o log no textArea
			}else{
				System.out.println("\nObjeto recebiddo não corresponde " + cliente.getRemoteSocketAddress().toString());
				textField.setText(textField.getText() + "\nObjeto recebiddo não corresponde " + cliente.getRemoteSocketAddress().toString());//coloca o log no textArea

			}
			entrada.close();//finaliza a entrada
			saida.close();//finaliza a saida
			cliente.close();//fecha o cliente
		} catch (SocketException e) {
			System.out.println("Filanizou o atendimento.");
			textField.setText(textField.getText() + "\nAtendimento foi finalizado.");//caso alguma exceï¿½ï¿½o desconheciada seja lanï¿½ada ela encerra a thread e ï¿½ exibida
			try {
				cliente.close();   //finaliza o cliente
			} catch (Exception ec) {
				textField.setText(textField.getText() + "\nErro fatal cliente nï¿½o finalizado: " + ec.getMessage());//cliente nï¿½o foi finalizado
			}
		} catch (Exception e) {//caso alguma exceï¿½ï¿½o seja lanï¿½ada
			e.printStackTrace();
			System.out.println("Excecao ocorrida na thread: " + e);
			textField.setText(textField.getText() + "\nExcecao ocorrida na thread: " + e.getMessage());//caso alguma exceï¿½ï¿½o desconheciada seja lanï¿½ada ela encerra a thread e ï¿½ exibida
			try {
				cliente.close();   //finaliza o cliente
			} catch (Exception ec) {
				textField.setText(textField.getText() + "\nErro fatal cliente nï¿½o finalizado: " + ec.getMessage());//cliente nï¿½o foi finalizado
			}
		}
	}
}
