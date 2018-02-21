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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class CidadeDAO {

    private Connection con;

    public CidadeDAO() throws SQLException {
        this.con = Conexao.getConnection();
    }

    public List buscarCidadesDisponiveis() {
        int idCanora;
        PreparedStatement stm;
        List<String> cidades = new ArrayList<>();
        try {
            stm = this.con.prepareStatement("SELECT * FROM cidade where id_uf='5'");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                cidades.add(rs.getString("nome_cidade"));
            }
            rs.close();
            stm.close();
            return cidades;

        } catch (SQLException ex) {
            Logger.getLogger(SolicitacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int buscarIdCidade(String nome) {
        PreparedStatement stm;
        int idCidades = -1;
        try {
            stm = this.con.prepareStatement("SELECT * FROM cidade WHERE nome_cidade='" + nome + "';");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                idCidades = rs.getInt("id_cidade");
            }
            rs.close();
            stm.close();
            return idCidades;

        } catch (SQLException ex) {
            Logger.getLogger(SolicitacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

}
