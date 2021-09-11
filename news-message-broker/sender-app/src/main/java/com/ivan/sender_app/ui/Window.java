package com.ivan.sender_app.ui;

import com.ivan.sender_app.business_logic.SenderBusinessLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

public class Window extends JFrame {
    private static Logger log = LoggerFactory.getLogger(Window.class);
    private final SenderBusinessLogic senderBusinessLogic;

    public Window(String name, SenderBusinessLogic senderBusinessLogic) {
        super(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 300);
        this.senderBusinessLogic = senderBusinessLogic;

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.YELLOW);


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
        bottomPanel2.add(new JLabel("Enter Host"));
        JTextField ipTextField = new JTextField(9);
        ipTextField.setText("127.0.0.1");
        bottomPanel2.add(ipTextField);
        bottomPanel2.add(new JLabel("Enter Port"));
        JTextField portTextField = new JTextField(4);
        portTextField.setText("8080");
        bottomPanel2.add(portTextField);
        JButton reconnectButton = new JButton("Reconnect");
        reconnectButton.addActionListener(event -> {

            try {
                int port = Integer.parseInt(portTextField.getText());
                senderBusinessLogic.reconnectAsync(ipTextField.getText(), port);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(getParent(),
                        "Invalid port.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

        });
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
