package gossipSimulator;

import java.io.IOException;

public class RunNodes{

	public static void main(String args[]) throws IOException, InterruptedException{
		int nodePort;
		int firstPort = 1500;
		String jarPath = "C:\\Users\\michael\\Desktop\\node.jar";
		String cmdNewDir = "C:\\Users\\michael\\Desktop\\new1";
		for(int i = 0; i < 5; i++){
			nodePort = (firstPort + i);
			String command = "cmd.exe /c cd \"" + cmdNewDir + "\" & start cmd.exe /k \"java -jar " + jarPath;
			Runtime.getRuntime().exec(command + " " + nodePort);
			Thread.sleep(2000);
		}
	}
}
