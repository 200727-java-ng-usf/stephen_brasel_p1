package com.revature.daos;

import com.revature.models.Reimbursement;
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

public class ReimbursementDao implements CrudDao<Reimbursement> {

	private final SessionFactory sessionFactory = HibernateSessionFactory.getInstance();

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

	@Override
	public List<Reimbursement> findAll() {
		List<Reimbursement> _reimbursements = new ArrayList<>();

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Reimbursement> query = cb.createQuery(Reimbursement.class);
			Root<Reimbursement> root = query.from(Reimbursement.class);
			query.orderBy(cb.asc(root.get("id")));
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

	@Override
	public boolean update(Reimbursement reimbursement) {

		Transaction tx = null;
		try (Session session = Objects.requireNonNull(sessionFactory).openSession()) {
			tx = session.beginTransaction();
//			session.update(Reimbursement);
			String hql = "update Reimbursement rb ";
			hql += "set rb.reimbursementStatus = :reimbursementStatus, ";

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
			if(reimbursement.getResolved() != null && reimbursement.getResolved().after(reimbursement.getSubmitted())
					&& reimbursement.getResolved().before(new Timestamp(System.currentTimeMillis()))){
				System.out.println("resolved after 1970 and before current date.");
				hql += "rb.resolved = :resolved, ";
			}
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

			hql += "rb.reimbursementType = :reimbursementType ";
			hql += "where rb.id = :id ";
			Query query = session.createQuery(hql);
			query.setParameter("reimbursementStatus", reimbursement.getReimb_status_id());

			if(reimbursement.getAmount() > 0d){
				query.setParameter("amount", reimbursement.getAmount());
			}
			if(reimbursement.getSubmitted() != null
					&& reimbursement.getSubmitted().after(new Timestamp(1597648962389L)) //1970 in millis
					&& reimbursement.getSubmitted().before(new Timestamp(System.currentTimeMillis()))){
				query.setParameter("submitted", reimbursement.getSubmitted());
			}
			if(reimbursement.getResolved() != null && reimbursement.getResolved().after(reimbursement.getSubmitted())
					&& reimbursement.getResolved().before(new Timestamp(System.currentTimeMillis()))){
				query.setParameter("resolved", reimbursement.getResolved());
			}
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
