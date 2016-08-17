package com.epam.hubarevichclient.command;

import com.epam.hubarevichclient.command.impl.EmptyCommand;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton_Hubarevich on 7/14/2016.
 */
public class ActionFactory {

    public final static String LOCALE = "locale";
    public final static String LAST_COMMAND = "lastcommand";
    public final static String COMMAND = "command";
    public final static String WRONG_ACTION  = "wrongAction";


    public static ActionCommand defineCommand(HttpServletRequest request, WebApplicationContext webApplicationContext) {

        ActionCommand current = new EmptyCommand();
        String action = getCommandName(request);
        if (action == null || action.isEmpty()) {
            return current;
        }
        try {
            current = (ActionCommand) webApplicationContext.getBean(action);
        } catch (IllegalArgumentException e) {
            request.setAttribute(WRONG_ACTION, action
                    /*+ MessageManagerWrapper
                    .getMessage("message.wrongaction",request.getSession().getAttribute(LOCALE).toString())*/);
        }
        return current;
    }

    public static String getCommandName(HttpServletRequest request) {
        return request.getParameter(COMMAND);
    }
}
