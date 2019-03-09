package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    Iterable<User> all(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestHeader(value = "Authorization",defaultValue = "") String token
    ) {
        if(service.isAuthorized(token)) {
            return service.getUsers(search);
        }
        return null;
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/me")
    User authenticateUser(@RequestBody User user){ return this.service.authenticateUser(user);}

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getUser(@PathVariable long id, @RequestHeader(value = "Authorization",defaultValue = "") String token){
        if(service.isAuthorized(token)) {
            return service.getUser(id);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{id}")
    void updateUser(
            @PathVariable long id,
            @RequestBody User updatedUser,
            @RequestHeader(value = "Authorization",defaultValue = "") String token
    ){
        if(service.isAuthorized(token)){
            this.service.updateUser(id, token, updatedUser);
        }
    }
}
