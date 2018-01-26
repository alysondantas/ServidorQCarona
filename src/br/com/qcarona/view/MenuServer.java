package br.com.qcarona.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.qcarona.controller.ControllerDados;
import br.com.qcarona.controller.ControllerServer;
import br.com.qcarona.threads.ThreadAtualizaGUI;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class MenuServer {

	private JFrame frame;
	private JTextField porta;
	private final ButtonGroup tipo = new ButtonGroup();
	private ControllerServer controllerServer = ControllerServer.getInstance();
	private ControllerDados controllerDados;
	private JTextArea textArea;
	private JButton startserver;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuServer window = new MenuServer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MenuServer() {
		initialize();
		try {
			controllerDados = ControllerDados.getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String nome = System.getProperty("os.name");//recupera o nome do SO
		if(nome.substring(0, 7).equals("Windows")){//se ele for WINDOWS é colocado um LookAndFeel do windows para rodar melhorar a aparencia
			try { 
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			} catch (InstantiationException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		frame = new JFrame();
		frame.setBounds(100, 100, 503, 371);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 467, 356);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Inicio", null, panel, null);
		panel.setLayout(null);
		
		startserver = new JButton("Start Server");
		startserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iniciaServidor();
			}
		});
		startserver.setBounds(337, 198, 115, 23);
		panel.add(startserver);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 442, 154);
		panel.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEnabled(false);
		scrollPane.setViewportView(textArea);
		
		porta = new JTextField();
		porta.setText("1099");
		porta.setEnabled(false);
		porta.setColumns(10);
		porta.setBounds(366, 253, 86, 20);
		panel.add(porta);
		
		JLabel label = new JLabel("Porta:");
		label.setBounds(331, 176, 46, 14);
		panel.add(label);
		
		JButton btnListarClientes = new JButton("Listar Clientes");
		btnListarClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listarUsuarios();
			}
		});
		btnListarClientes.setBounds(10, 198, 115, 23);
		panel.add(btnListarClientes);
	}
	
	
	public void iniciaServidor(){
		if(controllerServer!=null){
			String p = porta.getText();
			int port ;
			try{
				port = Integer.parseInt(p);
			}catch(Exception e){
				port = 1099;//porta padrão
			}
			
			startserver.setEnabled(false);
			
			
			
			controllerServer.iniciaServer(port,textArea);
			
			ThreadAtualizaGUI t = new ThreadAtualizaGUI();
			t.start();
		}
	}
	
	public void listarUsuarios(){
		try {
			controllerDados.listarUsuarios();
		} catch (SQLException e) {
			System.out.println("Problema no sql");
			e.printStackTrace();
		}
	}
}
