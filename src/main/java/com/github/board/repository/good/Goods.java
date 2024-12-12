package com.github.board.repository.good;

import com.github.board.repository.auth.user.Users;
import com.github.board.repository.post.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "good")
public class Goods {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_idx",referencedColumnName = "idx")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "posts_idx",referencedColumnName = "idx")
    private Posts post;


    public Goods(Posts posts, Users user) {
        setPost(posts);
        setUser(user);
    }
}

