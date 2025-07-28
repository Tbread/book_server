package com.tbread.book.external;

import com.tbread.book.user.entity.User;

public interface PersonalAuthenticationService {
    boolean isMatchCi(User user,String ci);
}
