package com.singtel.inbox.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.google.inject.Inject;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.model.RemovedCategory;
import com.singtel.inbox.service.ICategoryService;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.UUID;

/**
 * Created by Dongwu on 12/1/2016.
 */
public class CategoryService implements ICategoryService {
    private DynamoDBMapper db;
    private ModelMapper mapper;

    @Inject
    public CategoryService(DynamoDBMapper db, ModelMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }

    @Override
    public Category create(Category category) {
        if (category.getId() == null) {
            category.setId(UUID.randomUUID());
        }
        category.setCreated(System.currentTimeMillis());
        db.save(category);
        return category;
    }

    @Override
    public Category update(Category category) {
/*        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        Category itemRetrieved = db.load(Category.class, category.getId(), config);
        if(itemRetrieved==null){
            throw new NotFoundException("")
        }*/
        db.save(category);
        return category;
    }

    @Override
    public List<Category> get() {
        DynamoDBScanExpression expression = new DynamoDBScanExpression();
        List<Category> categories = db.scan(Category.class, expression);
        return categories;
    }

    @Override
    public Category get(UUID id) {
        Category category = null;
        try {
            category = db.load(Category.class, id);
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return category;
    }

    @Override
    public Category remove(UUID categoryId) {
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        Category category = null;
        try {
            category = db.load(Category.class, categoryId, config);
            if (category != null) {
                RemovedCategory removedCategory = mapper.map(category, RemovedCategory.class);
                removedCategory.setRemoved(System.currentTimeMillis());
                db.save(removedCategory);
                db.delete(category);
            }
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return category;
    }
}