package com.singtel.inbox.model.action.input;

/**
 * Created by Dongwu on 21/1/2016.
 */
public class UpdateReadStatusInput {
    private String account;
    private long created;
    private int readStatus;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
