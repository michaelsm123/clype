package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Matthew Michaels
 *
 *this class represents data in the form of files
 */
public class FileClypeData extends ClypeData {
	
	String fileContents = null;
	String fileName;
	
	/**
	 * 
	 * @param username
	 * @param filename
	 * @param type
	 * 
	 * constructor to set the username, filename, and type
	 */
	public FileClypeData(String username, String filename, int type) {
		super(username,type);
		fileName = filename;
	}
	
	/**
	 * default constructor
	 */
	public FileClypeData() {
		super("Anon",1);
	}
	
	/**
	 * 
	 * @param filename
	 * 
	 * sets the filename variable
	 */
	public void setFileName(String filename) {
		fileName = filename;
	}
	
	/**
	 * 
	 * @return fileName
	 * 
	 * returns the fileName variable
	 */
	public String getFileName() {
		return fileName;
	}
	
	
	/**
	 * @return fileContents
	 * 
	 * returns the fileContents variable
	 */
	public String getData() {
		return fileContents;
	}
	
	/**
	 * @return fileContents
	 * 
	 * returns the decrypted fileContents variable
	 */
	public String getData(String key) {
		return decrypt(fileContents,key);
	}
	
	/**
	 * reads the file contents
	 */
	public void readFileContents() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String currentLine = reader.readLine();
			fileContents = "";
			
			while (currentLine != null) {
				fileContents += currentLine + "\n";
				currentLine = reader.readLine();
			}
			reader.close();
		}
		catch (FileNotFoundException fnf) {
			System.err.println("File not found\n");
			fileContents = "File not found\n";
		}
		catch (IOException io) {
			System.err.println("IO exception\n");
		}
	}
	
	/**
	 * reads the file contents and encrypts them
	 */
	public void readFileContents(String key) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String currentLine = reader.readLine();
			fileContents = "";
			
			while (currentLine != null) {
				fileContents += encrypt(currentLine,key) + "\n";
				currentLine = reader.readLine();
			}
			reader.close();
		}
		catch (FileNotFoundException fnf) {
			System.err.println("File not found\n");
			fileContents = "File not found\n";
		}
		catch (IOException io) {
			System.err.println("IO exception\n");
		}
	}
	
	/**
	 * writes the file contents
	 */
	public void writeFileContents() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
			writer.write(fileContents);
			writer.close();
		}
		catch (FileNotFoundException fnf) {
			System.err.println("File not found\n");
		}
		catch (IOException io) {
			System.err.println("IO exception\n");
		}
	}

	/**
	 * writes the decrypted file contents
	 */
	public void writeFileContents(String key) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
			String decryptedContents = decrypt(fileContents,key);
			writer.write(decryptedContents);
			writer.close();
		}
		catch (FileNotFoundException fnf) {
			System.err.println("File not found\n");
		}
		catch (IOException io) {
			System.err.println("IO exception\n");
		}
	}
	
	/**
	 * returns hash code of the FileClypeData object
	 */
	public int hashCode() {
		return getType() + getDate().hashCode() + getUserName().hashCode() + fileName.hashCode();
	}
	
	/**
	 * compares two FileClypeData objects
	 */
	public boolean equals (Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FileClypeData)) {
			return false;
		}
		
		FileClypeData clypedata = (FileClypeData)obj;
		if (clypedata.getData()!= null && this.fileContents != null) {
			if (!clypedata.getData().equals(this.fileContents)) {
				return false;
			}
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
		else if (clypedata.getFileName() != this.getFileName()) {
			return false;
		}
		return true;
	}
	
	/**
	 * prints a string representation of the FileClypeData object
	 */
	public String toString() {
		String stringRep = super.getUserName() + " sent the file \"" + this.getFileName() + "\" on " + super.getDate() + ". The user's type is currently set to ";
		switch (super.getType()) {
			case 0: stringRep += "0.";
			case 1: stringRep += "1.";
			case 2: stringRep += "2.";
			case 3: stringRep += "3.";
		}
		return stringRep;
	}
}
