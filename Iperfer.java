import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


class Iperfer {
    public static void main(String[] args) {
        //input parsing
        if(args.length == 7 && args[0].equals("-c")) {
            client(args[2], Integer.parseInt(args[4]), Integer.parseInt(args[6]));
        } else if(args.length == 3 && args[0].equals("-s")) {
            server(Integer.parseInt(args[2]));
        } else {
            System.err.println("Error: missing or additional arguments");
            System.exit(1);
        }
        
    }

    private static void client(String hostName, int portNumber, int time) {
        if(portNumber < 1024 || portNumber > 65535) {
            System.err.println("Error: port number must be in the range 1024 to 65535");
            System.exit(1);
        }

        try {
            Socket clientSocket = new Socket(hostName, portNumber);
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            
        } catch(UnknownHostException e) {
            System.err.println(e);
            System.exit(1);
        } catch(IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        
    }

    private static void server(int portNumber) {
        if(portNumber < 1024 || portNumber > 65535) {
            System.err.println("Error: port number must be in the range 1024 to 65535");
            System.exit(1);
        }
    }
}