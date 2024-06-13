package com.climbing.api.command;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Comment;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import com.climbing.domain.gym.SNS;
import java.time.LocalDate;
import java.util.List;

public record UpdateGymCommand(Long id, String name, Address address, Coordinates coordinates, String contact,
                               LocalDate latestSettingDate, SNS sns, String homepage, List<String> images,
                               String defaultImage, List<OpenHour> openHours, List<Pricing> pricing, List<String> tags,
                               String description, List<String> accommodations, List<String> grades,
                               List<Comment> comments) {
}
