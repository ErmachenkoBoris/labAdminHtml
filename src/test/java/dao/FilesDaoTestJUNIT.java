package dao;

import models.FileString;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FilesDaoTestJUNIT {

    private FilesDao filesDao;

    @BeforeClass
    public static void beforeClass() {

        System.out.println("BefilesDao.deleteAll();fore FilesDao.class");
    }

    @AfterClass
    public  static void afterClass() {
        System.out.println("After FilesDao.class");
    }

    @Before
    public void initTest() {
        filesDao = new FilesDao();
    }

    @After
    public void afterTest() {

        filesDao = null;
    }

    @Test
    public void findById() {
        filesDao.deleteAll();
        int index= 12345;
        FileString fs = new FileString("1",index,1,1, index);
        filesDao.save(fs);
        FileString res = filesDao.findById(1);
        Assert.assertNotEquals(res, fs);
    }

    @Test
    public void findAllByWtiter() {
        filesDao.deleteAll();

        int index= 12345;
        FileString fs1 = new FileString("1",1,1,index, 1);
        filesDao.save(fs1);

        FileString fs2 = new FileString("2",2,1,index, 1);
        filesDao.save(fs2);

        FileString fs3 = new FileString("3",3,3,index+1, 1);
        filesDao.save(fs3);

        List<FileString> res = filesDao.findAllByWtiter(index);

        Assert.assertEquals(res.size(), 2);

        Assert.assertEquals(res.get(0).getWriter(), fs1.getWriter());

        Assert.assertEquals(res.get(0).getContent(), fs1.getContent());

        Assert.assertEquals(res.get(0).getFileIndex(), fs1.getFileIndex());

        Assert.assertEquals(res.get(1).getWriter(), fs2.getWriter());

        Assert.assertEquals(res.get(1).getContent(), fs2.getContent());

        Assert.assertEquals(res.get(1).getFileIndex(), fs2.getFileIndex());


    }

    @Test
    public void findAllByFile() {
        filesDao.deleteAll();

        int index= 12345;
        FileString fs1 = new FileString("1",index,1,1, 1);
        filesDao.save(fs1);

        FileString fs2 = new FileString("2",index,1,2, 1);
        filesDao.save(fs2);

        FileString fs3 = new FileString("3",index+1,3,1, 1);
        filesDao.save(fs3);

        List<FileString> res = filesDao.findAllByFile(index);

        Assert.assertEquals(2, res.size());

        Assert.assertEquals(res.get(0).getWriter(), fs1.getWriter());

        Assert.assertEquals(res.get(0).getContent(), fs1.getContent());

        Assert.assertEquals(res.get(0).getFileIndex(), fs1.getFileIndex());

        Assert.assertEquals(res.get(1).getWriter(), fs2.getWriter());

        Assert.assertEquals(res.get(1).getContent(), fs2.getContent());

        Assert.assertEquals(res.get(1).getFileIndex(), fs2.getFileIndex());
    }

    @Test
    public void save() {
        filesDao.deleteAll();
        List<FileString> tmpRes =  new ArrayList<FileString>();;
        final int count = 5;
        for(int i = 0; i < count; i++){
            FileString tmpTmp =new FileString("content",i,i,i, i);
            tmpRes.add(tmpTmp);
            filesDao.save(tmpTmp);

        }

        List<FileString> res = filesDao.findAll();
        assertEquals(count, res.size());

        for(int i = 0; i < res.size(); i++){
            assertEquals(i, res.get(i).getFileIndex());
            assertEquals(i, res.get(i).getWriter());
            assertEquals(i, res.get(i).getPosition());
            assertEquals("content", res.get(i).getContent());

        }
    }

    @Test
    public void update() {
        filesDao.deleteAll();
        FileString tmp =new FileString("content",1,1,1, 1);
        filesDao.save(tmp);

        tmp.setPosition(2);
        tmp.setContent("new content");
        tmp.setWriter(2);
        tmp.setFileIndex(2);
        filesDao.update(tmp);

        List<FileString> res = filesDao.findAll();

        assertEquals(2, res.get(0).getPosition());
        assertEquals(2, res.get(0).getWriter());
        assertEquals(2, res.get(0).getFileIndex());
        assertEquals("new content", res.get(0).getContent());
    }

    @Test
    public void delete() {
        filesDao.deleteAll();
        FileString tmp =new FileString("content",1,1,1, 1);
        filesDao.save(tmp);

        filesDao.delete(tmp);
        List<FileString> res = filesDao.findAll();

        assertEquals(0,res.size());
        List<FileString> tmpRes =  new ArrayList<FileString>();;
        final int count = 5;
        for(int i = 0; i < count; i++){
            FileString tmpTmp =new FileString("content",i,i,i, i);
            tmpRes.add(tmpTmp);
            filesDao.save(tmpTmp);

        }

        filesDao.delete(tmpRes.get(0));

        res = filesDao.findAll();
        assertEquals(4, res.size());

        res = filesDao.findAllByWtiter(0);

        for(int i = 0; i < res.size(); i++){
            assertNotEquals(0, res.get(i).getFileIndex());
            assertNotEquals(0, res.get(i).getWriter());
            assertNotEquals(0, res.get(i).getPosition());

        }
    }

    @Test
    public void findAll() {
        filesDao.deleteAll();

        final int count = 5;
        for(int i = 0; i < count; i++){
            filesDao.save(new FileString("content",i,i,i, i));
        }


        List<FileString> res = filesDao.findAll();
        assertEquals(count, res.size());

        for(int i = 0; i < count; i++) {
            assertEquals(i, res.get(i).getFileIndex());
            assertEquals(i, res.get(i).getWriter());
            assertEquals(i, res.get(i).getPosition());
            assertEquals("content", res.get(i).getContent());

        }
    }

    @Test
    public void deleteAll() {
        filesDao.deleteAll();

        final int count = 5;
        for(int i = 0; i < count; i++){
            filesDao.save(new FileString("content",i,i,i, i));
        }

        List<FileString> res = filesDao.findAll();
        assertEquals(count, res.size() );
    }
}
