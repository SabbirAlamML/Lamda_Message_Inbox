package com.singtel.inbox.service;

import com.singtel.inbox.model.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    Category create(Category category);

    Category update(Category category);

    List<Category> get();

    Category get(UUID id);

    Category remove(UUID categoryId);
}
