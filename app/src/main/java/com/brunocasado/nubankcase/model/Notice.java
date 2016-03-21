package com.brunocasado.nubankcase.model;

import android.support.v4.util.ArrayMap;

import java.io.Serializable;

/**
 * Created by Bruno Casado on 07/03/2016.
 */
public class Notice implements Serializable {

    private String title;
    private String description;
    private ArrayMap<String, String> primary_action;
    private ArrayMap<String, String> secondary_action;
    private ArrayMap<String, ArrayMap<String, String>> links;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayMap<String, String> getPrimary_action() {
        return primary_action;
    }

    public void setPrimary_action(ArrayMap<String, String> primaryAction) {
        this.primary_action = primaryAction;
    }

    public ArrayMap<String, String> getSecondary_action() {
        return secondary_action;
    }

    public void setSecondary_action(ArrayMap<String, String> secondaryAction) {
        this.secondary_action = secondaryAction;
    }

    public ArrayMap<String, ArrayMap<String, String>> getLinks() {
        return links;
    }

    public void setLinks(ArrayMap<String, ArrayMap<String, String>> links) {
        this.links = links;
    }
}
