/**
 * CS 640 - Computer Networks
 * Assignment 1
 */
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.time.Instant;
import java.time.Duration;

public class Iperfer1 {
    private static String errorMessage = "Error: missing or additional arguments";
	private static String portNumberError = "Error: port number must be in the range 1024 to 65535";

    private static void printError(String message){
		System.out.println(message);
		System.exit(0);
	}
    //sends chunks

    public static void main(String[] args) {
        //checks to see if its in client mode first
        if ((args.length == 7 && args[0].equals("-c"))){
            clientMode(args[2], Integer.parseInt(args[4]), Integer.parseInt(args[6]));
        }
        //checks to see if its server if not client mode
        else if (args.length == 3 && args[0].equals("-s")){
            serverMode(Integer.parseInt(args[2]));
        }
        //args not correct causes error
        else{
            printError(errorMessage);
        }
    }

    public static void clientMode(String hostname, int portNumber, int size) {

        if (portNumber < 1024 || portNumber > 65535){
            printError(portNumberError)            
        }
            byte[] newChunk = new byte[1000];
            Arrays.fill(newChunk, (byte)0);
            //creates new socket
            Socket sockets = new Socket(hostname, portNumber);
            OutputStream getOut = sockets.getOutputStream();
            Instant start = Instant.now();
            Instant end = Instant.now();
            Duration timer = Duration.between(start, end);
            long sizeKB = 0;
            //while loops to find size
            while (timer.toSeconds() < size){
                end = Instant.now();
                timer = Duration.between(start, end);
                getOut.write(newChunk);
                sizeKB++;
            }
            getOut.write(-1);
            double sizeMB = sizeKB/1000;
            System.out.println("sent=" + sizeKB + " KB rate=" + sizeKB/size + " Mbps");
            getOut.close();
            sockets.close();
    }
    
    public static void serverMode(int portNumber) {
        if (portNumber < 1024 || portNumber > 65535){
            printError(portNumberError);
        }

        // long totalBytes = 0;
        // ServerSocket serverSock = new ServerSocket(portNumber);
        // Socket sock = serverSock.accept();
        // InputStream input = sock.getInputStream();
        // Instant start = Instant.now(); 
        // int total;
        // while((total = input.read()) != -1){
        //     totalBytes += total + 1;
        // }
        // Instant end = Instant.now();
        // Duration limit = Duration.between(start, end);
        // long timer = limit.toSeconds();
        // long sizeKB = totalBytes/1000;
        // double sizeMB = sizeKB/1000.0;
        // System.out.println("sent=" + sizeKB + " KB rate=" + sizeMB/timer + " Mbps");
        // serverSock.close();
    }

}