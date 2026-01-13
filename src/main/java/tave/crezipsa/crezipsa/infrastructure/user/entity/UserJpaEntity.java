package tave.crezipsa.crezipsa.infrastructure.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, length = 50)
    private String nickName;

    @Column(unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 255)
    private String profileImageUrl;

    @Column
    private Boolean role = true;

    private LocalDate birth;

    @Column(length = 255)
    private String activeYoutube;

    @Column(length = 255)
    private String activeTiktok;

    @Column(length = 255)
    private String activeInsta;

    @Enumerated(EnumType.STRING)
    private Platform mainPlatform;

}
