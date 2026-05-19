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
    /**
     * Returns incoming requests for the supplied user filtered by status.
     *
     * @param receiverId identifier of the receiving user
     * @param status required friend-request status
     * @return matching friend requests
     */
    List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequestStatus status);

    /**
     * Returns outgoing requests for the supplied user filtered by status.
     *
     * @param senderId identifier of the sending user
     * @param status required friend-request status
     * @return matching friend requests
     */
    List<FriendRequest> findBySenderIdAndStatus(Long senderId, FriendRequestStatus status);

    /**
     * Returns all relations for a user where the supplied status is active.
     *
     * @param senderId identifier matched against the sender side
     * @param receiverId identifier matched against the receiver side
     * @param status required friend-request status
     * @return matching friend requests
     */
    List<FriendRequest> findBySenderIdOrReceiverIdAndStatus(Long senderId, Long receiverId, FriendRequestStatus status);

    /**
     * Returns the relationship history between two users for the supplied set of statuses.
     *
     * @param userId identifier of the first user
     * @param otherUserId identifier of the second user
     * @param statuses statuses that should be included in the result
     * @return ordered list of matching relationship records
     */
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
