package com.epam.hubarevichclient.command;

import com.epam.hubarevichclient.command.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton_Hubarevich on 7/14/2016.
 */
public interface ActionCommand  {

    String execute(HttpServletRequest request) throws CommandException;
}
