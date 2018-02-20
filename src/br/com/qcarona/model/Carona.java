/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.qcarona.model;

import java.util.Date;

/**
 *
 * @author marco
 */
public class Carona {
    private int idCarona;
    private int cidadeOrigem;
    private int cidadeDestino;
    private int idUsuarioOfertante;
    private Date horarioCarona;

    public Carona(int idCarona, int cidadeOrigem, int cidadeDestino, int idUsuarioOfertante, Date horarioCarona) {
        this.horarioCarona = horarioCarona;
        this.idCarona = idCarona;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.idUsuarioOfertante = idUsuarioOfertante;
    }

    public int getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(int cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public int getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(int cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public int getIdUsuarioOfertante() {
        return idUsuarioOfertante;
    }

    public void setIdUsuarioOfertante(int idUsuarioOfertante) {
        this.idUsuarioOfertante = idUsuarioOfertante;
    }

    public int getIdCarona() {
        return idCarona;
    }

    public void setIdCarona(int idCarona) {
        this.idCarona = idCarona;
    }

    public Date getHorarioCarona() {
        return horarioCarona;
    }

    public void setHorarioCarona(Date horarioCarona) {
        this.horarioCarona = horarioCarona;
    }
    
    
}
