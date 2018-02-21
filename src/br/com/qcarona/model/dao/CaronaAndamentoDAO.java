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
public class CaronaAndamentoDAO {

    private Connection con;

    public CaronaAndamentoDAO() throws SQLException {
        this.con = Conexao.getConnection();
    }

    public boolean insertCaronaAndamento(int idOfertante, int idAproveitador, int idCaronasOfertadas) {
        try {
            CaronaDAO caronaDAO = new CaronaDAO();
            caronaDAO.atualizar("quantVagas", "0", idCaronasOfertadas);

            String sql = "insert into  caronasEmAndamento (idOfertante, idAproveitador, idCaronasOfertadas) values(?,?,?)";

            PreparedStatement stm = this.con.prepareStatement(sql);
            stm.setInt(1, idOfertante);
            stm.setInt(2, idAproveitador);
            stm.setInt(3, idCaronasOfertadas);
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CaronaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean removerCaronaAndamento(int idCarona) {
        try {
            String sql = "DELETE FROM  caronasEmAndamento WHERE idCaronaAndamento='" + idCarona + "'";
            PreparedStatement stm = this.con.prepareStatement(sql);
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CaronaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<String> buscarCaronasAndamentoIDUSuario(int idUser) {
        List<String> caronas = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = this.con.prepareStatement("SELECT * FROM caronasEmAndamento WHERE idAproveitador='" + idUser + "';");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                caronas.add(rs.getInt("idOfertante") + ", " + rs.getInt("idCaronasOfertadas"));
            }
            rs.close();
            stm.close();
            return caronas;

        } catch (SQLException ex) {
            Logger.getLogger(CaronaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
