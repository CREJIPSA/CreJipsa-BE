package tave.crezipsa.crezipsa.application.user.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.application.user.dto.response.UserSignUpResponse;
import tave.crezipsa.crezipsa.domain.user.command.CreateUserCommand;
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

        if(userRepository.existsByEmail(request.email())){
           throw  new CommonException(ErrorCode.USER_ALREADY_EXISTS_EMAIL);
        }
        if(userRepository.existsByNickName(request.nickName())){
            throw new CommonException(ErrorCode.USER_ALREADY_EXISTS_NICKNAME);
        }

        CreateUserCommand command = new CreateUserCommand(
                request.nickName(),
                request.email(),
                request.gender(),
                false,
                request.birth(),
                request.activeYoutube(),
                request.activeInsta(),
                request.activeTiktok(),
                request.mainPlatform()
        );

        User user = User.createFromUser(command);
        User newUser = userRepository.save(user);
        return new UserSignUpResponse(newUser.getNickName(), newUser.getEmail());
    }
}
