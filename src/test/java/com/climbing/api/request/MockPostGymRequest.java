package com.climbing.api.request;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;

import java.lang.reflect.Field;

public class MockPostGymRequest {
    public static PostGymRequest of() {
        try {
            PostGymRequest request = new PostGymRequest();
            Field[] fields = request.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("address")) {
                    field.set(request, new Address("jibun", "road", "unit"));
                } else if (field.getName().equals("coordinates")) {
                    field.set(request, new Coordinates(0f, 0f));
                } else {
                    field.set(request, field.getName());
                }
            }
            return request;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
