package com.github.board.repository.post;


import com.github.board.repository.auth.user.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@Table(name="posts")

public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성
    private Integer idx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩
    @JoinColumn(name = "user_idx", nullable = false)
    private Users user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at = LocalDateTime.now();
}
