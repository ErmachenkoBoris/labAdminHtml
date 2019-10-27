

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class FileChooserEx {
    //private static int[] disStart;
    int EditStartSymbol = 0;
    int EditEndSymbol = 0;
    private static int[] disStart = {10,20, 0};
    private static int[] disCount = {5,10,0};
    public static void main(String[] args) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                new FileChooserEx().createUI(disStart, disCount);
            }
        };

        EventQueue.invokeLater(r);
    }

    private void createUI(final int[] disStart,final int[] disCount) {

        final Path[] finalPath = {Paths.get("")};
        final int[] StartEdit = {0};
        final int[] EndEdit = {0};
        final JTextArea editArea = new JTextArea("Edit");
        final JButton editBtnConfirm = new JButton("Confirm Edit");
        final String[] BeforEdit = {""};
        JButton editBitton = new JButton("edit");
        editBitton.setActionCommand("текст который выводится нажатием на кнопку");


        final JTextField inputEditStart = new JTextField(10);//поле ввода
        final JTextField inputEditEnd = new JTextField(10);//поле ввода

        JLabel outputLabelStart= new JLabel("edit FROM row:");
        JLabel outputLabelEnd= new JLabel("edit TO row:");


        final JPanel panel = new JPanel();

        JFrame.setDefaultLookAndFeelDecorated(true);
        final JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        final JScrollPane scroll = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        final JButton saveBtn = new JButton("Save");
        final JButton openBtn = new JButton("Open");


        final JTextPane mainText = new JTextPane();
        mainText.setEditable(false);

        final Highlighter highlighter =mainText.getHighlighter();

        panel.setAutoscrolls(true);



        editBtnConfirm.addActionListener(new ActionListener(){        //обработчик событий кнопки
            public void actionPerformed(ActionEvent e){
                String editText=editArea.getText();
                String partOneText = mainText.getText().substring(0, EditStartSymbol);
                String partTWoText = mainText.getText().substring(EditEndSymbol+EditStartSymbol, mainText.getText().length());
                mainText.setText(partOneText+editText+partTWoText);

                panel.remove(editArea);
                panel.remove(editBtnConfirm);
                panel.add(mainText);
                panel.validate();
                panel.repaint();

                scroll.validate();
                scroll.repaint();
            }
        });

        editBitton.addActionListener(new ActionListener(){        //обработчик событий кнопки
            public void actionPerformed(ActionEvent e){


                StartEdit[0] = Integer.parseInt(inputEditStart.getText()); //ввод
                EndEdit[0] = Integer.parseInt(inputEditEnd.getText()); //ввод
                EndEdit[0]++;

                try {
                    int flag=0;
                    int i=0;

                    String EditContent ="";
                    for (String s : Files.readAllLines(finalPath[0], StandardCharsets.UTF_8)) {
                        if(flag==0) {EditStartSymbol += s.length();}

                        if(i>=StartEdit[0] && i<EndEdit[0]) {
                            if(flag!=1){flag =1;}

                            EditContent+=s;
                            EditEndSymbol+=s.length();
                            EditContent+='\n';
                        } else {
                            if(EditContent.length()!=0 && flag==1) {
                                flag=2;

                                editArea.setText(EditContent);

                                BeforEdit[0] = EditContent;
                                panel.add(editBtnConfirm);
                                panel.add(editArea);
                                panel.validate();
                                panel.repaint();

                                scroll.validate();
                                scroll.repaint();
                            }
                        }
                        i++;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

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
                JFileChooser openFile = new JFileChooser();
                int result = openFile.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    finalPath[0] = openFile.getSelectedFile().toPath();
                    try {
                        String contentString = "";

                        int i=0;
                        int k=0;

                        int dis=0;

                        int textCount = 0;
                        String wholeContentString = "";


                        int[] lengthText = new int[10];
                        DefaultHighlighter.DefaultHighlightPainter[] textLights = new DefaultHighlighter.DefaultHighlightPainter[10];

                        for(int t=0; t<10;t++) {
                            textLights[t]=new DefaultHighlighter.DefaultHighlightPainter(Color.WHITE);
                            lengthText[t]=0;

                        }

                        textCount++;

                        for (String s : Files.readAllLines(finalPath[0], StandardCharsets.UTF_8)) {
                            s="("+i+")"+s;
                            if(i>=disStart[k] && i< (disStart[k]+disCount[k])) {

                                if(dis!=1)dis=1;

                                if(i==disStart[k] && contentString.length()!=0) {

                                    wholeContentString+=contentString;
                                    mainText.setText(wholeContentString);

                                    lengthText[textCount]=wholeContentString.length();
                                    textLights[textCount]= new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
                                    textCount++;

                                    contentString="";
                                }
                                contentString += s;
                                contentString += '\n';

                            } else {
                                if (contentString.length() != 0 && dis==1) {
                                    dis=2;
                                    wholeContentString+=contentString;
                                    mainText.setText(wholeContentString);

                                    lengthText[textCount]=wholeContentString.length();

                                    textLights[textCount]= new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                                    textCount++;

                                    contentString = "";
                                    k++;

                                    if (i == disStart[k]) {continue;}
                                } else {

                                    contentString += s;
                                    contentString += '\n';
                                }
                            }
                            i++;

                        }



                        if(contentString.length()!=0){
                            wholeContentString+=contentString;
                            mainText.setText(wholeContentString);

                            lengthText[textCount]=wholeContentString.length();
                            textLights[textCount]= new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
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



                    } catch (IOException | BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        JPanel grid = new JPanel(new GridLayout(5, 2, 5, 0) );

        grid.add (saveBtn);
        grid.add (openBtn);
        grid.add(outputLabelStart);
        grid.add(outputLabelEnd);
        grid.add(inputEditStart);
        grid.add(inputEditEnd);
        grid.add(editBitton);

        panel.add(grid, BorderLayout.SOUTH);


        frame.getContentPane().add(scroll);

        frame.setPreferredSize(new Dimension(600, 800));

        frame.setTitle("File Chooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
