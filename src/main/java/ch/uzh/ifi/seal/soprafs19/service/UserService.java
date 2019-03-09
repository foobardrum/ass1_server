package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers(String search) {
        if(!search.equals("")){
            try {
                Node rootNode = new RSQLParser().parse(search);
                Specification<User> spec = rootNode.accept(new CustomRsqlVisitor<User>());
                return this.userRepository.findAll(spec);
            }catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Query Invalid: "+e.getMessage());
            }
        }
        return this.userRepository.findAll();
    }

    public User getUser(long id){
        User User = this.userRepository.findById(id);
        if(User == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User with following Id not found: "+id);
        }
        return User;
    }

    public User createUser(User newUser) {
        if(userRepository.findByUsername(newUser.getUsername()) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"username "+newUser.getUsername()+" already taken.");
        }
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUser(long id, String token, User updatedUser){
        User existingUser = userRepository.findById(id);
        if(existingUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User with following Id not found: "+id);
        }
        if(!existingUser.getToken().equals(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You're not unauthorized to update this user!");
        }
        if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
        if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
        if (updatedUser.getStatus() != null) existingUser.setStatus(updatedUser.getStatus());
        if (updatedUser.getBirthdayDate() != null) existingUser.setBirthdayDate(updatedUser.getBirthdayDate());
        userRepository.save(existingUser);
    }

    public User authenticateUser(User userToAuthenticate){
        User user = userRepository.findByUsername(userToAuthenticate.getUsername());
        if(user != null && user.getPassword().equals(userToAuthenticate.getPassword())){
            user.setStatus(UserStatus.ONLINE);
            userRepository.save(user);
            return user;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Invalid authentication data provided!");
        }
    }

    public void isAuthorized(String token){
        if(!userRepository.existsByToken(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Provided token is unauthorized!");
        }
    }
}
