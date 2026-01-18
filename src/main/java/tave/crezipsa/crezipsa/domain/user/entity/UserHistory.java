package tave.crezipsa.crezipsa.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name = "user_id")
    private Long userId;

    private String historyContent;

    public UserHistory(Long userId, String historyContent) {
        this.userId = userId;
        this.historyContent = historyContent;
    }

}
