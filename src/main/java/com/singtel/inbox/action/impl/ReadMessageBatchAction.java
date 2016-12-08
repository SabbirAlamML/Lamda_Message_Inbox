package com.singtel.inbox.action.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.input.ReadMessageBatchInput;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gedongwu on 28/10/2016.
 */
public class ReadMessageBatchAction extends AbstractAction<ReadMessageBatchInput, BatchResult<Integer>> {
    private final Logger LOGGER = Logger.getLogger(ReadMessageBatchAction.class);
    @Inject
    private IMessageService messageService;

    @Override
    protected BatchResult<Integer> trigger(ReadMessageBatchInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        if (input.getMessages() != null && input.getMessages().size() > 0) {
            List<Message> messages = input.getMessages().stream().distinct().map(message -> new Message(message.getAccount(), message.getCreateDate())).collect(Collectors.toList());
            messages = messageService.get(messages);
            if (input.isReadStatus()) {
                messages.forEach(message -> message.setReadCount(message.getReadCount() + 1));
            }
            messages.forEach(message -> message.setReadStatus(input.isReadStatus()));
            List<DynamoDBMapper.FailedBatch> failedBatches = messageService.update(messages);
            BatchResult<Integer> output = new BatchResult<>(messages.size());
            if (failedBatches != null && failedBatches.size() > 0) {
                output.setResult(output.getResult() - failedBatches.size());
                output.setFailedBatches(failedBatches);
            }
            LOGGER.info((input.isReadStatus() ? "[READ]" : "[UNREAD]") + " Batch success:" + output.getResult() + " failed:" + failedBatches.size() + ".");
            return output;
        } else {
            return new BatchResult<>(0);
        }
    }

    @Override
    protected Type getInputType() {
        return ReadMessageBatchInput.class;
    }
}
