package com.singtel.inbox.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.google.inject.Inject;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.RemovedMessage;
import com.singtel.inbox.service.IRemovedMessageService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.singtel.inbox.configuration.DynamoDBConfiguration.DB_PREFIX;
import static com.singtel.inbox.configuration.DynamoDBConfiguration.REMOVED_MESSAGE_TABLE_NAME;

/**
 * Created by gedongwu on 16/8/2016.
 */
public class RemovedMessageService implements IRemovedMessageService {
    private final Logger LOGGER = Logger.getLogger(RemovedMessageService.class);
    private DynamoDBMapper db;
    private ModelMapper mapper;

    @Inject
    public RemovedMessageService(DynamoDBMapper db, ModelMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }

    @Override
    public List<RemovedMessage> get(String account) {
        RemovedMessage messageKey = new RemovedMessage();
        messageKey.setAccount(account);

        DynamoDBQueryExpression<RemovedMessage> queryExpression = new DynamoDBQueryExpression<RemovedMessage>()
                .withHashKeyValues(messageKey);
        List<RemovedMessage> messages = db.query(RemovedMessage.class, queryExpression);
        return messages;
    }

    @Override
    public List<RemovedMessage> get(List<RemovedMessage> removedMessages) {
        Map<String, List<Object>> items = db.batchLoad(removedMessages);
        if (items.containsKey(DB_PREFIX + REMOVED_MESSAGE_TABLE_NAME)) {
            removedMessages = (List<RemovedMessage>) (List<?>) items.get(DB_PREFIX + REMOVED_MESSAGE_TABLE_NAME);
            return removedMessages;
        } else {
            return null;
        }
    }

    @Override
    public List<DynamoDBMapper.FailedBatch> restore(List<RemovedMessage> removedMessages) {
        List<DynamoDBMapper.FailedBatch> result = null;
        try {
            List<Message> messages = removedMessages.stream()
                    .map(removedMessage -> mapper.map(removedMessage, Message.class))
                    .collect(Collectors.toList());
            result = db.batchSave(messages);
            db.batchDelete(removedMessages);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage() + "\n" + ex.getStackTrace());
        }
        return result;
    }
}
