package org.luvx.json.base;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author: Ren, Xie
 */
public interface JsonMapper {
    <T> T json2Bean(String json, Class<T> clazz);

    /**
     * 解析为map
     *
     * @param json
     * @return
     */
    Map json2Map(String json);

    List json2List(String json);

    <T> List<T> json2List(String json, final Type type);

    Map bean2Map(Object o);

    String bean2Json(Object o, String dateFormatPattern);

    String bean2Json(Object o);

    <T> T map2Bean(Map map, Class<T> clazz);
}
