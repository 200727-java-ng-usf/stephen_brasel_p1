package com.revature.dtos;

import java.util.Objects;

public class IntegerDto {
	int id;

	public IntegerDto() {
	}

	public IntegerDto(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IntegerDto that = (IntegerDto) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "IntegerDto{" +
				"id=" + id +
				'}';
	}
}
