package com.revature.models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name="revabursement.ers_reimbursements")
public class Reimbursement {
	@Id
	@Column(name = "reimb_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private AppUser author;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resolver_id")
	private AppUser resolver;
//	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reimb_status_id")
	@Enumerated(EnumType.STRING)
	private ReimbursementStatus reimbursementStatus;
//	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reimb_type_id")
	@Enumerated(EnumType.STRING)
	private ReimbursementType reimbursementType;

	public Reimbursement() {
	}

	public Reimbursement(double amount, Timestamp submitted, Timestamp resolved, String description, String receiptURI, AppUser author, AppUser resolver, ReimbursementStatus reimbursementStatus, ReimbursementType reimbursementType) {
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receiptURI = receiptURI;
		this.author = author;
		this.resolver = resolver;
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

	public AppUser getAuthor() {
		return author;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}

	public AppUser getResolver() {
		return resolver;
	}

	public void setResolver(AppUser resolver) {
		this.resolver = resolver;
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
				Objects.equals(submitted, that.submitted) &&
				Objects.equals(resolved, that.resolved) &&
				Objects.equals(description, that.description) &&
				Objects.equals(receiptURI, that.receiptURI) &&
				Objects.equals(author, that.author) &&
				Objects.equals(resolver, that.resolver) &&
				reimbursementStatus == that.reimbursementStatus &&
				reimbursementType == that.reimbursementType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amount, submitted, resolved, description, receiptURI, author, resolver, reimbursementStatus, reimbursementType);
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
				", author=" + author +
				", resolver=" + resolver +
				", reimbursementStatus=" + reimbursementStatus +
				", reimbursementType=" + reimbursementType +
				'}';
	}
}
