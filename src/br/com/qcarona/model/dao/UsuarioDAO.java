package br.com.qcarona.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.qcarona.model.Conexao;
import br.com.qcarona.model.Usuario;

public class UsuarioDAO {
	private Connection con;
	public UsuarioDAO() throws SQLException{
		//construtor da classe assim sempre vc vai ter uma conexao
		this.con = Conexao.getConnection();
	} 
	public List<Usuario> retornaConsultaClientes() throws SQLException{
		List<Usuario> lista = new ArrayList<Usuario>();
		PreparedStatement stm = this.con.prepareStatement("SELECT * FROM usuarios");
		ResultSet rs = stm.executeQuery();
		while(rs.next()){
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

	public Usuario realizaLogin(String usuario, String senha) throws SQLException{
		System.out.println("Usuario e senha para login: " + usuario + " " + senha);
		PreparedStatement stm = this.con.prepareStatement("SELECT * FROM usuarios WHERE email='" + usuario +"' AND senha='" + senha + "' LIMIT 1;");
		ResultSet rs = stm.executeQuery();
		if(rs.next()){
			Usuario user = new Usuario();
			user.setId(rs.getLong("id"));
			user.setNome(rs.getString("nome"));
			user.setSobreNome(rs.getString("sobrenome"));
			System.out.println("Nome: " + user.getNome() + " Id: " + user.getId());
			rs.close();
			stm.close();
			return user;
		}else{
			System.out.println("deu ruim aqui");
		}
		rs.close();
		stm.close();
		return null;
	}
}
