package main;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import data.ClypeData;

public class ClypeServer {
	
	/**instance variables**/
	private int port;	//integer representing port number on server connected to
	private boolean closeConnection;	//boolean representing whether connection is closed or not
	private ClypeData dataToReceiveFromClient; //ClypeData object representing data received from the client
	private ClypeData dataToSendToClient; //ClypeData object representing data sent to client
	private ObjectInputStream inFromClient;	//ObjectInputStream to receive information from client
	private ObjectOutputStream outToClient;	//ObjectOutputStream to send information to client
	private final static int defaultPort = 7000;
	private ServerSocket serverskt;
	private Socket skt;
	private ArrayList<ServerSideClientIO> serverSideClientIOList;
	protected ArrayList<String> userList;
	
	/**constructor that sets port number,
	should set dataToReceiveFromClient and dataToSendToClient as null.**/
	public ClypeServer( int port ) throws IllegalArgumentException{
		
		this.port = port;
		if(this.port < 1024){
			throw new IllegalArgumentException("IllegalArgumentException: port number entered is less than 1024");
		}
		
		serverSideClientIOList = new ArrayList<ServerSideClientIO>();
		userList = new ArrayList<String>();
	}
	
	/**default constructor that sets port to default port number 7000,
	default port number should be set up as a constant,
	this constructor should call another constructor.**/
	public ClypeServer() throws IllegalArgumentException{
		this(defaultPort);
	}
	
	/**main method that uses command line arguments to create a new ClypeServer object,
	 * and starts the ClypeServer object**/
	public static void main(String[] args){
		if (args.length == 0){
			ClypeServer server = new ClypeServer();
			server.start();
		}
		else{
			String t_port = args[0];
			int t_port_num = Integer.parseInt(t_port);
			
			ClypeServer server = new ClypeServer(t_port_num);
			server.start();
		}
	}
	
	/**does not return anything, just a declaration**/
	public void start(){
		try{
			serverskt = new ServerSocket(port);
		}catch (IOException ioe){
			System.err.println("Unable to open server socket");
			ioe.printStackTrace();
		}
		while(!closeConnection){
			try{
				skt = serverskt.accept();
				ServerSideClientIO newSocket = new ServerSideClientIO(this,skt);
				serverSideClientIOList.add(newSocket);
				Thread clientThread = new Thread(newSocket);
				clientThread.start();
			} catch (IOException ioe){
				System.err.println("Unable to open socket\n");
			}
		} try{
			System.out.println("closing socket");
			skt.close();
			serverskt.close();
		} catch (IOException ioe){
			System.err.println("Unable to close input/output stream or socket\n");
		}
	}
	
	/**takes in ClypeData object 'dataToBroadcastToClients',
	 * and does not return anything.**/
	public synchronized void broadcast(ClypeData dataToBroadcastToClients){
		if (dataToBroadcastToClients.getType() != 1) {
			for(int i = 0; i < serverSideClientIOList.size(); i++){
				ServerSideClientIO currentClient = serverSideClientIOList.get(i);
				currentClient.setDataToSendToClient(dataToBroadcastToClients);
				currentClient.sendData();
			}
		}
	}
	
	/**does not return anything, takes in a ServerSideClientIO object 'serverSideClientToRemove'**/
	public synchronized void remove(ServerSideClientIO serverSideClientRemove){
		ServerSideClientIO clientToRemove = serverSideClientRemove;
		serverSideClientIOList.remove(clientToRemove);
		clientToRemove.closeConnection = true;
		System.out.println("Removed client");
	}
	
	/**returns the port**/
	public int getPort(){
		return port;
	}

	/**should be correctly overloaded**/
	public int hashcode(Object obj){
		
		ClypeServer otherData = (ClypeServer)obj;
		int result = 17;
		result = port + result;
		return result;
	}
	
	/**should be correctly overridden**/
	public boolean equals(Object obj){
		
		if (this.getClass() == obj.getClass()){
			if (this == null && obj !=null || this !=null && obj != null){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return false;
		}
	}
	
	/**should be overridden to return a full description of the class with all instance variables**/
	public String toString(){
		
		String description = "Data to Receive from Client: " + dataToReceiveFromClient.toString() + 
				"\nPort: " + port +
				"Data to Send to Client: " + dataToSendToClient.toString();
		return description;
	}
}
