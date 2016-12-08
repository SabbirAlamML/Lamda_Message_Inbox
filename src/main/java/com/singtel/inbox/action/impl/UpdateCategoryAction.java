package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.model.action.input.UpdateCategoryInput;
import com.singtel.inbox.service.ICategoryService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by Dongwu on 21/1/2016.
 */
public class UpdateCategoryAction extends AbstractAction<UpdateCategoryInput, Category> {
    private final Logger LOGGER = Logger.getLogger(UpdateCategoryAction.class);
    @Inject
    private ICategoryService categoryService;
    @Inject
    private ModelMapper mapper;

    @Override
    protected Category trigger(UpdateCategoryInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        UUID uuid;
        if (input.getPurgeDays() < 0) {
            throw new BadRequestException("Purge day value cannot be negative.");
        }
        try {
            uuid = UUID.fromString(input.getCategoryId());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Invalid category id \"" + input.getCategoryId() + "\"\n" + ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }
        Category category = categoryService.get(uuid);
        if (category == null) {
            LOGGER.error("Category \"" + input.getCategoryId() + "\" is not exist.");
            throw new NotFoundException("Category \"" + input.getCategoryId() + "\" is not exist.");
        }
        mapper.map(input, category);
        return categoryService.update(category);
    }

    @Override
    protected Type getInputType() {
        return UpdateCategoryInput.class;
    }
}
