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
                .gender(e.getGender())
                .profileImageUrl(e.getProfileImageUrl())
                .role(e.getRole())
                .birth(e.getBirth())
                .activeYoutube(e.getActiveYoutube())
                .activeTiktok(e.getActiveTiktok())
                .activeInsta(e.getActiveInsta())
                .mainPlatform(e.getMainPlatform())
                .build();
    }
    public static UserJpaEntity toJpaUserEntity(User d) {
        if (d == null) {return null;}

        return UserJpaEntity.builder()
                .userId(d.getUserId())
                .nickName(d.getNickName())
                .email(d.getEmail())
                .gender(d.getGender())
                .profileImageUrl(d.getProfileImageUrl())
                .role(d.isRole())
                .birth(d.getBirth())
                .activeYoutube(d.getActiveYoutube())
                .activeTiktok(d.getActiveTiktok())
                .activeInsta(d.getActiveInsta())
                .mainPlatform(d.getMainPlatform())
                .build();
    }

}
