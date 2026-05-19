package ru.alafonin4.socialservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alafonin4.socialservice.dto.NotificationsResponse;
import ru.alafonin4.socialservice.dto.SocialPersonalizationResponse;
import ru.alafonin4.socialservice.service.NotificationService;
import ru.alafonin4.socialservice.service.SocialPersonalizationService;

@RestController
@RequestMapping("/api")
public class SocialInsightsController {

    @Autowired
    private SocialPersonalizationService socialPersonalizationService;

    @Autowired
    private NotificationService notificationService;

    /**
     * Returns the social personalization.
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/social-personalization/user/{userId}")
    public ResponseEntity<SocialPersonalizationResponse> getSocialPersonalization(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(socialPersonalizationService.buildProfile(userId));
    }

    /**
     * Returns the notifications.
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/notifications/user/{userId}")
    public ResponseEntity<NotificationsResponse> getNotifications(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(notificationService.buildFeed(userId));
    }
}
