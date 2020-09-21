package com.revature.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.revature.models.AppUser;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * The Data Transfer Object for <code>{@link Reimbursement}</code>s, including:
 * reimbursement id
 * amount
 * timestamp submitted
 * timestamp resolved
 * description
 * receipt URI
 * author
 * resolver
 * reimbursement status
 * reimbursement type
 */
public class ReimbursementDto {
	private int id;
	private double amount;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.sss")
	private Timestamp submitted;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.sss")
	private Timestamp resolved;
	private String description;
	private String receiptURI;
	private String author;
	private String resolver;
	private String reimb_status_id;
	private String reimb_type_id;

	public ReimbursementDto() {
	}

	public ReimbursementDto(int id, double amount, Timestamp submitted, Timestamp resolved, String description, String receiptURI, String author, String resolver, String reimb_status_id, String reimb_type_id) {
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receiptURI = receiptURI;
		this.author = author;
		this.resolver = resolver;
		this.reimb_status_id = reimb_status_id;
		this.reimb_type_id = reimb_type_id;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getResolver() {
		return resolver;
	}

	public void setResolver(String resolver) {
		this.resolver = resolver;
	}

	public String getReimb_status_id() {
		return reimb_status_id;
	}

	public void setReimb_status_id(String reimb_status_id) {
		this.reimb_status_id = reimb_status_id;
	}

	public String getReimb_type_id() {
		return reimb_type_id;
	}

	public void setReimb_type_id(String reimb_type_id) {
		this.reimb_type_id = reimb_type_id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReimbursementDto that = (ReimbursementDto) o;
		return id == that.id &&
				Double.compare(that.amount, amount) == 0 &&
				Objects.equals(submitted, that.submitted) &&
				Objects.equals(resolved, that.resolved) &&
				Objects.equals(description, that.description) &&
				Objects.equals(receiptURI, that.receiptURI) &&
				Objects.equals(author, that.author) &&
				Objects.equals(resolver, that.resolver) &&
				Objects.equals(reimb_status_id, that.reimb_status_id) &&
				Objects.equals(reimb_type_id, that.reimb_type_id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amount, submitted, resolved, description, receiptURI, author, resolver, reimb_status_id, reimb_type_id);
	}

	@Override
	public String toString() {
		return "ReimbursementDto{" +
				"id=" + id +
				", amount=" + amount +
				", submitted=" + submitted +
				", resolved=" + resolved +
				", description='" + description + '\'' +
				", receiptURI='" + receiptURI + '\'' +
				", author='" + author + '\'' +
				", resolver='" + resolver + '\'' +
				", reimb_status_id='" + reimb_status_id + '\'' +
				", reimb_type_id='" + reimb_type_id + '\'' +
				'}';
	}
}
