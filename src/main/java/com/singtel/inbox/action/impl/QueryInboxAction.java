package com.singtel.inbox.action.impl;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Action;
import com.singtel.inbox.model.CategorySetting;
import com.singtel.inbox.model.Resource;
import com.singtel.inbox.model.action.input.QueryInboxInput;
import com.singtel.inbox.model.action.output.CategoryOutput;
import com.singtel.inbox.model.action.output.MessageOutput;
import com.singtel.inbox.service.ICategoryService;
import com.singtel.inbox.service.ICategorySettingService;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by Dongwu on 25/1/2016.
 * Path : /inbox/onepass/{account}/{platform}
 * Method : GET
 * <p>
 * Path : /inbox/servicenumber/{account}/{platform}
 * Method : GET
 */
public class QueryInboxAction extends AbstractAction<QueryInboxInput, List<CategoryOutput>> {
    private final Logger LOGGER = Logger.getLogger(QueryInboxAction.class);
    @Inject
    private IMessageService messageService;
    @Inject
    private ICategoryService categoryService;
    @Inject
    private ICategorySettingService categorySettingService;
    @Inject
    private ModelMapper mapper;

    @Override
    protected List<CategoryOutput> trigger(QueryInboxInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        if (input.getAccounts().size() > 0) {
            HashMap<UUID, List<MessageOutput>> messageMap = new HashMap<>();
            final List<CategorySetting> settings = categorySettingService.get(input.getAccounts().get(0));
            long timestamp = System.currentTimeMillis();
            for (String account : input.getAccounts()) {
                try {
                    messageService.get(account).stream()
                            .filter(c -> /*c.getEventEndDate() >= timestamp && */c.getSendDate() <= timestamp && c.getPlatformList().contains(input.getPlatform()))
                            .forEach(message -> {
                                if (!messageMap.containsKey(message.getCategoryId())) {
                                    messageMap.put(message.getCategoryId(), new ArrayList<>());
                                }
                                MessageOutput messageOutput = mapper.map(message, MessageOutput.class);
                                Optional<Action> action = message.getActionList().stream().filter(a -> a.getPlatformList().contains(input.getPlatform())).findFirst();
                                messageOutput.setButtons(action.isPresent() ? action.get().getButtonList() : null);
                                Optional<Resource> thumbnail = message.getThumbnailList().stream().filter(a -> a.getPlatformList().contains(input.getPlatform())).findFirst();
                                messageOutput.setThumbnail(thumbnail.isPresent() ? thumbnail.get().getUrl() : null);
                                messageOutput.setExpired(message.getEventEndDate() < timestamp);
                                messageMap.get(message.getCategoryId()).add(messageOutput);
                            });
                } catch (ResourceNotFoundException ex) {
                    LOGGER.debug("Account \"" + account + "\" have no message.");
                    continue;
                }
            }
            List<CategoryOutput> categories = categoryService.get().stream()
                    .map(category -> {
                        CategoryOutput item = mapper.map(category, CategoryOutput.class);
                        if (settings != null && settings.size() > 0) {
                            Optional<CategorySetting> s = settings.stream().filter(setting -> setting.getCategoryId().equals(category.getId())).findFirst();
                            item.setSubscribed(!s.isPresent() || s.get().isSubscribed());
                        } else {
                            item.setSubscribed(true);
                        }
                        if (messageMap.containsKey(category.getId())) {
                            item.setMessageList(messageMap.get(category.getId()).stream().sorted((c1, c2) -> Long.compare(c2.getSendDate(), c1.getSendDate())).collect(Collectors.toList()));
                        }
                        return item;
                    }).sorted((c1, c2) -> Integer.compare(c1.getOrder(), c2.getOrder())).collect(toList());
            return categories;
        } else {
            LOGGER.error("Account list is empty.");
            throw new BadRequestException("Account list is empty.");
        }
    }

    @Override
    protected Type getInputType() {
        return QueryInboxInput.class;
    }
}
