package data;
import java.util.Date;
import java.io.Serializable;

public abstract class ClypeData implements Serializable {
	
	/**instance variables**/
	protected String userName; //string representing userName
	protected int type;	//int representing a type
	protected Date date;	//Date
	protected final static int USERS = 0;	//sets users equal to 0
	protected final static int LOG_OUT = 1;	//sets log_out equal to 1
	protected final static int SEND_FILE = 2;	//sets send_file equal to 2
	protected final static int SEND_MESSAGE = 3;	//sets send_message equal to 3
	protected final static int SEND_IMG = 4;	//sets send_img equal to 4
	protected final static int SEND_VID = 5;	//sets send_vid equal to 5
	private static final long serialVersionUID = 1L;	//sets serialVersionUID equal to 1L
	
	/**constructor to set up userName and type, date should be automatically here**/
	public ClypeData (String userName, int type){
		
		this.userName = userName;
		this.type = type;
		this.date = new Date();
		
	}
	
	/**constructor to create anonymous user, whose name should be "Anon",
	should call another constructor**/
	public ClypeData(int type){
		
		this.type = type;
		this.userName = "Anon";
	
	}
	
	/**default constructor, should call another constructor.**/
	public ClypeData(){
		this(null, -1);
	}
	
	/**return the type**/ 
	public int getType() {
		return type;
	}
	
	/**return the user name**/
	public String getUserName(){
		return userName;
	}
	
	/**return the date**/
	public Date getDate(){
		return date;
	}
	
	/**abstract method to return the data contained in this class
	(contents of instant message or contents of a file)**/
	public abstract String getData();
	public abstract String getData( String key );
	
	public int hashCode(Object obj){
		
		ClypeData otherData = (ClypeData)obj;
		int result = 17;
		result = result * otherData.userName.length();
		result = result + otherData.type;
		return result;
	}
	
	/**
	 * compares two clypeData objects
	 */
	public boolean equals(Object obj){
		if (obj == null){
			return false;
		}
		if (!(obj instanceof ClypeData)){
			return false;
		}
		ClypeData otherData = (ClypeData)obj;
		
		return this.userName.equals(otherData.userName) &&
				this.type == otherData.type &&
				this.date.equals(otherData.date);
	}
	
	/**
	 * returns clypeData object as a string
	 */
	public String toString(){
		String description = "ClypeData" + "\nUser Name: " + userName +
				"\nType: " + type +
				"\nDate: " + date;
		return description;
	}
	
	/**encryption*/
	protected static String encrypt(String toEncrypt, String key) {
		
		char[] messageChar = toEncrypt.toCharArray();
		char[] keyChar = key.toCharArray();
		char[] encryptedMessage = new char[messageChar.length];
		char tempCharVal = 0;
		char tempKeyVal = 0;
		char tempNewVal = 0;
		boolean charUpperCase = false;
		boolean keyUpperCase = false;
		int adjuster = 0;
		for (int i = 0; i < messageChar.length; i++) {
			
			tempCharVal = messageChar[i];
			tempKeyVal = keyChar[(i-adjuster)%(keyChar.length)];
			
			if ((tempCharVal >= 'A' && tempCharVal <= 'Z') || (tempCharVal >= 'a' && tempCharVal <= 'z')) {
				if (Character.isUpperCase(tempCharVal)) {
					charUpperCase = true;
				}
				else {
					charUpperCase = false;
				}
				if (Character.isUpperCase(tempKeyVal)) {
					keyUpperCase = true;
				}
				else {
					keyUpperCase = false;
				}
				
				if (charUpperCase && !keyUpperCase) {
					tempKeyVal -= 32;
				}
				else if (!charUpperCase && keyUpperCase) {
					tempKeyVal += 32;
				}
				
				if (charUpperCase) {
					tempNewVal = (char) (tempCharVal + (tempKeyVal - 64));
					if (tempNewVal > 90) {
						tempNewVal -= 26;
					}
				}
				else {
					tempNewVal = (char) (tempCharVal + (tempKeyVal - 96));
					if (tempNewVal > 122) {
						tempNewVal -= 26;
					}
				}
			encryptedMessage[i] = tempNewVal;
			}
			else {
				encryptedMessage[i] = tempCharVal;
				if (tempCharVal == '\n') {
					adjuster++;
				}
			}
		}
		String encryptedString = new String(encryptedMessage);
		return encryptedString;
	}


 
	/**decryption**/
	protected static String decrypt(String toDecrypt, String key) {
		
		char[] messageChar = toDecrypt.toCharArray();
		char[] keyChar = key.toCharArray();
		char[] decryptedMessage = new char[messageChar.length];
		char tempCharVal = 0;
		char tempKeyVal = 0;
		char tempNewVal = 0;
		boolean charUpperCase = false;
		boolean keyUpperCase = false;
		int adjuster = 0;
		for (int i = 0; i < messageChar.length; i++) {
			
			tempCharVal = messageChar[i];
			tempKeyVal = keyChar[(i-adjuster)%(keyChar.length)];
			
			if ((tempCharVal >= 'A' && tempCharVal <= 'Z') || (tempCharVal >= 'a' && tempCharVal <= 'z')) {
				if (Character.isUpperCase(tempCharVal)) {
					charUpperCase = true;
				}
				else {
					charUpperCase = false;
				}
				if (Character.isUpperCase(tempKeyVal)) {
					keyUpperCase = true;
				}
				else {
					keyUpperCase = false;
				}
				
				if (charUpperCase && !keyUpperCase) {
					tempKeyVal -= 32;
				}
				else if (!charUpperCase && keyUpperCase) {
					tempKeyVal += 32;
				}
				
				if (charUpperCase) {
					tempNewVal = (char) (tempCharVal - (tempKeyVal - 64));
					if (tempNewVal < 65) {
						tempNewVal += 26;
					}
				}
				else {
					tempNewVal = (char) (tempCharVal - (tempKeyVal - 96));
					if (tempNewVal < 97) {
						tempNewVal += 26;
					}
				}
			decryptedMessage[i] = tempNewVal;
			}
			else {
				decryptedMessage[i] = tempCharVal;
				if (tempCharVal == '\n') {
					adjuster++;
				}
			}
		}
		String encryptedString = new String(decryptedMessage);
		return encryptedString;
	}
    
   /* public static void main (String[] args) {
    	String message = "hello moto";
    	String key = "doggo";
    	
    	String encrypted = encrypt(message,key);
    	System.out.println(encrypted);
    	String decrypted = decrypt(encrypted,key);
    	System.out.println(decrypted);
    }*/
}
