package org.luvx.json.fastjson;

import com.alibaba.fastjson2.JSON;
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
    public Map json2Map(String json) {
        return JSON.parseObject(json, LinkedHashMap.class);
    }

    @Override
    public List json2List(String json) {
        return JSON.parseObject(json, LinkedList.class);
    }

    @Override
    public <T> List<T> json2List(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    @Override
    public String bean2Json(Object o) {
        return JSON.toJSONString(o);
    }

    @Override
    public String bean2Json(Object o, String dateFormatPattern) {
        // return JSON.toJSONStringWithDateFormat(o, dateFormatPattern, SerializerFeature.WriteDateUseDateFormat);
        return "";
    }

    @Override
    public <T> T json2Bean(String json, Class<T> valueType) {
        return JSON.parseObject(json, valueType);
    }

    @Override
    public Map bean2Map(Object o) {
        String json = JSON.toJSONString(o);
        return JSON.parseObject(json, LinkedHashMap.class);
    }

    @Override
    public <T> T map2Bean(Map map, Class<T> clazz) {
        String json = JSON.toJSONString(map);
        return JSON.parseObject(json, clazz);
    }
}
