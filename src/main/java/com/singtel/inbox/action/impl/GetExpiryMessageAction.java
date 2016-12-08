package com.singtel.inbox.action.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.singtel.inbox.action.AbstractAction;
import com.singtel.inbox.exception.BadRequestException;
import com.singtel.inbox.exception.InternalErrorException;
import com.singtel.inbox.exception.NotFoundException;
import com.singtel.inbox.model.Category;
import com.singtel.inbox.model.Message;
import com.singtel.inbox.model.action.MessageKey;
import com.singtel.inbox.model.action.output.BatchResult;
import com.singtel.inbox.service.ICategoryService;
import com.singtel.inbox.service.IMessageService;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gedongwu on 7/9/2016.
 */
public class GetExpiryMessageAction extends AbstractAction<String, BatchResult<List<String>>> {
    private final Logger LOGGER = Logger.getLogger(RemoveExpiryMessageAction.class);
    @Inject
    private AmazonS3Client s3Client;
    @Inject
    private IMessageService messageService;
    @Inject
    private ICategoryService categoryService;

    @Override
    protected BatchResult<List<String>> trigger(String input, Context context) throws BadRequestException, InternalErrorException, NotFoundException {
        long timestamp = System.currentTimeMillis();
        List<Category> categories = categoryService.get();
        List<Message> messageList = new ArrayList<>();
        for (Category category : categories) {
            int purgeDays = category.getPurgeDays();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.add(Calendar.DATE, -purgeDays);
            List<Message> messages = messageService.get(category.getId(), calendar.getTimeInMillis(), 0);
            if (messages != null && messages.size() > 0) {
                messageList.addAll(messages);
            }
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filePattern = "Production/ExpiredMessages/Processing/" + format.format(date) + "_";
        List<MessageKey> keys = messageList.stream().map(message -> new MessageKey(message.getAccount(), message.getCreateDate())).collect(Collectors.toList());
        List<List<MessageKey>> keyBatch = split(keys, 500);
        List<String> fileNames = new ArrayList<>();
        for (int i = 0; i < keyBatch.size(); i++) {
            String fileName = filePattern + String.valueOf(i) + ".log";
            s3Client.putObject("com.singtel.inbox", fileName, new Gson().toJson(keyBatch.get(i)));
            fileNames.add(fileName);
        }
        return new BatchResult<>(fileNames);
    }

    @Override
    protected Type getInputType() {
        return null;
    }

    private List<List<MessageKey>> split(List<MessageKey> ary, int subSize) {
        int count = ary.size() % subSize == 0 ? ary.size() / subSize : ary.size() / subSize + 1;

        List<List<MessageKey>> subAryList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int index = i * subSize;

            List<MessageKey> list = new ArrayList<>();
            int j = 0;
            while (j < subSize && index < ary.size()) {
                list.add(ary.get(index++));
                j++;
            }

            subAryList.add(list);
        }
        return subAryList;
    }
}
