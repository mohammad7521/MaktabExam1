package chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        final BufferedReader in;
        final PrintWriter out;
        final Scanner scanner = new Scanner(System.in);
        final ServerSocket serverSocket;
        final Socket clientSocket;


        try {
            serverSocket = new ServerSocket(4000);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            Thread sender = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    while (true) {
                        msg = scanner.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            sender.start();



            Thread receiver=new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg=in.readLine();

                    while(msg!=null){
                        System.out.println("Client: "+msg);
                        msg=in.readLine();
                    }
                    System.out.println("client disconnected.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
