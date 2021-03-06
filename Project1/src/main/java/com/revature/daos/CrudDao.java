package com.revature.daos;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The base Interface for the Database operations
 * Create
 * Replace
 * Update
 * Delete
 * @param <T> The Type of Repository to operate upon.
 */
public interface CrudDao<T> {
	public abstract void save(T t);
	public abstract List<T> findAll();
	public abstract Optional<T> findById(int id);
	public abstract boolean update(T t);
	public abstract boolean deleteById(int id);
}
