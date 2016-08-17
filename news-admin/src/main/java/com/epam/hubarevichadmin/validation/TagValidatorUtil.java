package com.epam.hubarevichadmin.validation;


import com.epam.hubarevich.domain.Tag;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Class-validator for tag creation form
 */
@Component
public class TagValidatorUtil implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Tag.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        Tag tag = (Tag) object;

        if(tag.getTagName() == null||tag.getTagName().equals("")) {
            errors.rejectValue("tagName", "You must provide Tag Name!");
        }

        if(tag.getTagName().length()>20||tag.getTagName().length()<2){
            errors.rejectValue("tagName", "Too long tagname");
        }

    }
}
