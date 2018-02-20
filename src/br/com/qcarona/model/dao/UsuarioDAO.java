package br.com.qcarona.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;

import br.com.qcarona.model.Conexao;
import br.com.qcarona.model.Usuario;

public class UsuarioDAO {

	private Connection con;

	public UsuarioDAO() throws SQLException {
		//construtor da classe assim sempre vc vai ter uma conexao
		this.con = Conexao.getConnection();
	}

	public List<Usuario> retornaConsultaClientes() throws SQLException {
		List<Usuario> lista = new ArrayList<Usuario>();
		PreparedStatement stm = this.con.prepareStatement("SELECT * FROM usuarios");
		ResultSet rs = stm.executeQuery();
		while (rs.next()) {
			Usuario cli = new Usuario();
			cli.setId(rs.getLong("id"));
			cli.setNome(rs.getString("nome"));
			cli.setSobreNome(rs.getString("sobrenome"));
			lista.add(cli);
			System.out.println("Nome: " + cli.getNome() + " Id: " + cli.getId());
		}
		rs.close();
		stm.close();
		return lista;
	}

	public Usuario realizaLogin(String usuario, String senha) throws SQLException {
		System.out.println("Usuario e senha para login: " + usuario + " " + senha);
		PreparedStatement stm = this.con.prepareStatement("SELECT * FROM usuarios WHERE email='" + usuario + "' AND senha='" + senha + "' LIMIT 1;");
		ResultSet rs = stm.executeQuery();
		if (rs.next()) {
			Usuario user = new Usuario();
			user.setId(rs.getLong("id"));
			user.setNome(rs.getString("nome"));
			user.setSobreNome(rs.getString("sobrenome"));
			System.out.println("Nome: " + user.getNome() + " Id: " + user.getId());
			rs.close();
			stm.close();
			return user;
		} else {
			System.out.println("deu ruim aqui");
		}
		rs.close();
		stm.close();
		return null;
	}

	public Usuario ObtemPerfil(int id) throws SQLException {
		PreparedStatement stm = this.con.prepareStatement("SELECT * FROM usuarios WHERE id='" + id + "' LIMIT 1;");
		ResultSet rs = stm.executeQuery();
		if (rs.next()) {
			Usuario user = new Usuario();
			user.setId(rs.getLong("id"));
			user.setNome(rs.getString("nome"));
			user.setSobreNome(rs.getString("sobrenome"));
			user.setEmail(rs.getString("email"));
			user.setData(rs.getString("data"));
			user.setNumero(rs.getString("numero"));
			user.setQualificacao(rs.getString("qualificacao"));
			System.out.println("Nome: " + user.getNome() + " Id: " + user.getId());
			rs.close();
			stm.close();
			return user;
		} else {
			System.out.println("deu ruim aqui");
		}
		rs.close();
		stm.close();
		return null;
	}

	public boolean realizaCadastro(String nome, String sobrenome, String email, String senha, String data, String tel, String cep) {
		// TODO Auto-generated method stub
		//String sql = "INSERT INTO usuarios (nome, sobrenome, email, senha, numero, cep, data) VALUES ('"+ nome +"', '"+ sobrenome +"', '"+ email +"', '"+ senha +"', '"+ tel +"', '"+ cep +"', '"+ data +"');";
		String sql = "Insert into  usuarios (nome, sobrenome, email, senha, numero, cep, data) values(?, ?, ?, ?,?,?,?)";
		//ResultSet rs = stm.executeQuery();
		//System.out.println("Chegou aqui");
		try {
			PreparedStatement stm = this.con.prepareStatement(sql);
			stm.setString(1, nome);
			stm.setString(2, sobrenome);
			stm.setString(3, email);
			stm.setString(4, senha);
			stm.setString(5, tel);
			stm.setString(6, cep);
			stm.setString(7, data);
			stm.executeUpdate();
			stm.close();

			return true;
			//JOptionPane.showMessageDialog(null,"Registro Inserido Com Sucesso","Mensagem do Programa",JOptionPane.INFORMATION_MESSAGE);
			//stm.close();
			//con.close();
			//return true;
		} catch (SQLException ex) {
			//ex.getMessage();
			//ex.printStackTrace();
			return false;
			//JOptionPane.showMessageDialog(null,"Conex�o n�o estabelecida Comandos Digitados Inv�lidos","Mensagem do Programa",JOptionPane.ERROR_MESSAGE);
		}

	}

