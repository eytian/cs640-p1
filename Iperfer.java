/**
 * UW Madison CS640
 * Annabelle Shultz & Eric Tian
 * Assignment 1
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

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

    /**
     * This method is called when the client command is used. It sends packets, or byte[]
     * arrays of 1000 size.
     * @param hostName string name of the host to send data to
     * @param portNumber int port number of the server
     * @param time int time in seconds to send data
     */
    private static void client(String hostName, int portNumber, int time) {
        if(portNumber < 1024 || portNumber > 65535) {
            System.err.println("Error: port number must be in the range 1024 to 65535");
            System.exit(1);
        }

        try (
            Socket clientSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        )   {
            long start = System.nanoTime();
            long nstime = (long) (time*Math.pow(10, 9));
            double sentKB = 0;
            //while (current time - start time < total time)
            while(System.nanoTime() - start < nstime) {
                byte[] chunk = new byte[1000];
                Arrays.fill(chunk, (byte)0);
                out.println(chunk);
                in.readLine();
                sentKB++;
            }
            double rate = (sentKB/1000.0)/(double)time;
            System.out.println("sent = " + sentKB+ "KB rate = " + rate + "Mbps");
        } catch(UnknownHostException e) {
            System.err.println(e);
            System.exit(1);
        } catch(IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * This method is called when the server mode command is used. It opens a server socket and
     * listens for a client connection on the port number.
     * @param portNumber int port number
     */
    private static void server(int portNumber) {
        if(portNumber < 1024 || portNumber > 65535) {
            System.err.println("Error: port number must be in the range 1024 to 65535");
            System.exit(1);
        }

        long start = 0;
        double receivedKB = 0;
        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        )   {
            String input;
            start = System.nanoTime();
            while((input = in.readLine()) != null) {
                out.println(input);
                receivedKB++;
            }
        } catch(IOException e) {
            System.err.println(e);
            System.exit(1);
        } finally {
            long time = (System.nanoTime() - start)/(long) Math.pow(10, 9);
            double rate = (receivedKB/1000.0)/time;
            System.out.println("sent = " + receivedKB + "KB rate = " + rate + "Mbps");
        }
    }
}