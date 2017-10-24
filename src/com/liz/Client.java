package com.liz;

import javax.swing.*;
import java.awt.*;

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
    private JComboBox sendComboBox;
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
        getDateButton = new JButton();
        getDateButton.setText("获取日期");
        getDirButton = new JButton();
        getDirButton.setText("获取当前目录");
        sendButton = new JButton();
        sendButton.setText("发送信息");
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
