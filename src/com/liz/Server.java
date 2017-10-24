package com.liz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends JFrame {
    private JPanel totalPanel;
    private JPanel secPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel portPanel;
    private JTextField portField;
    private JLabel portLabel;
    private JButton portButton;
    private JPanel receivePanel;
    private JEditorPane receiveEditorPane;
    private JLabel currLabel;

    Server() {
        super("Server");
        totalPanel = new JPanel();
        add(totalPanel);
        addPanel();

        this.setPreferredSize(new Dimension(300,400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private class DatagramServer extends Thread {
        DatagramSocket socket;
        DatagramPacket packet;
        int client;
        public DatagramServer(DatagramSocket socket, DatagramPacket packet, int client) {
            this.socket = socket;
            this.packet = packet;
            this.client = client;
            start();
        }

        public void run() {
            try {
                byte[] received = packet.getData();
                receiveEditorPane.setText(receiveEditorPane.getText() + "Received from client= " + client + " " + new String(received));
//                byte[] buf = received;
//                InetAddress address = packet.getAddress();
//                int port = packet.getPort();
//                packet = new DatagramPacket(buf, buf.length, address, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void addPanel() {
        totalPanel.setLayout(new BorderLayout());
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        totalPanel.add(BorderLayout.CENTER,topPanel);

        portPanel = new JPanel();
        portPanel.setLayout(new FlowLayout());
        topPanel.add(BorderLayout.NORTH,portPanel);
        portLabel = new JLabel();
        portLabel.setText("请输入服务端口号: ");
        portPanel.add(portLabel);
        portField = new JTextField();
        portField.setText("12000");
        portPanel.add(portField);
        portButton = new JButton();
        portButton.setText("Start");
//        portButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(portButton.getText().equals("Start")) {
//                    if (!portField.getText().equals(String.valueOf(Integer.parseInt(portField.getText()))))
//                        receiveEditorPane.setText(receiveEditorPane.getText() + "端口填写错误,请填写一个1~65535间的数字\n");
//                    else {
//                        byte[] buf = new byte[256];
//                        try {
//                            DatagramSocket socket = new DatagramSocket(Integer.parseInt(portField.getText()));
//                            DatagramPacket packet = new DatagramPacket(buf, 256);
//                            receiveEditorPane.setText(receiveEditorPane.getText() + "服务启动,端口为:" + portField.getText());
//                            socket.receive(packet);
//                        } catch (SocketException e1) {
//                            receiveEditorPane.setText(receiveEditorPane.getText() + e1.toString() + " Error!!!\n");
//                        } catch (IOException e1) {
//                            receiveEditorPane.setText(receiveEditorPane.getText() + e1.toString() + " Error!!!\n");
//                        }
//
//
//                    }
//                } else if (portButton.getText().equals("Stop")) {
//
//                } else {
//                    receiveEditorPane.setText(receiveEditorPane.getText() + "Button Error!!!\n");
//                }
//            }
//        });
        portPanel.add(portButton);

        receivePanel = new JPanel();
        receivePanel.setLayout(new BorderLayout());
        receivePanel.setBorder(BorderFactory.createTitledBorder("日志"));
        receiveEditorPane = new JEditorPane();
        receiveEditorPane.setEditable(false);
        receivePanel.add(receiveEditorPane);
        topPanel.add(BorderLayout.CENTER,receivePanel);

        currLabel = new JLabel();
        currLabel.setText("服务未启动");
        totalPanel.add(BorderLayout.SOUTH,currLabel);
    }

    public static void main(String[] args) {
	// write your code here
        Server server = new Server();
    }
}
