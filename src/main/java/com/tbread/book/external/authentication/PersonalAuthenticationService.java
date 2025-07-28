package com.tbread.book.external.authentication;

public interface PersonalAuthenticationService {
    boolean isMatchCi(String username,String ci);
    PersonalAuthenticationInfo requestPersonalAuthenticationInfo(String encryptedInfo);
}
