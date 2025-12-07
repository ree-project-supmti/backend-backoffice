// File: src/main/java/com/ree/sireleves/controller/AdminUserController.java
package com.ree.sireleves.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.ree.sireleves.dto.UserCreateDTO;
import com.ree.sireleves.service.UserService;
import com.ree.sireleves.dto.UserResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserService userService;
    public AdminUserController(UserService userService){this.userService = userService;}

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO dto){
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listUsers(){
        return ResponseEntity.ok(userService.listAll());
    }

    @PostMapping("/{uuid}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable String uuid){
        userService.resetPasswordForUser(uuid);
        return ResponseEntity.noContent().build();
    }
}
