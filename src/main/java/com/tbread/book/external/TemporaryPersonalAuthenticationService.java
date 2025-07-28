package com.tbread.book.external;

import com.tbread.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemporaryPersonalAuthenticationService implements PersonalAuthenticationService{

    private final UserRepository userRepository;

    @Override
    public boolean isMatchCi(String username, String ci) {
//        Optional<User> optionalUser = userRepository.findByUsername(username);
//        if (optionalUser.isPresent()) {
//            if (optionalUser.get().getCi().equals(ci)) {
//                return true;
//            }
//        }
//        return false;
//        api 미연동으로 실제 ci를 받아올수없으니 항상 true 리턴(임시)
        return true;
    }

    @Override
    public PersonalAuthenticationInfo requestPersonalAuthenticationInfo(String encryptedInfo) {
        return null;
    }
    //api 연동시 본인인증 서비스사별로 로직 작성
}
