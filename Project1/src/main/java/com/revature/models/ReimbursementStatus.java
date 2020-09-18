package com.revature.models;

import java.util.Arrays;

public enum ReimbursementStatus {
	APPROVED("Approved"){}
	,PENDING("Pending"){}
	,DENIED("Denied"){}
	;

	private String type;

	ReimbursementStatus(String type){
		this.type = type;
	}


	public static ReimbursementStatus getByName(String name){
		return Arrays.stream(ReimbursementStatus.values())
				.filter(role -> role.type.toLowerCase().equals(name.toLowerCase()))
				.findFirst()
				.orElse(PENDING);
	}

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
