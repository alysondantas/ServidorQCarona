package br.com.qcarona.controller;

import br.com.qcarona.model.Protocolo;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import br.com.qcarona.model.Usuario;
import br.com.qcarona.model.dao.UsuarioDAO;

public class ControllerDados {

    private static ControllerDados unicaInstancia;
    private UsuarioDAO userdao;

    private ControllerDados() throws SQLException {
        userdao = new UsuarioDAO();
    }

    /**
     * controla o instanciamento de objetos Controller
     *
     * @return unicaInstancia
     * @throws SQLException
     */
    public static synchronized ControllerDados getInstance() throws SQLException {
        if (unicaInstancia == null) {
            unicaInstancia = new ControllerDados();
        }
        return unicaInstancia;
    }

    /**
     * reseta o objeto Controller ja instanciado
     */
    public static void zerarSingleton() {
        unicaInstancia = null;
    }

    public String realizaLogin(String email, String senha) throws NoSuchAlgorithmException, SQLException {
        String senhaCrypt = geraMD5(senha);
        Usuario user = userdao.realizaLogin(email, senha);
        if (user == null) {
            return Protocolo.Notificacao.USUARIO_NAO_CADASTRADO+"";
        }
        return Protocolo.Notificacao.LOGIN_REALIZADO + "|" + user.getNome() + "|" + user.getId() + "|" + user.getSobreNome();
    }

    public void listarUsuarios() throws SQLException {
        userdao.retornaConsultaClientes();
    }

    public String geraMD5(String s) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(s.getBytes(), 0, s.length());
        return "" + new BigInteger(1, m.digest()).toString(16);
    }
}
