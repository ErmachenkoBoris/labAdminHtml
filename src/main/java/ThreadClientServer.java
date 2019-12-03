import models.FileString;
import services.FilesService;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ThreadClientServer implements Runnable {

    private static Socket clientDialog;

    public ThreadClientServer(Socket client) {
        ThreadClientServer.clientDialog = client;
    }

    FilesService filesService = new FilesService();
    List<FileString> listFiles = filesService.findAllFiles();
    List<FileString> NewlistFiles = filesService.findAllFiles();
    public int clientName = 1+((int)(Math.random() * 1000000));


    @Override
    public void run() {

        try {
            // инициируем каналы общения в сокете, для сервера
            // канал записи в сокет следует инициализировать сначала канал чтения для избежания блокировки выполнения программы на ожидании заголовка в сокете
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            ObjectOutputStream outObject = new ObjectOutputStream(clientDialog.getOutputStream());
            ObjectInputStream inputObject = new ObjectInputStream(clientDialog.getInputStream());
// канал чтения из сокета
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            System.out.println("DataInputStream created");

            System.out.println("DataOutputStream  created");

            // начинаем диалог с подключенным клиентом в цикле, пока сокет не
            // закрыт клиентом
            while (!clientDialog.isClosed()) {


                System.out.println("Server try to send listFiles to Client");

               outObject.writeObject(listFiles);

                out.writeUTF(String.valueOf(clientName));
                // серверная нить ждёт в канале чтения (inputstream) получения
                // данных клиента после получения данных считывает их

                List<FileString> listFilesNew = (List<FileString>)inputObject.readObject();
                String mode = in.readUTF();
                NewlistFiles = listFilesNew;
                for(int i=0; i< listFilesNew.size(); i++) {
                    System.out.println(listFilesNew.get(i).getContent());
                    System.out.println(listFilesNew.get(i).getWriter());
                }
                // и выводит в консоль
                System.out.println("READ from clientDialog message - " + mode);

                if (mode.equalsIgnoreCase("setWriter")) {
                    System.out.println("setWriter))))))");
                    filesService.updateFileSWriter(listFilesNew);
                }


                if (mode.equalsIgnoreCase("quit")) {

                    // если кодовое слово получено то инициализируется закрытие
                    // серверной нити
                    System.out.println("Client initialize connections suicide ...");
                    out.writeUTF("Server reply - " + mode + " - OK");
                    break;
                }

                // если условие окончания работы не верно - продолжаем работу -
                // отправляем эхо обратно клиенту

                System.out.println("Server try writing to channel");
                //out.writeUTF("Server reply - " + entry + " - OK");
              //  System.out.println("Server Wrote message to clientDialog.");

                // освобождаем буфер сетевых сообщений
                out.flush();

                // возвращаемся в началло для считывания нового сообщения
            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // если условие выхода - верно выключаем соединения
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            // закрываем сначала каналы сокета !
            in.close();
            out.close();

            // потом закрываем сокет общения с клиентом в нити моносервера
            clientDialog.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error, lost conection");
            for(int i = 0; i < NewlistFiles.size();i++){
                NewlistFiles.get(i).setWriter(0);
            }
            filesService.updateFileSWriter(NewlistFiles);
            e.printStackTrace();
        }
    }
}
