package com.revature.daos;

import java.util.Optional;
import java.util.Set;

public class ReimbursementDao implements CrudDao {
	@Override
	public void save(Object o) {

	}

	@Override
	public Set findAll() {
		return null;
	}

	@Override
	public Optional findById(int id) {
		return Optional.empty();
	}

	@Override
	public boolean update(Object o) {
		return false;
	}

	@Override
	public boolean deleteById(int id) {
		return false;
	}
}
