package ru.alafonin4.socialservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequestStatus status);

    List<FriendRequest> findBySenderIdAndStatus(Long senderId, FriendRequestStatus status);

    List<FriendRequest> findBySenderIdOrReceiverIdAndStatus(Long senderId, Long receiverId, FriendRequestStatus status);

    @Query("""
            select fr
            from FriendRequest fr
            where ((fr.senderId = :userId and fr.receiverId = :otherUserId)
                or (fr.senderId = :otherUserId and fr.receiverId = :userId))
              and fr.status in :statuses
            order by fr.createdAt desc, fr.id desc
            """)
    List<FriendRequest> findRelationsBetweenUsers(
            @Param("userId") Long userId,
            @Param("otherUserId") Long otherUserId,
            @Param("statuses") List<FriendRequestStatus> statuses
    );
}
