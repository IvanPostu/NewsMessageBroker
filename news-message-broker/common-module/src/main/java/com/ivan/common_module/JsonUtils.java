package com.ivan.common_module;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;

public abstract class JsonUtils {

    private JsonUtils() {}

    public static <T> String toJson(T obj)
            throws JsonGenerationException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        return json;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T toJavaObject(String json)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object javaObject = mapper.readValue(json, Object.class);

        return (T) javaObject;
    }

}
