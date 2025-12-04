package tave.crezipsa.crezipsa.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @Column(name = "user_id")
    private Long userId;

    private String category;

    private UserInterest(Long userId, String category) {
        this.userId = userId;
        this.category = category;
    }

    public static UserInterest create(Long userId, String category) {
        return new UserInterest(userId, category);
    }

}
