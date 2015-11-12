package Servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Servidor implements Runnable {
	
	private ServerSocket serve;
	
	private List<Atendente> atendentes;//N�o utilizamos para nada.
	
	private boolean inicializado;
	private boolean executando;
	
	private Thread thread;
	
	public Servidor(int porta) throws Exception{
		atendentes = new ArrayList<Atendente>();//N�o utilizamos para nada
		
		inicializado = false;
		executando = false;
		
		open(porta);
	}
	
	private void open(int porta)throws Exception{
		serve = new ServerSocket(porta);
		inicializado = true;
		
	}
	
	private void close() {
		
		for(Atendente atendente: atendentes){
			try {
				atendente.stop();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		try {
			serve.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		serve = null;
		
		inicializado = false;
		executando = false;
		
		thread = null;
	}
	
	public void start() {
		if(!inicializado || executando){
			return;
		}
		
		executando = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() throws Exception{
		executando = false;
		
		
		if(thread != null){
		thread.join();
		}
	}
	
	
	public void run() {
		System.out.println("Aguardadando conexao...");
		
		while(executando){
			
			try {
				
			serve.setSoTimeout(2500);	
			
			Socket socket = serve.accept();
			
			System.out.println("Conexao estabelecida.");
		
			
			Atendente atendente = new Atendente(socket);
			atendente.start();
			
			atendentes.add(atendente);//N�o utilizamos
			
			}catch(SocketTimeoutException e){
				// ignorar
			}catch (Exception e) {
				System.out.println(e);
				return;
			}
		}
		
		close();
	}
	
	public static void main(String[] args) throws Exception {
		try {
			System.out.println("Iniciando servidor.");
			OuvindoUDP ouvindoUDP = new OuvindoUDP();
			ouvindoUDP.start();
			Servidor servidor = new Servidor(2525);
			servidor.start();
			
			System.out.println("PRESSIONE <ENTER> para encerrar o Servidor.");
			new Scanner(System.in).nextLine();
			
			
			System.out.println("Encerrando servidor.");
			servidor.stop();
			ouvindoUDP.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
