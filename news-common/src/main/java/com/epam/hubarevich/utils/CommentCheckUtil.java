package com.epam.hubarevich.utils;

import com.epam.hubarevich.domain.Comment;

import java.util.Calendar;

/**
 * Util class for checking inputed date
 */
public class CommentCheckUtil {
    /**
     * Checks if the comment data inputed correct
     * @param comment Comment object
     * @return true in case of right data
     */
    public static boolean checkComment (Comment comment) {
        return comment.getCommentText().length() <= 100 && comment.getCommentCreationDate().getTime() <=
                Calendar.getInstance().getTimeInMillis();
    }
}