	public Usuario buscarUsuarioEmail(String email) throws SQLException {
		PreparedStatement stm = this.con.prepareStatement("SELECT * FROM usuarios WHERE email='" + email + "' LIMIT 1;");
		ResultSet rs = stm.executeQuery();
		if (rs.next()) {
			Usuario user = new Usuario();
			user.setId(rs.getLong("id"));
			user.setNome(rs.getString("nome"));
			user.setSobreNome(rs.getString("sobrenome"));
			user.setEmail(rs.getString("email"));
			System.out.println("Nome: " + user.getNome() + " Id: " + user.getId());
			rs.close();
			stm.close();
			return user;
		} else {
			System.out.println("deu ruim aqui: Nao houve retorno na busca por email.");
		}
		rs.close();
		stm.close();
		return null;
	}

	public Usuario buscarUsuarioID(int idUsuario) throws SQLException {
		PreparedStatement stm = this.con.prepareStatement("SELECT * FROM usuarios WHERE id='" + idUsuario + "' LIMIT 1;");
		ResultSet rs = stm.executeQuery();
		if (rs.next()) {
			Usuario user = new Usuario();
			user.setId(rs.getLong("id"));
			user.setNome(rs.getString("nome"));
			user.setEmail(rs.getString("email"));
			user.setSobreNome(rs.getString("sobrenome"));
			System.out.println("Nome: " + user.getNome() + " Id: " + user.getId());
			rs.close();
			stm.close();
			return user;
		} else {
			System.out.println("deu ruim aqui: Nao houve retorno na busca por id.");
		}
		rs.close();
		stm.close();
		return null;
	}

	public PreparedStatement buscarAmigosID(int idUsuario) throws SQLException {
		String sql = "SELECT nome,email,id FROM usuarios INNER JOIN amigos ON idprimusuario = '"+ idUsuario+"' AND idsecusuario = id;";
		PreparedStatement stm = this.con.prepareStatement(sql);
		//ResultSet rs = stm.executeQuery();

		return stm;
		//stm.close();
		//return rs;
	}

	public boolean editaUsuario(String nome, String sobrenome, String email, String senha, String data, String tel, String id) throws SQLException{
		try {
			String sql = "UPDATE usuarios SET nome =?, sobrenome = ?,email = ?, senha = ?, data = ?, numero = ? WHERE id = '"+id +"'";
			//String sql = "UPDATE contato SET nome=?, sobrenome=? WHERE id=?";
			PreparedStatement pstm = this.con.prepareStatement(sql);
			pstm.setString(1, nome);
			pstm.setString(2, sobrenome);
			pstm.setString(3, email);
			pstm.setString(4, senha);
			pstm.setString(5, data);
			pstm.setString(6, tel);

			pstm.executeUpdate(); // executeUpdate toda vez que for utilizar DML (insert, update, delete)

			//JOptionPane.showMessageDialog(null, "Atualizado com Sucesso");

			pstm.close();
			return true;
		}catch(SQLException erro){
			erro.printStackTrace();
			return false;
		}
	}

	public boolean apagaAmigo(int id1, int id2){
		String sql = "DELETE FROM amigos WHERE idprimusuario = '"+id1+"' AND idsecusuario = '" +id2+ "';";
		try {
			PreparedStatement pstm = this.con.prepareStatement(sql);
			pstm.executeUpdate();
			return true;
		}catch(SQLException erro){
			erro.printStackTrace();
			return false;
		}
	}
}
