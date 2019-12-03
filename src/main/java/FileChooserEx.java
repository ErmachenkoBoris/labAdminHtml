
import io.reactivex.subjects.PublishSubject;
import models.FileString;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class FileChooserEx {
    //private static int[] disStart;
    int countEditedRows = 0;
    int EditStartSymbol = 0;
    int EditEndSymbol = 0;
    final Path[] finalPath = {Paths.get("")};
    final int[] StartEdit = {0};
    final int[] EndEdit = {0};
    final JTextArea editArea = new JTextArea("Edit");
    final JButton editBtnConfirm = new JButton("Confirm Edit");
    final String[] BeforEdit = {""};
    final JPanel grid = new JPanel(new GridLayout(5, 2, 5, 0) );
    final JLabel labelError = new JLabel("Error: empty data");
    final JLabel clientNameLabel = new JLabel("client name");
    JButton editButton = new JButton("edit");
    final JTextField inputEditStart = new JTextField(10);//поле ввода
    final JTextField inputEditEnd = new JTextField(10);//поле ввода
    JLabel outputLabelStart= new JLabel("edit FROM row:");
    JLabel outputLabelEnd= new JLabel("edit TO row:");
    final JFrame frame = new JFrame();
    final JFrame formEdit = new JFrame("EditPanel");
    final JPanel panel = new JPanel(new BorderLayout());
    final JScrollPane scroll = new JScrollPane(panel,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    final JButton saveBtn = new JButton("Save");
    final JButton openBtn = new JButton("Index.html");

    private static int[] disStart = {1,20, 0};
    private static int[] disCount = {5,10,0};

    public PublishSubject<List<FileString>> fileStringSubjectSetWriter = PublishSubject.create();
    public PublishSubject<List<FileString>> fileStringSubjectUpdateFileSameRows = PublishSubject.create();
    public PublishSubject<List<FileString>> fileStringSubjectUpdateFileAddRows = PublishSubject.create();
    public PublishSubject<List<FileString>> fileStringSubjectUpdateFileDeleteRows = PublishSubject.create();
    private String clientName;

    public void createUI(final List<FileString> fileStrings, String clientName) throws IOException {
        this.clientName = clientName;
        clientNameLabel.setText("My name is " +this.clientName);
        editButton.setActionCommand("текст который выводится нажатием на кнопку");

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setLayout(new BorderLayout());


        final JTextPane mainText = new JTextPane();
        mainText.setEditable(false);

        panel.setAutoscrolls(true);



        editBtnConfirm.addActionListener(new ActionListener() {        //обработчик событий кнопки
            public void actionPerformed(ActionEvent e) {

                String editText = editArea.getText();

                String[] newContent = editText.split("\n"); //парсим обратно
                System.out.println(newContent.length);
                System.out.println(countEditedRows);
                if (newContent.length-1 == countEditedRows) {
                    List<FileString> fileStringsEdit = new ArrayList<FileString>();
                    for(int h = StartEdit[0]; h<StartEdit[0]+newContent.length; h ++) {
                        FileString tmp = fileStrings.get(h);
                        tmp.setContent(newContent[h-StartEdit[0]]);
                        tmp.setWriter(0);
                        fileStringsEdit.add(tmp);
                        System.out.println(fileStringsEdit.get(h-StartEdit[0]).getWriter());
                        System.out.println(fileStringsEdit.get(h-StartEdit[0]).getContent());
                    }
                    fileStringSubjectSetWriter.onNext(fileStringsEdit);
                }

                if (newContent.length > countEditedRows) {

                }

                if (newContent.length < countEditedRows) {

                }

            }
        });

        editButton.addActionListener(new ActionListener() {      //обработчик событий кнопки
            public void actionPerformed(ActionEvent e) {
                int errorFlag = 0;

                if (((inputEditStart.getText().length() == 0) || (inputEditEnd.getText().length() == 0))) {
                    if (errorFlag != 1) {
                        errorFlag = 1;
                        labelError.setForeground(Color.RED);
                        grid.add(labelError);
                        grid.revalidate();
                        return;
                    }
                }

                grid.remove(labelError);

                StartEdit[0] = Integer.parseInt(inputEditStart.getText()); //ввод
                EndEdit[0] = Integer.parseInt(inputEditEnd.getText()); //ввод

                countEditedRows= EndEdit[0]-StartEdit[0];

                formEdit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                formEdit.setSize(350, 150);
                final JPanel panelEdit = new JPanel();

                formEdit.setVisible(true);

                formEdit.setLayout(new BorderLayout());

                final JScrollPane scrollEdit = new JScrollPane(panelEdit,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

                formEdit.getContentPane().add(scrollEdit);
                formEdit.setPreferredSize(new Dimension(600, 800));
                formEdit.setTitle("File Chooser");
                formEdit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                formEdit.pack();
                formEdit.setVisible(true);
                formEdit.setLocationRelativeTo(null);

                EndEdit[0]++;

                int flag = 0;
                int i = 0;

                String EditContent = "";
                List<FileString> fileStringsEdit = new ArrayList<FileString>();
                for (i = StartEdit[0]; i < EndEdit[0]; i++) {
                    FileString tmp = fileStrings.get(i);
                    if(tmp.getWriter()!=0){
                        labelError.setForeground(Color.RED);
                        grid.add(labelError);
                        grid.revalidate();
                        break;
                    }
                    tmp.setWriter(Integer.parseInt(clientName));
                    fileStringsEdit.add(tmp);
                    String s =(fileStrings.get(i)).getContent();
                    EditContent += s;
                    EditContent += '\n';
                }

                fileStringSubjectSetWriter.onNext(fileStringsEdit);

                editArea.setText(EditContent);

                panelEdit.add(editBtnConfirm);
                panelEdit.add(editArea);
                panelEdit.validate();
                panelEdit.repaint();

                scroll.validate();
                scroll.repaint();
            }
        });

        saveBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser saveFile = new JFileChooser();
                int result = saveFile.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {

                    File targetFile = saveFile.getSelectedFile();

                    try {
                        if (!targetFile.exists()) {
                            targetFile.createNewFile();
                        }

                        FileWriter fw = new FileWriter(targetFile);

                        fw.write(mainText.getText().replaceAll("\\(\\d{1,}\\)", ""));

                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        openBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                    try {

                        int textCount = 0;
                        String wholeContentString = "";

                        int[] lengthText = new int[300];
                        DefaultHighlighter.DefaultHighlightPainter[] textLights = new DefaultHighlighter.DefaultHighlightPainter[300];

                        for(int t=0; t<300;t++) {
                            textLights[t]=new DefaultHighlighter.DefaultHighlightPainter(Color.WHITE);
                            lengthText[t]=0;
                        }

                        textCount++;

                        for (int z = 0; z < fileStrings.size(); z++) {
                            FileString fileString = fileStrings.get(z);
                            String s = fileStrings.get(z).getContent();
                            s="("+z+")"+s;

                            if(fileString.getWriter()!=0) {

                                s = s + "  // modifying by admin #" + fileString.getWriter();
                                textLights[textCount]= new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                            } else {
                                textLights[textCount]= new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
                            }

                            s+='\n';
                            wholeContentString+=s;
                            mainText.setText(wholeContentString);
                            lengthText[textCount]=wholeContentString.length();
                            textCount++;
                        }

                        for(int j=1; j< textCount; j++) {
                           mainText.getHighlighter().addHighlight(lengthText[j-1], lengthText[j], textLights[j]);
                        }

                        panel.add(mainText);
                           panel.validate();
                           panel.repaint();

                        scroll.validate();
                        scroll.repaint();



                    } catch ( BadLocationException ex) {
                        ex.printStackTrace();
                    }

                }
        });


        grid.setMaximumSize(new Dimension(200,100));
        grid.setMinimumSize(new Dimension(200,100));

        grid.add (saveBtn);
        grid.add (openBtn);
        grid.add(outputLabelStart);
        grid.add(outputLabelEnd);
        grid.add(inputEditStart);
        grid.add(inputEditEnd);
        grid.add(editButton);

        panel.add(grid,BorderLayout.SOUTH);
        frame.getContentPane().add(scroll);
        frame.setPreferredSize(new Dimension(600, 800));
        frame.setTitle("Admin - " + clientName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void txtFile(String cnvrt, Path file)
    {
        try {
            Files.write(file, cnvrt.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
