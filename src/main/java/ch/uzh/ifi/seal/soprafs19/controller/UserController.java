package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.NotAuthorizedException;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        service.isAuthorized(token);
        return service.getUsers(search);
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/me")
    User authenticateUser(@RequestBody User user){ return this.service.authenticateUser(user);}

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getUser(@PathVariable long id, @RequestHeader(value = "Authorization",defaultValue = "") String token){
        service.isAuthorized(token);
        return service.getUser(id);
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
        service.isAuthorized(token);
        this.service.updateUser(id, updatedUser);
    }
}
