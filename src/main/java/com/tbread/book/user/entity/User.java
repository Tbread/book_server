package com.tbread.book.user.entity;

import com.tbread.book.common.TimeStamp;
import com.tbread.book.user.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "`User`")
public class User extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //프로젝트 규모 및 성능상 uuid 스킵

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String ci;
    //이외 기타 개인정보값 스킵 (추후 따로 테이블 만들어서 관리)

    @Column(nullable = false)
    private UserRole userRole;

    public User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.ci = builder.ci;
        this.userRole = builder.userRole;
    }

    public static class UserBuilder {
        private String username;
        private String password;
        private String ci;
        private UserRole userRole;


        public UserBuilder(String username, String password, String ci) {
            this.username = username;
            this.password = password;
            this.ci = ci;
            this.userRole = UserRole.USER;
        }

        public UserBuilder setUserRole(UserRole userRole) {
            this.userRole = userRole;
            return this;
        }
    }
}
