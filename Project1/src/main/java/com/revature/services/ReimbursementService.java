package com.revature.services;

import com.revature.daos.ReimbursementDao;
import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReimbursementService {
	private ReimbursementDao reimbursementDao = new ReimbursementDao();

	public ReimbursementDao getReimbursementDao() {
		return reimbursementDao;
	}

	public void setReimbursementDao(ReimbursementDao reimbursementDao) {
		this.reimbursementDao = reimbursementDao;
	}

	/**
	 * Returns all users registered with the bank database.
	 * @return a Set of <code>{@link Reimbursement}</code>s that have been registered and saved to the bank database
	 */
	public List<Reimbursement> getAllReimbursements() throws ResourceNotFoundException {

		List<Reimbursement> reimbursements = reimbursementDao.findAll();
		if(reimbursements.isEmpty()){
			throw new ResourceNotFoundException();
		}
		return reimbursements;
	}

	/**
	 * Returns the first <code>{@link Reimbursement}</code> found with the given id.
	 * @param id the int id to search by
	 * @return the first <code>{@link Reimbursement}</code> found with the given id.
	 */
	public Reimbursement getReimbursementById(int id) throws ResourceNotFoundException {
		if(id <= 0){
			throw new InvalidRequestException("The provided id cannot be less than or equal to zero.");
		}

		return reimbursementDao.findById(id)
				.orElseThrow(ResourceNotFoundException::new);
	}

	/**
	 * Returns all <code>{@link Reimbursement}</code>s found with the given author's id.
	 * @param id the int id to search by
	 * @return all <code>{@link Reimbursement}</code>s found with the given author's id.
	 */
	public List<Reimbursement> getReimbursementByAuthor(int id) throws ResourceNotFoundException {
		if(id <= 0){
			throw new InvalidRequestException("The provided id cannot be less than or equal to zero.");
		}

		List<Reimbursement> reimbursements = reimbursementDao.findByAuthor(id);
		if(reimbursements.isEmpty()){
			throw new ResourceNotFoundException();
		}
		return reimbursements;
	}

	/**
	 * Returns the first <code>{@link Reimbursement}</code> found with the given id.
	 * @param id the int id to search by
	 * @return the first <code>{@link Reimbursement}</code> found with the given id.
	 */
	public List<Reimbursement> getPendingReimbursementByAuthor(int id) throws ResourceNotFoundException {
		if(id <= 0){
			throw new InvalidRequestException("The provided id cannot be less than or equal to zero.");
		}

		List<Reimbursement> reimbursements = reimbursementDao.findPendingReimbursementsByAuthor(id);
		if(reimbursements.isEmpty()){
			throw new ResourceNotFoundException();
		}
		return reimbursements;
	}
//
//	/**
//	 * This method authenticates a <code>{@link Reimbursement}</code>  that already exists within the database.
//	 * with the given credentials with the database. If a user already exists with the credentials
//	 * or a user does not exist with the credentials or any credential is either null or empty,
//	 * then the method will not authenticate.
//	 * @param credentials the <code>{@link Credentials}</code> to authenticate.
//	 */
//	public Reimbursement authenticate(Credentials credentials) throws AuthenticationException {
//		// Validate that the provided username and password are not non-values
//		if(credentials.getUsername() == null || credentials.getUsername().trim().equals("")
//				|| credentials.getPassword() == null || credentials.getPassword().trim().equals("")){
//			throw new InvalidRequestException("Invalid credential values provided");
//		}
//		Reimbursement user = reimbursementDao.findUserByUsername(credentials.getUsername())
//				.orElseThrow(AuthenticationException::new);
//		if(!user.validatePassword(
//				credentials.getPassword(),
//				user.getPasswordHash(),
//				user.getPasswordSalt())
//				|| !user.isActive()){
//			throw new AuthenticationException();
//		}
//		return user;
//	}

	/**
	 * This method registers a new <code>{@link Reimbursement}</code> into the database.
	 * @param newReimbursement the <code>{@link Reimbursement}</code> to store/register in the database.
	 */
	public void submit(Reimbursement newReimbursement){
		//
		if(!isReimbursementValid(newReimbursement)){
			throw new InvalidRequestException("Invalid reimbursement field values provided during submission!");
		}
//		Optional<Reimbursement> existingReimbursemennt = reimbursementDao.findById(newReimbursement.getId());
//
//		if(existingReimbursemennt.isPresent()){
//			throw new AuthenticationException("Provided reimbursement id is already in use!");
//		}

		reimbursementDao.save(newReimbursement);

//		app.setCurrentUser(newUser);
	}

	/**
	 * This method updates the records of a <code>{@link Reimbursement}</code>
	 * that exists on the database with the local record.
	 * @param reimbursement the <code>{@link Reimbursement}</code> to update.
	 * @return returns true if the update was successful, false otherwise.
	 */
	public boolean updateReimbursement(Reimbursement reimbursement){
		if(isReimbursementValid(reimbursement)) {
			return reimbursementDao.update(reimbursement);
		}
		throw new InvalidRequestException("Update request was malformed.");
	}

	/**
	 * This method deletes a <code>{@link Reimbursement}</code> with the given id from
	 * the the database, along with any records only pertinent to them.
	 * @param id the id of the <code>{@link Reimbursement}</code>.
	 * @return returns true if the deletion was successful, false if otherwise.
	 * 		If there was no such user, returns true.
	 */
	public boolean deleteReimbursementById(int id){
		return reimbursementDao.deleteById(id);
	}
	/**
	 * Validates that the given <code>{@link Reimbursement}</code> and its fields are
	 * valid (not null or empty strings). Does not perform validation on id or Role fields.
	 *
	 * @param reimbursement the <code>{@link Reimbursement}</code> to validate.
	 * @return true if the <code>{@link Reimbursement}</code> is valid.
	 */
	public boolean isReimbursementValid(Reimbursement reimbursement){

		if(reimbursement == null) {
			System.out.println("Reimbursement invalid due to: reimbursement is null");
			return false;
		}
		if(reimbursement.getAmount() < 0d ) {
			System.out.println("Reimbursement invalid due to: amount less than zero");
			return false;
		}
		if(reimbursement.getDescription() == null || reimbursement.getDescription().trim().equals("")) {
			System.out.println("Reimbursement invalid due to: description is either null or empty.");
			return false;
		}
		if(reimbursement.getSubmitted() == null
				|| reimbursement.getSubmitted().before(new Timestamp(1597648962389L)) //1970 in millis
				) {
			System.out.println("Reimbursement invalid due to: time submitted is null, before 1970, or after current date.");
			return false;
		}
		if(reimbursement.getResolved() != null){
			if(reimbursement.getResolved().before(reimbursement.getSubmitted())) {
				System.out.println("Reimbursement invalid due to: time resolved: " +
						reimbursement.getResolved() +
						" is either before time submitted: " +
						reimbursement.getSubmitted() +
						", or after current date: " +
						new Timestamp(System.currentTimeMillis()));
				return false;
			}
		}
		if(reimbursement.getAuthor() == null) {
			System.out.println("Reimbursement invalid due to: author is null");
			return false;
		}
		if(reimbursement.getReimb_status_id() == null) {
			System.out.println("Reimbursement invalid due to: no reimbursement status");
			return false;
		}
		if(reimbursement.getReimb_type_id() == null) {
			System.out.println("Reimbursement invalid due to: no reimbursement type");
			return false;
		}

		return true;
	}
}
