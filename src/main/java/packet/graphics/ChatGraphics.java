package packet.graphics;

import packet.clientPack.ClientChat;

import javax.swing.*;        // для работы с графическим интерфейсом
import java.awt.*;           // оконная библоиотека графического интерфейса
import  java.awt.event.*;    // для работы с  событиями (нажатие кнопки)
import java.io.IOException;  // исключения
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatGraphics extends JFrame {
    private JTextArea messageArea; // многострочная область для отображения текста
    private JTextField textField; // создание новых сообщений
    private ClientChat client;

    public ChatGraphics(){

        // Заголовок
        super("Chat application");
        setSize(400,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Стили, цвета, шрифты
        Color backgroundColor = new Color(240,240,240);
        Color textColor = new Color(50,50,50);
        Font textFont = new Font("Arial",Font.PLAIN,14);


        // Запрос на ввод имени пользователя при запуске приложения
        String name = JOptionPane.showInputDialog(this,"Enter your name:",
                "name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat application - " + name); // Укажите в заголовке окна имя пользователя
        // используем имя, чтобы обновить заголовок

//-----------------------------------------------------------------------------------------------

        messageArea = new JTextArea();
        messageArea.setEditable(false); // доступен ли  текст для редактирования
        add(new JScrollPane(messageArea), BorderLayout.CENTER); // JScrollPane - экран с прокруткой, BorderLayout - расположение окна
//------------------------------------------------------------------------------------------------
        // ActionListener получает запись при выполнении действия
        textField = new JTextField();

        // Стили текстового поля
        textField.setFont(textFont);
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);

        textField.addActionListener(new ActionListener() {

            // Когда пользователь нажиммает Enter создается строка сообщения
            // Новая строка сообщения включает метку времени и имя пользователя
            // Это отправляется на сервер через client.SendMessage метод
            // После отправки сообщения текстовое поле очищается и готово для следующего сообщения.

            public void actionPerformed(ActionEvent event /* e */) {
                textField.addActionListener(e-> {
                    String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " +
                            name + ": " + textField.getText();
                    client.sendMessage(message);
                    textField.setText("");
                });


                /*client.sendMessage(textField.getText()); // добавим метод sendMessage в ClientChat
                textField.setText("");*/
            }
        });
        add(textField, BorderLayout.SOUTH);

//------------------------------------------------------------------------------------------------
        // Настройка подключения
        // Инициализация и запуск чат-клиента
        try {
            this.client = new ClientChat("127.0.0.1",50728, this::onMessageReceived);
            client.startClient();
        }catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "error connecting to server",
                    "connection error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

//-------------------------------------------------------------------------------------------------

        // Кнопка выхода при закрытии окни чата, соединение будет разорвано
        JButton exitButton = new JButton("Exit");

        // Выход из чата
        exitButton.addActionListener(e -> {
            String departureMessage = name + " has left the chat";
            try {
                Thread.sleep(1000); // ждем 1 секунду, чтобы убедиться в отправке сообщения
            }catch (InterruptedException ie){
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        });
    }// public ChatGraphics()

//------------------------------------------------------------------------------------------------------
    // invokeLater запускает Runnable
    private void onMessageReceived(String message){
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatGraphics().setVisible(true);
        });
    }
}
