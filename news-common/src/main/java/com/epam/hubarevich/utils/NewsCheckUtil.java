package com.epam.hubarevich.utils;

import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.domain.dto.SearchDTO;

/**
 * Checks the NewsDto and SearchDto object before entering DAO layer
 */
public class NewsCheckUtil {



    public static boolean checkNewsDto(NewsDTO newsDTO) {

        boolean success = true;
        if (
                (newsDTO.getNews().getTitle().length() > 30) ||
                        (newsDTO.getNews().getShortText().length() > 100) ||
                        (newsDTO.getNews().getFullText().length() > 2000) ||
                        (newsDTO.getAuthor() == null)||
                        (newsDTO.getAuthor().getAuthorId()==0L)) {
            success = false;
        }


        return success;
    }


    public static boolean checkSearchDto(SearchDTO searchDto) {
        boolean success = true;
        if (searchDto.getAuthor() == null && searchDto.getTags() == null) {
            success = false;
        }
        if (searchDto.getTags() != null) {
            for (Tag tag : searchDto.getTags()) {
                if (tag.getTagName().length() > 30) {
                    success = false;
                }
            }
        }
        if ((searchDto.getAuthor() != null) && searchDto.getAuthor().getAuthorId() < 1) {
            success = false;
        }
        return success;
    }
}
