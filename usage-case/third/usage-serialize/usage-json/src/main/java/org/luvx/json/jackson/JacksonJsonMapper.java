package org.luvx.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.luvx.json.base.BaseJsonMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: Ren, Xie
 */
public class JacksonJsonMapper extends BaseJsonMapper {
    @Override
    public Map json2Map(String json) {
        try {
            return objectMapper.readValue(json, LinkedHashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List json2List(String json) {
        try {
            return objectMapper.readValue(json, LinkedList.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> json2List(String json, Type type) {
        TypeReference<T> typeReference = new TypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
        try {
            return (List<T>) objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String bean2Json(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String bean2Json(Object o, String dateFormatPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);
        try {
            return objectMapper.writer(sdf).writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T json2Bean(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public Map bean2Map(Object o) {
        return objectMapper.convertValue(o, LinkedHashMap.class);
    }

    @Override
    public <T> T map2Bean(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }
}
