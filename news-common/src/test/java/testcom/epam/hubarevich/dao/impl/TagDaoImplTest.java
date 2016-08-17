package testcom.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.impl.TagDAOImpl;
import com.epam.hubarevich.domain.Tag;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test class for Tag DAO methods
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans-test.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
public class TagDaoImplTest {
    private final Logger LOG = LogManager.getLogger(TagDaoImplTest.class);

    @Autowired
    TagDAOImpl tagDAO;

    @Test
    public void testGetTagsByNewsId(){
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag(1L,"Religion"));
        tags.add(new Tag(2L,"Belarus"));
        try {
            assertTrue(tags.toString().trim()
                    .equals(tagDAO.getTagsByNewsId(1L).toString().trim()));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }
    @Test
    public void testUnwireTagsNewsByNews(){
        try {
            tagDAO.unwireTagsNewsByNews(1L);
            assertTrue(tagDAO.getTagsByNewsId(1L).size()==0);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindAll(){

        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag(1L,"Religion"));
        tags.add(new Tag(2L,"Belarus"));
        tags.add(new Tag(3L,"Russia"));
        try {
            assertTrue(tags.containsAll(tagDAO.findAll()));
        } catch (DAOException e){
            LOG.error(e);
        }

    }

    @Test
    public void testFindDomainById(){

        Tag tag = new Tag(1L,"Religion");
        try {
            assertTrue(tag.equals(tagDAO.findDomainById(1L)));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testDelete(){
        try {
            tagDAO.delete(1L);
            assertTrue(tagDAO.findDomainById(1L)==null);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testCreate(){
        Tag tag = new Tag();
        tag.setTagName("Religion");
        try {
            Assert.assertNotEquals(Long.valueOf(1L), tagDAO.create(tag));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testUpdate(){

        Tag tag = new Tag(1L,"ReligionAtheism");
        try {
            tagDAO.update(tag);
            assertTrue(tag.equals(tagDAO.findDomainById(1L)));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testUnwireTagsNewsByTags() {

        try {
            tagDAO.unwireTagsNewsByTags(1L);
            assertTrue(!tagDAO.getTagsByNewsId(1L).contains(tagDAO.findDomainById(1L)));
        } catch (DAOException e) {
            LOG.error(e);
        }

    }
}

