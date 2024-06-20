package com.climbing.api.request;

import static java.util.Collections.singletonList;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Comment;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import com.climbing.domain.gym.SNS;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MockUpdateGymRequest {
    public static UpdateGymRequest of() {
        try {
            UpdateGymRequest request = new UpdateGymRequest("name",
                    new Address("jibun", "road", "unit"),
                    new Coordinates(1f, 1f),
                    "contact",
                    LocalDate.now(),
                    new SNS("twitter", "facebook", "instagram"),
                    "homepage",
                    singletonList("images"),
                    "defaultImage",
                    singletonList(new OpenHour("day", "10:00", "22:00")),
                    singletonList(new Pricing("item", 10000)),
                    null,
                    "description",
                    singletonList("grades"),
                    singletonList("accommodations")
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
