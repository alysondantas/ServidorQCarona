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
import java.sql.Date;
import java.util.Calendar;
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

    public boolean inserirCarona(String idUsuarioOfertante, String cidadeOrigem, String cidadeDestino, String data, String hora) {
        CidadeDAO cidadeDAO;
        String[] l = data.split("/");
        data = l[1] + "/" + l[0] + "/" + l[2];
        try {
            cidadeDAO = new CidadeDAO();
            int idOfer = Integer.parseInt(idUsuarioOfertante);
            int idCidadeOrigem = cidadeDAO.buscarIdCidade(cidadeOrigem);
            int idCidadeDestino = cidadeDAO.buscarIdCidade(cidadeDestino);
            java.util.Date d = new java.util.Date(data + " " + hora);
            java.sql.Date dataT = new java.sql.Date(d.getTime());

            String sql = "insert into  caronasOfertadas (idUsuarioOfertante, idCidadeOrigem, idCidadeDestino, dataHoraSaida, quantVagas) values(?,?,?,?,?)";

            PreparedStatement stm = this.con.prepareStatement(sql);
            stm.setInt(1, idOfer);
            stm.setInt(2, idCidadeOrigem);
            stm.setInt(3, idCidadeDestino);
            stm.setDate(4, dataT);
            stm.setInt(5, 1);
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CaronaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
            stm = this.con.prepareStatement("SELECT * FROM caronasOfertadas WHERE idCidadeOrigem='" + idCidadeOrigem + "' AND idCidadeDestino='" + idCidadeDestino + "' AND quantVagas > 0;");
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
                stm = this.con.prepareStatement("SELECT * FROM caronasOfertadas WHERE idCidadeOrigem='" + idOrigem + "' AND idCidadeDestino='" + idDestino + "' AND quantVagas > 0;");
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    caronas.add(new Carona(rs.getInt("idCaronasOfertadas"), rs.getInt("idCidadeOrigem"), rs.getInt("idCidadeDestino"), rs.getInt("idUsuarioOfertante"), rs.getDate("dataHoraSaida")));
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

    public Carona buscarCaronasPorID(int idCarona) {
        PreparedStatement stm;
        try {
            stm = this.con.prepareStatement("SELECT * FROM caronasOfertadas WHERE idCaronasOfertadas='" + idCarona + "' AND quantVagas > 0;");
            ResultSet rs = stm.executeQuery();
            Carona car;
            if (rs.next()) {
                car = new Carona(rs.getInt("idCaronasOfertadas"), rs.getInt("idCidadeOrigem"), rs.getInt("idCidadeDestino"), rs.getInt("idUsuarioOfertante"), rs.getDate("dataHoraSaida"));
                rs.close();
                stm.close();
                return car;
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
    public void atualizar(String campo, String valor, int idCarona) {
        PreparedStatement stm;
        try {
            stm = this.con.prepareStatement("UPDATE caronasOfertadas SET " + campo + "='" + valor + "' WHERE idCaronasOfertadas='" + idCarona + "';");
            ResultSet rs = stm.executeQuery();
            rs.close();
            stm.close();

        } catch (SQLException ex) {
            Logger.getLogger(CaronaDAO.class.getName()).log(Level.SEVERE, null, ex);    
        }
    }
}
