package com.singtel.inbox.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.UUID;

import static com.singtel.inbox.configuration.DynamoDBConfiguration.CATEGORY_TABLE_NAME;
import static com.singtel.inbox.configuration.DynamoDBConfiguration.DB_PREFIX;

/**
 * Created by Dongwu on 11/1/2016.
 */

@DynamoDBTable(tableName = DB_PREFIX + CATEGORY_TABLE_NAME)
public class Category {
    private UUID id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private int order;
    private long created;
    private int purgeDays;

    @DynamoDBHashKey(attributeName = "category_id")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "category_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "category_desc")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @DynamoDBAttribute(attributeName = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @DynamoDBAttribute(attributeName = "order")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @DynamoDBAttribute(attributeName = "created_timestamp")
    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @DynamoDBAttribute(attributeName = "purge_days")
    public int getPurgeDays() {
        return purgeDays;
    }

    public void setPurgeDays(int purgeDays) {
        this.purgeDays = purgeDays;
    }
}
