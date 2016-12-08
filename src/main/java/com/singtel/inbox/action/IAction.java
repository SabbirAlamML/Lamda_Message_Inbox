package com.singtel.inbox.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonElement;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;

/**
 * Created by Dongwu on 19/1/2016.
 */
public interface IAction {
    String handle(JsonElement request, Context lambdaContext) throws BadRequestException, InternalErrorException, NotFoundException;
}