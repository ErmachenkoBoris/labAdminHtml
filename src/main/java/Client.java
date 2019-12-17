import models.FileString;
import models.User;
import services.FilesService;
import services.UserService;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private static FileChooserEx ClientUI;

    public static String clientName = "default";
    public static String infoFromServer = "default";
    public static void main(String[] args) throws InterruptedException, IOException {
            ClientUI=new FileChooserEx();

        FilesService filesService = new FilesService();
        List<FileString> listFiles;

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
                    }
            );

            ClientUI.fileSaveSubject.subscribe(
                    fileStrings -> {
                        List<FileString> fileStringsTmp = new ArrayList<FileString>();
                        outObject.reset();
                        outObject.writeObject(fileStringsTmp);
                        oos.writeUTF("save");
                    }
            );

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

                if(br.ready()){

                    System.out.println("Client start writing in channel...");
                    String clientCommand = br.readLine();

                    oos.writeUTF(clientCommand);
                    oos.flush();
                    System.out.println("Clien sent message " + clientCommand + " to server.");

                    if(clientCommand.equalsIgnoreCase("quit")){


                        System.out.println("Client kill connections");

                        if(ois.read() > -1)     {
                            System.out.println("reading...");
                            String in = ois.readUTF();
                            System.out.println(in);
                        }

                        break;
                    }

                    System.out.println("Client sent message & start waiting for data from server...");
                    if(ois.read() > -1)     {
                        System.out.println("reading...");
                        String in = ois.readUTF();
                        System.out.println(in);
                    }
                }
            }
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
