// File: src/main/java/com/ree/sireleves/controller/UserController.java
package com.ree.sireleves.controller;
import com.ree.sireleves.dto.ChangePasswordRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import com.ree.sireleves.service.UserService;
import com.ree.sireleves.dto.UserResponseDTO;

@RestController
@RequestMapping("/api/users")
//@PreAuthorize("hasAnyRole('USER','SUPERADMIN')") // <- forcer rôle

public class UserController {
    private final UserService userService;
    public UserController(UserService userService){this.userService = userService;}

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication auth){
        String uuid = (String) auth.getPrincipal();
        return ResponseEntity.ok(userService.findByUuid(uuid));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        String uuid = (String) authentication.getPrincipal();
        userService.resetOwnPassword(uuid, request.newPassword());
        return ResponseEntity.noContent().build();    }


}
