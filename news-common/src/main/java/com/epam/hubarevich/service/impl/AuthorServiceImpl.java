package com.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.service.AuthorService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.AuthorCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Anton_Hubarevich on 6/24/2016.
 */
@Component
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorDAO authorDAO;

    @Override
    public Long createAuthor(Author author) throws LogicException {
        Long authorId = 0L;
        try {
            if (AuthorCheckUtil.checkAuthorData(author)) {
                authorId = authorDAO.create(author);
            } else {
                throw new LogicException();
            }
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return authorId;
    }

    @Override
    public void deleteAuthor(Long authorId) throws LogicException {

        try {
            authorDAO.delete(authorId);
        } catch (DAOException e) {
            throw new LogicException();
        }
    }

    @Override
    public void updateAuthor(Author author) throws LogicException {
        try {
            if (AuthorCheckUtil.checkAuthorData(author)) {
                authorDAO.update(author);
            }
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public Author getAuthorByNewsId(Long newsId) throws LogicException {
        Author author;
        try {
            author = authorDAO.findAuthorByNewsId(newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return author;
    }

    @Override
    public List<Author> getListOfAuthors() throws LogicException {
        List<Author> authors;
        try {
            authors=authorDAO.findAll();
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return authors;
    }

    @Override
    public List<Author> getListAvailableAuthors() throws LogicException {
        List<Author> authors;
        try {
            authors=authorDAO.getAvailableAuthors();
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return authors;
    }

    @Override
    public void unwireNewsAuthors(Long newsId) throws LogicException {
        try {
            authorDAO.unwireNewsAuthors(newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }


    }
}
