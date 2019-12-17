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
    }


    @AfterClass
    public  static void afterClass() {
    }

    @Before
    public void initTest() {
        filesDao = new FilesDao();
        filesDao.deleteAll();
    }

    @After
    public void afterTest() {
        filesDao = null;
    }

    @Test
    public void findAllByWtiterManyEqualSize() {

        int index= 12345;
        FileString fs1 = new FileString("1",1,1,index, 1);
        filesDao.save(fs1);

        FileString fs2 = new FileString("2",2,1,index, 1);
        filesDao.save(fs2);

        FileString fs3 = new FileString("3",3,3,index+1, 1);
        filesDao.save(fs3);

        List<FileString> res = filesDao.findAllByWtiter(index);

        Assert.assertEquals(res.size(), 2);
    }

    @Test
    public void findAllByWtiterManyEqualValues() {

        int index= 12345;
        FileString fs1 = new FileString("1",1,1,index, 1);
        filesDao.save(fs1);

        FileString fs2 = new FileString("2",2,1,index, 1);
        filesDao.save(fs2);

        FileString fs3 = new FileString("3",3,3,index+1, 1);
        filesDao.save(fs3);

        List<FileString> res = filesDao.findAllByWtiter(index);

        Assert.assertEquals(res.get(0).getWriter(), fs1.getWriter());
        Assert.assertEquals(res.get(0).getContent(), fs1.getContent());
        Assert.assertEquals(res.get(0).getFileIndex(), fs1.getFileIndex());

        Assert.assertEquals(res.get(1).getWriter(), fs2.getWriter());
        Assert.assertEquals(res.get(1).getContent(), fs2.getContent());
        Assert.assertEquals(res.get(1).getFileIndex(), fs2.getFileIndex());
    }

    @Test
    public void findAllByWtiterOne() {

        int index= 12345;
        FileString fs1 = new FileString("1",1,1,index, 1);
        filesDao.save(fs1);

        List<FileString> res = filesDao.findAllByWtiter(index);

        Assert.assertEquals(1,res.size());
        Assert.assertEquals(res.get(0).getWriter(), fs1.getWriter());
        Assert.assertEquals(res.get(0).getContent(), fs1.getContent());
        Assert.assertEquals(res.get(0).getFileIndex(), fs1.getFileIndex());
    }

    @Test
    public void cantFindAllByWtiterIfEmptyData() {
        int index= 12345;
        List<FileString> res = filesDao.findAllByWtiter(index);

        Assert.assertEquals(0, res.size());
    }

    @Test
    public void cantFindById() {
        // id will be added in db
        int index= 12345;
        FileString fs = new FileString("1",1,1,1, index);
        filesDao.save(fs);
        FileString res = filesDao.findById(1);
        Assert.assertNotEquals(res, fs);
    }

    @Test
    public void findAllByFileMany() {
        int index= 12345;
        FileString fs1 = new FileString("1",index,1,1, 1);
        filesDao.save(fs1);

        FileString fs2 = new FileString("2",index,1,2, 1);
        filesDao.save(fs2);

        FileString fs3 = new FileString("3",index + 1,3,1, 1);
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
    public void findAllByFileOne() {
        int index= 12345;
        FileString fs1 = new FileString("1",index,1,1, 1);
        filesDao.save(fs1);

        List<FileString> res = filesDao.findAllByFile(index);

        Assert.assertEquals(1, res.size());

        Assert.assertEquals(res.get(0).getWriter(), fs1.getWriter());
        Assert.assertEquals(res.get(0).getContent(), fs1.getContent());
        Assert.assertEquals(res.get(0).getFileIndex(), fs1.getFileIndex());
    }

    @Test
    public void cantFindAllByFileIfEmptyDat() {
        int index= 12345;

        List<FileString> res = filesDao.findAllByFile(index);

        Assert.assertEquals(0, res.size());
    }


    @Test
    public void saveMany() {
        List<FileString> tmpRes =  new ArrayList<FileString>();;
        final int count = 5;
        for(int i = 0; i < count; i++){
            FileString tmpTmp =new FileString("content",i,i,i, i);
            tmpRes.add(tmpTmp);
            filesDao.save(tmpTmp);

        }

        List<FileString> res = filesDao.findAll();
        assertEquals(count, res.size());

        for(int i = 0; i < res.size(); i++) {
            assertEquals(i, res.get(i).getFileIndex());
            assertEquals(i, res.get(i).getWriter());
            assertEquals(i, res.get(i).getPosition());
            assertEquals("content", res.get(i).getContent());
        }
    }

    @Test
    public void saveOneEqualCounts() {
        List<FileString> tmpRes =  new ArrayList<FileString>();
        final int count = 1;
        for(int i = 0; i < count; i++){
            FileString tmpTmp =new FileString("content",i,i,i, i);
            tmpRes.add(tmpTmp);
            filesDao.save(tmpTmp);
        }
        List<FileString> res = filesDao.findAll();
        assertEquals(count, res.size());
    }

    @Test
    public void saveOneEqualValue() {
        List<FileString> tmpRes =  new ArrayList<FileString>();
        final int count = 1;
        for(int i = 0; i < count; i++){
            FileString tmpTmp =new FileString("content",i,i,i, i);
            tmpRes.add(tmpTmp);
            filesDao.save(tmpTmp);
        }

        List<FileString> res = filesDao.findAll();

        for(int i = 0; i < res.size(); i++) {
            assertEquals(i, res.get(i).getFileIndex());
            assertEquals(i, res.get(i).getWriter());
            assertEquals(i, res.get(i).getPosition());
            assertEquals("content", res.get(i).getContent());
        }
    }

    @Test
    public void updateMany() {
        List<FileString> tmpRes =  new ArrayList<FileString>();
        int count = 1;
        for(int i = 0; i < count; i++) {
            FileString tmpTmp =new FileString("content",i,i,i, i);
            tmpRes.add(tmpTmp);
            filesDao.save(tmpTmp);
        }

        for(int i = 0; i < count; i++) {
            tmpRes.get(i).setWriter(10*i);
            filesDao.update(tmpRes.get(i));
        }

        List<FileString> res = filesDao.findAll();

        for(int i = 0; i < count; i++) {
            assertEquals(tmpRes.get(i).getWriter(), res.get(i).getWriter());
        }
    }

    @Test
    public void updateOne() {
        FileString tmp =new FileString("content",1,1,1, 1);
        filesDao.save(tmp);

        final int tmpInt = 2;
        tmp.setPosition(tmpInt);
        tmp.setContent("new content");
        tmp.setWriter(tmpInt);
        tmp.setFileIndex(tmpInt);
        filesDao.update(tmp);

        List<FileString> res = filesDao.findAll();

        assertEquals(tmpInt, res.get(0).getPosition());
        assertEquals(tmpInt, res.get(0).getWriter());
        assertEquals(tmpInt, res.get(0).getFileIndex());
        assertEquals("new content", res.get(0).getContent());
    }

    @Test
    public void deleteOne() {
        FileString tmp =new FileString("content",1,1,1, 1);
        filesDao.save(tmp);
        filesDao.delete(tmp);

        List<FileString> res = filesDao.findAll();

        assertEquals(0,res.size());
    }

    @Test
    public void deleteMany() {
        List<FileString> tmpRes =  new ArrayList<FileString>();
        final int count = 5;
        for(int i = 0; i < count; i++){
            FileString tmpTmp =new FileString("content",i,i,i, i);
            tmpRes.add(tmpTmp);
            filesDao.save(tmpTmp);
        }
        filesDao.delete(tmpRes.get(0));
        List<FileString> res = filesDao.findAll();
        assertEquals(count-1, res.size());

        for(int i = 0; i < res.size(); i++){
            assertNotEquals(0, res.get(i).getFileIndex());
            assertNotEquals(0, res.get(i).getWriter());
            assertNotEquals(0, res.get(i).getPosition());
        }
    }

    @Test
    public void findAllManyEqualCount() {
        final int count = 5;
        for(int i = 0; i < count; i++){
            filesDao.save(new FileString("content",i,i,i, i));
        }
        List<FileString> res = filesDao.findAll();
        assertEquals(count, res.size());
    }

    @Test
    public void findAllManyEqualValue() {
        final int count = 5;
        for(int i = 0; i < count; i++){
            filesDao.save(new FileString("content",i,i,i, i));
        }
        List<FileString> res = filesDao.findAll();

        for(int i = 0; i < count; i++) {
            assertEquals(i, res.get(i).getFileIndex());
            assertEquals(i, res.get(i).getWriter());
            assertEquals(i, res.get(i).getPosition());
            assertEquals("content", res.get(i).getContent());
        }
    }

    @Test
    public void deleteAllMany() {
        final int count = 5;
        for(int i = 0; i < count; i++){
            filesDao.save(new FileString("content",i,i,i, i));
        }
        filesDao.deleteAll();

        List<FileString> res = filesDao.findAll();
        assertEquals(0, res.size() );
    }

    @Test
    public void deleteAllOne() {
        final int count = 1;
        for(int i = 0; i < count; i++){
            filesDao.save(new FileString("content",i,i,i, i));
        }
        filesDao.deleteAll();
        List<FileString> res = filesDao.findAll();
        assertEquals(0, res.size() );
    }

    @Test
    public void cantDeleteAllIfDBEmpty() {
        assertEquals(0, filesDao.deleteAll() );
    }

    @Test
    public void cantSaveEmptyFile() {
        FileString tmp = new FileString();
        int result = filesDao.save(tmp);
        assertEquals(0, result);
    }
}

