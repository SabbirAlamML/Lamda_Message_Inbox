package com.singtel.inbox.model.action;

/**
 * Created by gedongwu on 16/8/2016.
 */
public class RemovedMessageKey {
    private String account;
    private long removedDate;

    public RemovedMessageKey(String account, long removedDate) {
        this.account = account;
        this.removedDate = removedDate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(long removedDate) {
        this.removedDate = removedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemovedMessageKey that = (RemovedMessageKey) o;

        if (removedDate != that.removedDate) return false;
        return account != null ? account.equals(that.account) : that.account == null;

    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (int) (removedDate ^ (removedDate >>> 32));
        return result;
    }
}
