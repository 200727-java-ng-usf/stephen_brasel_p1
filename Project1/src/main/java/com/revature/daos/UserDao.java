package com.revature.daos;

import com.revature.dtos.Credentials;
import com.revature.models.AppUser;
import com.revature.models.Role;
import com.revature.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class UserDao implements CrudDao<AppUser> {
	private final SessionFactory sessionFactory = HibernateSessionFactory.getInstance();

	@Override
	public void save(AppUser appUser) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			session.save(appUser);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
		}
	}

	@Override
	public List<AppUser> findAll() {
		List<AppUser> _users = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			Root<AppUser> root = query.from(AppUser.class);
			query.select(root);
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
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			session.update(appUser);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteById(int id) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			session.createQuery("delete AppUser au where au.id = :id")
				.setParameter("id", id);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}

	public Optional<AppUser> findUserByUsername(String userName) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			_user = Optional.of(session.createQuery(
					"from AppUser au where au.username = :un"
					, AppUser.class)
					.setParameter("un", userName)
					.getSingleResult()
			);
			tx.commit();
		} catch (NoResultException nre) {
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _user;
	}
//
//	public Optional<AppUser> findUserByCredentials(Credentials creds) {
//		Optional<AppUser> _user = Optional.empty();
//
//		Transaction tx = null;
//		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
//			tx = session.beginTransaction();
//			AppUser retrievedUser = session.createQuery(
//					"from AppUser au where au.username = :un"
//					, AppUser.class)
//					.setParameter("un", creds.getUsername())
//					.getSingleResult();
//			_user = Optional.of(retrievedUser);
////			}
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			if (tx != null) tx.rollback();
//		}
//		return  _user;
//	}

	public Optional<AppUser> findUserByEmail(String email) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			_user = Optional.of(session.createQuery(
					"from AppUser au where au.email = :em"
					, AppUser.class)
					.setParameter("em", email)
					.getSingleResult()
			);
			tx.commit();
		} catch (NoResultException nre) {
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return  _user;
	}

	public Optional<AppUser> findUserById(int id) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
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
		}
		return  _user;
	}

	// TODO Test Me
	public List<AppUser> findUserbyRole(Role role) {
		List<AppUser> _users = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			_users = session.createQuery("from AppUser au where au.role_id = :id"
					, AppUser.class)
					.setParameter("id", Role.getOrdinal(role))
					.list();
			System.out.println(_users);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _users;
	}
}
