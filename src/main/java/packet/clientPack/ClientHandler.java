package packet.clientPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;


// отслеживание списка всех подключенных клиентов
public class ClientHandler implements Runnable{
    private Socket clientSocket; // сокет
    private List<ClientHandler> clients; // список клиентов
    private PrintWriter out; /// вывод

    /* PrintWriter - это подкласс. Writer, который используется для печати форматированных данных в OutputStream
    или другой. Writer, которым он управляет. */

    private BufferedReader in; // считывание входящих клиентов

    // конструктор передачи значений
    public ClientHandler(Socket socket,List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.clients = clients;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void run(){
        try {

            String inputLine;
            while ((inputLine = in.readLine()) != null) { // если во входящей линии есть клиенты
                for (ClientHandler aClient : clients) { // перебираем их
                    aClient.out.println(inputLine); // и выводим
                }
            }
        }catch (IOException e){
                System.out.println("error" + e.getMessage());
        }finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
