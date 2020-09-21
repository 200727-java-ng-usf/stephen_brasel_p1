package com.revature.dtos;

import com.revature.models.AppUser;

import java.util.Objects;

/**
 * The Data Transfer Object for <code>{@link AppUser}</code>s, including:
 * <code>{@link AppUser}</code> id
 * whether or not the <code>{@link AppUser}</code> is active
 * first name
 * last name
 * user name
 * password
 * email
 * role
 */
public class UserDto {
	//region Fields
	private int id;
	private boolean active;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String role;

	public UserDto() {
	}

	public UserDto(int id, boolean active, String firstName, String lastName, String username, String password, String email, String role) {
		this.id = id;
		this.active = active;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserDto userDto = (UserDto) o;
		return id == userDto.id &&
				active == userDto.active &&
				Objects.equals(firstName, userDto.firstName) &&
				Objects.equals(lastName, userDto.lastName) &&
				Objects.equals(username, userDto.username) &&
				Objects.equals(password, userDto.password) &&
				Objects.equals(email, userDto.email) &&
				Objects.equals(role, userDto.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, active, firstName, lastName, username, password, email, role);
	}

	@Override
	public String toString() {
		return "UserDto{" +
				"id=" + id +
				", active=" + active +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", role='" + role + '\'' +
				'}';
	}
}
