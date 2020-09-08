package com.revature.models;

import java.util.Arrays;

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


	public static ReimbursementType getByName(String name){
		return Arrays.stream(ReimbursementType.values())
				.filter(role -> role.type.equals(name))
				.findFirst()
				.orElse(OTHER);
	}

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
