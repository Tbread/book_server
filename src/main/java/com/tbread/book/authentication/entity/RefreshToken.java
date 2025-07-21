package com.tbread.book.authentication.entity;

import com.tbread.book.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
    //추후 mysql 에서 해당 컬럼 기반으로 제거 스케쥴러 설정 혹은 자체 스케쥴러 클래스 작성?

    public RefreshToken(User user,String token,LocalDateTime expiredAt){
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
    }
    //확장가능성 낮으므로 빌더패턴 미사용
}