package com.climbing.global.converter;

import com.climbing.global.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.Type;

@Converter
public abstract class JsonConverter<T> implements AttributeConverter<T, String> {
    final TypeReference<T> typeReference = new TypeReference<>() {};
    @Override
    public String convertToDatabaseColumn(T entity) {
        if(entity == null) {
            return null;
        }
        return JsonUtil.toJson(entity);
    }
    @Override
    public T convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty() || dbData.equals("[]")) {
            return null;
        } else {
            return JsonUtil.toObject(dbData, typeReference);
        }
    }
}
