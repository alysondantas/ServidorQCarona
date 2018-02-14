/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.qcarona.model.dao;

import br.com.qcarona.model.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author marcos
 */
public class AmigosDAO {
    private Connection con;
    
    public AmigosDAO() throws SQLException {
        this.con = Conexao.getConnection();
    }

    public boolean inserirAmizade(String email1, String email2) {
        String sql = "insert into  amigos (idPrimUsuario, idSecUsuario) values(?, ?)";
        try {
            PreparedStatement stm = this.con.prepareStatement(sql);
            stm.setString(1, email1);
            stm.setString(2, email2);
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
}
