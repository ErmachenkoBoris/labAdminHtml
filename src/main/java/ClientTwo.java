import models.FileString;
import models.User;
import services.FilesService;
import services.UserService;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ClientTwo {

    private static FileChooserEx ClientUI;

    public static String clientName = "default";
    public static String infoFromServer = "default";
    public static void main(String[] args) throws InterruptedException, IOException {
        ClientUI=new FileChooserEx();

        FilesService filesService = new FilesService();
        List<FileString> listFiles;

// запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента
        try(Socket socket = new Socket("localhost", 3345);
            BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream());

            ObjectInputStream inputObject = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
        )
        {

            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");

            ClientUI.fileStringSubjectUpdateOldRows.subscribe(
                    fileStrings -> {
                        System.out.println("NewRows");
                        System.out.println(fileStrings.size());
                        outObject.reset();
                        outObject.writeObject(fileStrings);
                        oos.writeUTF("NewRows");
                    }
            );

            ClientUI.fileStringSubjectSetWriter.subscribe(
                    fileStrings -> {
                        outObject.reset();
                        outObject.writeObject(fileStrings);
                        oos.writeUTF("setWriterOrUpdate");

                        // String commandЕ =  ois.readUTF();
                        //  System.out.println( commandЕ );

                    }
            );

// проверяем живой ли канал и работаем если живой
            while(!socket.isOutputShutdown()){
                listFiles= (List<FileString>) inputObject.readObject();
                infoFromServer =  ois.readUTF();

                if(infoFromServer.equals("update")) {

                    System.out.println( "UPDATE" );
                    ClientUI.update(listFiles);
                } else {
                    clientName = infoFromServer;
                    System.out.println( clientName );

                    ClientUI.createUI(listFiles, clientName);
                }

// ждём консоли клиента на предмет появления в ней данных
                if(br.ready()){


// данные появились - работаем
                    System.out.println("Client start writing in channel...");
                    // Thread.sleep(1000);
                    String clientCommand = br.readLine();

// пишем данные с консоли в канал сокета для сервера
                    oos.writeUTF(clientCommand);
                    oos.flush();
                    System.out.println("Clien sent message " + clientCommand + " to server.");
                    Thread.sleep(1000);
// ждём чтобы сервер успел прочесть сообщение из сокета и ответить

// проверяем условие выхода из соединения
                    if(clientCommand.equalsIgnoreCase("quit")){

// если условие выхода достигнуто разъединяемся
                        System.out.println("Client kill connections");
                        Thread.sleep(2000);

// смотрим что нам ответил сервер на последок перед закрытием ресурсов
                        if(ois.read() > -1)     {
                            System.out.println("reading...");
                            String in = ois.readUTF();
                            System.out.println(in);
                        }

// после предварительных приготовлений выходим из цикла записи чтения
                        break;
                    }

// если условие разъединения не достигнуто продолжаем работу
                    System.out.println("Client sent message & start waiting for data from server...");
                    Thread.sleep(2000);

// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
                    if(ois.read() > -1)     {

// если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
                        System.out.println("reading...");
                        String in = ois.readUTF();
                        System.out.println(in);
                    }
                }
            }
// на выходе из цикла общения закрываем свои ресурсы
            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
