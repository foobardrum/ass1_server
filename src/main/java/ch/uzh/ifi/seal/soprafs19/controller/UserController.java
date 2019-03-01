package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    User getUser(@PathVariable long id){
        User User = service.getUser(id);
        if(User == null) throw new UserNotFoundException(id);
        return User;
    }

    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }
}
