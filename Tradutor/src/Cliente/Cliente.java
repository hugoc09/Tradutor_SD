package Cliente;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Cliente implements Runnable{
 
		private Socket socket;
 
		private BufferedReader in;
		private PrintStream out;
 
		private boolean inicializado;
		private boolean executando;
 
		private Thread  thread;
 
		public Cliente(InetAddress endereco, int porta) throws Exception{
			inicializado = false;
			executando   = false;
  
			open(endereco, porta);
		}
 
		private void open(InetAddress endereco, int porta) throws Exception{
			try {
				socket = new Socket(endereco, porta); 
				
				in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintStream(socket.getOutputStream());
   
				inicializado = true;
			}
			catch (Exception e) {
				System.out.println(" Servidores não encontrados em rede ");
				close();
				//throw e;
			}
		}
 
		private void close(){
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e){
					
					System.out.println(e);
				}
			}

			if (out != null) {
				try {
					out.close();
				}
				catch (Exception e){
					
					System.out.println(e);
				}
			}

			if (socket != null) {
				try {
					socket.close();
				}
				catch (Exception e){
					System.out.println(e);
				}
			}
  
			in     = null;
			out    = null;
			socket = null;
  
			inicializado = false;
			executando   = false;
  
			thread = null;
 
		}
 
		public void start() {
			if (!inicializado || executando) {
				return;
			}
  
			executando = true;
			thread = new Thread(this);
			thread.start();
		}

		public void stop() throws Exception{
			executando = false;
  
			if (thread != null) {
				thread.join();
			}
		}
		
		public boolean isInicializado() {
			  return inicializado;
			 }
 
		public boolean isExecutando() {
			return executando;
		}
 
		public void send(String mensagem) {
			out.println(mensagem);
		}
 
 
		@Override
		public void run() {
  
			while (executando) {
				try {
					socket.setSoTimeout(3000);
    
					String mensagem = in.readLine();
    
					if (mensagem == null) {
						break;
					}

					System.out.println(
							"Tradução recebida do servidor: " +
									mensagem);
					System.out.println("Digite uma palavra: ");
   
				}
				catch (SocketTimeoutException e) {
					// Ignorar
				}
				catch (Exception e) {
					  Object[] options = { "OK"};
				      JOptionPane.showOptionDialog(null, "Conexão com servidor perdida. O servidor remoto foi desligado inesperadamente.", "Aviso",
				          JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				              null, options, options[0]);
					break;
				}
			}
			close();
		}
 
		public static void main(String[] args) throws Exception {
  
			System.out.println("Iniciando cliente ...");
			System.out.println("< Tradutor de palavras >");
			System.out.println(" *Portugues* >>> *Ingles* ");
			boolean sair = false;
			Scanner scanner;//= new Scanner(System.in);
			String mensagem = null;
		 
			do{	
				PacketUDP paquete = new PacketUDP();
				
				paquete.start();
				paquete.stop();
				
				if(PacketUDP.ip!=null && PacketUDP.porta!=0){
					System.out.println("Servidor encontrado. Conexão feita!");
					Cliente cliente = new Cliente(PacketUDP.ip, PacketUDP.porta);
					cliente.start();
					PacketUDP.ip=null;
					PacketUDP.porta=0;
					scanner = new Scanner(System.in);
					if(mensagem!=null){
						cliente.send(mensagem);
					}
			 
					System.out.println("Digite uma palavra: ");
					while (cliente.isInicializado()){
						
					 
					  mensagem = scanner.nextLine();
					 
					 
					 if (!cliente.isExecutando() ) {
						 System.out.println("Conexão perdida...");  
						 break;
						 }
					 
					 cliente.send(mensagem);
					
	   
					 if ("FIM".equalsIgnoreCase(mensagem)){
						 System.out.println("Encerrando cliente ...");   
						 sair = true;
						 cliente.stop();
						 scanner.close();
						 break;}
			
					}
			 
				}		
			}while(!sair);
		}
	}