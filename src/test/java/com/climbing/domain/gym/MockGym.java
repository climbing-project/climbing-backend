package com.climbing.domain.gym;

import static java.util.Collections.singletonList;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MockGym {
    public static Gym of(Long id) {
        try {
            Gym gym = new Gym();
            gym.update("name",
                    "jibun",
                    "road",
                    "unit",
                    1f,
                    1f,
                    "contact",
                    LocalDate.now(),
                    "twitter",
                    "facebook",
                    "instagram",
                    "homepage",
                    singletonList("images"),
                    "defaultImage",
                    singletonList(new OpenHour("day", "10:00", "22:00")),
                    singletonList(new Pricing("item", 10000)),
                    null,
                    "description",
                    singletonList("grades"),
                    singletonList("accommodations"),
                    singletonList(new Comment("user", LocalDateTime.now(), "text"))
            );

            Field idField = gym.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(gym, id);
            return gym;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
