package main;

class ClientSideServerListener implements Runnable{

	
	private ClypeClient client;
	
	/**
	 * 
	 * @param client
	 */
	public ClientSideServerListener(ClypeClient client) {
		this.client = client;
	}
	
	/**
	 * runs thread
	 */
	public void run() {
		while (!client.closeConection) {
			client.recieveData();
			client.printData();
		}
		System.out.println("Connection closed\n");
	}
}
