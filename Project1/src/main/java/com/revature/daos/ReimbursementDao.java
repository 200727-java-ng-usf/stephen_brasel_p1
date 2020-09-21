package com.revature.daos;

import com.revature.models.AppUser;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.*;

/**
 * The <code>{@link Reimbursement}</code> Data Access Object
 * Implements <code>{@link CrudDao}</code><<code>{@link Reimbursement}</code>>
 */
public class ReimbursementDao implements CrudDao<Reimbursement> {

	/**
	 * The <code>{@link HibernateSessionFactory}</code> sessionFactory instance.
	 */
	private final SessionFactory sessionFactory = HibernateSessionFactory.getInstance();

	/**
	 * Saves a <code>{@link Reimbursement}</code> to the repository.
	 * @param reimbursement the <code>{@link Reimbursement}</code> to save
	 */
	@Override
	public void save(Reimbursement reimbursement) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			session.save(reimbursement);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			if(tx != null) tx.rollback();
		}

	}

	/**
	 * Returns all reimbursements in the repository in an <code>{@link ArrayList}</code><<code>{@link Reimbursement}</code>>
	 * @return an <code>{@link ArrayList}</code><<code>{@link Reimbursement}</code>> of all <code>{@link Reimbursement}</code>s in the repository.
	 */
	@Override
	public List<Reimbursement> findAll() {
		List<Reimbursement> _reimbursements = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Reimbursement> query = cb.createQuery(Reimbursement.class);
			Root<Reimbursement> root = query.from(Reimbursement.class);
			query.orderBy(cb.asc(root.get("submitted")));
			_reimbursements = session.createQuery(query).list();
			System.out.println(_reimbursements);
			tx.commit();
		} catch (NoResultException nre) {
			System.out.println("This is here so that Hibernate doesn't bypass the try/catch system.");
			nre.printStackTrace();
			if (tx != null) tx.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _reimbursements;
	}

	/**
	 * Returns all <code>{@link Reimbursement}</code>s by the same <code>{@link AppUser}</code> Author.
	 * @param id the id associated with the <code>{@link AppUser}</code> Author.
	 * @return an <code>{@link ArrayList}</code><<code>{@link Reimbursement}</code>> of all <code>{@link Reimbursement}</code>s by the <code>{@link AppUser}</code> Author.
	 */
	public List<Reimbursement> findByAuthor(int id) {
		List<Reimbursement> _reimbursements = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Reimbursement> query = cb.createQuery(Reimbursement.class);
			Root<Reimbursement> root = query.from(Reimbursement.class);
			query.orderBy(cb.asc(root.get("submitted")));
			query.select(root).where(cb.equal(root.get("author"), id));
			_reimbursements = session.createQuery(query).list();
			System.out.println(_reimbursements);
			tx.commit();
		} catch (NoResultException nre) {
			System.out.println("This is here so that Hibernate doesn't bypass the try/catch system.");
			nre.printStackTrace();
			if (tx != null) tx.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _reimbursements;
	}

	/**
	 * Returns all <code>{@link ReimbursementStatus#PENDING}</code> <code>{@link Reimbursement}</code>s by the same <code>{@link AppUser}</code> Author.
	 * @param id the id associated with the <code>{@link AppUser}</code> Author.
	 * @return an <code>{@link ArrayList}</code><<code>{@link Reimbursement}</code>> of all <code>{@link ReimbursementStatus#PENDING}</code> <code>{@link Reimbursement}</code>s by the <code>{@link AppUser}</code> Author.
	 */
	public List<Reimbursement> findPendingReimbursementsByAuthor(int id) {
		List<Reimbursement> _reimbursements = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Reimbursement> query = cb.createQuery(Reimbursement.class);
			Root<Reimbursement> root = query.from(Reimbursement.class);
			query.orderBy(cb.asc(root.get("submitted")));
			query.select(root).where(cb.equal(root.get("author"), id));
			query.select(root).where(cb.equal(root.get("reimb_status_id"), ReimbursementStatus.PENDING));
			_reimbursements = session.createQuery(query).list();
			System.out.println(_reimbursements);
			tx.commit();
		} catch (NoResultException nre) {
			System.out.println("This is here so that Hibernate doesn't bypass the try/catch system.");
			nre.printStackTrace();
			if (tx != null) tx.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return _reimbursements;
	}

	/**
	 * Returns a <code>{@link Reimbursement}</code>s with the given id.
	 * @param id the id associated with the <code>{@link Reimbursement}</code>.
	 * @return a <code>{@link Reimbursement}</code>s with the given id.
	 * 			If no <code>{@link Reimbursement}</code> is found, returns null.
	 */
	@Override
	public Optional<Reimbursement> findById(int id) {
		Optional<Reimbursement> _reimbursement = Optional.empty();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Reimbursement> query = cb.createQuery(Reimbursement.class);
			Root<Reimbursement> root = query.from(Reimbursement.class);
			System.out.println(id);
			query.select(root).where(cb.equal(root.get("id"), id));
			_reimbursement = Optional.of(session.createQuery(query).getSingleResult());
			System.out.println(_reimbursement);
			tx.commit();
		} catch (NoResultException nre) {
			System.out.println("This is here so that Hibernate doesn't bypass the try/catch system.");
			nre.printStackTrace();
			if (tx != null) tx.rollback();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) tx.rollback();
		}
		return  _reimbursement;
	}

	/**
	 * Returns true if a successful update occurs.
	 * @param reimbursement the <code>{@link Reimbursement}</code> to update.
	 * @return true if update was successful.
	 */
	@Override
	public boolean update(Reimbursement reimbursement) {

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
//			session.update(Reimbursement);
			String hql = "update Reimbursement rb ";
			hql += "set rb.reimb_status_id = :reimbursementStatus, ";

			// author
			// resolver

			if(reimbursement.getAmount() > 0d){
				System.out.println("amount above zero.");
				hql += "rb.amount = :amount, ";
			}
			if(reimbursement.getSubmitted() != null
					&& reimbursement.getSubmitted().after(new Timestamp(1597648962389L)) //1970 in millis
					&& reimbursement.getSubmitted().before(new Timestamp(System.currentTimeMillis()))){
				System.out.println("submitted after 1970 and before current date.");
				hql += "rb.submitted = :submitted, ";
			}
			hql += "rb.resolved = :resolved, ";

			if(reimbursement.getDescription() != null && !reimbursement.getDescription().trim().equals("")){
				System.out.println("description present.");
				hql += "rb.description = :description, ";
			}
			if(reimbursement.getReceiptURI() != null && !reimbursement.getReceiptURI().trim().equals("")){
				System.out.println("receipt present.");
				hql += "rb.receiptURI = :receiptURI, ";
			}
			if(reimbursement.getAuthor() != null){
				System.out.println("author present.");
				hql += "rb.author = :author, ";
			}
			if(reimbursement.getResolver() != null){
				System.out.println("resolver present.");
				hql += "rb.resolver = :resolver, ";
			}

			hql += "rb.reimb_type_id = :reimbursementType ";
			hql += "where rb.id = :id ";
			Query query = session.createQuery(hql);
			query.setParameter("reimbursementStatus", reimbursement.getReimb_status_id());

			if(reimbursement.getAmount() > 0d){
				query.setParameter("amount", reimbursement.getAmount());
			}
			if(reimbursement.getSubmitted() != null
					&& reimbursement.getSubmitted().after(new Timestamp(1597648962389L)) //1970 in millis
			){
				query.setParameter("submitted", reimbursement.getSubmitted());
			}
			query.setParameter("resolved", reimbursement.getResolved());

			if(reimbursement.getDescription() != null && !reimbursement.getDescription().trim().equals("")){
				query.setParameter("description", reimbursement.getDescription());
			}
			if(reimbursement.getReceiptURI() != null && !reimbursement.getReceiptURI().trim().equals("")){
				query.setParameter("receiptURI", reimbursement.getReceiptURI());
			}
			if(reimbursement.getAuthor() != null){
				query.setParameter("author", reimbursement.getAuthor());
			}
			if(reimbursement.getResolver() != null){
				query.setParameter("resolver", reimbursement.getResolver());
			}

			query.setParameter("reimbursementType", reimbursement.getReimb_type_id());
			query.setParameter("id", reimbursement.getId());
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
	 * Deletes a <code>{@link Reimbursement}</code> by the <code>{@link Reimbursement}</code>s id.
	 * @param id the id of the <code>{@link Reimbursement}</code> to delete.
	 * @return true if the deletion was successful.
	 */
	@Override
	public boolean deleteById(int id) {
		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			Query query = session.createQuery("delete Reimbursement rb where rb.id = :id ")
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
