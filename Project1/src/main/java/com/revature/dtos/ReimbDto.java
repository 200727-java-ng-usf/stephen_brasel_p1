package com.revature.dtos;

import java.util.Objects;

public class ReimbDto {
	private double amount;
	private String description;
	private String receiptURI;
	private String author;
	private String type;

	public ReimbDto() {
	}

	public ReimbDto(double amount, String description, String receiptURI, String author, String type) {
		this.amount = amount;
		this.description = description;
		this.receiptURI = receiptURI;
		this.author = author;
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReimbDto that = (ReimbDto) o;
		return Double.compare(that.amount, amount) == 0 &&
				Objects.equals(description, that.description) &&
				Objects.equals(receiptURI, that.receiptURI) &&
				Objects.equals(author, that.author) &&
				Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, description, receiptURI, author, type);
	}

	@Override
	public String toString() {
		return "ReimbursementDto{" +
				"amount=" + amount +
				", description='" + description + '\'' +
				", receiptURI='" + receiptURI + '\'' +
				", author='" + author + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
