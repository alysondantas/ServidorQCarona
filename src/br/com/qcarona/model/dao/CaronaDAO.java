/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.qcarona.model.dao;

import br.com.qcarona.model.Carona;
import br.com.qcarona.model.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
     *
     * @param idUsuarioSolicitante
     * @param idCidadeOrigem
     * @param idCidadeDestino
     * @return
     */
    public List buscarCaronasDiponiveis(int idUsuarioSolicitante, int idCidadeOrigem, int idCidadeDestino) {
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

    public List buscarCaronasDiponiveis(int idCidadeOrigem, int idCidadeDestino) {
        int idCanora;
        PreparedStatement stm;
        try {
            stm = this.con.prepareStatement("SELECT * FROM caronasOfertadas WHERE idCidadeOrigem='" + idCidadeOrigem + "' AND idCidadeDestino='" + idCidadeDestino + "';");
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

    public List buscarCaronasDiponiveis(String cidadeOrigem, String cidadeDestino) {
        try {
            CidadeDAO cidadeDAO = new CidadeDAO();
            int idOrigem = cidadeDAO.buscarIdCidade(cidadeOrigem);
            int idDestino = cidadeDAO.buscarIdCidade(cidadeDestino);

            List<Carona> caronas = new ArrayList<>();
            PreparedStatement stm;
            try {
                stm = this.con.prepareStatement("SELECT * FROM caronasOfertadas WHERE idCidadeOrigem='" + idOrigem + "' AND idCidadeDestino='" + idDestino + "';");
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    caronas.add(new Carona(rs.getInt("idCaronasOfertadas"),rs.getInt("idCidadeOrigem"), rs.getInt("idCidadeDestino"), rs.getInt("idUsuarioOfertante"), rs.getDate("dataHoraSaida")));
                }
                rs.close();
                stm.close();
                return caronas;

            } catch (SQLException ex) {
                Logger.getLogger(CaronaDAO.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaronaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
