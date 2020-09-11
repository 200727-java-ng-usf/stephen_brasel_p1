package com.revature.services;

import com.revature.models.AppUser;
import com.revature.models.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class UserServiceTest {
	private UserService sut;
	Set<AppUser> mockUsers = new HashSet<>();

	@Before
	public void setUp() throws Exception {
		sut = new UserService();
		mockUsers.add(new AppUser(1, "Alice", "Anderson", "aanderson", "password", "aanderson@revature.net", Role.ADMIN));
	}

	@After
	public void tearDown() throws Exception {
		sut = null;
	}

	@Test
	public void getAllUser() {
	}

	@Test
	public void getUsersByRole() {
	}

	@Test
	public void getUsersById() {
	}

	@Test
	public void getUserById() {
	}

	@Test
	public void getUserByUsername() {
	}

	@Test
	public void authenticate() {
	}

	@Test
	public void register() {
	}

	@Test
	public void updateUser() {
	}

	@Test
	public void deleteUserById() {
	}

	@Test
	public void validateUserFields() {
	}

	@Test
	public void isUserValid() {
	}
}