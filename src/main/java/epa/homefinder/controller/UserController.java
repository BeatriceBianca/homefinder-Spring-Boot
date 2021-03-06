package epa.homefinder.controller;

import epa.homefinder.dto.*;
import epa.homefinder.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private UserService userService;
    private List<Map<String, String>> mapList = new ArrayList<>();

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

    @PostMapping("/getUserAds")
    public List<AdDto> getUserAds(@RequestBody EmailDto userEmail) {
        return userService.getUserAds(userEmail);
    }

    @GetMapping("/getUserEmails")
    public List<String> getUserEmails() {
        return userService.getUserEmails();
    }
}
