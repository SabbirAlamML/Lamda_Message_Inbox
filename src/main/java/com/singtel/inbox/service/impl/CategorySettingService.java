package com.singtel.inbox.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.google.inject.Inject;
import com.singtel.inbox.model.CategorySetting;
import com.singtel.inbox.service.ICategorySettingService;

import java.util.List;
import java.util.UUID;

/**
 * Created by Dongwu on 25/1/2016.
 */
public class CategorySettingService implements ICategorySettingService {
    private DynamoDBMapper db;

    @Inject
    public CategorySettingService(DynamoDBMapper db) {
        this.db = db;
    }

    @Override
    public CategorySetting get(String account, UUID category_id) {
        CategorySetting setting = null;
        try {
            setting = db.load(CategorySetting.class, account, category_id);
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return setting;
    }

    @Override
    public List<CategorySetting> get(String account) {
        CategorySetting settingKey = new CategorySetting();
        settingKey.setAccount(account);
        DynamoDBQueryExpression<CategorySetting> queryExpression = new DynamoDBQueryExpression<CategorySetting>()
                .withHashKeyValues(settingKey);
        return db.query(CategorySetting.class, queryExpression);
    }

    @Override
    public void update(CategorySetting setting) {
        db.save(setting);
    }

    @Override
    public CategorySetting remove(String account, UUID category_id) {
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        CategorySetting setting = null;
        try {
            setting = db.load(CategorySetting.class, account, category_id, config);
            if (setting != null) {
                db.delete(setting);
            }
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return setting;
    }

    @Override
    public int remove(String account) {
        List<CategorySetting> settings = get(account);
        if (settings != null && settings.size() > 0) {
            settings.forEach(setting -> db.delete(setting));
        }
        return settings.size();
    }
}
