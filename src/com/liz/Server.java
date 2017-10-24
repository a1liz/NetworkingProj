package com.liz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server extends JFrame {
    private JPanel totalPanel;
    private JPanel topPanel;
    private JPanel portPanel;
    private JTextField portField;
    private JLabel portLabel;
    private JButton portButton;
    private JPanel receivePanel;
    private JEditorPane receiveEditorPane;
    private JLabel currLabel;
    private DatagramSocket socket;

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
        public DatagramServer(DatagramSocket socket) {
            this.socket = socket;
            start();
        }

        public void run() {
            while(true) {
                try {
                    byte[] buf = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    // 若socket已被关闭,则会抛出SocketException,跳出循环
                    try {
                        socket.receive(packet);
                    } catch (SocketException e) {
                        break;
                    }
                    String received = new String(packet.getData());
                    String[] strings = received.split("\\|");
                    receiveEditorPane.setText(receiveEditorPane.getText() + "Received from client= " + packet.getAddress().getHostName() + ":" + packet.getPort() + "\n");
                    int type = Integer.parseInt(strings[0]);
                    String contents = "";
                    String notice = "";
                    SimpleDateFormat df;
                    switch (type) {
                        case 1:
                            df = new SimpleDateFormat("HH:mm:ss");
                            contents += df.format(new Date());
                            notice = "接收到获取时间请求";
                            break;
                        case 2:
                            df = new SimpleDateFormat("yyyy-MM-dd");
                            contents += df.format(new Date());
                            notice = "接收到获取日期请求";
                            break;
                        case 3:
                            File file = new File(strings[1]);
                            File[] files = file.listFiles();
                            for (File f : files) {
                                contents += f.getName() + "\t";
                            }
                            notice = "接收到获取目录请求";
                            break;
                        case 4:
                            contents += "服务器已接受到消息: " + strings[1];
                            notice = "接收到信息";
                            break;
                    }
                    receiveEditorPane.setText(receiveEditorPane.getText() + notice + ":" + contents + "\n");
                    buf = contents.getBytes();
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(buf, buf.length, address, port);
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        portButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(portButton.getText().equals("Start")) {
                    // 当端口不为数字时报错
                    if (!portField.getText().equals(String.valueOf(Integer.parseInt(portField.getText()))))
                        receiveEditorPane.setText(receiveEditorPane.getText() + "端口填写错误,请填写一个1~65535间的数字\n");
                    else {
                        // 正常开启服务
                        portButton.setText("Stop");
                        currLabel.setText("服务启动,端口为:" + portField.getText());
                        try {
                            socket = new DatagramSocket(Integer.parseInt(portField.getText()));
                        } catch (SocketException e1) {
                            e1.printStackTrace();
                        }
                        // 开启DatagramServer线程
                        new DatagramServer(socket);
                    }
                } else if (portButton.getText().equals("Stop")) {
                    // 关闭socket,会导致已开启的DatagramServer线程因获取到SocketException而停止
                    socket.close();
                    currLabel.setText("服务未启动");
                    portButton.setText("Start");
                } else {
                    receiveEditorPane.setText(receiveEditorPane.getText() + "Button Error!!!\n");
                }
            }
        });
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
