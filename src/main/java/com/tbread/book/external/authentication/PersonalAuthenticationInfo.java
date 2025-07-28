package com.tbread.book.external.authentication;

public abstract class PersonalAuthenticationInfo {
    public String ci;
    public String di;

    public abstract PersonalAuthenticationInfo decrypt(String encryptedInfo);
    //kmc 등을 생각해봤을땐 문자열말고 키벨류쌍값으로 받는게나을지도?

    public String getCi(){
        return this.ci;
    };
    public String getDi(){
        return this.di;
    }
}
