package com.github.board.service.good;

import com.github.board.repository.auth.user.UserRepository;
import com.github.board.repository.auth.user.Users;
import com.github.board.repository.good.GoodRepository;
import com.github.board.repository.good.Goods;
import com.github.board.repository.post.Posts;
import com.github.board.web.dto.good.GoodResult;
import com.github.board.web.dto.good.GoodStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GoodService {

    private final GoodRepository goodRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 공통 로직
    protected Optional<Goods> checkExistingGood(Integer postId, Integer userId) {
        Posts posts = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Users user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return goodRepository.findByUserAndPost(user,posts);
    }

    // 좋아요 추가 기능
    public GoodResult likePost(Integer postId, Integer userId) {

        Optional<Goods> existingGood = checkExistingGood(postId, userId);

        if (existingGood.isPresent()) {
            return new GoodResult(GoodStatus.ALREADY_LIKED.getMessage(), GoodStatus.ALREADY_LIKED.getCode());
        }else {
            Posts posts = postRepository.findById(postId).orElseThrow();
            Users user = userRepository.findById(userId).orElseThrow();

            goodRepository.save(new Goods(posts,user));
            return new GoodResult(GoodStatus.LIKED.getMessage(), GoodStatus.LIKED.getCode());
        }

    }

    // 좋아요 취소 기능
    public GoodResult deleteLikePost(Integer postId, Integer userId) {

        Optional<Goods> existingGood = checkExistingGood(postId, userId);

        if (!existingGood.isPresent()) {
            return new GoodResult(GoodStatus.ALREADY_UNLIKED.getMessage(), GoodStatus.ALREADY_UNLIKED.getCode());
        }else {
            goodRepository.delete(existingGood.get());
            return new GoodResult(GoodStatus.UNLIKED.getMessage(), GoodStatus.UNLIKED.getCode());
        }


    }

}
