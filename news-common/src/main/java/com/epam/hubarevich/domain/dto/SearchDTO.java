package com.epam.hubarevich.domain.dto;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.Tag;

import java.io.Serializable;
import java.util.List;

/**
 * Class represents Search Criteria Object
 * Serves to choose the search method for service layer
 * @author Anton_Hubarevich
 * @version 1.0
 * @see Tag
 * @see Author
 */
public class SearchDTO implements Serializable{
    /**
     * Set of Tags to search
     */
    private List<Tag> tags;

    /**
     * Author to search
     */
    private Author author;

    /**
     * Most commented news marker
     */

    private Long prevId;
    private Long nextId;


    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getPrevId() {
        return prevId;
    }

    public void setPrevId(Long prevId) {
        this.prevId = prevId;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    public void setNull(){
        this.author=null;
        this.tags=null;
        this.nextId=null;
        this.prevId=null;
    }
}
