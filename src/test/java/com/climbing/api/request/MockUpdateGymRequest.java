package com.climbing.api.request;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import java.lang.reflect.Field;
import java.util.Collections;

public class MockUpdateGymRequest {
    public static UpdateGymRequest of() {
        try {
            UpdateGymRequest request = new UpdateGymRequest(
                    "name",
                    new Address("kibun", "road", "unit"),
                    new Coordinates(1f, 1f),
                    "description",
                    Collections.singletonList("tags"),
                    null,
                    null,
                    "accommodations",
                    "contact",
                    null
            );
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
