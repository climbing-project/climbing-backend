package com.climbing.api.response;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import com.climbing.domain.gym.SNS;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder

public record GetGymResponse(String name, Address address, Coordinates coordinates, String contact,
                             LocalDate latestSettingDay, SNS sns, String homepage, List<String> images,
                             String defaultImage, List<OpenHour> openHours, List<Pricing> pricing, List<String> tags,
                             String description, List<String> grades, List<String> accommodations,
                             List<GetCommentResponse> comments, int likeNumber) {
    public static GetGymResponse from(Gym gym) {
        return GetGymResponse.builder()
                .name(gym.getName())
                .address(new Address(gym.getJibunAddress(), gym.getRoadAddress(), gym.getUnitAddress()))
                .coordinates(new Coordinates(gym.getLatitude(), gym.getLongitude()))
                .contact(gym.getContact())
                .latestSettingDay(gym.getLatestSettingDate())
                .sns(new SNS(gym.getTwitter(), gym.getFacebook(), gym.getInstagram()))
                .homepage(gym.getHomepage())
                .images(gym.getImages())
                .defaultImage(gym.getDefaultImage())
                .openHours(gym.getOpenHours())
                .pricing(gym.getPricing())
                .tags(gym.getGymTags().stream().map(o -> o.getTag().getValue()).toList())
                .description(gym.getDescription())
                .grades(gym.getGrades())
                .accommodations(gym.getAccommodations())
                .comments(GetCommentResponse.from(gym.getComments()))
                .likeNumber(gym.getLikes())
                .build();
    }
}
