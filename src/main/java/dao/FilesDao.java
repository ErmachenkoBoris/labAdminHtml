package dao;

import models.FileString;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;


import java.util.List;



public class FilesDao {


    public FileString findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(FileString.class, id);
    }

    public List<FileString> findAllByWtiter(int writerIndex) {

        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();

        Query query = session.createQuery("FROM FileString WHERE writer=:writerIndex");
        query.setParameter("writerIndex", writerIndex);
        List<FileString> files = query.list();

        return files;
    }

    public List<FileString> findAllByFile(int fileInd) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();

        Query query = session.createQuery("FROM FileString WHERE fileIndex=:fileInd");
        query.setParameter("fileInd", fileInd);
        List<FileString> files = query.list();

        return files;
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

        public static void deleteAll() {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            final List<?> instances = session.createCriteria(FileString.class).list();
            for (Object obj : instances) {
                session.delete(obj);
            }
            session.getTransaction().commit();
        }

}
