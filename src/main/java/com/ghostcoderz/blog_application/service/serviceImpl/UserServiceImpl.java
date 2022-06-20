package com.ghostcoderz.blog_application.service.serviceImpl;

import com.ghostcoderz.blog_application.config.AppConstants;
import com.ghostcoderz.blog_application.entity.Role;
import com.ghostcoderz.blog_application.entity.User;
import com.ghostcoderz.blog_application.exceptions.ResourceNotFoundException;
import com.ghostcoderz.blog_application.payload.UserDto;
import com.ghostcoderz.blog_application.repository.RoleRepo;
import com.ghostcoderz.blog_application.repository.UserRepo;
import com.ghostcoderz.blog_application.service.serviceInterface.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = dtoToUser(userDto);
        this.userRepo.save(user);
        return userToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = dtoToUser(userDto);
        User userInDB = userRepo.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                                "User", "id" , userId));
        userInDB.setName(user.getName());
        userInDB.setEmail(user.getEmail());
        userInDB.setPassword(user.getPassword());
        userInDB.setAbout(user.getAbout());
        this.userRepo.save(userInDB);

        return userToDto(userInDB);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = this.userRepo.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id" , userId));
        return userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return this.userRepo.findAll().stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            this.userRepo.deleteById(userId);
        } catch(Exception e) {
            throw new ResourceNotFoundException(
                    "User", "id", userId);
        }
    }


    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = dtoToUser(userDto);

        //Encoded the password
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //adding roles
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER_ROLE).get();

        user.getRoles().add(role);

        User newUser = this.userRepo.save(user);

        return userToDto(newUser);
    }

    private UserDto userToDto(User user){
        return this.modelMapper.map(user, UserDto.class);
    }

    private User dtoToUser(UserDto userDto){
        return this.modelMapper.map(userDto, User.class);
    }

}
