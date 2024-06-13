package com.climbing.domain.gym.converter;

import com.climbing.global.util.JsonUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Converter
public class JsonConverter<T> implements AttributeConverter<T, String> {
    @Override
    public String convertToDatabaseColumn(T entity) {
        if(entity == null) {
            return null;
        }
        return JsonUtil.toJson(entity);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) {
            return null;
        }
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType pt)) {
            throw new IllegalStateException("Incorrect use of TypeToken: " + type);
        }
        Type actualType = pt.getActualTypeArguments()[0];
        return JsonUtil.toObject(dbData, (Class<T>) actualType);
    }
}
