package com.revature.daos;

import com.revature.models.AppUser;

import java.util.Optional;
import java.util.Set;

public class UserDao implements CrudDao<AppUser> {
	@Override
	public void save(AppUser appUser) {

	}

	@Override
	public Set<AppUser> findAll() {
		return null;
	}

	@Override
	public Optional<AppUser> findById(int id) {
		return Optional.empty();
	}

	@Override
	public boolean update(AppUser appUser) {
		return false;
	}

	@Override
	public boolean deleteById(int id) {
		return false;
	}

	public Optional<AppUser> findUserByUsername(String userName) {
		Optional<AppUser> _user = Optional.empty();
		return  _user;
	}

	public Optional<AppUser> findUserByCredentials(String username, String password) {
		Optional<AppUser> _user = Optional.empty();
		return  _user;
	}
}
