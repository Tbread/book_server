package com.tbread.book.external;

public abstract class PersonamAuthenticationInfo {
    public String ci;
    public String di;

    public abstract PersonamAuthenticationInfo decrypt(String encryptedInfo);
    //kmc 등을 생각해봤을땐 문자열말고 키벨류쌍값으로 받는게나을지도?
}
