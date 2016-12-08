package com.singtel.inbox.action.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.input.QueryMessageInput;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Dongwu on 25/1/2016.
 */
public class RemoveMessageByAccountAction extends AbstractAction<QueryMessageInput, BatchResult<Integer>> {
    private final Logger LOGGER = Logger.getLogger(RemoveMessageByAccountAction.class);
    @Inject
    private IMessageService messageService;

    @Override
    protected BatchResult<Integer> trigger(QueryMessageInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        List<Message> messages = messageService.get(input.getAccount());
        List<DynamoDBMapper.FailedBatch> failedBatches = messageService.remove(messages);
        BatchResult<Integer> output = new BatchResult<>(messages.size());
        if (failedBatches != null && failedBatches.size() > 0) {
            output.setResult(output.getResult() - failedBatches.size());
            output.setFailedBatches(failedBatches);
        }
        LOGGER.info("[DELETE] Account '" + input.getAccount() + "' success:" + output.getResult() + " failed:" + failedBatches.size() + ".");
        return output;
    }

    @Override
    protected Type getInputType() {
        return QueryMessageInput.class;
    }
}
