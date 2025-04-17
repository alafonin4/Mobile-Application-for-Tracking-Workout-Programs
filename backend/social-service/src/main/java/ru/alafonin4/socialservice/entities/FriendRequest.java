package ru.alafonin4.socialservice.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "friend_requests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private Long receiverId;
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;
    private LocalDateTime createdAt;
}
