/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.qcarona.model.dao;

import br.com.qcarona.model.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class CaronaDAO {
     private Connection con;

    public CaronaDAO() throws SQLException {
        this.con = Conexao.getConnection();
    }
    
    /**
     * Retona o id da carona disponivel
     * @param idUsuarioSolicitante
     * @param idCidadeOrigem
     * @param idCidadeDestino
     * @return 
     */
    public List buscarCaronasDiponiveis(int idUsuarioSolicitante, int idCidadeOrigem, int idCidadeDestino){
        int idCanora;
        PreparedStatement stm;
        try {
            stm = this.con.prepareStatement("SELECT * FROM caronasOfertadas WHERE idCidadeOrigem='" + idCidadeOrigem + "' AND idCidadeDestino='" + idCidadeDestino + "' AND  idUsuarioOfertante=;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                rs.close();
                stm.close();
                return null;
            } else {
                rs.close();
                stm.close();
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SolicitacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
