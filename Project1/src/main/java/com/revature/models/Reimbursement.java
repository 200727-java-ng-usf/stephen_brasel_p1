package com.revature.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name="revabursement.ers_reimbursements")
public class Reimbursement {
	@Id
	@Column(name = "reimb_id")
	private int id;
	@Column(name = "amount")
	private double amount;
	@Column(name = "submitted")
	private Timestamp submitted;
	@Column(name = "resolved")
	private Timestamp resolved;
	@Column(name = "description")
	private String description;
	@Column(name = "receipt")
	private String receiptURI;
	@Column(name = "author_id")
	private int authorId;
	@Column(name = "resolver_id")
	private int resolverId;
	@Column(name = "reimb_status_id")
	private ReimbursementStatus reimbursementStatus;
	@Column(name = "reimb_type_id")
	private ReimbursementType reimbursementType;

	public Reimbursement() {
	}

	public Reimbursement(double amount, Timestamp submitted, Timestamp resolved, String description, String receiptURI, int authorId, int resolverId, ReimbursementStatus reimbursementStatus, ReimbursementType reimbursementType) {
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receiptURI = receiptURI;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.reimbursementStatus = reimbursementStatus;
		this.reimbursementType = reimbursementType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp submitted) {
		this.submitted = submitted;
	}

	public Timestamp getResolved() {
		return resolved;
	}

	public void setResolved(Timestamp resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReceiptURI() {
		return receiptURI;
	}

	public void setReceiptURI(String receiptURI) {
		this.receiptURI = receiptURI;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getResolverId() {
		return resolverId;
	}

	public void setResolverId(int resolverId) {
		this.resolverId = resolverId;
	}

	public ReimbursementStatus getReimbursementStatus() {
		return reimbursementStatus;
	}

	public void setReimbursementStatus(ReimbursementStatus reimbursementStatus) {
		this.reimbursementStatus = reimbursementStatus;
	}

	public ReimbursementType getReimbursementType() {
		return reimbursementType;
	}

	public void setReimbursementType(ReimbursementType reimbursementType) {
		this.reimbursementType = reimbursementType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reimbursement that = (Reimbursement) o;
		return id == that.id &&
				Double.compare(that.amount, amount) == 0 &&
				authorId == that.authorId &&
				resolverId == that.resolverId &&
				Objects.equals(submitted, that.submitted) &&
				Objects.equals(resolved, that.resolved) &&
				Objects.equals(description, that.description) &&
				Objects.equals(receiptURI, that.receiptURI) &&
				reimbursementStatus == that.reimbursementStatus &&
				reimbursementType == that.reimbursementType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amount, submitted, resolved, description, receiptURI, authorId, resolverId, reimbursementStatus, reimbursementType);
	}

	@Override
	public String toString() {
		return "Reimbursement{" +
				"id=" + id +
				", amount=" + amount +
				", submitted=" + submitted +
				", resolved=" + resolved +
				", description='" + description + '\'' +
				", receiptURI='" + receiptURI + '\'' +
				", author_id=" + authorId +
				", resolver_id=" + resolverId +
				", reimbursementStatus=" + reimbursementStatus +
				", reimbursementType=" + reimbursementType +
				'}';
	}
}
