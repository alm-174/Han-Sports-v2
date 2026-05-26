package com.javaweb.service;

<<<<<<< HEAD
import com.javaweb.domain.Role;
import com.javaweb.domain.User;
import com.javaweb.domain.request.ReqRegisterDTO;
import com.javaweb.domain.request.ReqUserCreateDTO;
import com.javaweb.domain.request.ReqUserUpdateDTO;
=======
import com.javaweb.domain.User;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.role.ResRoleDTO;
import com.javaweb.domain.response.user.ResCreateUserDTO;
import com.javaweb.domain.response.user.ResUpdateUserDTO;
import com.javaweb.domain.response.user.ResUserDTO;
<<<<<<< HEAD
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
=======
import com.javaweb.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
<<<<<<< HEAD
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
=======
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    }

    public boolean isEmailExists(String email)
    {
        return this.userRepository.existsByEmail(email);
    }

    public boolean isUserExists(long id)
    {
        return this.userRepository.existsById(id);
    }

<<<<<<< HEAD
    @Transactional
    public ResCreateUserDTO createUser(ReqUserCreateDTO req) throws IdInvalidException {
        if (this.userRepository.existsByEmail(req.getEmail())) {
            throw new IdInvalidException("Email đã tồn tại");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(this.passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());
        user.setAddress(req.getAddress());
        user.setPhone(req.getPhone());
        user.setRole(this.getRoleOrThrow(normalizeRoleName(req.getRoleName(), "USER")));

        User currentUser = this.userRepository.save(user);
        return this.convertToResCreateUserDTO(currentUser);
    }

    @Transactional
    public void googleUser(String email, String name){
        if(this.userRepository.existsByEmail(email)){
            return;
        }
        User user = new User();
        user.setEmail(email);
        user.setFullName(name);
        user.setRole(this.roleRepository.findByName("USER").isPresent()?this.roleRepository.findByName("USER").get():null);
        this.userRepository.save(user);

    }

    @Transactional
    public ResCreateUserDTO register(ReqRegisterDTO req) throws IdInvalidException {
        ReqUserCreateDTO createDTO = new ReqUserCreateDTO();
        createDTO.setEmail(req.getEmail());
        createDTO.setPassword(req.getPassword());
        createDTO.setFullName(req.getFullName());
        createDTO.setAddress(req.getAddress());
        createDTO.setPhone(req.getPhone());
        createDTO.setRoleName("USER");
        return this.createUser(createDTO);
    }

    @Transactional
    public ResUpdateUserDTO updateUser(ReqUserUpdateDTO user) throws IdInvalidException {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new IdInvalidException("User không tồn tại"));

        Optional<User> existingUser = this.userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent() && existingUser.get().getId() != currentUser.getId()) {
            throw new IdInvalidException("Email đã tồn tại");
        }

        currentUser.setEmail(user.getEmail());
        currentUser.setFullName(user.getFullName());
        currentUser.setPhone(user.getPhone());
        currentUser.setAddress(user.getAddress());
        if (user.getRoleName() != null && !user.getRoleName().isBlank()) {
            currentUser.setRole(this.getRoleOrThrow(normalizeRoleName(user.getRoleName(), "USER")));
        }

        User updateUser = this.userRepository.save(currentUser);
        return this.convertToResUpdateUserDTO(updateUser);
    }

    @Transactional
=======
    public ResCreateUserDTO createUser(User user){
        User currentUser = this.userRepository.save(user);
        ResCreateUserDTO  resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(currentUser.getId());
        resCreateUserDTO.setEmail(currentUser.getEmail());
        resCreateUserDTO.setFullName(currentUser.getFullName());
        resCreateUserDTO.setAddress(currentUser.getAddress());
        resCreateUserDTO.setPhone(currentUser.getPhone());
        resCreateUserDTO.setCreatedAt(currentUser.getCreatedAt());
        ResRoleDTO role = new ResRoleDTO(currentUser.getRole().getName(), currentUser.getRole().getDecription());
        resCreateUserDTO.setRole(role);
        return resCreateUserDTO;
    }

    public ResUpdateUserDTO updateUser(User user){
        Optional<User> optionalUser = this.userRepository.findById(user.getId());
        if(optionalUser.isPresent()){
            User currentUser = optionalUser.get();
            currentUser.setEmail(user.getEmail());
            currentUser.setFullName(user.getFullName());
            currentUser.setPhone(user.getPhone());
            currentUser.setAddress(user.getAddress());
            User updateUser = this.userRepository.save(currentUser);

            //DTO
            ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
            resUpdateUserDTO.setId(updateUser.getId());
            resUpdateUserDTO.setEmail(updateUser.getEmail());
            resUpdateUserDTO.setFullName(updateUser.getFullName());
            resUpdateUserDTO.setAddress(updateUser.getAddress());
            resUpdateUserDTO.setPhone(updateUser.getPhone());
            resUpdateUserDTO.setUpdatedAt(updateUser.getUpdatedAt());
            ResRoleDTO role = new ResRoleDTO(updateUser.getRole().getName(), updateUser.getRole().getDecription());
            resUpdateUserDTO.setRole(role);
            return resUpdateUserDTO;
        }
        return null;
    }

