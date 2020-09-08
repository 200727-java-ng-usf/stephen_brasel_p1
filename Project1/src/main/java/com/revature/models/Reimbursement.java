package com.revature.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Reimbursement {
	private int id;
	private double amount;
	private Timestamp submitted;
	private Timestamp resolved;
	private String description;
	private String receiptURI;
	private int author_id;
	private int resolver_id;
	private ReimbursementStatus reimbursementStatus;
	private ReimbursementType reimbursementType;

	public Reimbursement() {
	}

	public Reimbursement(double amount, Timestamp submitted, Timestamp resolved, String description, String receiptURI, int author_id, int resolver_id, ReimbursementStatus reimbursementStatus, ReimbursementType reimbursementType) {
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receiptURI = receiptURI;
		this.author_id = author_id;
		this.resolver_id = resolver_id;
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

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public int getResolver_id() {
		return resolver_id;
	}

	public void setResolver_id(int resolver_id) {
		this.resolver_id = resolver_id;
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
				author_id == that.author_id &&
				resolver_id == that.resolver_id &&
				Objects.equals(submitted, that.submitted) &&
				Objects.equals(resolved, that.resolved) &&
				Objects.equals(description, that.description) &&
				Objects.equals(receiptURI, that.receiptURI) &&
				reimbursementStatus == that.reimbursementStatus &&
				reimbursementType == that.reimbursementType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amount, submitted, resolved, description, receiptURI, author_id, resolver_id, reimbursementStatus, reimbursementType);
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
				", author_id=" + author_id +
				", resolver_id=" + resolver_id +
				", reimbursementStatus=" + reimbursementStatus +
				", reimbursementType=" + reimbursementType +
				'}';
	}
}
