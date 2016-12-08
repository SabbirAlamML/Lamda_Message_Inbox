package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.model.CategorySetting;
import com.singtel.inbox.model.action.input.QueryInboxInput;
import com.singtel.inbox.model.action.output.UnreadCountOutput;
import com.singtel.inbox.service.ICategoryService;
import com.singtel.inbox.service.ICategorySettingService;
import com.singtel.inbox.service.IMessageService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Dongwu on 3/3/2016.
 */
public class GetUnreadCountAction extends AbstractAction<QueryInboxInput, UnreadCountOutput> {
    @Inject
    private IMessageService messageService;
    @Inject
    private ICategoryService categoryService;
    @Inject
    private ICategorySettingService categorySettingService;

    @Override
    protected UnreadCountOutput trigger(QueryInboxInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        int count = 0;
        long timestamp = System.currentTimeMillis();
        final List<UUID> unsubscibed = categorySettingService.get(input.getAccounts().get(0)).stream()
                .filter(setting -> !setting.isSubscribed())
                .map(CategorySetting::getCategoryId)
                .collect(Collectors.toList());
        final List<UUID> subscribedCategoryId = categoryService.get().stream()
                .filter(category -> !unsubscibed.contains(category.getId()))
                .map(Category::getId)
                .collect(Collectors.toList());
        for (String account : input.getAccounts()) {
            count += messageService.get(account).stream()
                    .filter(c -> subscribedCategoryId.contains(c.getCategoryId()) && !c.getReadStatus() && c.getSendDate() <= timestamp && c.getPlatformList().contains(input.getPlatform())).count();
        }
        return new UnreadCountOutput(count);
    }

    @Override
    protected Type getInputType() {
        return QueryInboxInput.class;
    }
}
