package com.singtel.inbox.model.action.input;

/**
 * Created by Dongwu on 26/2/2016.
 */
public class ReadMessageInput {
    private String account;
    private long createDate;
    private boolean readStatus;

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

    public boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
