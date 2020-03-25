//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Lucas Wierer, Jonas Jahns (Minor changes)
//    Description: Data transfer object for users
//----------------------------------------------------------------------------------
package org.kickercup.api.model;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

	private long id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;

	public User() {
		
	}
	
	public User(String username, String password, String firstname, String lastname, String email) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

	@Column(name = "username", nullable = false, unique = true)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name = "firstname", nullable = false)
	public String getFirstName() {
		return firstname;
	}
	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}
	
	@Column(name = "lastname", nullable = false)
	public String getLastName() {
		return lastname;
	}
	public void setLastName(String lastName) {
		this.lastname = lastName;
	}
	
	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return this.toString();
	}
	
}
