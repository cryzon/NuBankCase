package com.brunocasado.nubankcase.model;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Bruno Casado on 15/03/2016.
 */
public class Chargeback {

    private String comment_hint;
    private String id;
    private String title;
    private boolean autoblock;
    private List<ArrayMap> reason_details = new ArrayList<ArrayMap>();
    private ArrayMap<String, ArrayMap<String, String>> links;

    public String getComment_hint() {
        return comment_hint;
    }

    public void setComment_hint(String comment_hint) {
        this.comment_hint = comment_hint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAutoblock() {
        return autoblock;
    }

    public void setAutoblock(boolean autoblock) {
        this.autoblock = autoblock;
    }

    public List<ArrayMap> getReason_details() {
        return reason_details;
    }

    public void setReason_details(List<ArrayMap> reason_details) {
        this.reason_details = reason_details;
    }

    public ArrayMap<String, ArrayMap<String, String>> getLinks() {
        return links;
    }

    public void setLinks(ArrayMap<String, ArrayMap<String, String>> links) {
        this.links = links;
    }
}
