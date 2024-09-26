package packet.graphics;

import packet.clientPack.ClientChat;

import javax.swing.*;        // для работы с графическим интерфейсом
import java.awt.*;           // оконная библоиотека графического интерфейса
import  java.awt.event.*;    // для работы с  событиями (нажатие кнопки)
import java.io.IOException;  // исключения
import java.text.SimpleDateFormat;
import java.util.Date;

/*public class ChatGraphics {

    // Запрос на ввод имени пользователя
    String name = JOptionPane.showInputDialog(this,"Enter your name:",
            "name Entry", JOptionPane.PLAIN_MESSAGE);
    this.setTitle("Chat application - " + name); // Укажите в заголовке окна имя пользователя

    textField.add
}*/


public class ChatGraphics extends JFrame {
    private JTextArea messageArea; // многострочная область для отображения текста
    private JTextField textField; // создание новых сообщений
    private ClientChat client;

    public ChatGraphics(){

        // Запрос на ввод имени пользователя при запуске приложения
        String name = JOptionPane.showInputDialog(this,"Enter your name:",
                "name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat application - " + name); // Укажите в заголовке окна имя пользователя
        // используем имя, чтобы обновить заголовок

//----------------------------------------------------------------------------------------------

        // super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // операция призакрытии файла
//-----------------------------------------------------------------------------------------------

        messageArea = new JTextArea();
        messageArea.setEditable(false); // доступен ли  текст для редактирования
        add(new JScrollPane(messageArea), BorderLayout.CENTER); // JScrollPane - экран с прокруткой, BorderLayout - расположение окна
//------------------------------------------------------------------------------------------------
        // ActionListener получает запись при выполнении действия
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {

            // Когда рользователь нажиммает Enter создается строка сообщения
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

//-------------------------------------------------------------------------------------------------
       // Кнопка выхода при закрытии окни чата, соединение будет разорвано
        JButton exitButton = new JButton("Exit");           // создали JButton с пометкой exit
        exitButton.addActionListener(e->System.exit(0));  // прослушиватель действий, выполняющий System.exit(0)
        JPanel bottomPanel = new JPanel(new BorderLayout());   // для размещения поля ввода и кнопки
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);


    }// public ChatGraphics()
//--------------------------------------------------------------------------------------------------
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
