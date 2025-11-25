package tave.crezipsa.crezipsa.infrastructure.user.mapper;

import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.infrastructure.user.entity.UserJpaEntity;

//domain <-->Jpa 객체 변환
public class UserMapper {

    public static User toUserDomain(UserJpaEntity e) {
        if (e == null) {return null;}

        return User.builder()
                .userId(e.getUserId())
                .nickName(e.getNickName())
                .email(e.getEmail())
                .password(e.getPassword())
                .gender(e.getGender())
                .profileImageUrl(e.getProfileImageUrl())
                .role(e.getRole())
                .birth(e.getBirth())
                .activeYotube(e.getActiveYoutube())
                .activeTiktok(e.getActiveTiktok())
                .activeInsta(e.getActiveInsta())
                .build();
    }
    public static UserJpaEntity toJpaUserEntity(User d) {
        if (d == null) {return null;}

        return UserJpaEntity.builder()
                .userId(d.getUserId())
                .nickName(d.getNickName())
                .email(d.getEmail())
                .password(d.getPassword())
                .gender(d.getGender())
                .profileImageUrl(d.getProfileImageUrl())
                .role(d.isRole())
                .birth(d.getBirth())
                .activeYoutube(d.getActiveYotube())
                .activeTiktok(d.getActiveTiktok())
                .activeInsta(d.getActiveInsta())
                .build();
    }

}
