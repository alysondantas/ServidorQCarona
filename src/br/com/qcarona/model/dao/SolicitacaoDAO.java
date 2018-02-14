/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.qcarona.model.dao;

import br.com.qcarona.exception.JaExisteAmizadeException;
import br.com.qcarona.model.Conexao;
import br.com.qcarona.model.Usuario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class SolicitacaoDAO {

    private Connection con;

    public SolicitacaoDAO() throws SQLException {
        this.con = Conexao.getConnection();
    }

    public boolean inserirAmizade(Usuario user1, Usuario user2) throws JaExisteAmizadeException {
        if (verificarSolicitacao(user1, user2)) {
            throw new JaExisteAmizadeException();
        }
        String sql = "insert into solicitacaoAmizade (idUsuarioSolicitante, idUsuarioSolicitado, dataSolicitacao) values(?, ?, ?)";
        try {
            PreparedStatement stm = this.con.prepareStatement(sql);
            stm.setInt(1, Integer.parseInt(user1.getId().toString()));
            stm.setInt(2, Integer.parseInt(user2.getId().toString()));
            stm.setDate(3, new Date(Calendar.getInstance().getTimeInMillis()));
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean verificarSolicitacao(Usuario user1, Usuario user2) {
        PreparedStatement stm;
        try {
            stm = this.con.prepareStatement("SELECT * FROM solicitacaoAmizade WHERE idUsuarioSolicitante='" + user1.getId() + "' AND idUsuarioSolicitado='" + user2.getId() + "' OR  idUsuarioSolicitante='" + user2.getId() + "' AND idUsuarioSolicitado='" + user1.getId() + "' LIMIT 1;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                rs.close();
                stm.close();
                return true;
            } else {
                rs.close();
                stm.close();
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SolicitacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
}
