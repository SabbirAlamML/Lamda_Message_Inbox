package com.singtel.inbox.model.action.output;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.util.List;

/**
 * Created by gedongwu on 7/7/2016.
 */
public class BatchResult<T> {
    T result;
    List<DynamoDBMapper.FailedBatch> failedBatches;

    public BatchResult(T result) {
        this.result = result;
    }

    public BatchResult(T result, List<DynamoDBMapper.FailedBatch> failedBatches) {
        this.result = result;
        this.failedBatches = failedBatches;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<DynamoDBMapper.FailedBatch> getFailedBatches() {
        return failedBatches;
    }

    public void setFailedBatches(List<DynamoDBMapper.FailedBatch> failedBatches) {
        this.failedBatches = failedBatches;
    }
}
