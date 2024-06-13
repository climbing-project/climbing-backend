package com.climbing.domain.gym;

import static java.util.Collections.*;

import java.lang.reflect.Field;

public class MockGym {
    public static Gym of(Long id) {
        try {
            Gym gym = Gym.create("name",
                             "jibun",
                             "road",
                             "unit",
                             1f,
                             1f,
                             "description",
                             singletonList(new Tag("tags")),
                             singletonList(new Pricing("item", 10000)),
                             singletonList(new OpenHour("day", "10:00", "22:00")),
                             "accommodations",
                             "contact",
                             singletonList("grades"));
            Field idField = gym.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(gym, id);
            return gym;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
