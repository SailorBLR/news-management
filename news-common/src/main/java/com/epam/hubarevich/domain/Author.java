package com.epam.hubarevich.domain;

import org.jboss.logging.annotations.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Class used to represent Author entity
 *
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Entity
@Table(name = "AUTHORS")
public class Author extends Domain {
    private static final long serialVersionUID = 1L;

    /**
     * Author identifier
     */

    @Id
    @SequenceGenerator(name = "COM_SEQ",
            sequenceName = "COMMENTS_SEQ",
            allocationSize = 50, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "COM_SEQ")
    @Column(name = "AUTHOR_ID")
    private Long authorId;

    /**
     * Author name
     */

    @Column(name = "AUTHOR_NAME")
    private String authorName;

    /**
     * Author expiration date
     * NULL if is not expired
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRED")
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date expired;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "NEWS_AUTHORS",
            joinColumns = @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "AUTHOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "NEWS_ID", referencedColumnName = "NEWS_ID"))
    private Set<News> news;

    public Set<News> getNews() {
        return news;
    }

    public void setNews(Set<News> news) {
        this.news = news;
    }

    public Author() {
    }

    /**
     * Author constructor
     *
     * @param authorId   positive Long identifier
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
                ", authorName='" + authorName + '}';
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
        return true;

    }

    @Override
    public int hashCode() {
        int result = authorId.hashCode();
        result = 31 * result + authorName.hashCode();
        return result;
    }
}
