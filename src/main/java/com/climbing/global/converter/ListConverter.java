package com.climbing.global.converter;

import com.climbing.domain.gym.Pricing;
import com.climbing.global.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class ListConverter<T> extends JsonConverter<List<T>> {
    @Override
    public String convertToDatabaseColumn(List<T> entity) {
        if(entity == null) {
            return null;
        }
        return JsonUtil.toJson(entity);
    }
    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty() || dbData.equals("[]")) {
            return null;
        } else {
            try {
                Type type = getClass().getGenericSuperclass();
                Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                return new ObjectMapper().readerForListOf((Class<T>) actualTypeArgument).readValue(dbData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
