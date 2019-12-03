package dao;

import models.FileString;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;
import java.util.List;

public class FilesDao {

    public FileString findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(FileString.class, id);
    }

    public void save(FileString file) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(file);
        tx1.commit();
        session.close();
    }

    public void update(FileString file) {
        System.out.println(file);
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(file);
        tx1.commit();
        session.close();
    }

    public void delete(FileString file) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(file);
        tx1.commit();
        session.close();
    }



    public List<FileString> findAll() {
        List<FileString> files = (List<FileString>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("FROM FileString ORDER BY position").list();

        return files;
    }

    public void deleteAll() {
        HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("DELETE FROM FileString");
    }
}
