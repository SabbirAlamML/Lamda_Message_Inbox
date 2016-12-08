package com.singtel.inbox.model.action;

/**
 * Created by gedongwu on 7/7/2016.
 */
public class MessageKey {

    private String account;
    private long createDate;

    public MessageKey(String account, long createDate) {
        this.account = account;
        this.createDate = createDate;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageKey that = (MessageKey) o;

        if (createDate != that.createDate) return false;
        return account != null ? account.equals(that.account) : that.account == null;

    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (int) (createDate ^ (createDate >>> 32));
        return result;
    }
}
