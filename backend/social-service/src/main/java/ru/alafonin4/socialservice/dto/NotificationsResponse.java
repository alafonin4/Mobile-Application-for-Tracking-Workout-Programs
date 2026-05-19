package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class NotificationsResponse {
    private Long userId;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<NotificationItemDto> notifications = new ArrayList<>();

    /**
     * Returns the identifier of the user.
     * @return result of the operation
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Updates the identifier of the user.
     * @param userId identifier of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the notifications.
     * @return prepared list with the requested data
     */
    public List<NotificationItemDto> getNotifications() {
        return notifications;
    }

    /**
     * Updates the notifications.
     * @param notifications new notifications
     */
    public void setNotifications(List<NotificationItemDto> notifications) {
        this.notifications = notifications;
    }
}
