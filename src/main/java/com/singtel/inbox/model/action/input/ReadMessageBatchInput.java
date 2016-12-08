package com.singtel.inbox.model.action.input;

import com.singtel.inbox.model.action.MessageKey;

import java.util.List;

/**
 * Created by gedongwu on 28/10/2016.
 */
public class ReadMessageBatchInput {
    private boolean readStatus;
    private List<MessageKey> messages;

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public List<MessageKey> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageKey> messages) {
        this.messages = messages;
    }
}
