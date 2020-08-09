package com.converter.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Autorisation {

	@Id
	private String UID;
	private String Administrator;
	private String Username;
	private String Passwords;
	private String FirstName;
	private String LastName;
	private String Telephone;
	private String Address;

	public Autorisation() {
	}

	public Autorisation(String uID, String administrator, String username, String password, String firstName,
			String lastName, String telephone, String address) {
		super();
		UID = uID;
		Administrator = administrator;
		Username = username;
		Passwords = password;
		FirstName = firstName;
		LastName = lastName;
		Telephone = telephone;
		Address = address;
	}

	public Autorisation(String uID) {
		UID = uID;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getAdministrator() {
		return Administrator;
	}

	public void setAdministrator(String administrator) {
		Administrator = administrator;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPasswords() {
		return Passwords;
	}

	public void setPasswords(String passwords) {
		Passwords = passwords;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	@Override
	public String toString() {
		return "Autorisation [UID=" + UID + ", Administrator=" + Administrator + ", Username=" + Username
				+ ", Password=" + Passwords + ", FirstName=" + FirstName + ", LastName=" + LastName + ", Telephone="
				+ Telephone + ", Address=" + Address + "]";
	}

}
