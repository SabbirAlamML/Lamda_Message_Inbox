package com.singtel.inbox.action.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.ICategoryService;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dongwu on 29/2/2016.
 */
public class RemoveExpiryMessageAction extends AbstractAction<Integer, BatchResult<Integer>> {
    private final Logger LOGGER = Logger.getLogger(RemoveExpiryMessageAction.class);
    @Inject
    private IMessageService messageService;
    @Inject
    private ICategoryService categoryService;

    @Override
    protected BatchResult<Integer> trigger(Integer count, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        long timestamp = System.currentTimeMillis();
        List<Category> categories = categoryService.get();
        List<Message> messageList = new ArrayList<>();
        LOGGER.info("[DELETE EXPIRY]" + String.valueOf(categories.size()) + " categories found");
        for (Category category : categories) {
            int purgeDays = category.getPurgeDays();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.add(Calendar.DATE, -purgeDays);
            List<Message> messages = messageService.get(category.getId(), calendar.getTimeInMillis(), count);
            LOGGER.info("[DELETE EXPIRY]" + String.valueOf(messages.size()) + " expiry message found in category " + category.getName());
            if (messages != null && messages.size() > 0) {
                messages = messageService.get(messages);
                messageList.addAll(messages);
                LOGGER.info("[DELETE EXPIRY]" + String.valueOf(messages.size()) + " expiry message has load in category " + category.getName());
                break;
            }
        }
        if (messageList.size() > 0) {
            LOGGER.info("[DELETE EXPIRY]" + String.valueOf(messageList.size()) + " expiry message start to delete");
            List<DynamoDBMapper.FailedBatch> failedBatches = messageService.remove(messageList);

            BatchResult<Integer> output = new BatchResult<>(messageList.size());
            if (failedBatches != null && failedBatches.size() > 0) {
                output.setResult(output.getResult() - failedBatches.size());
                output.setFailedBatches(failedBatches);
            }
            LOGGER.info("[DELETE EXPIRY] Batch success:" + output.getResult() + " failed:" + failedBatches.size() + ".");
            return output;
        } else {
            LOGGER.info("[DELETE EXPIRY] No message expired.");
            return new BatchResult<>(0);
        }
    }

    @Override
    protected Type getInputType() {
        return Integer.TYPE;
    }
}
