package br.com.qcarona.controller;

import java.net.ServerSocket;

import javax.swing.JTextArea;

import br.com.qcarona.threads.ThreadServidor;
import br.com.qcarona.threads.ThreadServidorConexao;



public class ControllerServer {
	private ServerSocket server;
    private ThreadServidorConexao thread;
    private static ControllerServer unicaInstancia;
	
	private ControllerServer(){
		
	}
	
	/**
	 * controla o instanciamento de objetos Controller
	 * @return unicaInstancia
	 */
	public static synchronized ControllerServer getInstance(){
		if(unicaInstancia==null){
			unicaInstancia = new ControllerServer();
		}
		return unicaInstancia;
	}

	/**
	 * reseta o objeto Controller ja instanciado
	 */
	public static void zerarSingleton (){
		unicaInstancia = null;
	}

	
	public String iniciaServer(int porta, JTextArea textArea){
        try {     
		System.out.println("Incializando o servidor...");
		textArea.setText(textArea.getText() + "Incializando o servidor... \n");

		server = new ServerSocket(porta);//instancia um socket server na porta desejada
		System.out.println("Servidor iniciado, ouvindo a porta " + porta);
		textArea.setText(textArea.getText() + "Servidor iniciado, ouvindo a porta " + porta);//indica que o servidor foi ligado em determinada porta

		ThreadServidor threadserver = new ThreadServidor(thread, textArea, server);//thread que permite a atualização periodica da GUI
		threadserver.start();

	}catch(Exception e){
		e.printStackTrace();//exibe a exceção que foi lançada
		textArea.setText(textArea.getText() + "Excecao ocorrida ao criar thread: " + e);//caso alguma exceção desconheciada seja lançada ela encerra a thread e é exibida
	}

	return null;
    }

}
