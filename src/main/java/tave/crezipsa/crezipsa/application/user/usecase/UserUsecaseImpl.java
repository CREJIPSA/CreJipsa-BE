package tave.crezipsa.crezipsa.application.user.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUsecaseImpl implements UserUsecase {

    private final UserRepository userRepository;

    @Override
    public UserSignUpResponse signUp(UserSignUpRequest request) {

        if(userRepository.findByEmail(request.email()).isPresent()){
           throw  new CommonException(ErrorCode.USER_ALREADY_EXISTS_EMAIL);
        }
        if(userRepository.findByNickName(request.nickName()).isPresent()){
            throw new CommonException(ErrorCode.USER_ALREADY_EXISTS_NICKNAME);
        }

        User user = User.builder()
                .nickName(request.nickName())
                .email(request.email())
                .password(request.password())
                .gender(request.gender())
                .role(true)
                .birth(request.birth())
                .activeYotube(request.activeYoutube())
                .activeInsta(request.activeInsta())
                .activeTiktok(request.activeTiktok())
                .mainPlatform(request.mainPlatfrom())
                .build();

        User newUser = userRepository.save(user);

        return new UserSignUpResponse(newUser.getNickName(), newUser.getEmail());
    }
}