>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    public void deleteUserById(long id){
        this.userRepository.deleteById(id);
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public ResultPaginationDTO fetchAllUsers(Specification<User> spec, Pageable pageable){
        Page<User> users = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+1);
        meta.setPagesize(pageable.getPageSize());
        meta.setPages(users.getTotalPages());
        meta.setTotal(users.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        List<ResUserDTO> listUser = users.getContent().
                stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(listUser);

        return resultPaginationDTO;
    }

<<<<<<< HEAD
    public ResUserDTO getuserById(long id) throws IdInvalidException {
        User currentUser = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User không tồn tại"));
        return this.convertToResUserDTO(currentUser);
=======
    public ResUserDTO getuserById(long id){
        Optional<User> optionalUser = this.userRepository.findById(id);
        if(optionalUser.isPresent()){
            User currentUser = optionalUser.get();

            //DTO
            ResUserDTO resUserDTO = this.convertToResUserDTO(currentUser);
            return resUserDTO;
        }
        return null;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    }

    public User getUserByUsername(String username){
        Optional<User> optinalUser = this.userRepository.findByEmail(username);
        if(optinalUser.isPresent()){
            return optinalUser.get();
        }
        return null;
    }

    public void updateUserToken(String token, String email)
    {
        User currentUser = this.getUserByUsername(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

<<<<<<< HEAD
    private Role getRoleOrThrow(String roleName) throws IdInvalidException {
        return this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new IdInvalidException("Role không tồn tại: " + roleName));
    }

    private String normalizeRoleName(String roleName, String fallback) {
        String value = (roleName == null || roleName.isBlank()) ? fallback : roleName;
        value = value.trim().toUpperCase(Locale.ROOT);
        if (value.startsWith("ROLE_")) {
            value = value.substring("ROLE_".length());
        }
        return value;
    }

    private ResCreateUserDTO convertToResCreateUserDTO(User currentUser) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(currentUser.getId());
        resCreateUserDTO.setEmail(currentUser.getEmail());
        resCreateUserDTO.setFullName(currentUser.getFullName());
        resCreateUserDTO.setAddress(currentUser.getAddress());
        resCreateUserDTO.setPhone(currentUser.getPhone());
        resCreateUserDTO.setCreatedAt(currentUser.getCreatedAt());
        resCreateUserDTO.setRole(this.convertToResRoleDTO(currentUser.getRole()));
        return resCreateUserDTO;
    }

    private ResUpdateUserDTO convertToResUpdateUserDTO(User updateUser) {
        ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
        resUpdateUserDTO.setId(updateUser.getId());
        resUpdateUserDTO.setEmail(updateUser.getEmail());
        resUpdateUserDTO.setFullName(updateUser.getFullName());
        resUpdateUserDTO.setAddress(updateUser.getAddress());
        resUpdateUserDTO.setPhone(updateUser.getPhone());
        resUpdateUserDTO.setUpdatedAt(updateUser.getUpdatedAt());
        resUpdateUserDTO.setRole(this.convertToResRoleDTO(updateUser.getRole()));
        return resUpdateUserDTO;
    }

    private ResRoleDTO convertToResRoleDTO(Role role) {
        if (role == null) {
            return null;
        }
        return new ResRoleDTO(role.getName(), role.getDecription());
    }

=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    public ResUserDTO convertToResUserDTO(User user){
        ResUserDTO resUserDTO = new ResUserDTO();
        resUserDTO.setId(user.getId());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setFullName(user.getFullName());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setPhone(user.getPhone());
        resUserDTO.setCreatedAt(user.getCreatedAt());
        resUserDTO.setUpdatedAt(user.getUpdatedAt());
<<<<<<< HEAD
        resUserDTO.setRole(this.convertToResRoleDTO(user.getRole()));
=======
        ResRoleDTO role = new ResRoleDTO(user.getRole().getName(), user.getRole().getDecription());
        resUserDTO.setRole(role);
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        return resUserDTO;
    }

}
