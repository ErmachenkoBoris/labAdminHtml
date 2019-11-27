package models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "useruser")
public class User {

    @Id
    @Column(name = "userid")
    private int id;
    @Column(name = "startwriteline")
    private int startWriteLine;
    @Column(name = "endwriteline")
    private int endWriteLine;
    @Column(name = "file")
    private int file;

    public User() {
    }

    public User(int startWriteLine, int endWriteLine, int file) {
        this.startWriteLine = startWriteLine | 0;
        this.endWriteLine = endWriteLine | 0;
        this.file = file| 0;
    }

    public void setStartWriteLine(int startWriteLine) {
        this.startWriteLine=startWriteLine;
    }
    public void setEndWriteLine(int endWriteLine) {
        this.endWriteLine=endWriteLine;
    }
    public void setFile(int file) {
        this.file=file;
    }

    public int getStartWriteLine() {
        return this.startWriteLine;
    }
    public int getEndWriteLine() {
        return this.endWriteLine;
    }
    public int getFile() {
        return this.file;
    }

    @Override
    public String toString() {
        return "models.User{" +
                "id=" + id +
                ", startWriteLine='" + startWriteLine + '\'' +
                ", endWriteLine=" + endWriteLine + '\'' +
                ", file=" + file +
                '}';
    }
}
