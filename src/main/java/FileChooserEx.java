
import io.reactivex.subjects.PublishSubject;
import models.FileString;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.swing.*;
import javax.swing.text.BadLocationException;


public class FileChooserEx {

    final int widthGrid = 200;
    final int heightGrid = 100;
    final int widthFrame = 600;
    final int heightFrame = 800;
    final int fileIndex=0;
    final int widthEditFrame = 600;
    final int heightEditFrame = 300;
    final int countEditedRows = 0;
    final int[] StartEdit = {0};
    final int[] EndEdit = {0};
    final JTextArea editArea = new JTextArea("Edit");
    final JButton editBtnConfirm = new JButton("Confirm Edit");
    final JPanel grid = new JPanel(new GridLayout(5, 2, 5, 0) );
    final JLabel labelError = new JLabel("");
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
    int attemp = 0;
    final JTextPane mainText = new JTextPane();

    public PublishSubject<List<FileString>> fileStringSubjectSetWriter = PublishSubject.create();
    public PublishSubject<List<FileString>> fileStringSubjectUpdateOldRows = PublishSubject.create();
    public PublishSubject<Boolean> fileSaveSubject = PublishSubject.create();
    private String clientName;
    public List<FileString> fileStrings;
    public UI uiService = new UI();


    public void update(List<FileString> newfileStrings) throws IOException {
        this.fileStrings = newfileStrings;
        try {
                uiService.update(newfileStrings, mainText, panel, scroll);
        } catch ( BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public void createUI(final List<FileString> fileStrings, String clientName) throws IOException {
        System.out.println("CreatUI");
        this.fileStrings = fileStrings;
        this.clientName = clientName;
        uiService.init(clientNameLabel, clientName, frame, mainText, panel, editButton, labelError);

        editBtnConfirm.addActionListener(new ActionListener() {        //обработчик событий кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
                List<FileString> fileStringsEdit = uiService.editBtnConfirmActionPerformed(editArea, StartEdit[0], formEdit, fileIndex);
                if(attemp == 0)fileStringSubjectUpdateOldRows.onNext(fileStringsEdit);
                attemp++;
            }
        });

        editButton.addActionListener(new ActionListener() {      //обработчик событий кнопки
            @Override
            public void actionPerformed(ActionEvent e) {

                    StartEdit[0] = uiService.editGetStartIndex(inputEditStart);
                    EndEdit[0] = uiService.editGetLastIndex(inputEditEnd);
                    List<FileString> fileStringsEdit = uiService.editButtonActionPerfomed(
                            inputEditStart,
                            inputEditEnd,
                            labelError,
                            grid,
                            StartEdit[0],
                            EndEdit[0],
                            countEditedRows,
                            formEdit,
                            fileStrings,
                            clientName,
                            editArea,
                            editBtnConfirm,
                            scroll,
                            widthEditFrame,
                            heightEditFrame);

                    if (fileStringsEdit != null) {
                        fileStringSubjectSetWriter.onNext(fileStringsEdit);
                    }



            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fileSaveSubject.onNext(true);
            }
        });

        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                    try {
                        uiService.openBtnActionPerformed(fileStrings, mainText, panel, scroll);
                    } catch ( BadLocationException ex) {
                        ex.printStackTrace();        panel.validate();
        panel.repaint();

        scroll.validate();
        scroll.repaint();
                    }
                }
        });

        uiService.gridSetSize(grid, widthGrid, heightGrid);


        grid.add (saveBtn);
        grid.add (openBtn);
        grid.add(outputLabelStart);
        grid.add(outputLabelEnd);
        grid.add(inputEditStart);
        grid.add(inputEditEnd);
        grid.add(editButton);
        grid.add(labelError);

        panel.add(grid,BorderLayout.SOUTH);
        frame.getContentPane().add(scroll);

        uiService.frameInit(frame, clientName, widthFrame, heightFrame);

    }

}
