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
                System.out.println("Log: request: " + request);
                if (request != "null");
                {
                    StringTokenizer stringTokenizer = new StringTokenizer(request);
                    if ((stringTokenizer.countTokens() >= 2) && stringTokenizer.nextToken().equals("GET")) {
                        if ((request = stringTokenizer.nextToken()).startsWith("/"))
                            request = request.substring(1);
                        if (request.endsWith("/") || request.equals(""))
                            request = request + "index.html";
                        showDoc(dataOutputStream, request);
                    } else {
                        dataOutputStream.writeBytes("400 Bad Request");
                    }
                    dataOutputStream.close();
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void showDoc(DataOutputStream out, String request) throws Exception {
            try {
                File f = new File("htdocs/" + request);
                String[] tmp = request.split("\\.");
                String fileExtension = tmp[tmp.length - 1];
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                int len = (int) f.length();
                byte[] buf = new byte[len];
                in.readFully(buf);
                in.close();

                String response = "HTTP/1.1 200 OK\r\n" + "Content-Length: " + buf.length + " \r\nContent-Type: " + getContentType(fileExtension) + "\r\n\r\n";
                out.writeBytes(response);
                out.write(buf);
                System.out.println("Log: send " + response);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                String response = "HTTP/1.1 404 Not Found \r\n" + "Content-Type text/HTML \r\n\r\n" + "<!DOCTYPE html><html><head><title> 404 NOT FOUND</title></head><body>404 not found!</body></html>";
                out.writeBytes(response);
                System.out.println("Log: send " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private String getContentType(String s) {
            String type;
            switch (s) {
                case "html":
                    type = "text/html;charset=UTF-8";
                    break;
                case "htm":
                    type = "text/html;charset=UTF-8";
                    break;
                case "png":
                    type = "image/png";
                    break;
                case "gif":
                    type = "image/gif";
                    break;
                case "jpg":
                    type = "image/jpeg";
                    break;
                case "jpeg":
                    type = "image/jpeg";
                    break;
                case "css":
                    type = "text/css";
                    break;
                case "js":
                    type = "application/x-javascript";
                    break;
                case "bmp":
                    type = "application/x-bmp";
                    break;
                default:
                    type = "text/plain;charset=gbk";
                    break;
            }
            return type;
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
