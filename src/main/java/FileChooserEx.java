

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class FileChooserEx {
    //private static int[] disStart;
    private static int[] disStart = {10,0};
    private static int[] disCount = {10,0};
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
        ArrayList textAreaArray = new ArrayList<JTextArea>();
        final JPanel panel = new JPanel();
        //panel.add(textAreaArray);
      // panel.setLayout(new BorderLayout());

        JFrame.setDefaultLookAndFeelDecorated(true);
        final JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        final JScrollPane scroll = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        final JButton saveBtn = new JButton("Save");
        final JButton openBtn = new JButton("Open");

        final JTextArea[] textArea = new JTextArea[10];
        final JTextArea[] textAreaDis = new JTextArea[10];

        final JTextPane[] panelsText = new JTextPane[10];
        final JTextPane[] panelsTextDis = new JTextPane[10];

        final JTextPane mainText = new JTextPane();
        final Highlighter highlighter =mainText.getHighlighter();

        //final JTextPane[] panelsTextDis = new JTextPane[10];

        final int[] e = {0};
        final int[] r = {0};

        saveBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
//                JFileChooser saveFile = new JFileChooser();
//                int result = saveFile.showSaveDialog(null);
//
//                if (result == JFileChooser.APPROVE_OPTION) {
//
//                    File targetFile = saveFile.getSelectedFile();
//
//                    try {
//                        if (!targetFile.exists()) {
//                            targetFile.createNewFile();
//                        }
//
//                        FileWriter fw = new FileWriter(targetFile);
//
//                        fw.write(textArea.getText());
//                        fw.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });
        openBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser openFile = new JFileChooser();
                int result = openFile.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    Path path = openFile.getSelectedFile().toPath();
                    try {
                        String contentString = "";
                        String contentDisString = "";
                        int i=0;
                        int k=0;
                        int j=0;
                        int[] greenLights = new int[10];
                        int[] redLights = new int[10];
                        for (String s : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                            if(i>=disStart[k] && i< (disStart[k]+disCount[k])) {
                                if(i==disStart[k] && contentString.length()!=0) {
                                   // textArea[e[0]] = new JTextArea("edit");
                                   // textArea[e[0]].setText(contentString);

                                    //panelsText[e[0]] = new JTextPane();
                                   // panelsText[e[0]].setText(contentString);

                                    mainText.setText(contentString);
                                    highlighter.addHighlight(1, 10, new DefaultHighlighter.DefaultHighlightPainter(
                                            Color.red));
                                    contentString="";
                                    e[0]++;
                                }
                                System.out.println("3324234");
                                contentDisString += s;
                                contentDisString += '\n';
                               // System.out.println(contentDisString);
                            } else {
                                if (contentDisString.length() != 0) {

                                   // textAreaDis[r[0]] = new JTextArea("write");
                                    //textAreaDis[r[0]].setText(contentDisString);

                                    panelsTextDis[r[0]] = new JTextPane();
                                    panelsTextDis[r[0]].setText(contentString);
                                    System.out.println("contentDisString");
                                    contentDisString = "";
                                    r[0]++;
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
                            //textArea[e[0]] = new JTextArea("edit");
                            //textArea[e[0]].setText(contentString);

                            panelsText[e[0]] = new JTextPane();
                            panelsText[e[0]].setText(contentString);
                            e[0]++;
                        }


                        for(int z = 0; z< e[0]; z++){
                            System.out.println("---------");


                            //textAreaDis[z].setSize(50,200);

                           // panel.add(textAreaDis[z]);

                            panelsText[z].setSize(300,200);

                            Highlighter h = panelsText[z].getHighlighter();
                            h.addHighlight(1, 10, new DefaultHighlighter.DefaultHighlightPainter(
                                    Color.red));
                            panelsText[z].setEditable(false);

                            panel.add(panelsText[z]);

                            panel.setAutoscrolls(true);


                           // panel.validate();
                          //  panel.repaint();

                            scroll.validate();
                            scroll.repaint();
                            //System.out.println(panel);
;
                        }


                        for(int z = 0; z< r[0]; z++){
                            System.out.println("33333");
                            //textArea[z].setOpaque ( false );
                            //textArea[z].setSize(50,200);
                          //  textArea[z].setLocation(10, 300);

                            //panel.add(textArea[z]);
                            panelsTextDis[z].setSize(300,200);
                            panelsTextDis[z].setEditable(false);
                            panel.add(panelsTextDis[z]);
                            panel.setAutoscrolls(true);



                           // panel.validate();
                           // panel.repaint();

                            scroll.validate();
                            scroll.repaint();
                          // System.out.println(panel);
                           // scroll.revalidate();
                        }



                    } catch (IOException | BadLocationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0) );

        grid.add (saveBtn);
        grid.add (openBtn);

        panel.add(grid, BorderLayout.SOUTH);

        frame.add(scroll);

        //frame.getContentPane().add(scroll);

        frame.setPreferredSize(new Dimension(550, 500));

        frame.setTitle("File Chooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
