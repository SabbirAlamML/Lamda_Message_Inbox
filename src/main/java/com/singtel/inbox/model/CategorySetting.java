package com.singtel.inbox.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.UUID;

import static com.singtel.inbox.configuration.DynamoDBConfiguration.CATEGORY_SETTING_TABLE_NAME;
import static com.singtel.inbox.configuration.DynamoDBConfiguration.DB_PREFIX;

/**
 * Created by Dongwu on 25/1/2016.
 */
@DynamoDBTable(tableName = DB_PREFIX + CATEGORY_SETTING_TABLE_NAME)
public class CategorySetting {
    private String account;
    private UUID categoryId;
    private boolean subscribed;
    private boolean notify;

    @DynamoDBHashKey(attributeName = "user_account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @DynamoDBRangeKey(attributeName = "category_id")
    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryid) {
        this.categoryId = categoryid;
    }

    @DynamoDBAttribute(attributeName = "subscribed")
    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    @DynamoDBAttribute(attributeName = "notify")
    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}