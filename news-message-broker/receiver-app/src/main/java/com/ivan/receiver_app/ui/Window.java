package com.ivan.receiver_app.ui;

import com.ivan.common_module.models.ConnectionType;
import com.ivan.receiver_app.business_logic.ReceiverBusinessLogic;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

public class Window extends JFrame {
    // private static Logger log = LoggerFactory.getLogger(Window.class);
    private final ReceiverBusinessLogic receiverBusinessLogic;
    private final JTextArea out;

    public Window(String name, ReceiverBusinessLogic receiverBusinessLogic) {
        super(name);

        this.receiverBusinessLogic = receiverBusinessLogic;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 300);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.lightGray);
        topPanel.add(new JLabel("Enter Topic"));
        JTextField topicTextField = new JTextField(10);
        topPanel.add(topicTextField);

        JRadioButton tcpSocket = new JRadioButton("TCP Socket", true);
        JRadioButton gRpc = new JRadioButton("gRPC proto", false);
        {
            ButtonGroup group = new ButtonGroup();
            group.add(tcpSocket);
            group.add(gRpc);
            JPanel panel = new JPanel();
            panel.add(tcpSocket);
            panel.add(gRpc);

            topPanel.add(panel);
        }

        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new GridLayout(3, 0));
        out = new JTextArea(10, 30);
        out.setBackground(Color.WHITE);
        out.setEditable(false);
        out.setText("");

        receiverBusinessLogic.setReceiveCallback(n -> {
            String text = String.format("Topic:%s\nCategory: %s\nAuthor: %s\nText:%s\n\n",
                    n.getTopic(), n.getCategory(), n.getAuthor(), n.getContent());

            out.setText(out.getText() + text);
        });

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

                ConnectionType connectionType = tcpSocket.isSelected()
                        ? ConnectionType.TCP_SOCKET
                        : gRpc.isSelected()
                                ? ConnectionType.GRPC_PROTO
                                : null;
                String host = ipTextField.getText();
                int port = Integer.parseInt(portTextField.getText());
                String topic = topicTextField.getText();

                reconnect(host, port, connectionType, topic);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(getParent(),
                        "Invalid port.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

        });
        bottomPanel2.add(reconnectButton);

        JScrollPane scrollPane = new JScrollPane(out);
        JPanel centerPanel = new JPanel();
        centerPanel.add(BorderLayout.NORTH, scrollPane);

        centerPanel.add(BorderLayout.SOUTH, bottomPanel1);

        this.getContentPane().add(BorderLayout.SOUTH, bottomPanel2);
        this.getContentPane().add(BorderLayout.CENTER, centerPanel);
        this.getContentPane().add(BorderLayout.NORTH, topPanel);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    private void reconnect(String host, int port, ConnectionType connectionType, String topic) {

        if (topic == null || "".equals(topic)) {
            JOptionPane.showMessageDialog(getParent(),
                    "Please set a valid topic",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        receiverBusinessLogic.reconnectAsync(host, port, connectionType, topic);
    }

}
