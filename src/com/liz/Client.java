package com.liz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

/**
 * Created by liz on 17/10/24.
 */
public class Client extends JFrame {
    private JPanel totalPanel;
    private JPanel secPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel portPanel;
    private JTextField urlField;
    private JTextField portField;
    private JPanel sendPanel;
    private JEditorPane sendEditorPane;
    private JPanel choosePanel;
    private JButton getTimeButton;
    private JButton getDateButton;
    private JButton getDirButton;
    private JButton sendButton;
    private JPanel receivePanel;
    private JEditorPane receiveEditorPane;
    private JLabel currLabel;

    Client() {
        super("Client");
        totalPanel = new JPanel();
        add(totalPanel);
        addPanel();

        this.setPreferredSize(new Dimension(600,500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void sendPacket(int type) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buf = new byte[256];
        String contents;
        if (type < 1 || type > 4) {
            receiveEditorPane.setText(receiveEditorPane.getText() + "function sendPacket type error!\n");
            return;
        }
        contents = String.valueOf(type) + "|";
        String notice = "";
        // 根据参数type，向Server发送不同的内容
        switch (type) {
            case 1:
                notice = "发送获取时间请求";
                break;
            case 2:
                notice = "发送获取日期请求";
                break;
            case 3:
//                if (sendEditorPane.getText().equals("")) {
//                    notice = "发送获取当前目录请求";
//                    contents += ".|";
//                    break;
//                }
//                notice = "发送获取" + sendEditorPane.getText() + "目录请求";
//                contents += sendEditorPane.getText() + "|";
                notice = "发送获取当前目录请求";
                contents += ".|";
                break;
            case 4:
                notice = "发送信息请求";
                contents += sendEditorPane.getText() + "|";
                break;
        }


        buf = contents.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(urlField.getText()), Integer.parseInt(portField.getText()));
        socket.send(packet);

        buf = new byte[256];
        packet = new DatagramPacket(buf,buf.length);
        // 此处设置了一个超时阀值（500ms），超时之后会在日志部分收到提示信息。
        // 否则会将正常获取到的Server信息打印至日志区域
        try {
            socket.setSoTimeout(500);
            socket.receive(packet);
        } catch (SocketTimeoutException e) {
            currLabel.setText("连接服务器失败,请重试");
            receiveEditorPane.setText(receiveEditorPane.getText() + "连接服务器失败,请重试\n");
            socket.close();
            return;
        }
        receiveEditorPane.setText(receiveEditorPane.getText() + notice +"成功\n");
        currLabel.setText("连接到服务器"+urlField.getText()+":"+portField.getText()+"成功!");
        String received = new String(packet.getData());
        receiveEditorPane.setText(receiveEditorPane.getText() + "接收到: " + received + "\n");
        socket.close();
    }

    private void addPanel() {
        totalPanel.setLayout(new BorderLayout());
        secPanel = new JPanel();
        secPanel.setLayout(new GridLayout(0,1));
        totalPanel.add(BorderLayout.CENTER,secPanel);
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        secPanel.add(topPanel);
        secPanel.add(bottomPanel);

        portPanel = new JPanel();
        portPanel.setLayout(new FlowLayout());
        topPanel.add(BorderLayout.NORTH,portPanel);
        portPanel.add(new JLabel("请输入服务器地址: "));
        urlField = new JTextField();
        urlField.setText("localhost");
        portPanel.add(urlField);
        portPanel.add(new JLabel("端口: "));
        portField = new JTextField();
        portField.setText("12000");
        portPanel.add(portField);


        sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());
        sendPanel.setBorder(BorderFactory.createTitledBorder("需要发送的内容"));
        topPanel.add(BorderLayout.CENTER,sendPanel);
        sendEditorPane = new JEditorPane();
        sendPanel.add(sendEditorPane);


        choosePanel = new JPanel();
        choosePanel.setLayout(new FlowLayout());
        getTimeButton = new JButton();
        getTimeButton.setText("获取时间");
        getTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendPacket(1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        getDateButton = new JButton();
        getDateButton.setText("获取日期");
        getDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendPacket(2);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        getDirButton = new JButton();
        getDirButton.setText("获取目录");
        getDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendPacket(3);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        sendButton = new JButton();
        sendButton.setText("发送信息");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendPacket(4);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        choosePanel.add(getTimeButton);
        choosePanel.add(getDateButton);
        choosePanel.add(getDirButton);
        choosePanel.add(sendButton);
        bottomPanel.add(BorderLayout.NORTH,choosePanel);

        receivePanel = new JPanel();
        receivePanel.setLayout(new BorderLayout());
        receivePanel.setBorder(BorderFactory.createTitledBorder("日志"));
        receiveEditorPane = new JEditorPane();
        receiveEditorPane.setEditable(false);
        receivePanel.add(receiveEditorPane);
        bottomPanel.add(BorderLayout.CENTER,receivePanel);

        currLabel = new JLabel();
        currLabel.setText("未连接到服务器");
        totalPanel.add(BorderLayout.SOUTH,currLabel);
    }

    public static void main(String[] args) {
        // write your code here
        Client client = new Client();
    }
}
