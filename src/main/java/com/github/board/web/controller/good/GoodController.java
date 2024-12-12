package com.github.board.web.controller.good;


import com.github.board.repository.auth.userDetails.CustomUserDetails;
import com.github.board.service.good.GoodService;
import com.github.board.web.dto.good.GoodResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GoodController {

    private final GoodService goodService;

    // 좋아요 기능
    @PostMapping("/goods/{Id}")
    public ResponseEntity<GoodResult> likePost(@PathVariable Integer Id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GoodResult("로그인된 사용자만 좋아요를 누를 수 있습니다.", 401));
        }

        Integer userId = userDetails.getUserId();
        GoodResult result = goodService.likePost(Id,userId);
        return ResponseEntity.ok(result);

    }

    // 좋아요 취소 기능
    @DeleteMapping("/goods/{Id}")
    public ResponseEntity<GoodResult> deleteLikePost(@PathVariable Integer Id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        GoodResult result = goodService.deleteLikePost(Id, userId);
        return ResponseEntity.ok(result);
    }

}
