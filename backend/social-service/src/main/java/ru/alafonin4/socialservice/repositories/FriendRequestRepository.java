package ru.alafonin4.socialservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    // Получить запросы, полученные пользователем, которые находятся в состоянии ожидания
    List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequestStatus status);

    // Получить запросы, отправленные пользователем, которые находятся в состоянии ожидания
    List<FriendRequest> findBySenderIdAndStatus(Long senderId, FriendRequestStatus status);
    List<FriendRequest> findBySenderIdOrReceiverIdAndStatus(Long senderId, Long receiverId, FriendRequestStatus status);

}
