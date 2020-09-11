package com.revature.daos;

import com.revature.dtos.Credentials;
import com.revature.models.AppUser;
import com.revature.models.Role;
import com.revature.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class UserDao implements CrudDao<AppUser> {
	private final SessionFactory sessionFactory = HibernateSessionFactory.getInstance();

	@Override
	public void save(AppUser appUser) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(appUser);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
		}
		session.close();
	}

	@Override
	public List<AppUser> findAll() {
		List<AppUser> _users = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(HibernateSessionFactory.getInstance()).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			_users = session.createQuery(query).list();
			System.out.println(_users);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _users;
	}

	@Override
	public Optional<AppUser> findById(int id) {
		return findUserById(id);
	}

	@Override
	public boolean update(AppUser appUser) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(appUser);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		session.close();
		return true;
	}

	@Override
	public boolean deleteById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.createQuery("delete AppUser au where au.id = :id")
				.setParameter("id", id);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		session.close();
		return true;
	}

	public Optional<AppUser> findUserByUsername(String userName) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(HibernateSessionFactory.getInstance()).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			Root<AppUser> root = query.from(AppUser.class);
			System.out.println(userName);
			query.select(root).where(cb.equal(root.get("username"), userName));
			_user = Optional.of(session.createQuery(query).getSingleResult());
			System.out.println(_user);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
			e.printStackTrace();
		}
		return  _user;
	}

	public Optional<AppUser> findUserByCredentials(Credentials creds) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(HibernateSessionFactory.getInstance()).openSession()) {
			tx = session.beginTransaction();
			AppUser retrievedUser = session.createQuery(
					"from AppUser au where au.username = :un and password = :pw"
					, AppUser.class)
					.setParameter("un", creds.getUsername())
					.setParameter("pw", creds.getPassword())
					.getSingleResult();
			_user = Optional.of(retrievedUser);
//			Query query = session.createQuery("FROM com.revature.model.Employee", Employee.class);
//			List<Employee> employees = query.list();
//			for (Employee e : employees) {
//				System.out.println("Entry: "
//						+ e.getFirstname() + " "
//						+ e.getLastname() + " "
//						+ e.getSalary());
//			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
			e.printStackTrace();
		}
		return  _user;
	}

	public Optional<AppUser> findUserByEmail(String email) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(HibernateSessionFactory.getInstance()).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			Root<AppUser> root = query.from(AppUser.class);
			System.out.println(email);
			query.select(root).where(cb.equal(root.get("email"), email));
			_user = Optional.of(session.createQuery(query).getSingleResult());
			System.out.println(_user);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
			e.printStackTrace();
		}
		return  _user;
	}

	public Optional<AppUser> findUserById(int id) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(HibernateSessionFactory.getInstance()).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			Root<AppUser> root = query.from(AppUser.class);
			System.out.println(id);
			query.select(root).where(cb.equal(root.get("email"), id));
			_user = Optional.of(session.createQuery(query).getSingleResult());
			System.out.println(_user);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
			e.printStackTrace();
		}
		return  _user;
	}

	public List<AppUser> findUserbyRole(Role role) {
		List<AppUser> _users = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(HibernateSessionFactory.getInstance()).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			_users = session.createQuery(query).list();
			System.out.println(_users);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _users;
	}
}
