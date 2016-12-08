package com.singtel.inbox.action.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.RemovedMessage;
import com.singtel.inbox.model.action.RemovedMessageKey;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.IRemovedMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gedongwu on 16/8/2016.
 */
public class RestoreMessageBatchAction extends AbstractAction<List<RemovedMessageKey>, BatchResult<Integer>> {
    private final Logger LOGGER = Logger.getLogger(RestoreMessageBatchAction.class);
    @Inject
    private IRemovedMessageService removedMessageService;

    @Override
    protected BatchResult<Integer> trigger(List<RemovedMessageKey> input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        if (input.size() > 0) {
            List<RemovedMessage> messages = input.stream().distinct().map(message -> new RemovedMessage(message.getAccount(), message.getRemovedDate())).collect(Collectors.toList());
            messages = removedMessageService.get(messages);
            List<DynamoDBMapper.FailedBatch> failedBatches = removedMessageService.restore(messages);
            BatchResult<Integer> output = new BatchResult<>(messages.size());
            if (failedBatches != null && failedBatches.size() > 0) {
                output.setResult(output.getResult() - failedBatches.size());
                output.setFailedBatches(failedBatches);
            }
            LOGGER.info("[RESTORE] Batch success:" + output.getResult() + " failed:" + failedBatches.size() + ".");
            return output;
        } else {
            return new BatchResult<>(0);
        }
    }

    @Override
    protected Type getInputType() {
        return new TypeToken<List<RemovedMessageKey>>() {
        }.getType();
    }
}
