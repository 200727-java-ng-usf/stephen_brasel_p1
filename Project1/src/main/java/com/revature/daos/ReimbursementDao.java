package com.revature.daos;

import com.revature.models.Reimbursement;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ReimbursementDao implements CrudDao<Reimbursement> {
	@Override
	public void save(Reimbursement reimbursement) {

	}

	@Override
	public List<Reimbursement> findAll() {
		return null;
	}

	@Override
	public Optional<Reimbursement> findById(int id) {
		return Optional.empty();
	}

	@Override
	public boolean update(Reimbursement reimbursement) {
		return false;
	}

	@Override
	public boolean deleteById(int id) {
		return false;
	}
}
