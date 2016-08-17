package com.epam.hubarevichclient.command.impl;

import com.epam.hubarevichclient.command.ActionCommand;
import com.epam.hubarevichclient.command.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton_Hubarevich on 7/14/2016.
 */
public class EmptyCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        return null;
    }
}
