package com.javaweb.controller;

import com.javaweb.domain.User;
<<<<<<< HEAD
import com.javaweb.domain.request.ReqUserCreateDTO;
import com.javaweb.domain.request.ReqUserUpdateDTO;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
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
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;

=======
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
<<<<<<< HEAD
    public UserController(UserService userService) {
        this.userService = userService;
=======
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    }

    @PostMapping("/users")
    @ApiMessage("create a user")
<<<<<<< HEAD
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody @Valid ReqUserCreateDTO user) throws IdInvalidException {
=======
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody @Valid User user) throws IdInvalidException {
        if(this.userService.isEmailExists(user.getEmail()))
        {
            throw new IdInvalidException("Email đã tồn tại");
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/users")
    @ApiMessage("update a user")
<<<<<<< HEAD
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody @Valid ReqUserUpdateDTO user) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user));
=======
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        if(!this.userService.isUserExists(user.getId()))
        {
            throw new IdInvalidException("User không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUser(user));
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
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
