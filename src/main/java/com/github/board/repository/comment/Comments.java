package com.github.board.repository.comment;

import com.github.board.repository.auth.user.Users;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of="idx")
@Entity
@Builder
@DynamicInsert
@Table(name="comments")
public class Comments {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idx")
    private Integer idx;


    @Column(name="posts_idx")
    private Integer postsIdx;

    @Column(name="body")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_idx", referencedColumnName = "idx")
    private Users user;


    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
