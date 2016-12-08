package com.singtel.inbox.service;

import com.singtel.inbox.model.CategorySetting;

import java.util.List;
import java.util.UUID;

/**
 * Created by Dongwu on 25/1/2016.
 */
public interface ICategorySettingService {
    CategorySetting get(String account, UUID category_id);

    List<CategorySetting> get(String account);

    void update(CategorySetting setting);

    CategorySetting remove(String account, UUID category_id);

    int remove(String account);
}
