package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.sql.Timestamp;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.util.StringUtils.replace;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserController userController;

    @Autowired
    private UserService userService;

    @Test
    public void postUserSuccess() throws Exception {
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testUser\",\"password\":\"testPassword\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.location", notNullValue()));
    }


    @Test
    public void postUserFail() throws Exception {
        User user = new User();
        user.setUsername("testUser2");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);
        //add same user again
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\""+createdUser.getUsername()+"\",\"password\":\""+createdUser.getPassword()+"\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void getUsersUnauthorized() throws Exception {
        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void getUsers() throws Exception {
        User user = new User();
        user.setUsername("testUser3");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);
        mvc.perform(get("/users")
                .header("Authorization",createdUser.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

    }

    @Test
    public void getUserUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("testUser4");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);
        mvc.perform(get("/users/"+createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void getUser() throws Throwable {
        User user = new User();
        user.setUsername("testUser5");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);
        mvc.perform(get("/users/"+user.getId())
                .header("Authorization",createdUser.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(createdUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(createdUser.getUsername())))
                .andExpect(jsonPath("$.password", is(createdUser.getPassword())))
                .andExpect(jsonPath("$.status", is(createdUser.getStatus().toString())))
                .andExpect(jsonPath("$.registrationDateTime", is(
                        replace(new Timestamp(createdUser.getRegistrationDateTime().getTime()).toString()+"+0000"," ","T")
                )))
                .andExpect(jsonPath("$.birthdayDate", is(createdUser.getBirthdayDate())))
                .andExpect(jsonPath("$.token", is(createdUser.getToken())));
    }

    @Test
    public void getUserNotExistent() throws Exception {
        long mockId = 10000000;
        User foundUser = userService.getUser(mockId);
        Assert.assertNull(foundUser);

        User user = new User();
        user.setUsername("testUser6");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);

        mvc.perform(get("/users/"+mockId)
                .header("Authorization",createdUser.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void putUserUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("testUser7");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);
        String newUsername = "modifiedTestUser7";
        String newPassword = "modifiedTestPassword";
        mvc.perform(put("/users/"+createdUser.getId())
                .content("{\"username\":\""+newUsername+"\",\"password\":\""+newPassword+"\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void putUser() throws Exception {
        User user = new User();
        user.setUsername("testUser8");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);
        String newUsername = "modifiedTestUser8";
        String newPassword = "modifiedTestPassword";
        mvc.perform(put("/users/"+createdUser.getId())
                .content("{\"username\":\""+newUsername+"\",\"password\":\""+newPassword+"\"}")
                .header("Authorization",createdUser.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

    }

    @Test
    public void putUserNotFound() throws Exception {
        long mockId = 99999999;
        User foundUser = userService.getUser(mockId);
        Assert.assertNull(foundUser);

        User user = new User();
        user.setUsername("testUser9");
        user.setPassword("testPassword");
        User createdUser = userService.createUser(user);
        String newUsername = "modifiedTestUser9";
        String newPassword = "modifiedTestPassword";
        mvc.perform(put("/users/"+mockId)
                .content("{\"username\":\""+newUsername+"\",\"password\":\""+newPassword+"\"}")
                .header("Authorization",createdUser.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
}
