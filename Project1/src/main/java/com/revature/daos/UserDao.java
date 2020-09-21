package com.revature.daos;

import com.revature.models.AppUser;
import com.revature.models.Reimbursement;
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

/**
 * The <code>{@link AppUser}</code> Data Access Object
 * Implements <code>{@link CrudDao}</code><<code>{@link AppUser}</code>>
 */
public class UserDao implements CrudDao<AppUser> {

	/**
	 * The <code>{@link HibernateSessionFactory}</code> sessionFactory instance.
	 */
	private final SessionFactory sessionFactory = HibernateSessionFactory.getInstance();

	/**
	 * Saves the given <code>{@link AppUser}</code> to the repository.
	 * @param appUser the <code>{@link AppUser}</code> to save to the repository
	 */
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

	/**
	 * Returns all <code>{@link AppUser}</code>s in the repository in an <code>{@link ArrayList}</code><<code>{@link AppUser}</code>>
	 * @return an <code>{@link ArrayList}</code><<code>{@link AppUser}</code>> of all <code>{@link AppUser}</code>s in the repository.
	 */
	@Override
	public List<AppUser> findAll() {
		List<AppUser> _users = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			Root<AppUser> root = query.from(AppUser.class);
			query.orderBy(cb.asc(root.get("id")));
			_users = session.createQuery(query).list();
			System.out.println(_users);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _users;
	}

	/**
	 * Returns an <code>{@link AppUser}</code>s with the given id.
	 * @param id the id associated with the <code>{@link AppUser}</code>.
	 * @return an <code>{@link AppUser}</code>s with the given id.
	 * 			If no <code>{@link AppUser}</code> is found, returns null.
	 */
	@Override
	public Optional<AppUser> findById(int id) {
		return findUserById(id);
	}

	/**
	 * Returns true if a successful update occurs.
	 * @param appUser the <code>{@link AppUser}</code> to update.
	 * @return true if update was successful.
	 */
	@Override
	public boolean update(AppUser appUser) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
//			session.update(appUser);
			String hql = "update AppUser au ";
			hql += "set au.active = :active, ";

			// id
			// active
			// firstName
			// lastName
			// username
			// passwordHash
			// passwordSalt
			// email
			// role

			if(appUser.getFirstName() != null && !appUser.getFirstName().trim().equals("")){
				System.out.println("firstname present.");
				hql += "au.firstName = :firstName, ";
			}
			if(appUser.getLastName() != null && !appUser.getLastName().trim().equals("")){
				System.out.println("lastname present.");
				hql += "au.lastName = :lastName, ";
			}
			if(appUser.getUsername() != null && !appUser.getUsername().trim().equals("")){
				System.out.println("username present.");
				hql += "au.username = :username, ";
			}
			if(appUser.getPasswordHash() != null && appUser.getPasswordHash().length > 0){
				System.out.println("passwordHash present.");
				hql += "au.passwordHash = :passwordHash, ";
			}
			if(appUser.getPasswordSalt() != null && appUser.getPasswordSalt().length > 0){
				System.out.println("passwordSalt present.");
				hql += "au.passwordSalt = :passwordSalt, ";
			}
			if(appUser.getEmail() != null && !appUser.getEmail().trim().equals("")){
				System.out.println("email present.");
				hql += "au.email = :email, ";
			}
			hql += "au.role = :role ";
			hql += "where au.id = :id ";
			Query query = session.createQuery(hql);
			query.setParameter("active", appUser.isActive());

			if(appUser.getFirstName() != null && !appUser.getFirstName().trim().equals("")) {
				query.setParameter("firstName", appUser.getFirstName());
			}
			if(appUser.getLastName() != null && !appUser.getLastName().trim().equals("")) {
				query.setParameter("lastName", appUser.getLastName());
			}
			if(appUser.getUsername() != null && !appUser.getUsername().trim().equals("")) {
				query.setParameter("username", appUser.getUsername());
			}
			if(appUser.getPasswordHash() != null && appUser.getPasswordHash().length > 0) {
				query.setParameter("passwordHash", appUser.getPasswordHash());
			}
			if(appUser.getPasswordSalt() != null && appUser.getPasswordSalt().length > 0) {
				query.setParameter("passwordSalt", appUser.getPasswordSalt());
			}
			if(appUser.getEmail() != null && !appUser.getEmail().trim().equals("")) {
				query.setParameter("email", appUser.getEmail());
			}

