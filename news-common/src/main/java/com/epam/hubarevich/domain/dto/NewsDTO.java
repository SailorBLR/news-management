package com.epam.hubarevich.domain.dto;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;

import java.util.LinkedList;
import java.util.List;

/**
 * Class represents Data Transfer Object
 * Used to transfer data corresponded to News message
 *
 * @author Anton_Hubarevich
 * @version 1.0
 */
public class NewsDTO {

    /**
     * News message object
     */
    private News news;
    /**
     * Author object
     */
    private Author author;
    /**
     * Set of Tag objects
     */
    private List<Tag> tags;

    /**
     * Set of Comment objects
     */

    private List<Comment> comments;

    /**
     * Number of the News message in the resultset
     */
    private Integer rowNumber;



    public NewsDTO () {
        this.news = new News();
        this.author = new Author();
        this.tags = new LinkedList<>();
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }
}
