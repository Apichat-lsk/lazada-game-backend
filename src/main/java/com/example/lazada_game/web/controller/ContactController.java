package com.example.lazada_game.web.controller;

import com.example.lazada_game.application.*;
import com.example.lazada_game.domain.model.Contact;
import com.example.lazada_game.web.dto.ContactRequest;
import com.example.lazada_game.web.dto.ContactResponse;
import com.example.lazada_game.web.dto.UserTokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;
    private final DecodeToken decodeToken;
    private final ActivityLogsService activityLogsService;
    private final EmailService emailService;
    private final ContactService contactService;

    @PostMapping()
    public ResponseEntity<ContactResponse> createContact(HttpServletRequest request, @RequestBody @Valid Contact contactRequest) {
        ContactResponse result = new ContactResponse();
//        UserTokenInfo userInfo = decodeToken.decodeToken(request);
        try {
            service.createContact(contactRequest);
            contactService.sendEmail(contactRequest.getEmail(), contactRequest.getTitle(), contactRequest.getDescription());
            activityLogsService.createActivityLogs(null, contactRequest.getEmail(), "Title: " + " " + contactRequest.getTitle() + " : " + "Description :" + contactRequest.getDescription() + "Create contact success",
                    "Contact");
            result.setStatus(true);
            result.setMessage("ส่งคำร้องสำเร็จ");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            activityLogsService.createActivityLogs(null, contactRequest.getEmail(), "Title: " + " " + contactRequest.getTitle() + " : " + "Description :" + contactRequest.getDescription() + "Create contact failed",
                    "Contact");;
            throw new RuntimeException(e.getMessage());
        }
    }

}
