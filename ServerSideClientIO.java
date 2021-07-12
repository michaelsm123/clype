package main;
import data.ClypeData;
import data.MessageClypeData;
import data.PictureVideoClypeData;

import java.io.EOFException;
import java.io.IOException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OptionalDataException;

class ServerSideClientIO implements Runnable {

	/**Instance variables**/
	ClypeServer server;
	Socket clientSocket;
	boolean closeConnection = false;
	ClypeData dataToRecieveFromClient;
	ClypeData dataToSendToClient;
	ClypeData prevDataToReceiveFromClient = new MessageClypeData();
	ObjectInputStream inFromClient;
	ObjectOutputStream outToClient;
	/**constructor that takes ClypeServer object and Socket object as parameters.**/
	public ServerSideClientIO(ClypeServer server, Socket clientSocket){
		
		this.server = server;
		this.clientSocket = clientSocket;
		boolean closeConnection = false;
		}
	
	/**Overrides the run method in the Runnable interface**/
	public void run(){
		try{
			outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			inFromClient = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch(IOException ioe){
			System.err.println(ioe.getMessage());
		}
		while(!this.closeConnection){
			try{
				Thread.sleep(500);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			receiveData();
			server.broadcast(dataToRecieveFromClient);
		}
	}
		
	/**does not return anything, receives data from the client**/
	public void receiveData(){

			try{
				Thread.sleep(500);
			}catch (InterruptedException e){
				System.out.println("Unable to sleep thread\n");
			}
			try{
				dataToRecieveFromClient = (ClypeData)inFromClient.readObject();
				if (!server.userList.contains(dataToRecieveFromClient.getUserName()) && !dataToRecieveFromClient.getUserName().equals("LISTUSERS")) {
					server.userList.add(dataToRecieveFromClient.getUserName());
				}
				if (dataToRecieveFromClient.getType() == 1){
					server.remove(this);
					server.userList.remove(dataToRecieveFromClient.getUserName());
					String userString = "";
					for (int i = 0; i <server.userList.size(); i++) {
						userString += "\n" + server.userList.get(i);
					}
					ClypeData userList = new MessageClypeData("CURRENTUSERS",userString,0);
					dataToRecieveFromClient = userList;
				}
				else if (dataToRecieveFromClient.getType() == 4 || dataToRecieveFromClient.getType() == 5) {
					
				}
				else if(dataToRecieveFromClient.getData().equals("GIVEUSERS")) {
					String userString = "";
					//server.userList.add(dataToRecieveFromClient.getUserName());
					for (int i = 0; i <server.userList.size(); i++) {
						userString += "\n" + server.userList.get(i);
					}
					ClypeData userList = new MessageClypeData("CURRENTUSERS",userString,0);
					dataToRecieveFromClient = userList;
				}
			}
			catch (ClassNotFoundException e){
				System.err.println("cnfe\n");
			}
			catch (EOFException eof){
				System.err.println("EOF exception\n");
			}
			catch(OptionalDataException ode){
				System.err.println("optional data\n");
			}
			catch (IOException ioe){
				System.err.println("IO exception when recieving data\n");
			}
	}

	/**does not return anything, sends data to the client**/
	public void sendData(){
		
			try{
				outToClient.writeObject(dataToSendToClient);
				outToClient.flush();
				outToClient.reset();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
	}
	
	/**new mutator method to set the ClypeData variable dataToSendToClient**/
	public void setDataToSendToClient(ClypeData dataToSendToClient){
		this.dataToSendToClient = dataToSendToClient;
	}
}
