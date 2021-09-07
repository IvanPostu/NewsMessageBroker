package com.ivan.sender_app.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

public class Window extends JFrame {
    public Window(String name) {
        super(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 300);

        JPanel topPanel = new JPanel();
        JLabel connectionStatusLabel = new JLabel(Constants.CONNECTION_STATUS_TEXT + "None");
        topPanel.setBackground(Color.YELLOW);
        topPanel.add(connectionStatusLabel);


        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new GridLayout(3, 0));
        JTextField categoryTextField = new JTextField(10);
        JTextField authorTextField = new JTextField(10);

        {
            JPanel p = new JPanel();
            p.add(new JLabel("Enter Category"));
            p.add(categoryTextField);
            bottomPanel1.add(p, 0, 0);
        }
        {
            JPanel p = new JPanel();
            p.add(new JLabel("Enter Author"));
            p.add(authorTextField);
            bottomPanel1.add(p, 1, 0);
        }

        bottomPanel1.add(new JButton("Send news"), 2, 0);

        JPanel bottomPanel2 = new JPanel();
        bottomPanel2.setBackground(Color.LIGHT_GRAY);
        bottomPanel2.add(new JLabel("Enter IP"));
        JTextField ipTextField = new JTextField(9);
        bottomPanel2.add(ipTextField);
        bottomPanel2.add(new JLabel("Enter Port"));
        JTextField portTextField = new JTextField(4);
        bottomPanel2.add(portTextField);
        JButton reconnectButton = new JButton("Reconnect");
        bottomPanel2.add(reconnectButton);

        JTextArea ta = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(ta);
        JPanel centerPanel = new JPanel();
        centerPanel.add(BorderLayout.NORTH, scrollPane);
        centerPanel.add(BorderLayout.SOUTH, bottomPanel1);

        this.getContentPane().add(BorderLayout.SOUTH, bottomPanel2);
        this.getContentPane().add(BorderLayout.CENTER, centerPanel);
        this.getContentPane().add(BorderLayout.NORTH, topPanel);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}
