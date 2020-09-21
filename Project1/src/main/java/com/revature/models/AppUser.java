package com.revature.models;

import com.sun.crypto.provider.PBKDF2HmacSHA1Factory;
import org.hibernate.sql.Delete;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;


/**
 * Defines the basic Application user, with facets such as:
 * an id
 * whether or not they are active
 * first name
 * last name
 * username
 * the hash of their password
 * the salt of their password
 * email
 * the <code>{@link Role}</code>
 */
@Entity
@Table(name = "revabursement.ers_users")
public class AppUser {
	//region Fields
	@Id
	@Column(name="ers_user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "active")
	private boolean active = true;
	@Column(name="firstname")
	private String firstName;
	@Column(name="lastname")
	private String lastName;
	@Column(name="username")
	private String username;

	@Column(name="password_hash")
	private byte[] passwordHash;
	@Column(name="password_salt")
	private byte[] passwordSalt;
	@Column(name="email")
	private String email;
//	@Column(name="user_role_id")
	@Enumerated(EnumType.STRING)
	private Role role;
	//endregion

	//region Constructors
	public AppUser(){
		super();
	}

	public AppUser(String firstName, String lastName, String username, byte[] passwordHash, byte[] passwordSalt, String email) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
		this.email = email;
		this.role = Role.EMPLOYEE;
	}

	public AppUser(String firstName, String lastName, String username, String email) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.role = Role.EMPLOYEE;
	}

	public AppUser(String firstName, String lastName, String username, String email, Role role) {
		this(firstName, lastName, username, email);
		this.role = role;
	}
	public AppUser(String firstName, String lastName, String username, String password, String email) {
		this(firstName, lastName, username, email);
		saltAndHashPassword(password);
	}
	public AppUser(int id, String firstName, String lastName, String username, String password, String email, Role role) {
		this(firstName, lastName, username, email, role);
		saltAndHashPassword(password);
		this.id = id;
	}

	private AppUser(int id, String firstName, String lastName, String username, byte[] passwordHash, byte[] passwordSalt, String email, Role role) {
		this(firstName, lastName, username, email, role);
		this.id = id;
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
	}

	public AppUser(AppUser user){
		this(user.id, user.firstName, user.lastName, user.username, user.passwordHash, user.passwordSalt, user.email, user.role);
	}
	//endregion

	//region Getters and Setters
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

	public byte[] getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(byte[] passwordHash) {
		this.passwordHash = passwordHash;
	}

	public byte[] getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(byte[] passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public byte[] getString() {
		return passwordHash;
	}

	public void setString(byte[] password) {
		this.passwordHash = password;
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

	/**
	 * Processes a given <code>{@link String}</code> into a password salt and hash.
	 * @param password the <code>{@link String}</code> to salt and hash
	 * @return true if the process was successful
	 */
	public boolean saltAndHashPassword(String password){
		if(password == null || password.trim().equals("")){
			passwordHash = new byte[0];
			passwordSalt = new byte[0];
			return false;
		}
		try {
			SecureRandom random = new SecureRandom();
			passwordSalt = new byte[256];
			random.nextBytes(passwordSalt);
			int iterationCount = 65536;
			int keyLength = 128;
			char[] pca = password.toCharArray();
			KeySpec spec = new PBEKeySpec(pca, passwordSalt, iterationCount, keyLength);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			passwordHash = factory.generateSecret(spec).getEncoded();
			return true;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Validates a given password against the given hash and salt. If true, the password matches this users' account.
	 * @param password the <code>{@link String}</code> to check
	 * @param hash the <code>byte[]</code> of the password hash
	 * @param salt the <code>byte[]</code> of the password salt
	 * @return true if the password matches the salt and hash.
	 */
	public boolean validatePassword(String password, byte[] hash, byte[] salt){
		try{
			int iterationCount = 65536;
			int keyLength = 128;
			char[] pca = password.toCharArray();
			KeySpec spec = new PBEKeySpec(pca, salt, iterationCount, keyLength);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return Arrays.equals(hash, factory.generateSecret(spec).getEncoded());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	//endregion

	//region OverRidden Methods

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AppUser appUser = (AppUser) o;
		return id == appUser.id &&
				active == appUser.active &&
				Objects.equals(firstName, appUser.firstName) &&
				Objects.equals(lastName, appUser.lastName) &&
				Objects.equals(username, appUser.username) &&
				Arrays.equals(passwordHash, appUser.passwordHash) &&
				Arrays.equals(passwordSalt, appUser.passwordSalt) &&
				Objects.equals(email, appUser.email) &&
				role == appUser.role;
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(id, active, firstName, lastName, username, email, role);
		result = 31 * result + Arrays.hashCode(passwordHash);
		result = 31 * result + Arrays.hashCode(passwordSalt);
		return result;
	}

	@Override
	public String toString() {
		return "AppUser{" +
				"id=" + id +
				", active=" + active +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", role=" + role +
				'}';
	}

//endregion
}
