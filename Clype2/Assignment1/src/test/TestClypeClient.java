package test;

import main.ClypeClient;

public class TestClypeClient {
	public static void main(String args[]) {
		ClypeClient clypeclient = new ClypeClient("John","Server",1500);
		ClypeClient clypeclient1 = new ClypeClient("Henry","Stan the Server",1500);
		ClypeClient clypeclient2 = new ClypeClient("John","Server",1500);
		
		/*System.out.println(clypeclient.getHostName());
		System.out.println(clypeclient.getUserName());
		System.out.println(clypeclient.getPort());
		System.out.println(clypeclient.hashCode());
		System.out.println(clypeclient.toString());
		System.out.println(clypeclient.equals(clypeclient1));
		System.out.println(clypeclient.equals(clypeclient2));*/
		
		clypeclient.start();
	}
}
