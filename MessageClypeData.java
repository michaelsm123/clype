package data;

/**
 * 
 * @author Matthew Michaels
 *
 *this class represents data in the form of messages
 */
public class MessageClypeData extends ClypeData {

	private String message;
	private String key;
	
	public MessageClypeData(String username, String message, String key, int type) {
		super(username,type);
		this.message = encrypt(message,key);
		this.key = key;
	}
	
	/**
	 * 
	 * @param username
	 * @param message
	 * @param type
	 * 
	 * constructor to set the username, message, and type
	 */
	public MessageClypeData(String username, String message, int type) {
		super(username,type);
		this.message = message;
	}

	/**
	 * default constructor
	 */
	public MessageClypeData() {
		super("Anon",1);
		this.message = "";
	}

	@Override
	/**
	 * @return message
	 * 
	 * returns message variable
	 */
	public String getData() {
		return message;
	}
	
	/**
	 * @return message
	 * 
	 * decrypts message and returns it
	 */
	public String getData(String key) {
		return decrypt(message,key);
	}
	
	/**
	 * returns hash Code of the MessageClypeData object
	 */
	public int hashCode() {
		return super.getType() + super.getDate().hashCode() + super.getUserName().hashCode() + message.hashCode() ;
	}
	
	/**
	 * compares two MessageClypeData objects
	 */
	public boolean equals (Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MessageClypeData)) {
			return false;
		}
		
		MessageClypeData clypedata = (MessageClypeData)obj;
		if (!clypedata.getData().equals(this.message)) {
			return false;
		}
		else if (!clypedata.getDate().equals(this.getDate())) {
			return false;
		}
		else if (!clypedata.getUserName().equals(this.getUserName())) {
			return false;
		}
		else if (clypedata.getType() != this.getType()) {
			return false;
		}
		return true;
	}
	
	/**
	 * prints a string representation of the MessageClypeData object
	 */
	public String toString() {
		String stringRep = super.getUserName() + " sent the message \"" + this.getData() + "\" on " + super.getDate() + ". The user's type is currently set to ";
		switch (super.getType()) {
			case 0: stringRep += "0.";
			case 1: stringRep += "1.";
			case 2: stringRep += "2.";
			case 3: stringRep += "3.";
		}
		return stringRep;
	}
	
}
