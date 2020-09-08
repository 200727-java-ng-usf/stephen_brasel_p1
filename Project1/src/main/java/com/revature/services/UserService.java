package com.revature.services;

import com.revature.daos.UserDao;
import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.InvalidRequestException;
import com.revature.models.*;

import java.util.Optional;
import java.util.Set;


public class UserService {
	private UserDao userDao;

	//region Constructors
	public UserService(UserDao repo){
//		System.out.println("[LOG] - Instantiating " + this.getClass().getName());
		userDao = repo;
	}
	//endregion

	//region Methods

	/**
	 * Returns all users registered with the bank database.
	 * @return a Set of <code>{@link AppUser}</code>s that have been registered and saved to the bank database
	 */
	public Set<AppUser> getAllUser(){
		return null;
	}

	/**
	 * Returns all <code>{@link AppUser}</code>s who have the given <code>{@link Role}</code>.
	 * @param role the <code>{@link Role}</code> to search by.
	 * @return a Set of <code>{@link AppUser}</code>s that all have the given <code>{@link Role}</code>
	 */
	public Set<AppUser> getUsersByRole(Role role){
		return null;
	}

	/**
	 * Returns all <code>{@link AppUser}</code>s who have the given id.
	 * @param id the int id to search by.
	 * @return a Set of <code>{@link AppUser}</code>s that all have the given id. This should never be more than 1 user.
	 */
	public Set<AppUser> getUsersById(int id){
		return null;
	}

	/**
	 * Returns the first <code>{@link AppUser}</code> found with the given id.
	 * @param id the int id to search by
	 * @return the first <code>{@link AppUser}</code> found with the given id.
	 */
	public AppUser getUserById(int id){
		return null;
	}

	/**
	 * Returns the first <code>{@link AppUser}</code> found with the given username.
	 * @param username the String username to search by.
	 * @return the first <code>{@link AppUser}</code> found with the given username.
	 */
	public AppUser getUserByUsername(String username){
		return null;
	}

	/**
	 * This method authenticates a <code>{@link AppUser}</code>  that already exists within the database.
	 * with the given username and password with the database. If a user already exists with the username
	 * and password or a user does not exist with the username and password or either username or password
	 * are either null or empty, then the method will not authenticate.
	 * @param username the String username to authenticate.
	 * @param password the String password to authenticate.
	 */
	public void authenticate(String username, String password){
		// Validate that the provided username and password are not non-values
		if(username == null || username.trim().equals("")
				|| password == null || password.trim().equals("")){
			throw new InvalidRequestException("Invalid credential values provided");
		}
		AppUser authUser = userDao.findUserByCredentials(username, password)
				.orElseThrow(AuthenticationException::new);

//		app.setCurrentUser(authUser);

//		if(authUser == null){
//			throw new AuthenticationException("No user found with the provided credentials");
//		}
//
//		return authUser;
	}

	/**
	 * This method registers a new <code>{@link AppUser}</code> into the database.
	 * @param newUser the <code>{@link AppUser}</code> to store/register in the database.
	 */
	public void register(AppUser newUser){
		//
		if(!isUserValid(newUser)){
			throw new InvalidRequestException("Invalid user field values provided during registration!");
		}
		Optional<AppUser> existingUser = userDao.findUserByUsername(newUser.getUsername());

		if(existingUser.isPresent()){
			throw new AuthenticationException("Provided username is already in use!");
		}

		newUser.setRole(Role.EMPLOYEE);
		userDao.save(newUser);

//		app.setCurrentUser(newUser);
	}

	/**
	 * This method updates the records of a <code>{@link AppUser}</code>
	 * that exists on the database with the local record.
	 * @param user the <code>{@link AppUser}</code> to update.
	 * @return returns true if the update was successful, false otherwise.
	 */
	public boolean updateUser(AppUser user){
		return false;
	}

	/**
	 * This method deletes a <code>{@link AppUser}</code> with the given id from
	 * the the database, along with any records only pertinent to them.
	 * @param id the id of the <code>{@link AppUser}</code>.
	 * @return returns true if the deletion was successful, false if otherwise.
	 * 		If there was no such user, returns true.
	 */
	public boolean deleteUserById(int id){
		return false;
	}

	/**
	 * Validates that the given <code>{@link AppUser}</code> and its fields are
	 * valid (not null or empty strings). Does not perform validation on id or Role fields.
	 *
	 * @param user the <code>{@link AppUser}</code> to validate.
	 * @return true if the <code>{@link AppUser}</code> is valid.
	 */
	public boolean validateUserFields(AppUser user){
		return isUserValid(user);
	}


	/**
	 * Validates that the given <code>{@link AppUser}</code> and its fields are
	 * valid (not null or empty strings). Does not perform validation on id or Role fields.
	 *
	 * @param user the <code>{@link AppUser}</code> to validate.
	 * @return true if the <code>{@link AppUser}</code> is valid.
	 */
	public boolean isUserValid(AppUser user){

		if(user == null) return false;
		if(user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
		if(user.getLastName() == null || user.getLastName().trim().equals("")) return false;
		if(user.getUsername() == null || user.getUsername().trim().equals("")) return false;
		if(user.getPassword() == null || user.getPassword().trim().equals("")) return false;
		if(user.getEmail() == null || user.getEmail().trim().equals("")) return false;

		return true;
	}
	//endregion
}
