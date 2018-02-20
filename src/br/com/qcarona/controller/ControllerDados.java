package br.com.qcarona.controller;

import br.com.qcarona.model.Protocolo;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

	public String obterPerfil(String idS) throws SQLException{
		int id = Integer.parseInt(idS);
		Usuario user = userdao.ObtemPerfil(id);
		if (user == null) {
			return Protocolo.Notificacao.USUARIO_NAO_CADASTRADO+"";
		}
		return Protocolo.Notificacao.LOGIN_REALIZADO + "|" + user.getNome() + "|" + user.getSobreNome() +"|" + user.getData() + "|" + user.getEmail() + "|" + user.getNumero() + "|" + user.getQualificacao() + "|" + user.getId();
	}

	public void listarUsuarios() throws SQLException {
		userdao.retornaConsultaClientes();
	}

	public String geraMD5(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		return "" + new BigInteger(1, m.digest()).toString(16);
	}

	public boolean cadastra(String nome, String sobrenome, String email, String senha, String data, String tel, String cep){
		boolean b = false;
		if(nome != null && email !=null && senha!=null){
			b = userdao.realizaCadastro(nome, sobrenome, email, senha, data, tel, cep);
		}

		return b;

	} 

	public boolean editar(String nome, String sobrenome, String email, String senha, String data, String tel, String id) throws SQLException{
		boolean b = userdao.editaUsuario(nome, sobrenome, email, senha, data, tel, id);
		return b;
	}

	public String buscarAmigos(int id){
		PreparedStatement stm;
		try {
			stm = userdao.buscarAmigosID(id);
			ResultSet rs = stm.executeQuery();
			String s = "";
			boolean b = false;
			while(rs.next()){
				if(b){
					s = s + "&";
				}else{
					b = true;
				}
				s = s + rs.getLong("id") + "/" + rs.getString("nome") + "/" + rs.getString("email");
			}
			stm.close();
			System.out.println(s);
			return s;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public Usuario buscarUsuario(String email) throws SQLException{
		if(!email.trim().equals("")){
			return userdao.buscarUsuarioEmail(email);
		}
		return null;
	}

	public boolean solicitarAmizade(int id){
		return true;
	}

	public boolean solicitarAmizade(String email){
		return true;
	}
}
