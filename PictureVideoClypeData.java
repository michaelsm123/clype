package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PictureVideoClypeData extends ClypeData {

	byte[] imageBytes;
	
	/**
	 * 
	 * @param username
	 * @param type
	 * @param imageBytes
	 */
	public PictureVideoClypeData(String username, int type, byte[] imageBytes) {
		super(username,type);
		this.imageBytes = imageBytes;
	}
	
	/**
	 * default constructer
	 */
	public PictureVideoClypeData() {
		
	}
	
	/**
	 * 
	 * @return imageBytes
	 */
	public byte[] getBytes() {
		return imageBytes;
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getData(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
