package com.epam.hubarevich.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Class used to represent Comment entity
 * @author Anton_Hubarevich
 * @version 1.0
 */

public class Comment extends Domain {
    private static final long serialVersionUID = 1L;

    /**
     * Comment identifier
     */
    private Long commentId;
    /**
     * News message identifier
     */
    private Long newsId;
    /**
     * Comment text
     */
    private String commentText;

    /**
     * The name of comment author
     */
    private String commentAuthor;
    /**
     * Comment creation date
     */
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date commentCreationDate;

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
        if (!newsId.equals(comment.newsId)) return false;
        if (!commentText.equals(comment.commentText)) return false;
        if (!commentAuthor.equals(comment.commentAuthor)) return false;
        return commentCreationDate.equals(comment.commentCreationDate);

    }

    @Override
    public int hashCode() {
        int result = commentId.hashCode();
        result = 31 * result + newsId.hashCode();
        result = 31 * result + commentText.hashCode();
        result = 31 * result + commentAuthor.hashCode();
        result = 31 * result + commentCreationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", newsId=" + newsId +
                ", commentText='" + commentText + '\'' +
                ", commentAuthor='" + commentAuthor + '\'' +
                ", commentCreationDate=" + commentCreationDate +
                '}';
    }
}
