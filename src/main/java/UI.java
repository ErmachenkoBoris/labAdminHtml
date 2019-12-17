import models.FileString;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UI {

    public UI(){

    }

    public int isOpen = 0;

    public java.util.List<FileString> updateList = new ArrayList<FileString>();

    public List<FileString> editBtnConfirmActionPerformed(JTextArea editArea, int startEdit, JFrame formEdit, int fileIndex) {
        String editText = editArea.getText();

        String[] newContent = editText.split("\n"); //парсим обратно

        java.util.List<FileString> fileStringsEdit = new ArrayList<FileString>();

        for(int h = startEdit; h<startEdit+newContent.length; h ++) {
            FileString tmp = new FileString(newContent[h-startEdit], fileIndex, h, 0);
            fileStringsEdit.add(tmp);
        }


        formEdit.removeAll();
        formEdit.setVisible(false);
        return fileStringsEdit;
    }



    public void init(JLabel clientNameLabel, String clientName, JFrame frame, JTextPane mainText, JPanel panel, JButton editButton, JLabel labelError) {
        clientNameLabel.setText("My name is " +clientName);
        editButton.setActionCommand("текст который выводится нажатием на кнопку");

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setLayout(new BorderLayout());



        mainText.setEditable(false);

        panel.setAutoscrolls(true);
    }


    public List<FileString> editButtonActionPerfomed(JTextField inputEditStart, JTextField inputEditEnd, JLabel labelError, JPanel grid, int startEdit, int endEdit, int countEditedRows, JFrame formEdit, List<FileString> fileStrings, String clientName, JTextArea editArea, JButton editBtnConfirm, JScrollPane scroll, int widthEditFrame, int heightEditFrame) {
        if(isOpen==0){
            return null;
        }
        labelError.setText("");
        grid.revalidate();
        List<FileString> fileStringsEdit = new ArrayList<>();
        int errorFlag = 0;

        if(this.updateList.size()!=0){
            fileStrings = this.updateList;
        }

        if (((inputEditStart.getText().length() == 0) || (inputEditEnd.getText().length() == 0))) {
            labelError.setText("Error: EmptyData");
            labelError.setForeground(Color.RED);
            grid.revalidate();
            if (errorFlag != 1) {
                errorFlag = 1;
                return null;
            }
        }

        startEdit = Integer.parseInt(inputEditStart.getText()); //ввод
        endEdit = Integer.parseInt(inputEditEnd.getText()); //ввод



        for(int i = 0; i<fileStrings.size(); i++){
            if(i>= startEdit && i<=endEdit) {

                System.out.println(fileStrings.get(i).getWriter());
                if(fileStrings.get(i).getWriter()!=0){
                    labelError.setText("Error: strings were reserved");
                    labelError.setForeground(Color.RED);
                    grid.revalidate();
                    return null;

                }
            }

        }

        countEditedRows= endEdit-startEdit;

        formEdit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panelEdit = new JPanel();

        formEdit.setLayout(new BorderLayout());

        final JScrollPane scrollEdit = new JScrollPane(panelEdit,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        formEdit.getContentPane().add(scrollEdit);
        formEdit.setPreferredSize(new Dimension(widthEditFrame, heightEditFrame));
        formEdit.setTitle("File Chooser");
        formEdit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        formEdit.pack();
        formEdit.setVisible(true);
        formEdit.setLocationRelativeTo(null);

        endEdit++;

        int i = 0;

        String EditContent = "";

        for (i = startEdit; i < endEdit; i++) {
            FileString tmp = fileStrings.get(i);

            tmp.setWriter(Integer.parseInt(clientName));
            fileStringsEdit.add(tmp);
            String s =(fileStrings.get(i)).getContent();
            EditContent += s;
            EditContent += '\n';
        }

        ;


        editArea.setText(EditContent);

        panelEdit.add(editBtnConfirm);
        panelEdit.add(editArea);
        panelEdit.validate();
        panelEdit.repaint();

        scroll.validate();
        scroll.repaint();

        return fileStringsEdit;
    }

    public void openBtnActionPerformed(List<FileString> fileStrings, JTextPane mainText, JPanel panel, JScrollPane scroll) throws BadLocationException {

        this.isOpen = 1;
        if(this.updateList.size()!=0){
            System.out.println("from upadteList");
            fileStrings = this.updateList;
        }
        int textCount = 0;
        String wholeContentString = "";

        int[] lengthText = new int[fileStrings.size()*2];
        DefaultHighlighter.DefaultHighlightPainter[] textLights = new DefaultHighlighter.DefaultHighlightPainter[fileStrings.size()*2];

        // init
        for(int t=0; t<fileStrings.size();t++) {
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

            lengthText[textCount]=wholeContentString.length();
            textCount++;
        }

        mainText.setText(wholeContentString);

        for(int j=1; j< textCount; j++) {
            mainText.getHighlighter().addHighlight(lengthText[j-1], lengthText[j], textLights[j]);
        }

        panel.add(mainText);
        panel.validate();
        panel.repaint();

        scroll.validate();
        scroll.repaint();
    }

    public void gridSetSize(JPanel grid, int widthGrid, int heightGrid) {
        grid.setMaximumSize(new Dimension(widthGrid,heightGrid));
        grid.setMinimumSize(new Dimension(widthGrid,heightGrid));
    }

    public void frameInit(JFrame frame, String clientName, int widthFrame, int heightFrame) {
        frame.setPreferredSize(new Dimension(widthFrame, heightFrame));
        frame.setTitle("Admin - " + clientName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);    }

    public void update(List<FileString> fileStrings, JTextPane mainText, JPanel panel, JScrollPane scroll) throws BadLocationException {
        this.updateList = fileStrings;
        if(isOpen==0){
            return;
        }
        if (fileStrings.size() == 0) {
            mainText.setText("");
        } else {


            int textCount = 0;
            String wholeContentString = "";

            int[] lengthText = new int[300];
            DefaultHighlighter.DefaultHighlightPainter[] textLights = new DefaultHighlighter.DefaultHighlightPainter[300];

            for (int t = 0; t < 300; t++) {
                textLights[t] = new DefaultHighlighter.DefaultHighlightPainter(Color.WHITE);
                lengthText[t] = 0;
            }

            textCount++;

            for (int z = 0; z < fileStrings.size(); z++) {
                FileString fileString = fileStrings.get(z);
                String s = fileStrings.get(z).getContent();
                s = "(" + z + ")" + s;

                if (fileString.getWriter() != 0) {

                    s = s + "  // modifying by admin #" + fileString.getWriter();
                    textLights[textCount] = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                } else {
                    textLights[textCount] = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
                }

                s += '\n';
                wholeContentString += s;
                mainText.setText(wholeContentString);
                lengthText[textCount] = wholeContentString.length();
                textCount++;
            }

            for (int j = 1; j < textCount; j++) {
                mainText.getHighlighter().addHighlight(lengthText[j - 1], lengthText[j], textLights[j]);
            }
        }
        panel.add(mainText);
        panel.validate();
        panel.repaint();

        scroll.validate();
        scroll.repaint();
    }

    public void saveBtnActionPerfomed(JTextPane mainText) {
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

    public int editGetStartIndex(JTextField inputEditStart) {
        if(inputEditStart.getText().equals("")){
            return -1;
        }
        return Integer.parseInt(inputEditStart.getText());
    }

    public int editGetLastIndex(JTextField inputEditEnd) {
        if(inputEditEnd.getText().equals("")){
            return -1;
        }
        return Integer.parseInt(inputEditEnd.getText());
    }
}
