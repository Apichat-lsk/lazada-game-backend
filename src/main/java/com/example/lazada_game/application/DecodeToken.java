package com.example.lazada_game.application;

import com.example.lazada_game.web.dto.UserTokenInfo;
import com.example.lazada_game.web.jwtverify.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DecodeToken {

    private final JwtUtils jwtUtils;

    public UserTokenInfo decodeToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        Map<String, String> map = new HashMap<>();
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // ดึง claims (object)
            Claims claims = jwtUtils.extractAllClaims(token);
            String userId = claims.get("uid", String.class);
            String email = claims.get("email", String.class);
            UserTokenInfo userInfo = new UserTokenInfo(new ObjectId(userId), email);

            return userInfo;
        }
        return null;
    }
}
