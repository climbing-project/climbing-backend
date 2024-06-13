package com.climbing.api.command;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import java.util.List;

public record UpdateGymCommand(Long id, String name, Address address, Coordinates coordinates, String description,
                               List<String> tags, List<Pricing> pricing, List<OpenHour> openHours, String accommodations,
                               String contact, List<String> grades) {
}
