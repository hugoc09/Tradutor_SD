package Cliente;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.regex.Pattern;

public class PacketUDP implements Runnable{
	
	private DatagramSocket clientSocket;
	 
	private DatagramPacket pkgEnviado;
	private DatagramPacket pkgRecebido;
	
	
	static InetAddress ip;
	static int porta;
	
	private boolean inicializado;

	private boolean executando;

	private Thread  thread;
	
	public PacketUDP() throws Exception{
		inicializado = false;
		executando   = false;

		open();
	}
	
	private void open() {
		try {
			clientSocket = new DatagramSocket(); 
			//clientSocket.setBroadcast(true);
			
			InetAddress addr = InetAddress.getByName("192.168.1.106");
			
			String msgEnviada = "ola";
			pkgEnviado = new DatagramPacket(msgEnviada.getBytes(),msgEnviada.length(), addr, 2526);
			clientSocket.send(pkgEnviado);
			
			inicializado = true;
		}
		catch (Exception e) {
			//System.out.println("Nenhum servidor encontrado em rede ");
			close();
			//throw e;
		}
	}
	
	private void close(){

		if (clientSocket != null) {
			try {
				clientSocket.close();
			}
			catch (Exception e){
				System.out.println(e);
			}
		}

		
		
		pkgEnviado   = null;
		pkgRecebido  = null;
		clientSocket = null;

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
	
	@Override
	public void run() {

		byte[] recebeDados = new byte[1024];
		pkgRecebido = new DatagramPacket(recebeDados, recebeDados.length);
	 	
		try {
			
			clientSocket.setSoTimeout(6000);
		 	
		 	clientSocket.receive(pkgRecebido); 
		 	
		 	String s = new String(pkgRecebido.getData());
		
			String argumentos[] = s.split(Pattern.quote(";"));
			
			//Mostra no video
			System.out.println("Datagrama UDP recebido: " + argumentos[0] + argumentos[1] );
			
			String a = argumentos[0];
			String b = argumentos[1];
			
			
		 	ip = InetAddress.getByName(a);
		 	
		 	porta = Integer.parseInt(b);
			
		 	
		 	
		} catch (SocketTimeoutException g) {
			System.out.println("Servidores Offline");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		close();	
	}
		
	
	}


