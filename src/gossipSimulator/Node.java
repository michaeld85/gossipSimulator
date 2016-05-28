package gossipSimulator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Node {

	private int id;
	private int heartbit;
	private int value;
	private boolean up;
	private int[] me = new int[3];;
	private List<int[]> nodes;
	private Scanner reader;

	public static void main(String args[]){
		int i = Integer.parseInt(args[0]);
		Node n = new Node(i);
		n.start();
	}

	public Node(int number) {
		id = number;
		heartbit = 0;
		value = 0;
		up = true;
		nodes = new ArrayList<int[]>();
		me[0] = id;
		me[1] = heartbit;
		me[2] = value;
		nodes.add(me);
		this.reader = new Scanner(System.in);
	}

	public void start(){
		Thread counterThread = new Thread () {
			public void run () {
				while(up){
					try {
						me[1] = heartbit++;
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread listenerThread = new Thread () {
			public void run(){
				listen();
			}
		};
		Thread actionsThread = new Thread () {
			public void run () {
				connectToOthers();
				getUserInput();
			}
		};
		counterThread.start();
		listenerThread.start();
		actionsThread.start();
	}

	private void connectToOthers() {
		System.out.println("Hello, im " + this.id + ". Trying to connect to others.");
		String firstMsg = me[0] + "," + me[1] + "," + me[2] + "," + me[0] + ",";
		for(int i = 1; i < 4; i++){
			int nodeToTry = (this.id - i); 
			gossipWithNode(nodeToTry, firstMsg);
		}
	}

	private void getUserInput(){
		int newVal;
		while(up){
			System.out.println("Enter new val(0-9) for me: (11=list nodes | 00=shut down)");
			newVal = reader.nextInt();
			if(newVal == 00){
				up = false;
			} else if(newVal == 11){
				printNodeList();
			} else if(newVal < 10 && newVal >= 0){
				me[2] = newVal;
				sendToNodes(0);
			}
		}
		System.out.println("node num (" + id + ") is down");
		printNodeList();
	}

	public void sendToNodes(int notToThisNode){
		Random random = new Random();
		int r = random.nextInt(nodes.size() - 1);
		if(nodes.get(r + 1)[0] == notToThisNode && nodes.size() != 2){
			if(r == (nodes.size() - 2)){
				r--;
			} else{
				r++;
			}
		}
		String toSend = "";
		for(int[] node: nodes){
			toSend = node[0] + "," + node[1] + "," + node[2] + "," + me[0] + ",";
			gossipWithNode(nodes.get(r + 1)[0], toSend);
		}
	}

	public void gossipWithNode(int id, String sentence){
		try{
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.setSoTimeout(5000);
			InetAddress IPAddress = InetAddress.getByName("localhost");
			byte[] sendData = new byte[15];
			byte[] receiveData = new byte[15];
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, id);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try{
				clientSocket.receive(receivePacket);
			}catch(SocketTimeoutException e){
				clientSocket.close();
				return;
			}
			clientSocket.close();
		}catch(Exception e){
			System.out.println("Sending message failed, node still on.");
		}
	}

	@SuppressWarnings("resource")
	public void listen(){
		try{
			DatagramSocket serverSocket = new DatagramSocket(id);
			byte[] receiveData = new byte[15];
			byte[] sendData = new byte[15];
			while(up){
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String( receivePacket.getData());
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
				int fromWho = addDataToList(sentence);
				if(fromWho != 0){
					sendToNodes(fromWho);
				}
			}
		}catch(Exception e){
			System.out.println("Receiving message failed, node still on.");
		}
	}

	public int addDataToList(String sentence){
		String[] x = sentence.split(",");
		int nodeNum = Integer.parseInt(x[0]);
		int nodeHeartbit = Integer.parseInt(x[1]);
		int nodeVal = Integer.parseInt(x[2]);
		int from = Integer.parseInt(x[3]);
		for(int[] node: nodes){
			if(nodeNum == node[0]){
				if(node[2] == nodeVal){
					node[1] = nodeHeartbit;
					return 0;
				} else if(nodeHeartbit > node[1]){
					node[2] = nodeVal;
				}
				node[1] = nodeHeartbit;
				return from;
			}
		}
		int[] newNode = {nodeNum, nodeHeartbit, nodeVal};
		nodes.add(newNode);
		return from;
	}

	public void printNodeList(){
		System.out.println("DATA:");
		System.out.println("-------------");
		for(int[] node : nodes){
			System.out.println(node[0] + ", " + node[1] + ", " + node[2]);
		}
		System.out.println("-------------");
	}

	public void deleteDeadNode(int[] node){
		System.out.println("I am gessing " + node[0] + " is dead");
		nodes.remove(node);
	}
}
