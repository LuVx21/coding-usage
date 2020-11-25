package org.luvx.json;

import lombok.Getter;
import org.luvx.json.base.BaseJsonMapper;
import org.luvx.json.fastjson.FastJsonMapper;
import org.luvx.json.gson.GsonMapper;
import org.luvx.json.jackson.JacksonJsonMapper;

import java.util.Objects;

/**
 * @author: Ren, Xie
 */
public enum Json {
    FAST_JSON("fastjson"),
    GSON("gson"),
    JACKSON("jackson");

    @Getter
    private String type;

    Json() {
    }

    Json(String classType) {
        this.type = classType;
    }

    public BaseJsonMapper of() {
        if (Objects.equals(type, FAST_JSON.getType())) {
            return new FastJsonMapper();
        }
        if (Objects.equals(type, GSON.getType())) {
            return new GsonMapper();
        }
        if (Objects.equals(type, JACKSON.getType())) {
            return new JacksonJsonMapper();
        }
        return null;
    }
}
