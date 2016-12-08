package com.singtel.inbox;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.singtel.inbox.action.IAction;
import com.singtel.inbox.app.AppModule;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.TransactionLog;
import com.singtel.inbox.service.ITransactionLogService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.singtel.inbox.configuration.ActionPackageConfiguration.ACTION_PACKAGE_NAME;

/**
 * Created by Dongwu on 19/1/2016.
 */
public class RequestRouter {
    private static final Logger LOGGER = Logger.getLogger(RequestRouter.class);

    protected Injector injector;
    protected ITransactionLogService logService;

    public RequestRouter() {
        this.injector = Guice.createInjector(new AppModule());
        this.logService = injector.getInstance(ITransactionLogService.class);
    }

    public void handleRequest(InputStream input, OutputStream output, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        TransactionLog log = new TransactionLog();
        JsonParser parser = new JsonParser();
        JsonObject inputObj;
        try {
            inputObj = parser.parse(IOUtils.toString(input)).getAsJsonObject();
        } catch (IOException ex) {
            LOGGER.error("Error while reading request\n" + ex.getMessage());
            logService.exception(ex, log);
            throw new InternalErrorException(ex.getMessage());
        }

        if (inputObj == null || inputObj.get("action") == null || inputObj.get("action").getAsString().trim().equals("")) {
            LOGGER.error("Invalid inputObj, could not find action parameter");
            log.setException("Invalid inputObj, could not find action parameter");
            logService.commit(log);
            throw new BadRequestException("Could not find action value in request");
        }
        String actionClass = inputObj.get("action").getAsString();
        log.setAction(actionClass);
        if (inputObj.has("user_arn")) {
            log.setIdentityId(inputObj.get("user_arn").getAsString());
        }
        if (inputObj.has("ip")) {
            log.setIpAddress(inputObj.get("ip").getAsString());
        }
        IAction action;
        try {
            action = IAction.class.cast(Class.forName(ACTION_PACKAGE_NAME + actionClass).newInstance());
            injector.injectMembers(action);
        } catch (final InstantiationException e) {
            LOGGER.error("Error while instantiating action class\n" + e.getMessage());
            logService.exception(e, log);
            throw new InternalErrorException(e.getMessage());
        } catch (final IllegalAccessException e) {
            LOGGER.error("Illegal access while instantiating action class\n" + e.getMessage());
            logService.exception(e, log);
            throw new InternalErrorException(e.getMessage());
        } catch (final ClassNotFoundException e) {
            LOGGER.error("Action class could not be found\n" + e.getMessage());
            logService.exception(e, log);
            throw new InternalErrorException(e.getMessage());
        }

        if (action == null) {
            LOGGER.error("Invalid action class");
            log.setException("Invalid action class");
            logService.commit(log);
            throw new BadRequestException("Invalid action class");
        }

        JsonElement body = null;
        if (inputObj.get("body") != null) {
            body = inputObj.get("body");
            log.setRequest(body.toString());
            if (body.isJsonObject()) {
                if (body.getAsJsonObject().has("account")) {
                    log.setAccount(body.getAsJsonObject().get("account").getAsString());
                }
                if (body.getAsJsonObject().has("accounts")) {
                    log.setAccount(body.getAsJsonObject().get("accounts").toString());
                }
            }
        }

        LOGGER.info("[ENTER] User \"" + log.getIdentityId() + "\" access action \"" + actionClass + "\" with:\n" +
                ((body != null) ? body.toString() : "<Empty Body>."));
        String result;
        try {
            result = action.handle(body, context);
            log.setResponse(result);

        } catch (NotFoundException | InternalErrorException | BadRequestException e) {
            logService.exception(e, log);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while handle action\n" + e.getMessage());
            logService.exception(e, log);
            throw new InternalErrorException(e.getMessage(), e);
        }

        try {
            IOUtils.write(result, output, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            LOGGER.error("Error while writing response\n" + e.getMessage());
            logService.exception(e, log);
            throw new InternalErrorException(e.getMessage(), e);
        }
        try {
            logService.commit(log);
        } catch (Exception ex) {
            TransactionLog errorLog = new TransactionLog();
            errorLog.setIdentityId(log.getIdentityId());
            errorLog.setTimestamp(log.getTimestamp());
            errorLog.setIpAddress(log.getIpAddress());
            logService.exception(ex, errorLog);
        }
        LOGGER.info("[EXIT] Function finished executive in milliseconds : " + context.getRemainingTimeInMillis());
    }
}