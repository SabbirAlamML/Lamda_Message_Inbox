package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.service.ICategoryService;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Dongwu on 20/1/2016.
 * Path : /inbox/category
 * Method : GET
 */
public class GetCategoryAction extends AbstractAction<String, List<Category>> {
    @Inject
    private ICategoryService categoryService;

    @Override
    protected List<Category> trigger(String input, Context context) throws BadRequestException, InternalErrorException {
        List<Category> categories = categoryService.get();
        categories.stream().sorted((c1, c2) -> Integer.compare(
                c1.getOrder(), c2.getOrder()));
        return categories;
    }

    @Override
    protected Type getInputType() {
        return null;
    }
}
