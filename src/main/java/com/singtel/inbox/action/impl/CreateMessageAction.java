package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.configuration.MessageConfiguration;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.input.CreateMessageInput;
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
 * Created by Dongwu on 25/1/2016.
 * Path : /inbox/message/servicenumber
 * Method : POST
 * <p>
 * Path : /inbox/message/onepass
 * Method : POST
 */
public class CreateMessageAction extends AbstractAction<List<CreateMessageInput>, List<Message>> {
    private final Logger LOGGER = Logger.getLogger(CreateMessageAction.class);
    @Inject
    private IMessageService messageService;
    @Inject
    private ModelMapper mapper;

    @Override
    protected List<Message> trigger(List<CreateMessageInput> input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        if (input.size() > 0) {
            HashMap<String, List<Message>> messageMap = new HashMap<>();
            for (CreateMessageInput messageInput : input) {
                Message message = mapper.map(messageInput, Message.class);
                if (message.getAccount().length() <= 3) {
                    throw new BadRequestException("Account cannot be empty string.");
                }
                if (message.getPlatformList() == null || message.getPlatformList().isEmpty()) {
                    throw new BadRequestException("Your message must apply to at least one platform.");
                }
                if (message.getSendDate() < 0) {
                    throw new BadRequestException("Send date cannot be negative value.");
                }
                if (message.getEventEndDate() < 0) {
                    throw new BadRequestException("Event end date cannot be negative value.");
                }
                if (message.getEventEndDate() == 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.add(Calendar.DATE, MessageConfiguration.DEFAULT_EXPIRY_DAYS);
                    message.setEventEndDate(calendar.getTimeInMillis());
                }
                if (message.getSendDate() == 0) {
                    message.setSendDate(System.currentTimeMillis());
                }
                if (message.getThumbnailList() != null && message.getThumbnailList().size() > 0) {
                    message.setThumbnailList(message.getThumbnailList().stream().filter(t -> t != null && t.getPlatformList() != null && t.getPlatformList().size() > 0 && t.getUrl() != null && t.getUrl().length() > 0).collect(Collectors.toList()));
                }
                if (message.getActionList() != null && message.getActionList().size() > 0) {
                    message.setActionList(message.getActionList().stream().filter(t -> t != null && t.getPlatformList() != null && t.getPlatformList().size() > 0 && t.getButtonList() != null && t.getButtonList().size() > 0).collect(Collectors.toList()));
                }
                if (messageMap.containsKey(message.getAccount())) {
                    messageMap.get(message.getAccount()).add(message);
                } else {
                    List<Message> messages = new ArrayList<>();
                    messages.add(message);
                    messageMap.put(message.getAccount(), messages);
                }
                //message = messageService.create(message);
                //result.add(message);
            }
            BatchResult<List<Message>> batchResult = messageService.create(messageMap);
            return batchResult.getResult();
        } else {
            LOGGER.error("Account list is empty.");
            throw new BadRequestException("Account list is empty.");
        }
    }

    @Override
    protected Type getInputType() {
        return new TypeToken<ArrayList<CreateMessageInput>>() {
        }.getType();
    }
}
