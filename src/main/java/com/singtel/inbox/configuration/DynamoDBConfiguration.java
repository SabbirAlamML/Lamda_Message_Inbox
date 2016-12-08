package com.singtel.inbox.configuration;

/**
 * Created by Dongwu on 20/1/2016.
 */
public class DynamoDBConfiguration {
    //public static final String DB_PREFIX = "UAT_";
    public static final String DB_PREFIX = "PROD_";
    public static final String CATEGORY_TABLE_NAME = "inbox_category";
    public static final String CATEGORY_SETTING_TABLE_NAME = "inbox_category_setting";
    public static final String MESSAGE_TABLE_NAME = "inbox_message";

    public static final String REMOVED_CATEGORY_TABLE_NAME = "inbox_removed_category";
    public static final String REMOVED_MESSAGE_TABLE_NAME = "inbox_removed_message";

    public static final String MESSAGE_EXPIRY_INDEX = "category_id-eventEnd_timestamp-index";
}
