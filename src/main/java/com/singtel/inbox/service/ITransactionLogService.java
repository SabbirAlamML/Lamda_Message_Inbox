package com.singtel.inbox.service;

import com.singtel.inbox.model.TransactionLog;

/**
 * Created by Dongwu on 29/4/2016.
 */
public interface ITransactionLogService {
    void commit(TransactionLog log);

    void exception(Exception ex, TransactionLog log);
}
