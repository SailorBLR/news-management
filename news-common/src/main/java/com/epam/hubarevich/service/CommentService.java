package com.epam.hubarevich.service;

import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.service.exception.LogicException;

/**
 * Created by Anton_Hubarevich on 6/27/2016.
 */
public interface CommentService {
    Long addComment(Comment comment) throws LogicException;
    void deleteComment(Long commentId) throws LogicException;
    void updateComment(Comment comment) throws LogicException;
    void deleteAllCommentsFromNews(Long newsId) throws LogicException;

}
