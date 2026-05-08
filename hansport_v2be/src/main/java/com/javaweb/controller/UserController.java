package com.javaweb.controller;

import com.javaweb.domain.User;
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.user.ResCreateUserDTO;
import com.javaweb.domain.response.user.ResUpdateUserDTO;
import com.javaweb.domain.response.user.ResUserDTO;
import com.javaweb.service.UserService;
import com.javaweb.util.annotation.ApiMessage;
import com.javaweb.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("create a user")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody @Valid User user) throws IdInvalidException {
        if(this.userService.isEmailExists(user.getEmail()))
        {
            throw new IdInvalidException("Email đã tồn tại");
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        if(!this.userService.isUserExists(user.getId()))
        {
            throw new IdInvalidException("User không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUser(user));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws IdInvalidException {
        if(!this.userService.isUserExists(id))
        {
            throw new IdInvalidException("User không tồn tại");
        }
        this.userService.deleteUserById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users")
    @ApiMessage("get all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> spec,
                                                           Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(spec, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable long id) throws IdInvalidException {
        if(!this.userService.isUserExists(id))
        {
            throw new IdInvalidException("User không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getuserById(id));
    }

}
