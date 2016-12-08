package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.model.action.input.UpdateCategoryInput;
import com.singtel.inbox.service.ICategoryService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by Dongwu on 19/1/2016.
 * Path : /inbox/category
 * Method : POST
 */
public class CreateCategoryAction extends AbstractAction<UpdateCategoryInput, Category> {
    private final Logger LOGGER = Logger.getLogger(CreateCategoryAction.class);
    @Inject
    private ICategoryService categoryService;
    @Inject
    private ModelMapper mapper;

    @Override
    protected Category trigger(UpdateCategoryInput input, Context context) throws BadRequestException, InternalErrorException {
        UUID uuid;
        if (input.getPurgeDays() < 0) {
            throw new BadRequestException("Purge day cannot be negative value.");
        }
        try {
            uuid = UUID.fromString(input.getCategoryId());
        } catch (IllegalArgumentException ex) {
            uuid = UUID.randomUUID();
            LOGGER.error("Change Invalid category id \"" + input.getCategoryId() + "\" to \"" + uuid + "\"\n" + ex.getMessage());
        }
        Category category = mapper.map(input, Category.class);
        category.setId(uuid);
        return categoryService.create(category);
    }

    @Override
    protected Type getInputType() {
        return UpdateCategoryInput.class;
    }
}
