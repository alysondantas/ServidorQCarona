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
        public static final int OBTEM_PERFIL = 6;
        public static final int BUSCAR_AMIGOS = 7;
        public static final int DESFAZ_AMIGO = 8;
        public static final int BUSCA_SOLICITACAO_AMIZADE = 9;
        public static final int CIDADES_DISPONIVEIS = 10;
        public static final int ACEITA_SOLICITACAO = 11;
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
        public static final int RETORNO_BUSCA_AMIGOS = 108;
        public static final int OPERACAO_CONCLUIDA = 109;
        public static final int OPERACAO_NAO_CONCLUIDA = 110;
        public static final int RETORNO_CIDADES_DISPONIVEIS = 111;
    }
}
