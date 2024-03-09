package com.climbing.auth.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailInfo {
    private String receiver;
    private String title;
    private String content;
}
