package com.revature.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "revabursement.ers_users")
public class AppUser {
	//region Fields
	@Id
	@Column(name="ers_user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name="firstname")
	private String firstName;
	@Column(name="lastname")
	private String lastName;
	@Column(name="username")
	private String username;

	@Column(name="password_hash")
	private String password;
	@Column(name="email")
	private String email;
	@Column(name="user_role_id")
//	@Enumerated(EnumType.STRING)
	private Role role;
	//endregion

	//region Constructors
	public AppUser(){
		super();
	}

	public AppUser(String firstName, String lastName, String username, String password, String email) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = Role.EMPLOYEE;
	}

	public AppUser(String firstName, String lastName, String username, String password, String email, Role role) {
		this(firstName, lastName, username, password, email);
		this.role = role;
	}
	public AppUser(int id, String firstName, String lastName, String username, String password, String email, Role role) {
		this(firstName, lastName, username, password, email, role);
		this.id = id;
	}

	public AppUser(AppUser user){
		this(user.id, user.firstName, user.lastName, user.username, user.password, user.email, user.role);
	}
	//endregion

	//region Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getString() {
		return password;
	}

	public void setString(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	//endregion

	//region Methods
	//endregion

	//region OverRidden Methods

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AppUser appUser = (AppUser) o;
		return id == appUser.id &&
				Objects.equals(firstName, appUser.firstName) &&
				Objects.equals(lastName, appUser.lastName) &&
				Objects.equals(username, appUser.username) &&
				Objects.equals(password, appUser.password) &&
				Objects.equals(email, appUser.email) &&
				role == appUser.role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, username, password, email, role);
	}

	@Override
	public String toString() {
		return "AppUser{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", userName='" + username + '\'' +
				", email='" + email + '\'' +
				", role=" + role +
				'}';
	}

	//endregion
}
