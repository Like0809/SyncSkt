package com.like.common.network;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.like.common.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataPackage {
    public static final int REQUEST = 1;
    public static final int RESPONSE = 2;

    protected int id;
    protected int type;
    protected Map<String, Object> params;

    public DataPackage() {
        this.id = RequestIdMaker.getId();
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(16);
        }
        return params;
    }

    public DataPackage setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public DataPackage addParam(String key, Object value) {
        if (value != null) {
            getParams().put(key, value);
        }
        return this;
    }

    @JsonIgnore
    public <T> T getParam(String key, Class<T> clazz) {
        return JsonUtil.convert(getParams().get(key), clazz);
    }

    @JsonIgnore
    public <T> T getParam(String key, TypeReference valueTypeRef) {
        return JsonUtil.convert(getParams().get(key), valueTypeRef);
    }

    @JsonIgnore
    public String getString(String key) {
        return (String) params.get(key);
    }

    @JsonIgnore
    public int getInt(String key) {
        if (params.get(key) == null) {
            return 0;
        }
        return (int) params.get(key);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonIgnore
    public boolean isResponse() {
        return type == RESPONSE;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @JsonIgnore
    public static boolean isDataPackage(String json) {
        try {
            JsonNode jsonNode = JsonUtil.parse(json, JsonNode.class);
            return jsonNode != null && !jsonNode.isArray() && jsonNode.has("id") && jsonNode.has("type");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
