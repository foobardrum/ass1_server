package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.AuthFailedException;
import ch.uzh.ifi.seal.soprafs19.exception.NotAuthorizedException;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs19.exception.UsernameAlreadyTakenException;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    List<User> all(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestHeader(value = "Authorization",defaultValue = "") String token
    ) {
        if(service.isAuthorized(token)) {
            return service.getUsers(search);
        }else{
            throw new NotAuthorizedException();
        }
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/me")
    User authenticateUser(@RequestBody User user){
        User authenticatedUser  = this.service.authenticateUser(user);
        if(authenticatedUser == null){
            throw new AuthFailedException();
        }
        return authenticatedUser;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getUser(@PathVariable long id, @RequestHeader(value = "Authorization",defaultValue = "") String token){
        if(service.isAuthorized(token)) {
            User user = service.getUser(id);
            if(user == null){
                throw new UserNotFoundException(id);
            }
            return user;
        }else{
            throw new NotAuthorizedException();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/users", produces = "application/json;charset=UTF-8")
    String createUser(@RequestBody User newUser) {
        if(this.service.getUserByUsername(newUser.getUsername()) != null){
            throw new UsernameAlreadyTakenException(newUser.getUsername());
        }
        User createdUser = this.service.createUser(newUser);
        return "{\"location\":\"/users/"+createdUser.getId()+"\"}";
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{id}")
    void updateUser(
            @PathVariable long id,
            @RequestBody User updatedUser,
            @RequestHeader(value = "Authorization",defaultValue = "") String token
    ){
        if(service.isAuthorized(token)){
            User existingUser = this.service.getUser(id);
            if(existingUser != null){
                if(existingUser.getToken().equals(token)){
                    if(this.service.updateUser(id, updatedUser) == null){
                        throw new UserNotFoundException(id);
                    }
                }else{
                    throw new NotAuthorizedException();
                }
            }else{
                throw new UserNotFoundException(id);
            }
        }else{
            throw new NotAuthorizedException();
        }
    }
}
