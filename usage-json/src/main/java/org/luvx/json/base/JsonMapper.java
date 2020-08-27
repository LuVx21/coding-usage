package org.luvx.json.base;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author: Ren, Xie
 */
public interface JsonMapper {
    /**
     * 解析为map
     *
     * @param json
     * @return
     */
    Map toMap(String json);

    Map convertToMap(Object o);

    List toList(String json);

    <T> List<T> toList(String json, final Type type);

    String toJsonString(Object o);

    String toJsonWithDateFormat(Object o, String dateFormatPattern);

    <T> T toPojo(String json, Class<T> clazz);

    <T> T convertFromMap(Map map, Class<T> clazz);
}
