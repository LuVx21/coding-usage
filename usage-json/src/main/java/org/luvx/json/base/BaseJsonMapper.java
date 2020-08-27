package org.luvx.json.base;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.luvx.json.Json;

import java.util.*;

/**
 * @author: Ren, Xie
 */
@Slf4j
public abstract class BaseJsonMapper implements JsonMapper {
    private static final String CLASS_TYPE_OBJECT_MAPPER = ObjectMapper.class.getCanonicalName();
    private static final String CLASS_TYPE_GSON          = Gson.class.getCanonicalName();
    private static final String CLASS_TYPE_FASTJSON      = JSON.class.getCanonicalName();

    public static ObjectMapper objectMapper;
    public static Gson         gson;

    static {
        Json json = null;
        try {
            if (Class.forName(CLASS_TYPE_OBJECT_MAPPER) != null) {
                json = Json.JACKSON;
            } else if (Class.forName(CLASS_TYPE_GSON) != null) {
                json = Json.GSON;
            } else if (Class.forName(CLASS_TYPE_FASTJSON) != null) {
                json = Json.FAST_JSON;
            } else {
                throw new RuntimeException("未找到可用的json库");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        if (Json.JACKSON.equals(json) && objectMapper == null) {
            objectMapper = new ObjectMapper()
                    .findAndRegisterModules()
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
        if (Json.GSON.equals(json) && gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    // 解决gson序列化时出现整型变为浮点型的问题
                    .registerTypeAdapter(new TypeToken<Map<Object, Object>>() {
                            }
                                    .getType(), (JsonDeserializer<Map<Object, Object>>) (jsonElement, type, jsonDeserializationContext) -> {
                                Map<Object, Object> map = new LinkedHashMap<>();
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                for (Map.Entry<String, JsonElement> entry : entrySet) {
                                    Object obj = entry.getValue();
                                    if (obj instanceof JsonPrimitive) {
                                        map.put(entry.getKey(), ((JsonPrimitive) obj).getAsString());
                                    } else {
                                        map.put(entry.getKey(), obj);
                                    }
                                }
                                return map;
                            }
                    )
                    .registerTypeAdapter(new TypeToken<List<Object>>() {
                            }
                                    .getType(), (JsonDeserializer<List<Object>>) (jsonElement, type, jsonDeserializationContext) -> {
                                List<Object> list = new LinkedList<>();
                                JsonArray jsonArray = jsonElement.getAsJsonArray();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    if (jsonArray.get(i).isJsonObject()) {
                                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                        list.addAll(entrySet);
                                    } else if (jsonArray.get(i).isJsonPrimitive()) {
                                        list.add(jsonArray.get(i));
                                    }
                                }
                                return list;
                            }
                    )
                    .create();
        }
    }
}
