package com.singtel.inbox.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.singtel.inbox.configuration.DynamoDBConfiguration.DB_PREFIX;
import static com.singtel.inbox.configuration.DynamoDBConfiguration.REMOVED_MESSAGE_TABLE_NAME;

/**
 * Created by Dongwu on 26/1/2016.
 */
@DynamoDBTable(tableName = DB_PREFIX + REMOVED_MESSAGE_TABLE_NAME)
public class RemovedMessage {
    private String account;
    private long createDate;
    private UUID categoryId;
    private String subject;
    private String from;
    private String summary;
    private String content;
    private String contentType;
    private int priority;
    private List<Resource> thumbnailList;
    private String badgeIcon;
    private String referenceId;
    private String accountPrefix;
    private String accountNumber;
    private long sendDate;
    private long eventEndDate;
    private int readCount;
    private List<Action> actionList;
    private List<String> platformList;
    private long removedDate;
    private boolean readStatus;
    private String executor;
    private String groupingIndex;

    public RemovedMessage() {
        actionList = new ArrayList<>();
        platformList = new ArrayList<>();
    }

    public RemovedMessage(String account, long removedDate) {
        this.account = account;
        this.removedDate = removedDate;
    }

    @DynamoDBHashKey(attributeName = "user_account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @DynamoDBRangeKey(attributeName = "removed_timestamp")
    public long getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(long removedDate) {
        this.removedDate = removedDate;
    }

    @DynamoDBAttribute(attributeName = "created_timestamp")
    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @DynamoDBAttribute(attributeName = "category_id")
    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    @DynamoDBAttribute(attributeName = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @DynamoDBAttribute(attributeName = "from")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @DynamoDBAttribute(attributeName = "summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @DynamoDBAttribute(attributeName = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @DynamoDBAttribute(attributeName = "content_type")
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @DynamoDBAttribute(attributeName = "priority")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @DynamoDBAttribute(attributeName = "thumbnail_list")
    public List<Resource> getThumbnailList() {
        return thumbnailList;
    }

    public void setThumbnailList(List<Resource> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    @DynamoDBAttribute(attributeName = "badge_icon")
    public String getBadgeIcon() {
        return badgeIcon;
    }

    public void setBadgeIcon(String badgeIcon) {
        this.badgeIcon = badgeIcon;
    }

    @DynamoDBAttribute(attributeName = "reference_id")
    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @DynamoDBAttribute(attributeName = "account_prefix")
    public String getAccountPrefix() {
        return accountPrefix;
    }

    public void setAccountPrefix(String accountPrefix) {
        this.accountPrefix = accountPrefix;
    }

    @DynamoDBAttribute(attributeName = "account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @DynamoDBAttribute(attributeName = "eventEnd_timestamp")
    public long getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(long eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    @DynamoDBAttribute(attributeName = "send_timestamp")
    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    @DynamoDBAttribute(attributeName = "read_count")
    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    @DynamoDBAttribute(attributeName = "action_list")
    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    @DynamoDBAttribute(attributeName = "platform_list")
    public List<String> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<String> platformList) {
        this.platformList = platformList;
    }

    @DynamoDBAttribute(attributeName = "read_status")
    public boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    @DynamoDBAttribute(attributeName = "removed_by")
    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    @DynamoDBAttribute(attributeName = "grouping_index")
    public String getGroupingIndex() {
        return groupingIndex;
    }

    public void setGroupingIndex(String groupingIndex) {
        this.groupingIndex = groupingIndex;
    }
}
