package com.singtel.inbox.model.action.input;

import java.util.UUID;

/**
 * Created by Dongwu on 25/1/2016.
 */
public class SubscribeCategoryInput {
    private String account;
    private UUID categoryId;
    private boolean subscribed;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
