package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.QueryInvalidException;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs19.exception.UsernameAlreadyTakenException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                throw new QueryInvalidException(e.getMessage());
            }
        }
        return this.userRepository.findAll();
    }

    public User getUser(long id){ return this.userRepository.findById(id);}

    public User createUser(User newUser) {
        if(userRepository.findByUsername(newUser.getUsername()) != null){
            throw new UsernameAlreadyTakenException("username "+newUser.getUsername()+" already used");
        }
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUser(long id, User updatedUser){
        User existingUser = userRepository.findById(id);
        if(existingUser == null) throw new UserNotFoundException("Following Id not found: "+id);
        if(updatedUser.getName() != null ) existingUser.setName(updatedUser.getName());
        if(updatedUser.getUsername() != null )existingUser.setUsername(updatedUser.getUsername());
        if(updatedUser.getPassword() != null )existingUser.setPassword(updatedUser.getPassword());
        if(updatedUser.getStatus() != null )existingUser.setStatus(updatedUser.getStatus());
        if(updatedUser.getToken() != null )existingUser.setToken(updatedUser.getToken());
    }
}
