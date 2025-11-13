package tave.crezipsa.crezipsa.infrastructure.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;

import java.time.LocalDate;

//DB와 연결되는 실제 객체 - 도메인 분리를 위해
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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String nickName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(nullable = false)
    private Boolean role = false;

    private LocalDate birth;

    @Column(length = 255)
    private String activeYoutube;

    @Column(length = 255)
    private String activeTikTok;

    @Column(length = 255)
    private String activeInsta;
}
