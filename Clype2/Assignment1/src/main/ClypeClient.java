package main;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//import java.util.Scanner;
import data.ClypeData;
import data.MessageClypeData;
import data.PictureVideoClypeData;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import data.FileClypeData;
//import application.conversationBox;

/**
 * 
 * @author Matthew Michaels
 *
 *this class represents the client user
 */
public class ClypeClient implements Runnable{

	private static final int USERLIST = 0;
	private static final int LOGOUT = 1;
	private static final int SENDFILE = 2;
	private static final int SENDMESSAGE = 3;
	private static final int DEFAULTPORT = 7000;
	private static final String key = "doggo";
	private String userName;
	private String hostName;
	private int port;
	//private String serverIP;
	protected boolean closeConection;
	private ClypeData dataToSendToServer;
	private ClypeData dataToRecieveFromServer;
	//private Scanner inFromStd;
	private ObjectInputStream inFromServer;
	private ObjectOutputStream outToServer;
	private String userInput;
	private Socket skt;
	private TextArea conversationBox;
	private TextArea userBox;
	private Boolean dataReady = false;
	protected ArrayList<String> userList;
	ImageView picture;
	BufferedImage bufferedImage;
	MediaPlayer videoPlayer;
	MediaView player;
	
	/**
	 * 
	 * @param username
	 * @param hostname
	 * @param port
	 * @param conversationBox
	 * @param userBox
	 * @param picture
	 * @param videoPlayer
	 * @param player
	 * 
	 * constructor to set the username, hostname, and port
	 */
	public ClypeClient(String username, String hostname, int port, TextArea conversationBox, TextArea userBox, ImageView picture, MediaPlayer videoPlayer, MediaView player) {
		if (username == null || hostname == null || port < 1024) {
			throw new IllegalArgumentException();
		}
		userName = username;
		hostName = hostname;
		this.port = port;
		this.conversationBox = conversationBox;
		this.userBox = userBox;
		this.picture = picture;
		this.videoPlayer = videoPlayer;
		this.player = player;
		closeConection = false;
		dataToSendToServer = null;
		dataToRecieveFromServer = null;
		inFromServer = null;
		outToServer = null;
		userList = new ArrayList<String>();
		//userList.add(username);
	}
	
	/**
	 * 
	 * @param username
	 * @param hostname
	 * @param conversationBox
	 * @param userBox
	 * @param picture
	 * @param videoPlayer
	 * @param player
	 * 
	 * constructor to set the username, and hostname but uses the default port value
	 */
	public ClypeClient(String username, String hostname, TextArea conversationBox, TextArea userBox, ImageView picture, MediaPlayer videoPlayer, MediaView player) {
		this(username,hostname,DEFAULTPORT,conversationBox, userBox, picture, videoPlayer, player);
	}
	
	/**
	 * 
	 * @param username
	 * @param conversationBox
	 * @param userBox
	 * @param picture
	 * @param videoPlayer
	 * @param player
	 * 
	 *constructor to set the username but uses the default hostname and port values
	 */
	public ClypeClient(String username, TextArea conversationBox, TextArea userBox, ImageView picture, MediaPlayer videoPlayer, MediaView player) {
		this(username,"localhost",DEFAULTPORT,conversationBox, userBox, picture, videoPlayer, player);
	}
	
	
	/**
	 * @param conversationBox
	 */
	public ClypeClient(TextArea conversationBox, TextArea userBox, ImageView picture, MediaPlayer videoPlayer, MediaView player) {
		this("Anon","localhost",DEFAULTPORT,conversationBox, userBox, picture, videoPlayer, player);
	}
	
	/**
	 * default constructor
	 */
	public ClypeClient() {
		this("Anon","localhost",DEFAULTPORT, null, null,null, null, null);
	}
	
