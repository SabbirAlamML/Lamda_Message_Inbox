package com.singtel.inbox.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.*;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;

import java.lang.reflect.Type;

/**
 * Created by Dongwu on 19/1/2016.
 */
public abstract class AbstractAction<I, O> implements IAction {
    @Override
    public String handle(JsonElement request, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        try {
            O result;
            Gson gson = new GsonBuilder()
                    //.enableComplexMapKeySerialization()
                    //.serializeNulls()
                    //.setDateFormat(DateFormat.LONG)
                    //.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .setPrettyPrinting()
                    .create();
            Type type = getInputType();
            if (type == null) {
                result = trigger(null, context);
            } else {
                I input = gson.fromJson(request, getInputType());
                result = trigger(input, context);
            }
            return gson.toJson(result);
        } catch (JsonIOException | JsonSyntaxException ex) {
            throw new BadRequestException(ex.getMessage(), ex);
        }
    }

    protected abstract O trigger(I input, Context context) throws BadRequestException, InternalErrorException, NotFoundException;

    protected abstract Type getInputType();
}