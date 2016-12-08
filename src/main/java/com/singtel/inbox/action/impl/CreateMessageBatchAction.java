package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.configuration.MessageConfiguration;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.MessageKey;
import com.singtel.inbox.model.action.input.CreateMessageBatchInput;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dongwu on 1/3/2016.
 */
public class CreateMessageBatchAction extends AbstractAction<CreateMessageBatchInput, BatchResult<List<MessageKey>>> {
    private final Logger LOGGER = Logger.getLogger(CreateMessageAction.class);
    @Inject
    private IMessageService messageService;
    @Inject
    private ModelMapper mapper;

    @Override
    protected BatchResult<List<MessageKey>> trigger(CreateMessageBatchInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        if (input.getAccounts() != null && input.getAccounts().size() > 0) {
            String[] accounts = input.getAccounts().stream().distinct().toArray(String[]::new);
            if (input.getPlatformList() == null || input.getPlatformList().isEmpty()) {
                throw new BadRequestException("Your message must apply to at least one platform.");
            }
            if (input.getSendDate() < 0) {
                throw new BadRequestException("Send date cannot be negative value.");
            }
            if (input.getEventEndDate() < 0) {
                throw new BadRequestException("Event end date cannot be negative value.");
            }
            if (input.getEventEndDate() == 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.DATE, MessageConfiguration.DEFAULT_EXPIRY_DAYS);
                input.setEventEndDate(calendar.getTimeInMillis());
            }
            if (input.getSendDate() == 0) {
                input.setSendDate(System.currentTimeMillis());
            }
            if (input.getThumbnailList() != null && input.getThumbnailList().size() > 0) {
                input.setThumbnailList(input.getThumbnailList().stream().filter(t -> t != null && t.getPlatformList() != null && t.getPlatformList().size() > 0 && t.getUrl() != null && t.getUrl().length() > 0).collect(Collectors.toList()));
            }
            if (input.getActionList() != null && input.getActionList().size() > 0) {
                input.setActionList(input.getActionList().stream().filter(t -> t != null && t.getPlatformList() != null && t.getPlatformList().size() > 0 && t.getButtonList() != null && t.getButtonList().size() > 0).collect(Collectors.toList()));
            }
            HashMap<String, List<Message>> messageMap = new HashMap<>();
            for (String account : accounts) {
                if (account.length() <= 3) {
                    throw new BadRequestException("Account cannot be empty string.");
                }
                Message message = mapper.map(input, Message.class);
                if (messageMap.containsKey(account)) {
                    messageMap.get(account).add(message);
                } else {
                    List<Message> messages = new ArrayList<>();
                    messages.add(message);
                    messageMap.put(account, messages);
                }
            }
            BatchResult<List<Message>> batchResult = messageService.create(messageMap);
            List<MessageKey> keys = batchResult.getResult().stream().map(message -> new MessageKey(message.getAccount(), message.getCreateDate())).collect(Collectors.toList());
            return new BatchResult<>(keys, batchResult.getFailedBatches());
        } else {
            LOGGER.error("Account list is empty.");
            throw new BadRequestException("Account list is empty.");
        }
    }

    @Override
    protected Type getInputType() {
        return CreateMessageBatchInput.class;
    }
}
