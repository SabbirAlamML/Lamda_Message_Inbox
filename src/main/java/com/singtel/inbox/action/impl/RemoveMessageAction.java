package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.input.QueryMessageInput;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;

/**
 * Created by Dongwu on 25/1/2016.
 */
public class RemoveMessageAction extends AbstractAction<QueryMessageInput, Message> {
    private final Logger LOGGER = Logger.getLogger(RemoveMessageAction.class);
    @Inject
    private IMessageService messageService;

    @Override
    protected Message trigger(QueryMessageInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        Message message = messageService.remove(input.getAccount(), input.getCreateDate());
        if (message != null) {
            LOGGER.info("Delete message \"" + input.getAccount() + "\",\"" + input.getCreateDate() + "\" successfully");
            return message;
        } else {
            LOGGER.error("Invalid message \"" + input.getAccount() + "\",\"" + input.getCreateDate() + "\"");
            throw new NotFoundException("Invalid message \"" + input.getAccount() + "\",\"" + input.getCreateDate() + "\"");
        }
    }

    @Override
    protected Type getInputType() {
        return QueryMessageInput.class;
    }
}
