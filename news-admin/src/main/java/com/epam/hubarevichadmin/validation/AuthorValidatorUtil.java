package com.epam.hubarevichadmin.validation;

import com.epam.hubarevich.domain.Author;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;


@Component
public class AuthorValidatorUtil implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Author.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        Author author = (Author) object;

        if(author.getExpired()!=null) {
            author.setExpired(Calendar.getInstance().getTime());
        }

        if(author.getAuthorName() == null||author.getAuthorName().equals("")) {
            errors.rejectValue("authorName", "empty.author.name");
        }

        if(author.getAuthorName().length()>20){
            errors.rejectValue("authorName", "too.long.author.name");
        }

    }
}
