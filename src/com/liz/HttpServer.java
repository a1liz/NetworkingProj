package com.liz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by liz on 17/11/12.
 */
public class HttpServer extends Thread {
    public static final int port = 8090;
    protected ServerSocket listen;

    private class Connect extends Thread {
        Socket client;
        BufferedReader bufferedReader;
        DataOutputStream dataOutputStream;
        public Connect(Socket s) {
            client = s;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                dataOutputStream = new DataOutputStream(client.getOutputStream());
            } catch (IOException e) {
                try {
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }
            this.start();
        }
        public void run() {
            try {
                String request = bufferedReader.readLine();
                System.out.println("Request: " + request);
                StringTokenizer stringTokenizer = new StringTokenizer(request);
                if ((stringTokenizer.countTokens() >= 2) && stringTokenizer.nextToken().equals("GET")) {
                    if ((request = stringTokenizer.nextToken()).startsWith("/"))
                        request = request.substring(1);
                    if (request.endsWith("/")||request.equals(""))
                        request = request + "index.html";
                    File f = new File("htdocs/" + request);
                    showDoc(dataOutputStream,f);
                } else {
                    dataOutputStream.writeBytes("400 Bad Request");
                }
                dataOutputStream.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void showDoc(DataOutputStream out, File f) throws Exception {
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                int len = (int) f.length();
                byte[] buf = new byte[len];
                in.readFully(buf);
                in.close();
                out.writeBytes("HTTP/1.1 200 OK\r\n");
                out.writeBytes("Content-Length: " + buf.length + " \r\n");
                out.writeBytes("Content-Type: text/HTML \r\n\r\n");
                out.write(buf);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                out.writeBytes("HTTP/1.1 404 "+"Not Found \r\n");
                out.writeBytes("Content-Type text/HTML \r\n\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public HttpServer() {
        try {
            listen = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.start();
    }

    public void run() {
        try {
            while(true) {
                Socket client = listen.accept();
                Connect cc = new Connect(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        new HttpServer();
    }

}
