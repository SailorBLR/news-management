package com.epam.hubarevichadmin.validation;

import com.epam.hubarevich.domain.dto.NewsDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NewsValidatorUtil implements Validator{
    @Override
    public boolean supports(Class<?> aClass) {
        return NewsDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        NewsDTO newsDTO= (NewsDTO) object;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"news","error.empty.news.message");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"news.title","error.empty.news.title");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"news.fullText","error.empty.full.text");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"news.shortText","error.empty.short.text");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"author", "error.empty.author");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"author.authorId", "error.empty.authorName");


        if(newsDTO.getNews().getTitle()!=null&&
                newsDTO.getNews().getTitle().length()>30){
            errors.rejectValue("news.title", "error.title.length");
        }

        if(newsDTO.getNews().getFullText()!=null&&
                newsDTO.getNews().getFullText().length()>2000){
            errors.rejectValue("news.fullText", "error.text.long");
        }

        if(newsDTO.getNews().getShortText()!=null&&
                newsDTO.getNews().getShortText().length()>100){
            errors.rejectValue("news.shortText", "error.shorttext.long");
        }
        if(newsDTO.getNews().getTitle()!=null&&
                newsDTO.getNews().getTitle().length()>30){
            errors.rejectValue("news.title", "error.title.length");
        }

        if(newsDTO.getNews().getFullText()!=null&&
                newsDTO.getNews().getFullText().length()>2000){
            errors.rejectValue("news.fullText", "error.text.long");
        }

        if(newsDTO.getAuthor().getAuthorId()==0L){
            errors.rejectValue("author.authorId", "error.empty.author");
        }

    }
}
