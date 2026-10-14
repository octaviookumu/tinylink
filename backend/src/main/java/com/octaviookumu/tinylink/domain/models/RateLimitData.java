package com.octaviookumu.tinylink.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitData {
    // how many requests have been sent for that particular minute/hour
    private int minuteCount;
    private int hourCount;

    // from what time the minute/hour started
    private LocalDateTime minuteWindowStart;
    private LocalDateTime hourWindowStart;
}
