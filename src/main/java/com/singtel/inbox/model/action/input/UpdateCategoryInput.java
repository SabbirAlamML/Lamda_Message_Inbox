package com.singtel.inbox.model.action.input;

/**
 * Created by Dongwu on 21/1/2016.
 */
public class UpdateCategoryInput {
    private String categoryId;
    private String name;
    private String description;
    private String icon;
    private String color;
    private int order;
    private int purgeDays;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPurgeDays() {
        return purgeDays;
    }

    public void setPurgeDays(int purgeDays) {
        this.purgeDays = purgeDays;
    }
}