	/**
	 * starts the connection
	 */
	public void start() {
		Thread listener = new Thread(new ClientSideServerListener(this));
		
		//inFromStd = new Scanner(System.in);
		try {
			skt = new Socket(hostName,port);
			outToServer = new ObjectOutputStream(skt.getOutputStream());
			inFromServer = new ObjectInputStream(skt.getInputStream());
			MessageClypeData usersRequest = new MessageClypeData(this.getUserName(),"GIVEUSERS",3);
			dataToSendToServer = usersRequest;
			sendData();
		}
		catch (IOException ioe) {
			System.err.println("Failed to open socket\n");
		}
		listener.start();
		while (!closeConection) {
			readClientData();
			sendData();
		}
		try {
			outToServer.close();
			inFromServer.close();
			skt.close();
		}
		catch (IOException ioe) {
			System.err.println("Failed to close input stream, output stream or socket\n");
		}
	}
	
	/**
	 * reads the client data
	 */
	public void readClientData() {
		while (!dataReady) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dataReady = false;
		String userInputTemp = userInput.replace("\n", "").replace("\r", "");
		String userInputTempTemp = userInputTemp.split(" ")[0];
		switch(userInputTempTemp) {
		case "LISTUSERS":
			dataToSendToServer = new MessageClypeData("LISTUSERS","LISTUSERS",0);
			String userString = "List of currently Connected Users:\n";
			for (int i = 0; i <userList.size(); i++) {
				userString += userList.get(i) + "\n";
			}
			ClypeData userList = new MessageClypeData("LISTUSERS",userString,0);
			dataToRecieveFromServer = userList;
			break;
		case "DONE":
			dataToSendToServer = new MessageClypeData(this.getUserName(),"asdasd",1);
			closeConection = true;
			break;
		case "SENDFILE":
			if (userInputTemp.split("SENDFILE ")[1].contains("txt")) {
				dataToSendToServer = new FileClypeData(getUserName(),userInputTemp.split("SENDFILE ")[1],2);
				//((FileClypeData) dataToSendToServer).readFileContents(key);
				((FileClypeData) dataToSendToServer).readFileContents();
			}
			else if (userInputTemp.split("SENDFILE ")[1].contains("jpg") || (userInputTemp.split("SENDFILE ")[1].contains("JPG"))){
				File imageFile = new File(userInputTemp.split("SENDFILE ")[1]);
				try {
					byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
					dataToSendToServer = new PictureVideoClypeData(getUserName(),4,imageBytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				File videoFile = new File(userInputTemp.split("SENDFILE ")[1]);
				try {
					BufferedInputStream videoStream = new BufferedInputStream(new FileInputStream(videoFile));
					byte[] videoBytes = new byte[(int)videoFile.length()];
					videoStream.read(videoBytes, 0, videoBytes.length);
					dataToSendToServer = new PictureVideoClypeData(getUserName(),5,videoBytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			dataToSendToServer = new MessageClypeData(getUserName(),userInput,key,3);
			break;
		}	
	}
	
	/**
	 * sends data
	 */
	public void sendData() {
		try {
			if (!dataToSendToServer.getUserName().equals("LISTUSERS")) {
				outToServer.writeObject(dataToSendToServer);
				outToServer.flush();
				outToServer.reset();
			}
		}
		catch (IOException ioe) {
			System.err.println("Issue sending data to server\n");
		}
	}
	
	/**
	 * receives data
	 */
	public synchronized void recieveData() {
		try {
			dataToRecieveFromServer = (ClypeData) inFromServer.readObject();
			if (!userList.contains(dataToRecieveFromServer.getUserName())) {
				//userList.add(dataToRecieveFromServer.getUserName());
				userBox.clear();
				userBox.setText("The currently connected users are:");
				for (int i = 0; i < userList.size(); i++) {
					userBox.appendText(userList.get(i) + "\n");
				}
			}
			if (dataToRecieveFromServer.getUserName().equals("CURRENTUSERS")) {
				int listSize = userList.size();
				for (int i = 0; i<listSize; i++) {
					userList.remove(0);
				}
				String[] newUsers = dataToRecieveFromServer.getData().split("\n");
				userBox.clear();
				userBox.setText("The currently connected users are:");
				for (int i = 0; i < newUsers.length; i++) {
					userList.add(newUsers[i]);
					userBox.appendText(newUsers[i] + "\n");
				}
				if (!userList.contains(this.getUserName())) {
					userList.add(this.getUserName());
					userBox.appendText(this.getUserName() + "\n");
				}
			}
		}
		catch (IOException ioe) {
			System.err.println("Issue recieving data from server\n");
		} 
		catch (ClassNotFoundException e) {
			System.err.println("Class not found exception\n");
		}
 	}
	
	/**
	 * prints data
	 */
	public void printData() {
		if (dataToRecieveFromServer.getType() == 0) {
			if (!dataToRecieveFromServer.getUserName().equals("CURRENTUSERS")) {
				conversationBox.appendText(dataToRecieveFromServer.getData() + "\n");
			}
		}
		else if (dataToRecieveFromServer.getType() == 2){
			if (((FileClypeData)dataToRecieveFromServer).getFileName().contains("txt")) {
				//conversationBox.appendText(dataToRecieveFromServer.getUserName() + ": The contents of this file are as follow:\n" + dataToRecieveFromServer.getData(key));
				conversationBox.appendText(dataToRecieveFromServer.getUserName() + ": The contents of this file are as follow:\n" + dataToRecieveFromServer.getData());
			}
		}
		else if (dataToRecieveFromServer.getType() == 4){
				try {
					bufferedImage = ImageIO.read(new ByteArrayInputStream(((PictureVideoClypeData)dataToRecieveFromServer).getBytes()));
					File imageFile = new File("picture.jpg");
					ImageIO.write(bufferedImage, "jpg", imageFile);
					Image image = new Image(imageFile.toURI().toString());
					picture.setImage(image);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
		}
		else if (dataToRecieveFromServer.getType() == 5){
			try {
				FileOutputStream fileStream = new FileOutputStream("video.mp4");
				fileStream.write(((PictureVideoClypeData)dataToRecieveFromServer).getBytes());
				videoPlayer = new MediaPlayer(new Media(new File("video.mp4").toURI().toString()));
				player.setMediaPlayer(videoPlayer);
				//videoPlayer.play();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
	}
		else if (dataToRecieveFromServer.getType() == 3){
			conversationBox.appendText(dataToRecieveFromServer.getUserName() + ": " + dataToRecieveFromServer.getData(key));
		}
	}

	/**
	 * 
	 * @return videoPlayer
	 */
	public MediaPlayer getVideoPlayer() {
		return videoPlayer;
	}
	
	/**
	 * 
	 * @return userName
	 * 
	 * returns the username
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * 
	 * @return hostName
	 * 
	 * returns the hostname
	 */
	public String getHostName() {
		return hostName;
	}
	
	/**
	 * 
	 * @return port
	 * 
	 * returns the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * 
	 * @param state
	 * sets the variable dataReady
	 */
	public void setDataReady(boolean state) {
		dataReady = state;
	}
	
	/**
	 * 
	 * @param userInput
	 * sets userInput variable
	 */
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	
	/**
	 * returns the hash code
	 */
	public int hashCode() {	
		return userName.hashCode() + hostName.hashCode() + port /*+ dataToSendToServer.toString().hashCode() + dataToRecieveFromServer.toString().hashCode()*/;
	}
	
	/**
	 * compares two ClypeClient objects
	 */
	public boolean equals(Object obj) {
		ClypeClient client = (ClypeClient)obj;
		
		if (!client.getHostName().equals(this.hostName)) {
			return false;
		}
		else if (!client.getUserName().equals(this.userName)) {
			return false;
		}
		else if (client.getPort() != this.port) {
			return false;
		}
		else if (dataToSendToServer != null && client.dataToSendToServer != null) {
			if (!dataToSendToServer.toString().equals(client.dataToSendToServer.toString())) {
				return false;
			}
		}
		else if (dataToSendToServer != null && client.dataToSendToServer != null) {
			if (!dataToRecieveFromServer.toString().equals(client.dataToRecieveFromServer.toString())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * prints a string representation of the ClypeClient object
	 */
	public String toString() {
		String userPortInfo = "The current user " + userName + " is connected to " + hostName + " on port " + port + ".";
		String dataInfo = "";
		if (dataToSendToServer != null && dataToRecieveFromServer != null) {
			dataInfo = "The current data to send is " + dataToSendToServer.toString() + ", and the current data to recieve is " + dataToRecieveFromServer.toString() + ".";
		}
		return userPortInfo + " " + dataInfo;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		start();
	}
	
}
