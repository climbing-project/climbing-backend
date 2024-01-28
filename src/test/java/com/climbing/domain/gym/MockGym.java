package com.climbing.domain.gym;

import java.lang.reflect.Field;

public class MockGym {
    public static Gym of(Long id) {
        try {
            Gym gym = Gym.of("name",
                             "address",
                             "description",
                             "tags",
                             "pricings",
                             "openHours",
                             "accommodations",
                             "contacts",
                             "grades");
            Field idField = gym.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(gym, id);
            return gym;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
