package de.lehsten.casa.contextserver.types.entities.person.identity;

import de.lehsten.casa.contextserver.types.entities.person.Person;

/**
 * @author phil
 * 
 */
public class Identity extends Person{
	
	private String identityDomain;
	private String identityUserName;
	private Person user;

	/**
	 * @return IdentityDomain as {@link String}
	 */
	public String getIdentityDomain() {
		return identityDomain;
	}

	/**Sets the IdentityDomain of an {@link Identity}
	 * @param identityDomain
	 */
	public void setIdentityDomain(String identityDomain) {
		this.identityDomain = identityDomain;
		this.getProperties().put("IdentityDomain", identityDomain);
	}

	/**
	 * @return user as {@link Person} of the Identity
	 */
	public Person getUser() {
		return user;
	}

	/** 
	 * Sets the user {@link Person} of a specified {@link Identity}. 
	 * One user can have multiple Identities but an Identity can only 
	 * have one user.
	 * @author phil
	 * @param user
	 */
	public void setUser(Person user) {
		this.user = user;
		this.getProperties().put("User", this.user.toString());
	}

	/**
	 * @return IdentityUserName for the Identity
	 */
	public String getIdentityUserName() {
		return identityUserName;
	}

	/**
	 * Sets the IdentityUserName for a specified Identity
	 * @param identityUserName
	 */
	public void setIdentityUserName(String identityUserName) {
		this.identityUserName = identityUserName;
		this.getProperties().put("IdentityUserName", identityUserName);
	} 

}
