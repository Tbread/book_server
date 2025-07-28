package com.tbread.book.external;

import com.tbread.book.user.entity.User;

public interface PersonalAuthenticationService {
    boolean isMatchCi(String username,String ci);
    PersonalAuthenticationInfo requestPersonalAuthenticationInfo(String encryptedInfo);
}
