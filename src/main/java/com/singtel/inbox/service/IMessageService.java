package com.singtel.inbox.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.output.BatchResult;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dongwu on 25/1/2016.
 */
public interface IMessageService {
    Message create(Message message);

    BatchResult<List<Message>> create(HashMap<String, List<Message>> messageMap);

    Message get(String account, long created);

    List<Message> get(String account);

    List<Message> get(UUID categoryId, long offsetTimestamp, int limit);

    List<Message> get(List<Message> messages);

    Message update(Message message);

    List<DynamoDBMapper.FailedBatch> update(List<Message> messages);

    Message remove(String account, long created);

    List<DynamoDBMapper.FailedBatch> remove(List<Message> messages);
}
