package epa.homefinder.controller;

import org.springframework.web.bind.annotation.*;
import epa.homefinder.dto.*;
import epa.homefinder.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public void userRegistration(@RequestBody UserRegistrationDto userRegistrationDto){
        System.out.println(userRegistrationDto);
        userService.registerNewUser(userRegistrationDto);
    }

    @GetMapping("/userList")
    public List<UserDataDto> getUsersData() {
        return userService.getUsersData();
    }

    @PostMapping("/updateUser")
    public void  updateUser(@RequestBody UserDataDto userDataDto) {
        userService.updateUser(userDataDto);
    }

    @PostMapping("/getUserData")
    public UserDto getUserData(@RequestBody EmailDto emailDto) {
        return userService.getUserData(emailDto);
    }

    @PostMapping("/updateUserData")
    public void updateUserData(@RequestBody UserDtoUpdate userDtoUpdate) {
        userService.updateUserData(userDtoUpdate);
    }

    @GetMapping("/getUserEmails")
    public List<String> getUserEmails() {
        return userService.getUserEmails();
    }
}
