package com.singtel.inbox.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.singtel.inbox.model.RemovedMessage;

import java.util.List;

/**
 * Created by gedongwu on 16/8/2016.
 */
public interface IRemovedMessageService {
    List<RemovedMessage> get(String account);

    List<RemovedMessage> get(List<RemovedMessage> removedMessages);

    List<DynamoDBMapper.FailedBatch> restore(List<RemovedMessage> removedMessages);
}
