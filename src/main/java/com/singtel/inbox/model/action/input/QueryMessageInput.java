package com.singtel.inbox.model.action.input;

/**
 * Created by Dongwu on 28/1/2016.
 */
public class QueryMessageInput {
    private String account;
    private long createDate;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
