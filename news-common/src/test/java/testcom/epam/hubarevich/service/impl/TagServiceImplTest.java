package testcom.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.service.impl.TagServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;



@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {
    private final Logger LOG = LogManager.getLogger(TagServiceImplTest.class);

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    TagDAO tagDAO;

    @Test(expected = NumberFormatException.class)
    public void testGetListOfTags() {

        try {
            when(tagDAO.findAll()).thenThrow(new NumberFormatException());
            tagService.getListOfTags();
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testAddNewTag() {
        Tag tag = new Tag(1L, "Religion");
        try {
            when(tagDAO.create(tag)).thenThrow(new NumberFormatException());
            tagService.addNewTag(tag);

        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testDeleteTag() {

        try {
            doThrow(new NumberFormatException()).when(tagDAO).unwireTagsNewsByTags(anyLong());
            doThrow(new NumberFormatException()).when(tagDAO).delete(anyLong());
            tagService.deleteTag(1L);
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindTagsByNewsId() {
        List<Tag> tags = new LinkedList<>();
        try {
            when(tagDAO.getTagsByNewsId(anyLong())).thenReturn(tags);
            Assert.assertEquals(tags, tagService.findTagsByNewsId(anyLong()));
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testUpdateTag() {

        try {
            doThrow(new NumberFormatException()).when(tagDAO).update(new Tag(1L,"bebebe"));
            tagService.updateTag(new Tag(1L,"bebebe"));
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }
}
