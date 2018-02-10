/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.qcarona.model;

/**
 *
 * @author marco
 */
public abstract class Protocolo {
    
     public abstract class Solicitacao{
        public static final int FAZER_LOGIN = 0;
        public static final int CADASTRA_USUARIO = 1;
    }

    public abstract class Notificacao {
        public static final int USUARIO_NAO_CADASTRADO = 100;
        public static final int SENHA_INCORRETA = 101;
        public static final int LOGIN_REALIZADO = 102;
        public static final int USUARIO_CADASTRADO = 103;
    }
}
