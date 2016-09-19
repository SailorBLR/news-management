package com.epam.hubarevich.domain;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Class used to represent News message entity
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Entity
@Table(name ="NEWS")
public class News extends Domain {
    private static final long serialVersionUID = 1L;
    @Transient
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    /**
     * News message identifier
     */
    @Id
    @GeneratedValue(generator = "NewsGen")
    @SequenceGenerator(name = "NewsGen", sequenceName = "NEWS_SEQ")
    @Column(name = "NEWS_ID")
    private Long newsId;
    /**
     * News message title
     */
    @Size(min=1,max=30)
    @Column(name = "TITLE")
    private String title;
    /**
     * News message short text message
     */
    @Size(min=1,max=100)
    @Column(name = "SHORT_TEXT")
    private String shortText;
    /**
     * News message full text message
     */
    @Size(min=1,max=2000)
    @Column(name = "FULL_TEXT")
    private String fullText;
    /**
     * News message creation date
     */
    @Column(name = "CREATION_DATE")
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date newsCreationDate;
    /**
     * News message modification date
     */
   @Column(name = "MODIFICATION_DATE")
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date newsModificationDate;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "news")
    private Set<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "NEWS_AUTHORS",
            joinColumns = @JoinColumn(name = "NEWS_ID", referencedColumnName = "NEWS_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "AUTHOR_ID"))
    private Set<Author> authors;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "NEWS_TAGS",
            joinColumns = @JoinColumn(name = "NEWS_ID", referencedColumnName = "NEWS_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID", referencedColumnName = "TAG_ID"))
    private Set<Tag> tags;

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public News() {
    }

    /**
     * News messge constructor
     * @param newsId positive Long value
     * @param title String value. Limit 30 symbols
     * @param shortText String value. Limit 100 symbols
     * @param fullText String value. Limit 2000 symbols
     * @param newsCreationDate Timestamp value. News message creation date
     * @param newsModificationDate Date value. News message modification date
     */
    public News(Long newsId, String title, String shortText, String fullText,
                Date newsCreationDate, Date newsModificationDate) {
        this.newsId = newsId;
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.newsCreationDate = newsCreationDate;
        this.newsModificationDate = newsModificationDate;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public Date getNewsCreationDate() {
        return newsCreationDate;
    }

    public void setNewsCreationDate(Date newsCreationDate) {
        this.newsCreationDate = newsCreationDate;
    }

    public Date getNewsModificationDate() {
        return newsModificationDate;
    }

    public void setNewsModificationDate(Date newsModificationDate) {
        this.newsModificationDate = newsModificationDate;
    }




    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", title='" + title + '\'' +
                ", shortText='" + shortText + '\'' +
                ", fullText='" + fullText + '\'' +
                ", newsCreationDate=" + newsCreationDate +
                /*", newsModificationDate=" + newsModificationDate +*/
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

        News news = (News) o;

        if (!newsId.equals(news.newsId)){
            return false;
        }
        if (!title.equals(news.title)){
            return false;
        }
        if (!shortText.equals(news.shortText)) {
            return false;
        }
        if (!fullText.equals(news.fullText)) {
            return false;
        }
        if (!newsCreationDate.equals(news.newsCreationDate)) {
            return false;
        }
        return newsModificationDate.equals(news.newsModificationDate);

    }

    @Override
    public int hashCode() {
        int result = newsId.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + shortText.hashCode();
        result = 31 * result + fullText.hashCode();
        result = 31 * result + newsCreationDate.hashCode();
        result = 31 * result + newsModificationDate.hashCode();
        return result;
    }
}

