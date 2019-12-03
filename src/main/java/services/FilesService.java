package services;

import dao.FilesDao;
import models.FileString;

import java.io.File;
import java.util.List;

public class FilesService {

    private FilesDao filesDao = new FilesDao();

    public FilesService() {
    }

    public FileString findFiles(int id) {
        return filesDao.findById(id);
    }

    public void saveFile(FileString fileString) {
        filesDao.save(fileString);
    }

    public void deleteFile(FileString fileString) {
        filesDao.delete(fileString);
    }

    public void updateFile(FileString fileString) {
        filesDao.update(fileString);
    }

    public void updateFileSWriter(List<FileString> fileStrings, String writer) {
        int writerInt = Integer.parseInt(writer);
        for(int i = 0 ;i < fileStrings.size(); i++) {
            FileString tmp = fileStrings.get(i);
            tmp.setWriter(writerInt);
            filesDao.update(tmp);
        }
    }

    public List<FileString> findAllFiles() {
        return filesDao.findAll();
    }


    public void deletAll() {
        filesDao.deleteAll();
    }
}
