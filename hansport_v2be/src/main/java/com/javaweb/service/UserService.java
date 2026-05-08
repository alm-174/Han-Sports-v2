package com.javaweb.service;

import com.javaweb.domain.User;
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.role.ResRoleDTO;
import com.javaweb.domain.response.user.ResCreateUserDTO;
import com.javaweb.domain.response.user.ResUpdateUserDTO;
import com.javaweb.domain.response.user.ResUserDTO;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean isEmailExists(String email)
    {
        return this.userRepository.existsByEmail(email);
    }

    public boolean isUserExists(long id)
    {
        return this.userRepository.existsById(id);
    }

    public User googleUser(String email, String name){
        if(this.userRepository.existsByEmail(email)){
            return this.userRepository.findByEmail(email).isPresent() ?
                    this.userRepository.findByEmail(email).get():null;
        }
            User user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setRole(this.roleRepository.findByName("USER"));
            return this.userRepository.save(user);

    }

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

    public ResUserDTO getuserById(long id){
        Optional<User> optionalUser = this.userRepository.findById(id);
        if(optionalUser.isPresent()){
            User currentUser = optionalUser.get();

            //DTO
            ResUserDTO resUserDTO = this.convertToResUserDTO(currentUser);
            return resUserDTO;
        }
        return null;
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

    public ResUserDTO convertToResUserDTO(User user){
        ResUserDTO resUserDTO = new ResUserDTO();
        resUserDTO.setId(user.getId());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setFullName(user.getFullName());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setPhone(user.getPhone());
        resUserDTO.setCreatedAt(user.getCreatedAt());
        resUserDTO.setUpdatedAt(user.getUpdatedAt());
        ResRoleDTO role = new ResRoleDTO(user.getRole().getName(), user.getRole().getDecription());
        resUserDTO.setRole(role);
        return resUserDTO;
    }

}
