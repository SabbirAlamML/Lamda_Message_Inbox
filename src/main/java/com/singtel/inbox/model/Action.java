package com.singtel.inbox.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dongwu on 25/1/2016.
 */
@DynamoDBDocument
public class Action {
    private List<String> platformList;
    private List<Button> buttonList;

    public Action() {
        platformList = new ArrayList<>();
        buttonList = new ArrayList<>();
    }

    @DynamoDBAttribute(attributeName = "platform_list")
    public List<String> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<String> platformList) {
        this.platformList = platformList;
    }

    @DynamoDBAttribute(attributeName = "button_list")
    public List<Button> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<Button> buttonList) {
        this.buttonList = buttonList;
    }
}