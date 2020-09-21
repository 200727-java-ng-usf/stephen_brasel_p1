package com.revature.models;

import java.util.Arrays;

/**
 *  Defines different types that a <code>{@link Reimbursement}</code> could have.
 *  LODGING
 *  TRAVEL
 *  FOOD
 *  OTHER
 */
public enum ReimbursementType {
	LODGING("Lodging"){}
	,TRAVEL("Travel"){}
	,FOOD("Food"){}
	,OTHER("Other"){}
	;

	private String type;

	ReimbursementType(String type){
		this.type = type;
	}

	/**
	 * Returns a <code>{@link ReimbursementType}</code> with the given name, <code>{@link ReimbursementType#OTHER}</code> if none found.
	 * @param name the type to search for.
	 * @return a <code>{@link ReimbursementType}</code> with the given name, <code>{@link ReimbursementType#OTHER}</code> if none found.
	 */
	public static ReimbursementType getByName(String name){
		return Arrays.stream(ReimbursementType.values())
				.filter(role -> role.type.toLowerCase().equals(name.toLowerCase()))
				.findFirst()
				.orElse(OTHER);
	}

	/**
	 * Returns the ordinal number of the given <code>{@link ReimbursementType}</code>, 3 otherwise, which is the ordinal number for <code>{@link ReimbursementType#OTHER}</code>.
	 * @param role the <code>{@link ReimbursementType}</code> to search for.
	 * @return the ordinal number of the given <code>{@link ReimbursementType}</code>, 3 otherwise, which is the ordinal number for <code>{@link ReimbursementType#OTHER}</code>.
	 */
	public static int getOrdinal(ReimbursementType role){
		for (int i = 0; i < ReimbursementType.values().length; i++) {
			if(ReimbursementType.values()[i] == role) return i+1;
		}
		return getOrdinal(ReimbursementType.OTHER);
	}

	@Override public String toString(){
		return type;
	}

}
