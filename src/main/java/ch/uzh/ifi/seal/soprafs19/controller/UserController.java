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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    Iterable<User> all(@RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return service.getUsers(search);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getUser(@PathVariable long id){
        User User = service.getUser(id);
        if(User == null) throw new UserNotFoundException("Following Id not found: "+id);
        return User;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{id}")
    void updateUser(@PathVariable long id, @RequestBody User updatedUser){ this.service.updateUser(id, updatedUser);}
}
