package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.CategorySetting;
import com.singtel.inbox.model.action.input.SubscribeCategoryInput;
import com.singtel.inbox.service.ICategorySettingService;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;

/**
 * Created by Dongwu on 25/1/2016.
 */
public class SubscribeCategoryAction extends AbstractAction<SubscribeCategoryInput, CategorySetting> {
    @Inject
    private ICategorySettingService categorySettingService;
    @Inject
    private ModelMapper mapper;

    @Override
    protected CategorySetting trigger(SubscribeCategoryInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        CategorySetting setting = categorySettingService.get(input.getAccount(), input.getCategoryId());
        if (setting == null) {
            setting = mapper.map(input, CategorySetting.class);
            setting.setNotify(true);
        } else {
            setting.setSubscribed(input.isSubscribed());
        }
        categorySettingService.update(setting);
        return setting;
    }

    @Override
    protected Type getInputType() {
        return SubscribeCategoryInput.class;
    }
}
