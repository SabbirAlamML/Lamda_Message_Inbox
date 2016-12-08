package com.singtel.inbox.action.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.input.ReadMessageInput;
import com.singtel.inbox.model.action.output.ReadMessageOutput;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;

/**
 * Created by Dongwu on 28/1/2016.
 */
public class ReadMessageAction extends AbstractAction<ReadMessageInput, ReadMessageOutput> {
    private final Logger LOGGER = Logger.getLogger(ReadMessageAction.class);
    @Inject
    private IMessageService messageService;

    @Override
    protected ReadMessageOutput trigger(ReadMessageInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        int count = 0;
        try {
            Message message = messageService.get(input.getAccount(), input.getCreateDate());
            if (message != null) {
                if (input.getReadStatus()) {
                    message.setReadCount(message.getReadCount() + 1);
                }
                message.setReadStatus(input.getReadStatus());
                messageService.update(message);
                count = message.getReadCount();
            }
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
        }
        if (count > 0) {
            ReadMessageOutput output = new ReadMessageOutput();
            output.setReadCount(count);
            return output;
        } else {
            LOGGER.error("Invalid message \"" + input.getAccount() + "\",\"" + input.getCreateDate() + "\"");
            throw new NotFoundException("Invalid message \"" + input.getAccount() + "\",\"" + input.getCreateDate() + "\"");
        }
    }

    @Override
    protected Type getInputType() {
        return ReadMessageInput.class;
    }
}
