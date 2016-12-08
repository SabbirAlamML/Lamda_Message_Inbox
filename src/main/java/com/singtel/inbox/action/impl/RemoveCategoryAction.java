package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.model.action.input.RemoveCategoryInput;
import com.singtel.inbox.service.ICategoryService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by Dongwu on 20/1/2016.
 */
public class RemoveCategoryAction extends AbstractAction<RemoveCategoryInput, Category> {
    private final Logger LOGGER = Logger.getLogger(RemoveCategoryAction.class);
    @Inject
    private ICategoryService categoryService;

    @Override
    protected Category trigger(RemoveCategoryInput input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        UUID uuid;
        try {
            uuid = UUID.fromString(input.getCategoryId());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Invalid category id \"" + input.getCategoryId() + "\"\n" + ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }
        Category category = categoryService.remove(uuid);
        if (category == null) {
            LOGGER.error("Category \"" + input.getCategoryId() + "\" is not exist.");
            throw new NotFoundException("Category \"" + input.getCategoryId() + "\" is not exist.");
        }
        return category;
    }

    @Override
    protected Type getInputType() {
        return RemoveCategoryInput.class;
    }
}
