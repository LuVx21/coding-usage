package org.luvx.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.luvx.json.base.BaseJsonMapper;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: Ren, Xie
 */
public class FastJsonMapper extends BaseJsonMapper {
    @Override
    public Map toMap(String json) {
        return JSON.parseObject(json, LinkedHashMap.class);
    }

    @Override
    public List toList(String json) {
        return JSON.parseObject(json, LinkedList.class);
    }

    @Override
    public <T> List<T> toList(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    @Override
    public String toJsonString(Object o) {
        return JSON.toJSONString(o);
    }

    @Override
    public String toJsonWithDateFormat(Object o, String dateFormatPattern) {
        return JSON.toJSONStringWithDateFormat(o, dateFormatPattern, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public <T> T toPojo(String json, Class<T> valueType) {
        return JSON.parseObject(json, valueType);
    }

    @Override
    public Map convertToMap(Object o) {
        String json = JSON.toJSONString(o);
        return JSON.parseObject(json, LinkedHashMap.class);
    }

    @Override
    public <T> T convertFromMap(Map map, Class<T> clazz) {
        String json = JSON.toJSONString(map);
        return JSON.parseObject(json, clazz);
    }
}
