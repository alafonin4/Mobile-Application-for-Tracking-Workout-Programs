package ru.alafonin4.socialservice.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alafonin4.socialservice.dto.NotificationsResponse;
import ru.alafonin4.socialservice.dto.SocialPersonalizationResponse;
import ru.alafonin4.socialservice.service.NotificationService;
import ru.alafonin4.socialservice.service.SocialPersonalizationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocialInsightsControllerTest {

    @Mock
    private SocialPersonalizationService socialPersonalizationService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SocialInsightsController socialInsightsController;

    @Test
    void getSocialPersonalizationReturnsProfile() {
        SocialPersonalizationResponse profile = new SocialPersonalizationResponse();
        profile.setUserId(3L);
        when(socialPersonalizationService.buildProfile(3L)).thenReturn(profile);

        assertEquals(3L, socialInsightsController.getSocialPersonalization(3L).getBody().getUserId());
    }

    @Test
    void getNotificationsReturnsFeed() {
        NotificationsResponse response = new NotificationsResponse();
        response.setUserId(4L);
        when(notificationService.buildFeed(4L)).thenReturn(response);

        assertEquals(4L, socialInsightsController.getNotifications(4L).getBody().getUserId());
    }
}
