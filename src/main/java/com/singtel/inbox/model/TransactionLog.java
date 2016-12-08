package com.singtel.inbox.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Dongwu on 29/4/2016.
 */
public class TransactionLog {
    @SerializedName("log_id")
    private UUID id;
    @SerializedName("action")
    private String action;
    @SerializedName("request")
    private String request;
    @SerializedName("response")
    private String response;
    @SerializedName("call_timestamp")
    private Date timestamp;
    @SerializedName("duration")
    private long duration;
    @SerializedName("user_arn")
    private String identityId;
    @SerializedName("ip_address")
    private String ipAddress;
    @SerializedName("exception")
    private String exception;
    @SerializedName("stack_trace")
    private String stackTrace;
    @SerializedName("account")
    private String account;

    public TransactionLog() {
        id = UUID.randomUUID();
        timestamp = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request.length() > 128000 ? request.substring(0, 127997) + "..." : request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response.length() > 128000 ? response.substring(0, 127997) + "..." : response;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception.length() > 128000 ? exception.substring(0, 127997) + "..." : exception;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace.length() > 128000 ? stackTrace.substring(0, 127997) + "..." : stackTrace;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account.length() > 128000 ? account.substring(0, 127997) + "..." : account;
    }
}