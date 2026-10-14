package com.octaviookumu.tinylink.controllers;

import com.octaviookumu.tinylink.domain.dtos.ShortenUrlRequestDto;
import com.octaviookumu.tinylink.services.RateLimitService;
import com.octaviookumu.tinylink.services.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;
    private final RateLimitService rateLimitService;

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(
            @Valid @RequestBody ShortenUrlRequestDto shortenUrlRequestDto,
            HttpServletRequest httpRequest
            ){
        // get the IP
        String clientIp = getClientIp(httpRequest);

        // check the rate limit before creating
        if(!rateLimitService.isAllowed(clientIp)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of());
        }

    }

    private String getClientIp(HttpServletRequest httpRequest) {
        // the id can come from many places e.g. proxies, load balancers
        String xForwardedFor = httpRequest.getHeader("X-Forwarded-For");
        if(xForwardedFor != null && !xForwardedFor.isEmpty()){
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = httpRequest.getHeader("X-Real-IP");
        if(xRealIp != null && !xRealIp.isEmpty()){
            return xRealIp;
        }

        return httpRequest.getRemoteAddr();

    }

}
