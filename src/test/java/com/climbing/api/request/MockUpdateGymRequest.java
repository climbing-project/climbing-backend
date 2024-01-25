package com.climbing.api.request;

import java.lang.reflect.Field;

public class MockUpdateGymRequest {
    public static UpdateGymRequest of() {
        try {
            UpdateGymRequest request = new UpdateGymRequest();
            Field[] fields = request.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(request, field.getName());
            }
            return request;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
