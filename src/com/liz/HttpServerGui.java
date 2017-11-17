package com.liz;

import javax.swing.*;
import java.awt.*;

/**
 * Created by liz on 17/11/17.
 */
public class HttpServerGui extends JFrame {
    private JPanel panel;
    private JTextArea logArea;
    HttpServerGui() {
        super("Client");
        panel = new JPanel();
        add(panel);
        logArea = new JTextArea();
        logArea.setLineWrap(true);
        logArea.setEnabled(false);
        logArea.setBounds(0,0,600,500);
        add(logArea);

        this.setPreferredSize(new Dimension(600,500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void setLog(String s) {
        logArea.setText(logArea.getText() + "\n" + s);
    }

    public static void main(String[] args) {
        new HttpServerGui();
    }
}
