// File: src/main/java/com/ree/sireleves/controller/UserController.java
package com.ree.sireleves.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import com.ree.sireleves.service.UserService;
import com.ree.sireleves.dto.UserResponseDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){this.userService = userService;}

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication auth){
        String uuid = (String) auth.getPrincipal();
        return ResponseEntity.ok(userService.findByUuid(uuid));
    }
}
