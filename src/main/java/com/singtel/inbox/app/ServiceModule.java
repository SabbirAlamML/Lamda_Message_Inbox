package com.singtel.inbox.app;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kms.AWSKMS;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.singtel.inbox.service.*;
import com.singtel.inbox.service.impl.*;
import org.apache.log4j.Logger;

import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.matcher.Matchers.any;
import static com.singtel.inbox.app.ExceptionMethodInterceptor.exception;

/**
 * Created by Dongwu on 20/1/2016.
 */
public class ServiceModule extends AbstractModule {
    private static final Logger LOGGER = Logger.getLogger(ServiceModule.class);
    AmazonDynamoDB dbClient;
    AmazonKinesisFirehoseClient firehoseClient;
    private ServiceType type;

    public ServiceModule(ServiceType type, Region region) {
        this.type = type;
        /*try {
            credentials = new ProfileCredentialsProvider("dongwu").getCredentials();
        } catch (Exception ex) {
            throw new AmazonClientException("unable to authenticate the credentials.", ex);
        }*/
        //ClientConfiguration clientConfig = new ClientConfiguration();
        //clientConfig.setConnectionMaxIdleMillis(240000L);
        dbClient = new AmazonDynamoDBClient();
        dbClient.setRegion(region);
        //dbClient.setEndpoint("http://localhost:8000");

        firehoseClient = new AmazonKinesisFirehoseClient();
        firehoseClient.setRegion(Region.getRegion(Regions.EU_WEST_1));
    }
    //AWSCredentials credentials;

    public static ServiceModule getInstance(Region region) {
        return getInstance(ServiceType.DynamoDB, region);
    }

    public static ServiceModule getInstance(ServiceType type, Region region) {
        return new ServiceModule(type, region);
    }

    @Override
    protected void configure() {
        final Binder binder = binder();
        if (LOGGER.isDebugEnabled()) {
            binder.bindInterceptor(any(), any(), exception());
        }

        switch (type) {
            case DynamoDB:
                binder.bind(ICategoryService.class).to(CategoryService.class).in(SINGLETON);
                binder.bind(ICategorySettingService.class).to(CategorySettingService.class).in(SINGLETON);
                binder.bind(IMessageService.class).to(MessageService.class).in(SINGLETON);
                binder.bind(IRemovedMessageService.class).to(RemovedMessageService.class).in(SINGLETON);
                binder.bind(ITransactionLogService.class).to(TransactionLogService.class).in(SINGLETON);
                break;
        }
    }

    @Provides
    @Singleton
    public AmazonDynamoDB getDBClient() {
        return dbClient;
    }

    @Provides
    @Singleton
    public AmazonKinesisFirehoseClient getFirehoseClient() {
        return firehoseClient;
    }

    @Provides
    @Singleton
    public DynamoDBMapper getDBMapper(AmazonDynamoDB dbClient, AWSKMS kms) {
/*        if (kms != null) {
            EncryptionMaterialsProvider provider = new DirectKmsMaterialProvider(kms, KMSConfiguration.DB_ENCRYPTION_KEY_ID);
            return new DynamoDBMapper(dbClient, DynamoDBMapperConfig.DEFAULT, new AttributeEncryptor(provider));
        } else {*/
        return new DynamoDBMapper(dbClient);
//        }
    }

    public enum ServiceType {
        DynamoDB
    }
}
