package com.example.lazada_game.application;

import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.domain.repository.AuthRepository;
import com.example.lazada_game.web.dto.*;
import com.example.lazada_game.web.jwtverify.JwtUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogsService activityLogsService;
    private final JwtUtils jwtUtils;


    public RegisterResponse register(Users users) {
        RegisterResponse result = new RegisterResponse();
        try {
            if (authRepository.existsByEmail(users.getEmail())) {
//                throw new RuntimeException("Email already exists");
                result.setMessage("อีเมลนี้ถูกใช้งานแล้ว");
                result.setCheck(false);
                return result;
            } else if (authRepository.existsByUsername(users.getUsername())) {
//                throw new RuntimeException("Email already exists");
                result.setMessage("ชื่อผู้ใช้งานนี้ถูกใช้งานแล้ว");
                result.setCheck(false);
                return result;
            } else {
                result.setMessage("Send OTP Success");
                result.setCheck(true);
                return result;
            }
        } catch (Exception e) {
            System.err.println("Failed to process Redis message: " + e.getMessage());
            e.printStackTrace();
            result.setMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            result.setCheck(false);
            return result;
        }
    }

    public RegisterResponse checkDuplicate(Users users) {
        RegisterResponse result = new RegisterResponse();
        try {
            if (authRepository.existsByEmail(users.getEmail())) {
//                throw new RuntimeException("Email already exists");
                result.setMessage("อีเมลนี้ถูกใช้งานแล้ว");
                result.setCheck(false);
                return result;
            } else if (authRepository.existsByUsername(users.getUsername())) {
//                throw new RuntimeException("Email already exists");
                result.setMessage("ชื่อผู้ใช้งานนี้ถูกใช้งานแล้ว");
                result.setCheck(false);
                return result;
            } else {
                result.setMessage("Can use Email & Username");
                result.setCheck(true);
                return result;
            }
        } catch (Exception e) {
            System.err.println("Failed to process Redis message: " + e.getMessage());
            e.printStackTrace();
            result.setMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            result.setCheck(false);
            return result;
        }

    }

    public void handleMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("Received raw message: " + message);
            if (message.startsWith("\"") && message.endsWith("\"")) {
                message = mapper.readValue(message, String.class);
                System.out.println("Unescaped message: " + message);
            }
            Map<String, Object> payload = mapper.readValue(message, new TypeReference<>() {
            });
            boolean success = Boolean.TRUE.equals(payload.get("success"));
            String email = (String) payload.get("email");
            if (!success) {
                System.out.println("❌ OTP validation failed for: " + email);
                return;
            }
            Object rawUser = payload.get("users");
            if (rawUser == null) {
                System.out.println("❌ No user data found in OTP success payload for: " + email);
                return;
            }
            Users users = mapper.convertValue(rawUser, Users.class);
            if (authRepository.existsByEmail(users.getEmail())) {
                System.out.println("⚠️ Email already exists: " + users.getEmail());
                return;
            }
            Users newUser = new Users();
            newUser.setUsername(users.getUsername());
            newUser.setFullName(users.getFullName());
            newUser.setPassword(passwordEncoder.encode(users.getPassword()));
            newUser.setTel(users.getTel());
            newUser.setAddress(users.getAddress());
            newUser.setEmail(users.getEmail());
            newUser.setCreateAt(LocalDateTime.now());
            Users res = authRepository.register(newUser);
            ObjectId userId = new ObjectId(res.getId());
            activityLogsService.createActivityLogs(userId, users.getEmail(), "User " + res.getEmail() + " Register Success", "Register");
            System.out.println("✅ Registered user: " + newUser.getEmail());

        } catch (Exception e) {
            System.err.println("❌ Failed to process Redis message: " + message);
            e.printStackTrace();
        }
    }

    public JwtResponse login(LoginRequest loginRequest) {
        JwtResponse result = new JwtResponse();
        Users user = new Users();
        Optional<Users> optionalUser = authRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isEmpty()) {
            result.setMessage("ชื่อผู้ใช้งาน หรือ รหัสผ่าน");
            result.setStatus(false);
            Optional<Users> optionalUserEmail = authRepository.findByEmail(loginRequest.getUsername());

            if (optionalUserEmail.isEmpty()) {
                result.setMessage("อีเมล หรือ รหัสผ่านไม่ถูกต้อง");
                result.setStatus(false);
                return result;
            } else {
                user = optionalUserEmail.get();
            }
        } else {
            user = optionalUser.get();
        }
        System.out.println("Input Password :" + loginRequest.getPassword());
        System.out.println("Db Password :" + user.getPassword());
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            result.setMessage("รหัสผ่านไม่ถูกต้อง");
//            throw new RuntimeException("Invalid username or password");
            result.setStatus(false);
            return result;
        }
        ObjectId userId = new ObjectId(user.getId());
        activityLogsService.createActivityLogs(userId, loginRequest.getEmail(), "User " + user.getEmail() + " Login", "Login");
        result.setToken(jwtUtils.generateToken(user));
        result.setMessage("Login successfully");
        result.setStatus(true);
        return result;
    }

    public Optional<Users> checkUsersByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    public Users changePassword(ChangePasswordRequest request) {
        Users user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("ไม่มีอีเมลนี้ในระบบ: " + request.getEmail()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        activityLogsService.createActivityLogs(null, request.getEmail(), "User " + user.getEmail() + " Change Password", "Change Password");
        return authRepository.updateByEmail(user);
    }

}
