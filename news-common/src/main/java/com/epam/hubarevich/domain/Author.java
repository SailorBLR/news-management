package com.epam.hubarevich.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Class used to represent Author entity
 * @author Anton_Hubarevich
 * @version 1.0
 */

public class Author extends Domain {
    private static final long serialVersionUID = 1L;

    /**
     * Author identifier
     */
    private Long authorId;
    /**
     * Author name
     */
    private String authorName;

    /**
     * Author expiration date
     * NULL if is not expired
     */
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date expired;

    public Author() {
    }

    /**
     * Author constructor
     * @param authorId positive Long identifier
     * @param authorName String value. Limit 30 symbols
     */

    public Author(Long authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", authorName='" + authorName +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Author author = (Author) o;

        if (!authorId.equals(author.authorId)) {
            return false;
        }
        if (!authorName.equals(author.authorName)) {
            return false;
        }
        return !(expired != null ? !expired.equals(author.expired) : author.expired != null);

    }

    @Override
    public int hashCode() {
        int result = authorId.hashCode();
        result = 31 * result + authorName.hashCode();
        result = 31 * result + (expired != null ? expired.hashCode() : 0);
        return result;
    }
}
