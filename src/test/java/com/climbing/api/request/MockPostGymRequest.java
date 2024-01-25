package com.climbing.api.request;

import java.lang.reflect.Field;

public class MockPostGymRequest {
    public static PostGymRequest of() {
        try {
            PostGymRequest request = new PostGymRequest();
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
