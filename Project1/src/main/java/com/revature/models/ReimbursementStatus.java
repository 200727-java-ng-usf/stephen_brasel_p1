package com.revature.models;

import java.util.Arrays;

/**
 * Defines different states that a <code>{@link Reimbursement}</code> could be in.
 * APPROVED
 * PENDING
 * DENIED
 */
public enum ReimbursementStatus {
	APPROVED("Approved"){}
	,PENDING("Pending"){}
	,DENIED("Denied"){}
	;

	private String type;

	ReimbursementStatus(String type){
		this.type = type;
	}


	/**
	 * Returns a <code>{@link ReimbursementStatus}</code> of the given name, PENDING if none found.
	 * @param name the <code>{@link String}</code> to check against the states.
	 * @return the <code>{@link ReimbursementStatus}</code> with the given name,
	 * 			PENDING if none found.
	 */
	public static ReimbursementStatus getByName(String name){
		return Arrays.stream(ReimbursementStatus.values())
				.filter(role -> role.type.toLowerCase().equals(name.toLowerCase()))
				.findFirst()
				.orElse(PENDING);
	}

	/**
	 * Returns the ordinal position of a given <code>{@link ReimbursementStatus}</code>.
	 * @param role the role to check the position of.
	 * @return	the ordinal position of a given <code>{@link ReimbursementStatus}</code>.
	 * 			returns 1 if none found, which is the ordinal position of <code>{@link ReimbursementStatus#PENDING}</code>.
	 */
	public static int getOrdinal(ReimbursementStatus role){
		for (int i = 0; i < ReimbursementStatus.values().length; i++) {
			if(ReimbursementStatus.values()[i] == role) return i+1;
		}
		return getOrdinal(ReimbursementStatus.PENDING);
	}

	@Override public String toString(){
		return type;
	}

}
