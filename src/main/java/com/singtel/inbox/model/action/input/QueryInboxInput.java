package com.singtel.inbox.model.action.input;

import java.util.List;

/**
 * Created by Dongwu on 26/1/2016.
 */
public class QueryInboxInput {
    private List<String> accounts;
    private String platform;

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
