package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class NotificationsResponse {
    private Long userId;
    private List<NotificationItemDto> notifications = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<NotificationItemDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationItemDto> notifications) {
        this.notifications = notifications;
    }
}
