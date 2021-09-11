package com.ivan.common_module;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;

public abstract class JsonUtils {

    private JsonUtils() {}

    public static <T> String toJson(T obj)
            throws JsonGenerationException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        return json;
    }

    @SuppressWarnings({"unchecked"})
    public static LinkedHashMap<String, Object> toJavaObject(String json)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, Object> javaObject = mapper.readValue(json, LinkedHashMap.class);

        return javaObject;
    }

}
