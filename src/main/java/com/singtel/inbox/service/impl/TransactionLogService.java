package com.singtel.inbox.service.impl;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.Record;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.singtel.inbox.model.TransactionLog;
import com.singtel.inbox.service.ITransactionLogService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;

/**
 * Created by Dongwu on 29/4/2016.
 */
public class TransactionLogService implements ITransactionLogService {
    //private static final String DELIVERY_STREAM_NAME = "inbox-transaction-log-uat";
    private static final String DELIVERY_STREAM_NAME = "inbox-transaction-log-prod";

    @Inject
    AmazonKinesisFirehoseClient firehoseClient;

    @Override
    public void commit(TransactionLog log) {
        log.setDuration(System.currentTimeMillis() - log.getTimestamp().getTime());

        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(DELIVERY_STREAM_NAME);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                .create();
        String data = gson.toJson(log) + "\n";

        Record record = new Record().withData(ByteBuffer.wrap(data.getBytes()));
        putRecordRequest.setRecord(record);

// Put record into the DeliveryStream
        firehoseClient.putRecord(putRecordRequest);
    }

    @Override
    public void exception(Exception ex, TransactionLog log) {
        log.setException("[" + ex.toString() + "]" + ex.getMessage());
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        log.setStackTrace(sw.toString());
        commit(log);
    }
}