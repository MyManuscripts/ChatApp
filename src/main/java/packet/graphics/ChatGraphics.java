package packet.graphics;

import packet.clientPack.ClientChat;

import javax.swing.*;        // для работы с графическим интерфейсом
import java.awt.*;           // оконная библоиотека графического интерфейса
import  java.awt.event.*;    // для работы с  событиями (нажатие кнопки)
import java.io.IOException;  // исключения

public class ChatGraphics extends JFrame {
    private JTextArea messageArea; // многострочная область для отображения текста
    private JTextField textField; // создание новых сообщений
    private ClientChat client;

    public ChatGraphics(){
        super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // операция призакрытии файла

        messageArea = new JTextArea();
        messageArea.setEditable(false); // доступен ли  текст для редактирования
        add(new JScrollPane(messageArea), BorderLayout.CENTER); // JScrollPane - экран с прокруткой, BorderLayout - расположение окна

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                client.sendMessage(textField.getText()); // добавим метод sendMessage в ClientChat
                textField.setText("");
            }
        });
        add(textField, BorderLayout.SOUTH);

        // Инициализация и запуск чат клиента
        try {
            this.client = new ClientChat("127.0.0.1",50728, this::onMessageReceived);
            client.startClient();
        }catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "error connecting to server",
                    "connection error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    private void onMessageReceived(String message){
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatGraphics().setVisible(true);
        });
    }
}
