package com.climbing.domain.member.dto;

import java.util.Optional;

public record MemberUpdateDto(Optional<String> nickname) {
}
