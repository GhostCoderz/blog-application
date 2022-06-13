package com.ghostcoderz.blog_application.controller;

import com.ghostcoderz.blog_application.payload.ApiResponse;
import com.ghostcoderz.blog_application.payload.UserDto;
import com.ghostcoderz.blog_application.service.serviceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST - A User
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto userDto
    ){
        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    // PUT - Update A User
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody UserDto userDto,
            @PathVariable Long userId
    ){
        UserDto updatedUser = this.userService.updateUser(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE - Delete A User
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long userId
    ) {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(
                new ApiResponse("User deleted successfully", true),
                HttpStatus.OK
        );
    }

    // GET - Get All Users
    @GetMapping
    public List<UserDto> getAllUsers(){
        return this.userService.getAllUsers();
    }

    // GET - Get A User
    @GetMapping("/{userId}")
    public UserDto getSingleUser( @PathVariable Long userId){
        return this.userService.getUserById(userId);
    }

}
