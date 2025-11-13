package tave.crezipsa.crezipsa.infrastructure.user.mapper;

import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.infrastructure.user.entity.UserJpaEntity;

//domain <-->Jpa 객체 변환
public class UserMapper {

    public static User toUserDomain(UserJpaEntity e) {
        if (e == null) {return null;}

        return User.builder()
                .userId(e.getUserId())
                .name(e.getName())
                .nickName(e.getNickName())
                .email(e.getEmail())
                .password(e.getPassword())
                .gender(e.getGender())
                .profileImageUrl(e.getProfileImageUrl())
                .role(e.getRole())
                .dateOfBirth(e.getBirth())
                .activeYotube(e.getActiveInsta())
                .activeTikTok(e.getActiveTikTok())
                .activeInsta(e.getActiveInsta())
                .build();
    }
    public static UserJpaEntity toJpaUserEntity(User d) {
        if (d == null) {return null;}

        return UserJpaEntity.builder()
                .userId(d.getUserId())
                .name(d.getName())
                .nickName(d.getNickName())
                .email(d.getEmail())
                .password(d.getPassword())
                .gender(d.getGender())
                .profileImageUrl(d.getProfileImageUrl())
                .role(d.isRole())
                .birth(d.getDateOfBirth())
                .activeYoutube(d.getActiveYotube())
                .activeTikTok(d.getActiveTikTok())
                .activeInsta(d.getActiveInsta())
                .build();
    }

}
