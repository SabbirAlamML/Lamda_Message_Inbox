package com.singtel.inbox.model.action.output;

/**
 * Created by Dongwu on 3/3/2016.
 */
public class UnreadCountOutput {
    private int count;

    public UnreadCountOutput(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
