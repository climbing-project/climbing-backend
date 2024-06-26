package com.climbing.api.request;

import com.climbing.api.command.UpdateGymCommand;
import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Comment;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import com.climbing.domain.gym.SNS;
import java.time.LocalDate;
import java.util.List;

public record UpdateGymRequest(String name, Address address, Coordinates coordinates, String contact,
                               LocalDate latestSettingDate, SNS sns, String homepage, List<String> Images,
                               String defaultImage, List<OpenHour> openHours, List<Pricing> pricing,
                               List<String> tags, String description, List<String> grades, List<String> accommodations) {
    public UpdateGymCommand toCommand(Long gymId) {
        return new UpdateGymCommand(gymId, name, address, coordinates, contact, latestSettingDate, sns, homepage,
                Images, defaultImage, openHours, pricing, tags, description, accommodations, grades);
    }
}
