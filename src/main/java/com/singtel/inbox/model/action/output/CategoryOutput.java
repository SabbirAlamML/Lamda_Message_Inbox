package com.singtel.inbox.model.action.output;

import java.util.List;
import java.util.UUID;

/**
 * Created by Dongwu on 27/1/2016.
 */

public class CategoryOutput {
    private UUID id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private int order;
    private boolean subscribed;
    private List<MessageOutput> messageList;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public List<MessageOutput> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageOutput> messageList) {
        this.messageList = messageList;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
