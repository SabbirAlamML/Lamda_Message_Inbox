package com.singtel.inbox.model.action.input;

import com.singtel.inbox.model.Action;
import com.singtel.inbox.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dongwu on 1/3/2016.
 */
public class CreateMessageBatchInput {
    private List<String> accounts;
    private long sendDate;
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
    private String groupingIndex;
    private long eventEndDate;
    private List<Action> actionList;
    private List<String> platformList;

    public CreateMessageBatchInput() {
        actionList = new ArrayList<>();
        platformList = new ArrayList<>();
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Resource> getThumbnailList() {
        return thumbnailList;
    }

    public void setThumbnailList(List<Resource> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    public String getBadgeIcon() {
        return badgeIcon;
    }

    public void setBadgeIcon(String badgeIcon) {
        this.badgeIcon = badgeIcon;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getAccountPrefix() {
        return accountPrefix;
    }

    public void setAccountPrefix(String accountPrefix) {
        this.accountPrefix = accountPrefix;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(long eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    public List<String> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<String> platformList) {
        this.platformList = platformList;
    }

    public String getGroupingIndex() {
        return groupingIndex;
    }

    public void setGroupingIndex(String groupingIndex) {
        this.groupingIndex = groupingIndex;
    }
}
