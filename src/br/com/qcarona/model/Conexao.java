package br.com.qcarona.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Conexao {

    String driver = "org.postgresql.Driver";
    String user = "postgres";
    static String senha = "12345";
    String url = "jdbc:postgresql://localhost:5432/jaime";

    public Conexao() {

    }

    public boolean realizaconexao() {
        try {
            Class.forName(driver);
            Connection con = null;
            con = (Connection) DriverManager.getConnection(url, user, senha);
            System.out.println("Conex�o realizada com sucesso.");
            return true;
        } catch (ClassNotFoundException ex) {
            System.err.print(ex.getMessage());
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return false;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/qcarona?user=postgres&password=" + Conexao.senha);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e);
            throw new SQLException();
        }
    }

}
