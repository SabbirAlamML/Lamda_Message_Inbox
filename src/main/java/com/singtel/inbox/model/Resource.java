package com.singtel.inbox.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dongwu on 28/1/2016.
 */
@DynamoDBDocument
public class Resource {
    private List<String> platformList;
    private String url;

    public Resource() {
        this.platformList = new ArrayList<>();
    }

    @DynamoDBAttribute(attributeName = "platform_list")
    public List<String> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<String> platformList) {
        this.platformList = platformList;
    }

    @DynamoDBAttribute(attributeName = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
