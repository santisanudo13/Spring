package app.dao;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;





@Component
public class DatabaseLoader {

	@Autowired
	private UserRepository userRepository;


	@PostConstruct
	private void initDatabase() {


		saveDefaultUsers();

		

	}


	


	/**
	 * Guarda los usuarios por defecto
	 */
	private void saveDefaultUsers() {
		// User #1: "user", with password "p1" and role "USER"
		GrantedAuthority[] userRoles = {
				new SimpleGrantedAuthority("ROLE_USER") };
		User user = new User("user", "user", Arrays.asList(userRoles),"user@gmail.com");

		User u = null;
		u = userRepository.findOne("user");
		if(u == null)
			userRepository.save(user);

		// User #2: "root", with password "p2" and roles "USER" and "ADMIN"
		GrantedAuthority[] adminRoles = {
				new SimpleGrantedAuthority("ROLE_USER"),
				new SimpleGrantedAuthority("ROLE_ADMIN") };
		User admin = new User("admin", "admin", Arrays.asList(adminRoles),"admin@admin");

		User u2 = null;
		u2 = userRepository.findOne("admin");
		if(u2 == null)
			userRepository.save(admin);
		
		
		
		
		// User #2: "root", with password "p2" and roles "USER" and "ADMIN"
				GrantedAuthority[] bezanaRoles = {
						new SimpleGrantedAuthority("ROLE_USER"),
						new SimpleGrantedAuthority("ROLE_ADMIN") };
				User bezana = new User("bezana", "bezana", Arrays.asList(adminRoles),"bezana@bezana");

				User u3 = null;
				u3 = userRepository.findOne("bezana");
				if(u3 == null)
					userRepository.save(bezana);
	}




}
