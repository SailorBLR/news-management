package com.epam.hubarevich.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Class used to represent Comment entity
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Entity
@Table(name = "COMMENTS")
public class Comment extends Domain {
    private static final long serialVersionUID = 1L;

    /**
     * Comment identifier
     */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "CommentGen")
    @SequenceGenerator(name = "CommentGen", sequenceName = "COMMENTS_SEQ"
            ,initialValue = 50, allocationSize = 2)
    @Column(name = "COMMENT_ID")
    private Long commentId;
    /**
     * News message identifier
     */
    @Column(name = "NEWS_ID")
    private Long newsId;
    /**
     * Comment text
     */
    @Column(name = "COMMENT_TEXT")
    private String commentText;

    /**
     * The name of comment author
     */
    @Column(name = "COMMENT_AUTHOR")
    private String commentAuthor;
    /**
     * Comment creation date
     */
    @Column(name = "CREATION_DATE")
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date commentCreationDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NEWS_ID", insertable=false, updatable=false)
    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Comment() {
    }

    /**
     * Comment constructor
     * @param commentId positive Long value
     * @param newsId positive Long value
     * @param commentText String value. Limit 100 symbols
     * @param commentCreationDate Timestamp value
     */
    public Comment(Long commentId, Long newsId, String commentText, String commentAuthor, Date commentCreationDate) {
        this.commentId = commentId;
        this.newsId = newsId;
        this.commentText = commentText;
        this.commentAuthor = commentAuthor;
        this.commentCreationDate = commentCreationDate;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public Date getCommentCreationDate() {
        return commentCreationDate;
    }

    public void setCommentCreationDate(Date commentCreationDate) {
        this.commentCreationDate = commentCreationDate;
    }

    public void setCommentCreationDate(){
        this.commentCreationDate = Calendar.getInstance().getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        if (!commentId.equals(comment.commentId)) return false;
        if (!commentText.equals(comment.commentText)) return false;
        if (!commentAuthor.equals(comment.commentAuthor)) return false;
        return commentCreationDate.equals(comment.commentCreationDate);

    }

    @Override
    public int hashCode() {
        int result = commentId.hashCode();
        result = 31 * result + commentText.hashCode();
        result = 31 * result + commentAuthor.hashCode();
        result = 31 * result + commentCreationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", commentText='" + commentText + '\'' +
                ", commentAuthor='" + commentAuthor + '\'' +
                ", commentCreationDate=" + commentCreationDate +
                '}';
    }
}
