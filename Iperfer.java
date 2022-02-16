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

    public static void client(String hostName, int portNumber, int time){

        try (
            Socket tcpSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(tcpSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(tcpSocket.getInputStream()));
        ) {
                long totalTime = (long) (time*Math.pow(10,9));
                long startTime = System.nanoTime();
                boolean toFinish = false;
                long totalNumberOfBytes = 0;
                while(!toFinish){
                    byte[] dataChunk = new byte[1000];
                    totalNumberOfBytes+=(long)1000;
                    Arrays.fill(dataChunk, (byte)0);
                    out.println(dataChunk);
                    in.readLine();
                    toFinish = (System.nanoTime() - startTime >= totalTime);
                }
                int sentInKB = (int) (totalNumberOfBytes/1000);
                long rate = (totalNumberOfBytes/(long)Math.pow(10, 6))/time;
                System.out.print("sent="+sentInKB+"KB rate="+rate+"Mbps");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }

    private static void client2(String hostName, int portNumber, int time) {
        if(portNumber < 1024 || portNumber > 65535) {
            System.err.println("Error: port number must be in the range 1024 to 65535");
            System.exit(1);
        }

        try {
            Socket clientSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            long start = System.nanoTime();
            long nstime = (long) (time*Math.pow(10, 9));
            double sentKB = 0;
            
            //while (current time - start time < total time)
            while(System.nanoTime() - start < nstime) {
                byte[] chunk = new byte[1000];
                Arrays.fill(chunk, (byte)0);
                System.out.println("going");
                out.println(chunk);
                in.readLine();
                sentKB++;
            }
            double rate = (sentKB/1000.0)/(double)time;
            System.out.println("sent = " + sentKB+ "KB rate = " + rate + "Mbps");

            // long totalTime = (long) (time*Math.pow(10,9));
    		// long startTime = System.nanoTime();
    		// boolean toFinish = false;
    		// long totalNumberOfBytes = 0;
    		// while(!toFinish){
	        // 	byte[] dataChunk = new byte[1000];
	        // 	totalNumberOfBytes+=(long)1000;
	        // 	Arrays.fill(dataChunk, (byte)0);
	        //     out.println(dataChunk);
	        //     in.readLine();
	        //     toFinish = (System.nanoTime() - startTime >= totalTime);
    		// }
    		// int sentInKB = (int) (totalNumberOfBytes/1024);
    		// long rate = (totalNumberOfBytes/(long)Math.pow(2,20 ))/time;
    		// System.out.print("sent="+sentInKB+"KB rate="+rate+"Mbps");
        } catch(UnknownHostException e) {
            System.err.println(e);
            System.exit(1);
        } catch(IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        
    }

    private static void server2(int portNumber) {
        if(portNumber < 1024 || portNumber > 65535) {
            System.err.println("Error: port number must be in the range 1024 to 65535");
            System.exit(1);
        }

        long start = 0;
        double receivedKB = 0;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            String input;
            start = System.nanoTime();
            while((input = in.readLine()) != null) {
                out.println();
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
        // try (
        //     ServerSocket serverSocket =
        //         new ServerSocket(portNumber);
        //     Socket clientSocket = serverSocket.accept();     
        //     PrintWriter out =
        //         new PrintWriter(clientSocket.getOutputStream(), true);                   
        //     BufferedReader in = new BufferedReader(
        //         new InputStreamReader(clientSocket.getInputStream()));
        // ) {
        //     String inputLine;
        //     boolean firstTime = true;
        //     while ((inputLine = in.readLine()) != null) {
        //     	if(firstTime){
        //     		startTime = System.nanoTime();
        //     		firstTime = false;
        //     	}
        //     	totalSize+=1000;
        //         out.println(inputLine);
        //     }
        // } catch (IOException e) {
        //     System.out.println("Exception caught when trying to listen on port "
        //         + portNumber + " or listening for a connection");
        //     System.out.println(e.getMessage());
        // }finally{
        // 	int recievedInKB = (int) (totalSize/1024);
        // 	long time = System.nanoTime() - startTime;
    	// 	long rate = (long) ((totalSize/(long)Math.pow(2,20 ))/(time/Math.pow(10,9)));
    	// 	System.out.print("sent="+recievedInKB+"KB rate="+rate+"Mbps");
        // }
    }

    private static void server(int portNumber) {
        long totalSize = 0;
        long startTime = 0;
    try (
            ServerSocket serverSocket =
                new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();     
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            boolean firstTime = true;
            while ((inputLine = in.readLine()) != null) {
                if(firstTime){
                    startTime = System.nanoTime();
                    firstTime = false;
                }
                totalSize+=1000;
                out.println(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }finally{
            int recievedInKB = (int) (totalSize/1024);
            long time = System.nanoTime() - startTime;
            long rate = (long) ((totalSize/(long)Math.pow(2,20 ))/(time/Math.pow(10,9)));
            System.out.print("sent="+recievedInKB+"KB rate="+rate+"Mbps");
        }
    }
}