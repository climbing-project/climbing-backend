package com.climbing.api.request;

import com.climbing.api.command.UpdateGymCommand;
import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import java.util.List;

public record UpdateGymRequest(String name, Address address, Coordinates coordinates, String description,
                               List<String> tags, List<Pricing> pricing, List<OpenHour> openHours,
                               String accommodations, String contact, List<String> grades) {
    public UpdateGymCommand toCommand(Long gymId) {
        return new UpdateGymCommand(gymId, name, address, coordinates, description, tags, pricing, openHours,
                accommodations, contact, grades);
    }
}
