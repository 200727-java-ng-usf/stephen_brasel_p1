package com.revature.daos;

import java.util.Optional;
import java.util.Set;

public interface CrudDao<T> {
	public abstract void save(T t);
	public abstract Set<T> findAll();
	public abstract Optional<T> findById(int id);
	public abstract boolean update(T t);
	public abstract boolean deleteById(int id);
}
