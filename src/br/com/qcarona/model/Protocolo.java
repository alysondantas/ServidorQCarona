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
        public static final int BUSCAR_USUARIO_EMAIL = 2;
        public static final int SOLICITAR_AMIZADE = 3;
        public static final int CARONAS_DISPONIVEIS = 4;
        public static final int EDITAR_PERFIL = 5;
    }

    public abstract class Notificacao {
        public static final int USUARIO_NAO_CADASTRADO = 100;
        public static final int SENHA_INCORRETA = 101;
        public static final int LOGIN_REALIZADO = 102;
        public static final int USUARIO_CADASTRADO = 103;
        public static final int RETORNO_BUSCA_EMAIL = 104;
        public static final int SOLICITACAO_AMIZ_ENVIADA = 105;
        public static final int JA_EXISTE_SOLICITACAO_AMIZ = 106;
        public static final int CARONA_DISPONIVEL = 107;
    }
}
