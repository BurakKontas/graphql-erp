package tr.kontas.erp.core.platform.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JacksonProvider {

    private static final ObjectMapper INSTANCE = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    private JacksonProvider() {
    }

    public static ObjectMapper get() {
        return INSTANCE;
    }

    public static String serialize(Object value) {
        try {
            return INSTANCE.writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalStateException("JSON serialization failed", e);
        }
    }

    public static <T> T deserialize(String json, TypeReference<T> typeRef) {
        try {
            return INSTANCE.readValue(json, typeRef);
        } catch (Exception e) {
            throw new IllegalStateException("JSON deserialization failed", e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return INSTANCE.readValue(json, clazz);
        } catch (Exception e) {
            throw new IllegalStateException("JSON deserialization failed", e);
        }
    }
}

