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

import java.util.List;
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

    public List<User> getUsers(String search) {
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
        return this.userRepository.findById(id);
    }

    public User getUserByUsername(String username){
        return this.userRepository.findByUsername(username);
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User updateUser(long id, User updatedUser){
        User existingUser = userRepository.findById(id);
        if(existingUser == null){
           return null;
        }
        if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
        if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
        if (updatedUser.getStatus() != null) existingUser.setStatus(updatedUser.getStatus());
        if (updatedUser.getBirthdayDate() != null) existingUser.setBirthdayDate(updatedUser.getBirthdayDate());
        userRepository.save(existingUser);
        return existingUser;
    }

    public void deleteUser(long id){
        User user = userRepository.findById(id);
        userRepository.delete(user);
    }

    public User authenticateUser(User userToAuthenticate){
        User user = userRepository.findByUsername(userToAuthenticate.getUsername());
        if(user != null && user.getPassword().equals(userToAuthenticate.getPassword())){
            user.setStatus(UserStatus.ONLINE);
            userRepository.save(user);
            return user;
        }else{
            return null;
        }
    }

    public Boolean isAuthorized(String token){
        return userRepository.existsByToken(token);
    }
}
