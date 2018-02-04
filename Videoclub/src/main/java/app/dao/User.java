package app.dao;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class User {

    
	@Id
    private String user;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<GrantedAuthority> roles;
    
    
    private String email;

    public User() {
    }

    public User(String user, String password, List<GrantedAuthority> roles,String email) {
        this.user = user;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.roles = roles;
        this.email=email;
    }

    // getters, setters

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPasswordHash(String passwordHash) {
        this.password = passwordHash;
    }

    public List<GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(List<GrantedAuthority> roles) {
        this.roles = roles;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

		
	

}