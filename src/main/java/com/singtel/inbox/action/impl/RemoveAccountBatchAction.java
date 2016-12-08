package com.singtel.inbox.action.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.ICategorySettingService;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dongwu on 31/3/2016.
 */
public class RemoveAccountBatchAction extends AbstractAction<List<String>, HashMap<String, BatchResult<Integer>>> {
    private final Logger LOGGER = Logger.getLogger(RemoveAccountBatchAction.class);
    @Inject
    private IMessageService messageService;
    @Inject
    private ICategorySettingService categorySettingService;

    @Override
    protected HashMap<String, BatchResult<Integer>> trigger(List<String> input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        HashMap<String, BatchResult<Integer>> result = new HashMap<>();
        input.forEach(account -> {
            List<Message> messages = messageService.get(account);
            if (messages != null && messages.size() > 0) {
                List<DynamoDBMapper.FailedBatch> failedBatches = messageService.remove(messages);
                BatchResult<Integer> output = new BatchResult<>(messages.size());
                if (failedBatches != null && failedBatches.size() > 0) {
                    output.setResult(output.getResult() - failedBatches.size());
                    output.setFailedBatches(failedBatches);
                }
                LOGGER.info("[DELETE] Account '" + account + "' success:" + output.getResult() + " failed:" + failedBatches.size() + ".");
                categorySettingService.remove(account);
                result.put(account, output);
            } else {
                result.put(account, new BatchResult<>(0));
            }
        });
        return result;
    }

    @Override
    protected Type getInputType() {
        return new TypeToken<ArrayList<String>>() {
        }.getType();
    }
}
