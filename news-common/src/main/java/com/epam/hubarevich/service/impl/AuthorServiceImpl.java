package com.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.service.AuthorService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.AuthorCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AuthorServiceImpl implements AuthorService {
    private final String MESSAGE_NO_NEWS = "No such news message in the Database";
    private final String MESSAG_NO_AUTHOR = "No such Author in the Database";

    @Autowired
    private AuthorDAO authorDAO;

    @Override
    @Transactional
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
    @Transactional
    public void deleteAuthor(Long authorId) throws LogicException {

        if(authorId==null){
            throw new LogicException(MESSAG_NO_AUTHOR);
        }
        try {
            authorDAO.delete(authorId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    @Transactional
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
        if(newsId==null) {
            throw new LogicException(MESSAGE_NO_NEWS);
        }
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
    @Deprecated
    @Transactional
    public void unwireNewsAuthors(Long newsId) throws LogicException {
        if(newsId==null){
            throw new LogicException(MESSAGE_NO_NEWS);
        }
        try {
            authorDAO.unwireNewsAuthors(newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }
}
