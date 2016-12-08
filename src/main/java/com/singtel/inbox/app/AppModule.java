package com.singtel.inbox.app;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.input.CreateMessageBatchInput;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import static com.google.inject.matcher.Matchers.any;
import static com.singtel.inbox.app.ExceptionMethodInterceptor.exception;

/**
 * Created by Dongwu on 13/1/2016.
 */
public class AppModule extends AbstractModule {
    private static final Logger LOGGER = Logger.getLogger(AppModule.class);
    private Region region;
    private AWSKMS kms;
    private AmazonS3Client s3Client;
    private ModelMapper mapper;

    public AppModule() {
        mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<CreateMessageBatchInput, Message>() {
            protected void configure() {
                skip().setAccount(null);
            }
        });
        region = Region.getRegion(Regions.AP_SOUTHEAST_1);
        kms = new AWSKMSClient();
        kms.setRegion(region);
        s3Client = new AmazonS3Client();
        s3Client.setRegion(region);
    }

    @Override
    protected void configure() {
        final Binder binder = binder();
        if (LOGGER.isDebugEnabled()) {
            binder.bindInterceptor(any(), any(), exception());
        }
        install(ServiceModule.getInstance(region));
    }

    @Provides
    @Singleton
    public ModelMapper getModelMapper() {
        return mapper;
    }

    @Provides
    @Singleton
    public AWSKMS getKms() {
        return kms;
    }

    @Provides
    @Singleton
    public AmazonS3Client getS3Client() {
        return s3Client;
    }
}