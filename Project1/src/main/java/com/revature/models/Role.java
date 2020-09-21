package com.revature.models;

import java.util.Arrays;

/**
 * Defines the different <code>{@link Role}</code>s that an <code>{@link AppUser}</code> could have, including:
 * EMPLOYEE
 * MANAGER
 * ADMIN
 * LOCKED
 */
public enum Role {
	EMPLOYEE("Employee"){}
	,MANAGER("Manager"){}
	,ADMIN("Admin"){}
	,LOCKED("Locked"){}
	;

	private String type;

	Role(String type){
		this.type = type;
	}

	/**
	 * Returns a <code>{@link Role}</code> with the given name, <code>{@link Role#LOCKED}</code> if none found.
	 * @param name the name of the <code>{@link Role}</code> to search for.
	 * @return a <code>{@link Role}</code> with the given name, <code>{@link Role#LOCKED}</code> if none found.
	 */
	public static Role getByName(String name){
//
//		for (Role role : Role.values()) {
//			if(role.roleName.equals(name)){
//				return role;
//			}
//		}
//
//		return LOCKED;

		return Arrays.stream(Role.values())
				.filter(role -> role.type.toLowerCase().equals(name.toLowerCase()))
				.findFirst()
				.orElse(LOCKED);
	}

	/**
	 * Returns the ordinal position of the given <code>{@link Role}</code>, 3 otherwise, which is the ordinal position of <code>{@link Role#LOCKED}</code>.
	 * @param role the <code>{@link Role}</code> to search for.
	 * @return the ordinal position of the given <code>{@link Role}</code>, 3 otherwise, which is the ordinal position of <code>{@link Role#LOCKED}</code>.
	 */
	public static int getOrdinal(Role role){
		for (int i = 0; i < Role.values().length; i++) {
			if(Role.values()[i] == role) return i+1;
		}
		return getOrdinal(Role.LOCKED);
	}

	@Override public String toString(){
		return type;
	}

}