			query.setParameter("role", appUser.getRole());
			query.setParameter("id", appUser.getId());
			int result = query.executeUpdate();
			if(result <= 0) return false;
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}

	/**
	 * Deletes an <code>{@link AppUser}</code> by the <code>{@link AppUser}</code>s id.
	 * @param id the id of the <code>{@link AppUser}</code> to delete.
	 * @return true if the deletion was successful.
	 */
	@Override
	public boolean deleteById(int id) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("delete AppUser au where au.id = :id ")
					.setParameter("id", id);
			int result = query.executeUpdate();
			if(result <= 0) return false;
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}



	/**
	 * Deletes an <code>{@link AppUser}</code> by the <code>{@link AppUser}</code>s username.
	 * @param username the username of the <code>{@link AppUser}</code> to delete.
	 * @return true if the deletion was successful.
	 */
	public boolean deleteByUsername(String username) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("delete AppUser au where au.username = :username")
					.setParameter("username", username);
			int result = query.executeUpdate();
			if(result <= 0) return false;
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}

	/**
	 * Deactivates an <code>{@link AppUser}</code> by the <code>{@link AppUser}</code>s id.
	 * @param id the id of the <code>{@link AppUser}</code> to deactivate.
	 * @return true if the deactivation was successful.
	 */
	public boolean deactivateById(int id) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("update AppUser au " +
					"set au.active = :false " +
					"where au.id = :id")
					.setParameter("false", false)
					.setParameter("id", id);
			int result = query.executeUpdate();
			if(result <= 0) return false;
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}

	/**
	 * Deactivates an <code>{@link AppUser}</code> by the <code>{@link AppUser}</code>s username.
	 * @param username the username of the <code>{@link AppUser}</code> to deactivate.
	 * @return true if the deactivation was successful.
	 */
	public boolean deactivateByUsername(String username) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("update AppUser au " +
					"set au.active = :false " +
					"where au.username = :username")
					.setParameter("false", false)
					.setParameter("username", username);
			int result = query.executeUpdate();
			if(result <= 0) return false;
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}

	/**
	 * Returns an <code>{@link Optional}</code><<code>{@link AppUser}</code>> with the given username.
	 * @param userName the username associated with the desired <code>{@link AppUser}</code>
	 * @return an <code>{@link Optional}</code><<code>{@link AppUser}</code>> with the given username.
	 * 			If none match the username, an <code>{@link Optional}</code>.empty() is returned.
	 */
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

	/**
	 * Returns an <code>{@link Optional}</code><<code>{@link AppUser}</code>> with the given email.
	 * @param email the email associated with the desired <code>{@link AppUser}</code>
	 * @return an <code>{@link Optional}</code><<code>{@link AppUser}</code>> with the given email.
	 * 			If none match the email, an <code>{@link Optional}</code>.empty() is returned.
	 */
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

	/**
	 * Returns an <code>{@link Optional}</code><<code>{@link AppUser}</code>> with the given id.
	 * @param id the id associated with the desired <code>{@link AppUser}</code>
	 * @return an <code>{@link Optional}</code><<code>{@link AppUser}</code>> with the given id.
	 * 			If none match the id, an <code>{@link Optional}</code>.empty() is returned.
	 */
	public Optional<AppUser> findUserById(int id) {
		Optional<AppUser> _user = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AppUser> query = cb.createQuery(AppUser.class);
			Root<AppUser> root = query.from(AppUser.class);
			System.out.println(id);
			query.select(root).where(cb.equal(root.get("id"), id));
			_user = Optional.of(session.createQuery(query).getSingleResult());
			System.out.println(_user);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return  _user;
	}

	/**
	 * Returns an <code>{@link ArrayList}</code><<code>{@link AppUser}</code>> with the given <code>{@link Role}</code>.
	 * @param role the <code>{@link Role}</code> associated with the desired <code>{@link AppUser}</code>
	 * @return an <code>{@link Optional}</code><<code>{@link AppUser}</code>> with the given <code>{@link Role}</code>.
	 * 			If none match the <code>{@link Role}</code>, an <code>{@link Optional}</code>.empty() is returned.
	 */
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

	/**
	 * Reactivates the <code>{@link AppUser}</code> with the given id.
	 * @param id the id of the reactivated <code>{@link AppUser}</code>
	 * @return true if the reactivation was successful.
	 */
	public boolean reactivateById(int id) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("update AppUser au " +
					"set au.active = :true " +
					"where au.id = :id")
					.setParameter("true", true)
					.setParameter("id", id);
			int result = query.executeUpdate();
			if(result <= 0) return false;
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
			return false;
		}
		return true;
	}
}
