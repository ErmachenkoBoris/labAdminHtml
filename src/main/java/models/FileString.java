package models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "files")
public class FileString implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;

    @Column (name = "fileIndex")
    private int fileIndex;

    @Column (name = "position")
    private int position;

    @Column (name = "writer")
    private int writer;

    @Column (name = "content")
    private String content;

    public FileString() {
        this.fileIndex = -100;
        this.position = -100;
        this.content = "";
        this.writer = -100;
    }

    public FileString(String content, int fileIndex, int position, int writer) {
        // значения по умолчанию нельзя задать
        this.fileIndex = fileIndex;
        this.position = position;
        this.content = content;
        this.writer = writer;
    }

    public FileString(String content, int fileIndex, int position, int writer, int index) {
        this.fileIndex = fileIndex;
        this.position = position;
        this.content = content;
        this.writer = writer;
        this.id = index;
    }


    public String getContent() {
        return this.content;
    }
    public  void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return this.position;
    }
    public  void setPosition(int position) {
        this.position = position;
    }

    public int getWriter() {
        return this.writer;
    }
    public  void setWriter(int writer) {
        this.writer = writer;
    }

    public int getFileIndex() {
        return this.fileIndex;
    }
    public  void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }


}
