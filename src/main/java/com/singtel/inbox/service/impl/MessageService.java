package com.singtel.inbox.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.inject.Inject;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.RemovedMessage;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;

import java.util.*;

import static com.singtel.inbox.configuration.DynamoDBConfiguration.*;

/**
 * Created by Dongwu on 25/1/2016.
 */
public class MessageService implements IMessageService {
    private final Logger LOGGER = Logger.getLogger(MessageService.class);
    private DynamoDBMapper db;
    private ModelMapper mapper;
    //private static final int PURGED_DATE = 6;

    @Inject
    public MessageService(DynamoDBMapper db, ModelMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }

    @Override
    public Message create(Message message) {
        message.setCreateDate(System.currentTimeMillis());
        message.setReadCount(0);
        db.save(message);
        return message;
    }

    @Override
    public BatchResult<List<Message>> create(HashMap<String, List<Message>> messageMap) {
        if (messageMap.isEmpty()) {
            return null;
        }
        List<Message> messageList = new ArrayList<>();
        Iterator<Map.Entry<String, List<Message>>> iterator = messageMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Message>> entry = iterator.next();
            List<Message> messages = entry.getValue();

            long createTimestamp = 0;
            for (Message message : messages) {
                message.setAccount(entry.getKey());
                long timestamp = System.currentTimeMillis();
                if (timestamp > createTimestamp) {
                    createTimestamp = timestamp;
                } else {
                    createTimestamp++;
                }
                message.setCreateDate(createTimestamp);
                message.setReadCount(0);
                messageList.add(message);
            }
        }
        List<DynamoDBMapper.FailedBatch> failedBatches = db.batchSave(messageList);
        return new BatchResult<>(messageList, failedBatches);
    }

    @Override
    public Message get(String account, long created) {
        Message message = null;
        try {
            message = db.load(Message.class, account, created);
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return message;
    }

    @Override
    public List<Message> get(String account) {
        Message messageKey = new Message();
        messageKey.setAccount(account);

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withHashKeyValues(messageKey);
        List<Message> messages = db.query(Message.class, queryExpression);
        //List<Message> returnMessages = getPurgedList(messages);

        return messages;
    }

    @Override
    public List<Message> get(UUID categoryId, long offsetTimestamp, int limit) {
        List<Message> result;
/*        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":eventEnd", new AttributeValue().withN(String.valueOf(offsetTimestamp)));
        DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
                .withFilterExpression("eventEnd_timestamp < :eventEnd")
                .withExpressionAttributeValues(eav);

        return db.scan(Message.class, queryExpression);*/
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":categoryId", new AttributeValue().withS(categoryId.toString()));
        eav.put(":eventEnd", new AttributeValue().withN(String.valueOf(offsetTimestamp)));

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withIndexName(MESSAGE_EXPIRY_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("category_id = :categoryId and eventEnd_timestamp < :eventEnd")
                .withExpressionAttributeValues(eav);
        if (limit > 0) {
            queryExpression = queryExpression.withLimit(limit);
            LOGGER.info("[DELETE EXPIRY]Query message with limit " + String.valueOf(limit));
            QueryResultPage<Message> resultPage = db.queryPage(Message.class, queryExpression);
            result = resultPage.getResults();
        } else {
            result = db.query(Message.class, queryExpression);
        }
        return result;
    }

    @Override
    public List<Message> get(List<Message> messages) {
        Map<String, List<Object>> items = db.batchLoad(messages);
        if (items.containsKey(DB_PREFIX + MESSAGE_TABLE_NAME)) {
            messages = (List<Message>) (List<?>) items.get(DB_PREFIX + MESSAGE_TABLE_NAME);
            return messages;
        } else {
            return null;
        }
    }

/*    @Override
    @Deprecated
    public List<Message> getAllByPlatform(List<String> accounts, String platform) {
        String query = "user_account IN (";
        for (int i = 1; i < accounts.size()+1; i++) {
            query += ":account" + i + " , ";
        }
        query = query.substring(0, query.length() - 2) + ") ";

        query += "AND ";
        if (platform.equalsIgnoreCase(Global.MYSINGTELAPP)) {
            query += "(contains (platform_list, :mysingtelapp) OR contains (platform_list, :all)) ";
        } else if (platform.equalsIgnoreCase(Global.WEB)) {
            query += "contains (platform_list, :web) ";
        }

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        for (int i = 1; i < accounts.size()+1; i++)
            eav.put(":account" + i, new AttributeValue().withS(accounts.get(i - 1)));

        if (platform.equalsIgnoreCase(Global.MYSINGTELAPP)) {
            eav.put(":mysingtelapp", new AttributeValue().withS(Global.MYSINGTELAPP));
            eav.put(":all", new AttributeValue().withS(Global.ALL));
        } else if (platform.equalsIgnoreCase(Global.WEB)) {
            eav.put(":web", new AttributeValue().withS(Global.WEB));
        }
        DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
                .withFilterExpression(query)
                .withExpressionAttributeValues(eav);

        List<Message> messages = db.scan(Message.class, queryExpression);
        List<Message> returnMessages = getPurgedList(messages);

        return returnMessages;
    }

    @Override
    @Deprecated
    public int countUnread(List<String> accounts, String platform) {
        String query = "user_account IN (";
        for (int i = 1; i < accounts.size()+1; i++) {
            query += ":account" + i + " , ";
        }
        query = query.substring(0, query.length() - 2) + ") ";

        query += "AND ";
        if (platform.equalsIgnoreCase(Global.MYSINGTELAPP)) {
            query += "(contains (platform_list, :mysingtelapp) OR contains (platform_list, :all)) ";
        } else if (platform.equalsIgnoreCase(Global.WEB)) {
            query += "contains (platform_list, :web) ";
        }

        query += "AND read_status = :readStatus";

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        for (int i = 1; i < accounts.size()+1; i++)
            eav.put(":account" + i, new AttributeValue().withS(accounts.get(i - 1)));

        if (platform.equalsIgnoreCase(Global.MYSINGTELAPP)) {
            eav.put(":mysingtelapp", new AttributeValue().withS(Global.MYSINGTELAPP));
            eav.put(":all", new AttributeValue().withS(Global.ALL));
        } else if (platform.equalsIgnoreCase(Global.WEB)) {
            eav.put(":web", new AttributeValue().withS(Global.WEB));
        }

        eav.put(":readStatus", new AttributeValue().withN("0"));

        DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
                .withFilterExpression(query)
                .withExpressionAttributeValues(eav);

        List<Message> messages = db.scan(Message.class, queryExpression);
        List<Message> returnMessages = getPurgedList(messages);

        return (returnMessages.size() == 0) ? 0 : returnMessages.size();
    }*/

    @Override
    public Message update(Message message) {
        db.save(message);
        return message;
    }

    @Override
    public List<DynamoDBMapper.FailedBatch> update(List<Message> messages) {
        return db.batchSave(messages);
    }

    @Override
    public Message remove(String account, long created) {
        Message message = null;
        try {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
            message = db.load(Message.class, account, created, config);

            if (message != null) {
                RemovedMessage removedMessage = mapper.map(message, RemovedMessage.class);
                removedMessage.setRemovedDate(System.currentTimeMillis());
                db.save(removedMessage);
                db.delete(message);
            }
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return message;
    }

    public List<DynamoDBMapper.FailedBatch> remove(List<Message> messages) {
        List<DynamoDBMapper.FailedBatch> result = null;
        try {
            List<RemovedMessage> removedMessages = new ArrayList<>();
            long deleteTimestamp = 0;
            for (Message message : messages) {
                RemovedMessage removedMessage = mapper.map(message, RemovedMessage.class);
                long timestamp = System.currentTimeMillis();
                if (timestamp > deleteTimestamp) {
                    deleteTimestamp = timestamp;
                } else {
                    deleteTimestamp++;
                }
                removedMessage.setRemovedDate(deleteTimestamp);
                removedMessages.add(removedMessage);
            }
            db.batchSave(removedMessages);
            result = db.batchDelete(messages);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage() + "\n" + ex.getStackTrace());
        }
        return result;
    }
}
