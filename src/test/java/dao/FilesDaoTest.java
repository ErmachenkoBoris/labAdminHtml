package dao;

import models.FileString;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.*;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FilesDaoTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(FilesDao.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private FilesDao filesDao;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before FilesDao.class");
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
        FileString fs = new FileString("1",1,1,1);
        filesDao.save(fs);
        FileString res = filesDao.findById(1);
        Assert.assertNotEquals(res, fs);
    }

    @Test
    public void save() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void deleteAll() {
    }

    @Test
    public void testFindById() {
    }

    @Test
    public void testSave() {
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testFindAll() {
    }

    @Test
    public void testDeleteAll() {
    }
}
