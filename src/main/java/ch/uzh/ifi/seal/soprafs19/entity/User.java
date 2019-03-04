package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;

@Entity
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column(nullable = false, unique = true) 
	private String username;

	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true) 
	private String token;

	@Column(nullable = false)
	private UserStatus status;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date registrationDateTime = new Date();

	@Column
	@Temporal(TemporalType.DATE)
	private java.util.Date birthdayDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() { return password; }

	public void setPassword(String password) { this.password = password; }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Date getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(Date registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}

	public Date getBirthdayDate() {
		return birthdayDate;
	}

	public void setBirthdayDate(Date birthdayDate) {
		this.birthdayDate = birthdayDate;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}
