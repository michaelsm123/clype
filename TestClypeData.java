package test;

import data.FileClypeData;
import data.MessageClypeData;

public class TestClypeData {
	public static void main(String args[]) {
	
		MessageClypeData messageclypedata = new MessageClypeData("Cat","Hello friend",1);
		FileClypeData fileclypedata = new FileClypeData("Dog","Bones",1);
		MessageClypeData message1clypedata = new MessageClypeData("Cat","Meow",1);
		FileClypeData file1clypedata = new FileClypeData("Dog","Woof",1);		
		MessageClypeData message2clypedata = new MessageClypeData("Cat","Hello friend",1);
		FileClypeData file2clypedata = new FileClypeData("Dog","Bones",1);		
		
		MessageClypeData data = new MessageClypeData();
		FileClypeData file = new FileClypeData("John","C:\\Users\\Matt\\Downloads\\document.txt",2);
		String key = "EfcasS";
		/*System.out.println(messageclypedata.getType());
		System.out.println(fileclypedata.getType());
		System.out.println(messageclypedata.getUserName());
		System.out.println(fileclypedata.getUserName());
		System.out.println(messageclypedata.getDate());
		System.out.println(fileclypedata.getDate());
		System.out.println(messageclypedata.getData());
		System.out.println(fileclypedata.getData());
		System.out.println(messageclypedata.hashCode());
		System.out.println(fileclypedata.hashCode());
		System.out.println(messageclypedata.toString());
		System.out.println(fileclypedata.toString());
		System.out.println(messageclypedata.getType());
		System.out.println(fileclypedata.getType());
		System.out.println(messageclypedata.equals(message1clypedata));
		System.out.println(fileclypedata.equals(file1clypedata));
		System.out.println(messageclypedata.equals(message2clypedata));
		System.out.println(fileclypedata.equals(file2clypedata));
		System.out.println(fileclypedata.getFileName());
		fileclypedata.setFileName("Skeleton");
		System.out.println(fileclypedata.getFileName());*/
		
		String encrypted = data.encrypt("Hello my name is Jeff",key);
		System.out.println(encrypted);
		String decryptred = data.decrypt(encrypted, key);
		System.out.println(decryptred);
		
		file.readFileContents(key);
		System.out.println(file.getData());
		System.out.println(file.getData(key));
		file.writeFileContents(key);
		System.out.println(file.getData());
		System.out.println(file.getData(key));
	}
}
