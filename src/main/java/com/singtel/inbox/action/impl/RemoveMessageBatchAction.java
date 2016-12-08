package com.singtel.inbox.action.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.MessageKey;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gedongwu on 1/8/2016.
 */
public class RemoveMessageBatchAction extends AbstractAction<List<MessageKey>, BatchResult<Integer>> {
    private final Logger LOGGER = Logger.getLogger(RemoveMessageBatchAction.class);
    @Inject
    private IMessageService messageService;
    @Inject
    private ModelMapper mapper;

    @Override
    protected BatchResult<Integer> trigger(List<MessageKey> input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        if (input.size() > 0) {
            List<Message> messages = input.stream().distinct().map(message -> new Message(message.getAccount(), message.getCreateDate())).collect(Collectors.toList());
            messages = messageService.get(messages);
            List<DynamoDBMapper.FailedBatch> failedBatches = messageService.remove(messages);
            BatchResult<Integer> output = new BatchResult<>(messages.size());
            if (failedBatches != null && failedBatches.size() > 0) {
                output.setResult(output.getResult() - failedBatches.size());
                output.setFailedBatches(failedBatches);
            }
            LOGGER.info("[DELETE] Batch success:" + output.getResult() + " failed:" + failedBatches.size() + ".");
            return output;
        } else {
            return new BatchResult<>(0);
        }
    }

    @Override
    protected Type getInputType() {
        return new TypeToken<List<MessageKey>>() {
        }.getType();
    }
}
