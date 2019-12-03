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
    }

    public FileString(String content, int fileIndex, int position, int writer) {
        this.fileIndex = fileIndex | 0;
        this.position = position| 0;
        this.content = content;
        this.writer = writer| 0;
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
