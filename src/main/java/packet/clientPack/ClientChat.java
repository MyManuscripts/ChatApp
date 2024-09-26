package packet.clientPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

// Клиент устанавливает соединение с  сервером по адресу и порту
// Считываем сообщение с консоли и отправляем на сервер
// Прослушиваем сообщения от сервера и отправляем в консоль
// Клиент запускается в цикле до "exit"

public class ClientChat {
    /*private Socket socket = null; // для установки связи с сервером*/
    private Socket socket;
    /*private BufferedReader inputConsole = null; // выходной поток сообщений с записью в буфер*/
    private BufferedReader in;
    /*private PrintWriter out = null; // отправка сообщений*/
    private PrintWriter out;
    /*private BufferedReader in = null; // получение ответа от сервера*/
    private Consumer<String> onMessageReceived;

    public ClientChat(String serverAddress, int serverPort, Consumer<String>onMessageReceived)throws IOException{
        this.socket = new Socket(serverAddress,serverPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(),true);
        this.onMessageReceived = onMessageReceived;
    }

    public void sendMessage(String msg){
        out.println(msg);
    }

    public void startClient(){
        new Thread(()-> {
            try {
                String line;
                while ((line = in.readLine()) !=null){
                    onMessageReceived.accept(line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }



    /*public ClientChat(String address,int port){
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to the chat server");

            inputConsole = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader((socket.getInputStream())));

            String line = "";
            while (line.equals("exit")){ // прослушивание ответов от сервера в цикле
                line = inputConsole.readLine();
                out.println(line);
                System.out.println(in.readLine()); // и вывод
            }
            socket.close();
            inputConsole.close();
            out.close();


        }catch (UnknownHostException u){
            System.out.println("Host unknown " + u.getMessage());
        }catch (IOException i){
            System.out.println("Unexpected exception " + i.getMessage());
        }
    }

    public static void main(String[] args) {
        ClientChat clientChat = new ClientChat("127.0.0.1",50728);
    }*/

}
