package test;

import main.ClypeServer;

public class testClypeServer {

	public static void main(String[] args) {
		ClypeServer clypeserver = new ClypeServer(1500);
		ClypeServer clypeserver1 = new ClypeServer(1500);
		
		System.out.println(clypeserver.getPort());
		System.out.println(clypeserver.hashCode());
		System.out.println(clypeserver.toString());
		System.out.println(clypeserver.equals(clypeserver1));

	}

}
