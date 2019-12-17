//import Interceptors.AuditInterceptor;
import models.FileString;
import services.FilesService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {

    //final String pathToFile = "\"/home/boris/JAVA1/javaLABGIT/labAdminHtml/src/main/java/index.html\"";

    static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        final String pathToFile = "/home/boris/JAVA1/javaLABGIT/labAdminHtml/src/main/java/index.html";

        FilesService fileservice = new FilesService();

        List<ThreadClientServer> threadClientServers = new ArrayList<ThreadClientServer> ();
        int Size = 0;

        //AuditInterceptor Intercapt = new AuditInterceptor();

        try(FileReader reader = new FileReader(pathToFile))
        {

            int i = 0;

            for (String s : Files.readAllLines(Paths.get(pathToFile), StandardCharsets.UTF_8)) {
                FileString fileString = new FileString(s,0,i++,0);
                fileservice.saveFile(fileString);
                fileservice.updateFile(fileString);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

        // стартуем сервер на порту 3345 и инициализируем переменную для обработки консольных команд с самого сервера
        try (ServerSocket server = new ServerSocket(3345);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket created, command console reader for listen to server commands");

            // стартуем цикл при условии что серверный сокет не закрыт
            while (!server.isClosed()) {

                // проверяем поступившие комманды из консоли сервера если такие
                // были
                if (br.ready()) {
                    System.out.println("Main Server found any messages in channel, let's look at them.");

                    // если команда - quit то инициализируем закрытие сервера и
                    // выход из цикла раздачии нитей монопоточных серверов
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();
                        break;
                    }
                }

                // если комманд от сервера нет то становимся в ожидание
                // подключения к сокету общения под именем - "clientDialog" на
                // серверной стороне
                Socket client = server.accept();

                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable(при необходимости можно создать Callable)
                // монопоточную нить = сервер - MonoThreadClientHandler и тот
                // продолжает общение от лица сервера

                ThreadClientServer threadCS = new ThreadClientServer(client);
                threadClientServers.add(threadCS);
                System.out.println("-------------------------------------");
                threadClientServers.get(Size).updateRequire.subscribe(aBoolean -> {
                    for(int i=0; i< threadClientServers.size(); i++) {
                        System.out.println("MAIN SERVER ON NEXT");
                        ThreadClientServer tmp = threadClientServers.get(i);
                        tmp.updateRequireBase.onNext(true);
                    }
                });
                threadClientServers.get(Size).saveFile.subscribe(aBoolean->{
                    if(aBoolean)saveFile(fileservice);
                });
                Size++;

                executeIt.execute(threadCS);
                System.out.print("Connection accepted.");
            }

            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveFile(FilesService fileservice) throws IOException {
        List<FileString> actualList = fileservice.findAllFiles();
        String text = "";
        System.out.println("TRY TO WRITE");
        for(int i=0; i<actualList.size(); i++) {
            text += actualList.get(i).getContent();
            text +="\n";
        }
            FileWriter fw = new FileWriter("/home/boris/JAVA1/javaLABGIT/labAdminHtml/src/main/java/index.html");
            fw.write(text);
            fw.close();

    }
}
