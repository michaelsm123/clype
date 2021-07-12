package application;
	
import java.io.File;
import java.time.Duration;
import java.util.List;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
//import main.ClientSideServerListener;
import main.ClypeClient;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class Main extends Application {
	
	private String userMessage = "";
	private HBox convoAndUsers = new HBox();
	private TextArea conversationBox = new TextArea(); // box is within anchor pane
	private TextArea usersBox = new TextArea();
	private TextArea messageField = new TextArea();
	private Button sendMessage = new Button();
	private ClypeClient client = new ClypeClient();
	private Thread clientThread;// = new Thread(new ClypeClient(this));
	private HBox buttons = new HBox();
	private Button fileButton = new Button();
	private FileChooser fileChooser = new FileChooser();
	File chosenFile;
	private Button sendFileButton = new Button();
	private ImageView picture = new ImageView();
	private HBox imageBox = new HBox();
	//private Media test = new Media(new File("C:\\Users\\Matt\\Desktop\\VID_20171207_135147.mp4").toURI().toString());
	private MediaPlayer videoPlayer;// = new MediaPlayer(test);
	private MediaView player = new MediaView();
	private VBox videoBox = new VBox();
	private HBox videoButtons = new HBox();
	private Button playButton = new Button();
	private Button pauseButton = new Button();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FlowPane root = new FlowPane();
			Scene scene = new Scene(root,1850,800);
			primaryStage.setResizable(false);
			
			primaryStage.setTitle("Clype Messenger");
			
			conversationBox.setStyle( "-fx-background-color: #000000;");
			conversationBox.setMinWidth( 500 );
			conversationBox.setMinHeight( 600 );
			conversationBox.setMaxWidth( 500 );
			conversationBox.setMaxHeight( 600 );
			conversationBox.setTranslateX( 50 );
			conversationBox.setTranslateY( -60 );
			conversationBox.setEditable(false);
			
			usersBox.setStyle( "-fx-background-color: #000000;");
			usersBox.setMinWidth( 200 );
			usersBox.setMinHeight( 600 );
			usersBox.setMaxWidth( 200 );
			usersBox.setMaxHeight( 600 );
			usersBox.setTranslateX( 50 );
			usersBox.setTranslateY( -60 );
			usersBox.setEditable(false);
			usersBox.setText("The currently connected users are:\n");
			
			/*HBox messageBox = new HBox(); // box is within anchor pane
			messageBox.setStyle( "-fx-background-color: #444444;");
			messageBox.setMinWidth( 700 );
			messageBox.setMinHeight( 100 );
			messageBox.setTranslateX( 50 );
			messageBox.setTranslateY( -50 );
			messageBox.setAlignment(Pos.CENTER);*/
			
			messageField.setMinWidth( 700 );
			messageField.setMaxHeight( 100 );
			messageField.setTranslateX( 50 );
			messageField.setTranslateY( -50 );
			messageField.setWrapText(true);
			messageField.setOnKeyReleased(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent keyEvent) {
					if (keyEvent.getCode() == KeyCode.ENTER) {
						userMessage = messageField.getText();
						messageField.clear();
						client.setUserInput(userMessage);
						client.setDataReady(true);
					}
				}
			});
			
			
			sendMessage.setMinWidth( 100 );
			sendMessage.setMinHeight( 50 );
			sendMessage.setTranslateX( 90 );
			sendMessage.setTranslateY( 725 );
			sendMessage.setAlignment(Pos.CENTER);
			sendMessage.setText("Send Message/File");
			sendMessage.setStyle( "-fx-background-color: #bbff7f; -fx-font-size: 30");
			sendMessage.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent action) {
					userMessage = messageField.getText() + "\n";
					messageField.clear();
					client.setUserInput(userMessage);
					client.setDataReady(true);
				}
			});
			
			fileButton.setMinWidth( 100 );
			fileButton.setMinHeight( 50 );
			fileButton.setTranslateX( 50 );
			fileButton.setTranslateY( 725 );
			fileButton.setAlignment(Pos.CENTER);
			fileButton.setText("Choose file");
			fileButton.setStyle( "-fx-background-color: #bbff7f; -fx-font-size: 30");
			fileButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent action) {
					chosenFile = fileChooser.showOpenDialog(primaryStage);
					if (chosenFile != null) {
						fileButton.setText("File chosen");
						fileButton.setStyle( "-fx-background-color: #0066ff; -fx-font-size: 30");
					}
				}
			});
			
			sendFileButton.setMinWidth( 100 );
			sendFileButton.setMinHeight( 50 );
			sendFileButton.setTranslateX( 75 );
			sendFileButton.setTranslateY( 725 );
			sendFileButton.setAlignment(Pos.CENTER);
			sendFileButton.setText("Send file");
			sendFileButton.setStyle( "-fx-background-color: #bbff7f; -fx-font-size: 30");
			sendFileButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent action) {
					if (chosenFile != null) {
						if (chosenFile.getPath().contains("txt") || chosenFile.getPath().contains("TXT") || chosenFile.getPath().contains("jpg") || chosenFile.getPath().contains("JPG") || chosenFile.getPath().contains("mp4") || chosenFile.getPath().contains("MP4")) {
							userMessage = "SENDFILE " + chosenFile.getPath();
							chosenFile = null;
							client.setUserInput(userMessage);
							client.setDataReady(true);
						}
						else {
							conversationBox.appendText("\n**************************\nINCCORECT FILE TYPE .TXT, .JPG, or .MP4 ONLY\n**************************\n\n");
						}
						fileButton.setText("Choose file");
						fileButton.setStyle( "-fx-background-color: #bbff7f; -fx-font-size: 30");
					}
				}
			});
			
			imageBox.setStyle( "-fx-background-color: #000000;");
			imageBox.setMinWidth( 500 );
			imageBox.setMinHeight( 600 );
			imageBox.setMaxWidth( 500 );
			imageBox.setMaxHeight( 600 );
			imageBox.setTranslateX( 100 );
			imageBox.setTranslateY( -60 );
			
			videoBox.setStyle( "-fx-background-color: #000000;");
			videoBox.setMinWidth( 500 );
			videoBox.setMinHeight( 600 );
			videoBox.setMaxWidth( 500 );
			videoBox.setMaxHeight( 600 );
			videoBox.setTranslateX( 125 );
			videoBox.setTranslateY( -60 );
			
			picture.setFitWidth(500);
			picture.setFitHeight(600);
			
			player.setFitWidth(500);
			player.setFitHeight(600);
			//player.setTranslateX( 250 );
			//player.setTranslateY( -60 );
			player.setStyle( "-fx-background-color: #aabb33;");
			//videoPlayer.play();
			
			playButton.setMinWidth( 150 );
			playButton.setMinHeight( 75 );
			//playButton.setTranslateX( 0 );
			//playButton.setTranslateY( 0 ); 
			playButton.setAlignment(Pos.CENTER);
			playButton.setText("Play Video");
			playButton.setStyle( "-fx-background-color: #bbff7f; -fx-font-size: 30");
			playButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent action) {
					if (client.getVideoPlayer() != null) {
						videoPlayer = client.getVideoPlayer();
						videoPlayer.setOnEndOfMedia(new Runnable() {
							@Override
							public void run() {
								videoPlayer.seek(javafx.util.Duration.ZERO);
							}
						});
						//player.setMediaPlayer(videoPlayer);
						
						videoPlayer.play();
					}
				}
			});
			
			pauseButton.setMinWidth( 150 );
			pauseButton.setMinHeight( 75 );
			//playButton.setTranslateX( 0 );
			//playButton.setTranslateY( 0 ); 
			pauseButton.setAlignment(Pos.CENTER);
			pauseButton.setText("Pause Video");
			pauseButton.setStyle( "-fx-background-color: #bbff7f; -fx-font-size: 30");
			pauseButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent action) {
					if (client.getVideoPlayer() != null) {
						videoPlayer = client.getVideoPlayer();
						videoPlayer.pause();
					}
				}
			});
			
			root.getChildren().add(buttons);
			buttons.getChildren().add(fileButton);
			buttons.getChildren().add(sendFileButton);
			buttons.getChildren().add(sendMessage);
			root.getChildren().add(convoAndUsers);
			convoAndUsers.getChildren().add(conversationBox);
			convoAndUsers.getChildren().add(usersBox);
			convoAndUsers.getChildren().add(imageBox);
			convoAndUsers.getChildren().add(videoBox);
			//convoAndUsers.getChildren().add(player);
			imageBox.getChildren().add(picture);
			videoBox.getChildren().add(player);
			videoBox.getChildren().add(videoButtons);
			videoButtons.getChildren().add(playButton);
			videoButtons.getChildren().add(pauseButton);
			root.getChildren().add(messageField);
			
			
			primaryStage.setScene(scene);
			
			Application.Parameters params = getParameters();
			List argList = params.getRaw();
			
			if (argList.size() == 0) {
				//client = new Thread(new ClypeClient(conversationBox));
				client = new ClypeClient(conversationBox,usersBox, picture, videoPlayer,player);
			}
			else {
				String arg = (String) argList.get(0);
				if (arg.contains(":")) {
					String[] temp = arg.split("[:@]");
					//client = new Thread(new ClypeClient(temp[0],temp[1],Integer.valueOf(temp[2]),conversationBox));
					client = new ClypeClient(temp[0],temp[1],Integer.valueOf(temp[2]),conversationBox,usersBox,picture, videoPlayer,player);
				}
				else if (arg.contains("@")) {
					String[] temp = arg.split("@");
					//client = new Thread(new ClypeClient(temp[0],temp[1],conversationBox));
					client = new ClypeClient(temp[0],temp[1], conversationBox,usersBox,picture, videoPlayer,player);
				}
				else {
					//client = new Thread(new ClypeClient(arg,conversationBox));
					client = new ClypeClient(arg,conversationBox,usersBox,picture, videoPlayer,player);
				}
			}
			clientThread = new Thread(client);
			clientThread.start();	
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	  userMessage = "DONE";
		        	  client.setUserInput(userMessage);
		        	  client.setDataReady(true);
		        	  primaryStage.close();
		          }
		      });        
			
			
		} catch(Exception e) {	
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);

	}
}
