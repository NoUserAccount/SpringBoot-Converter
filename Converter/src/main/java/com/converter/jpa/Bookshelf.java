package com.converter.jpa;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bookshelf {

	@Id
	private String BID;
	private String UID;
	private String BookTitle;
	private String AuthorLastName;
	private String AuthorFirstName;
	private String BookGenre;
	private String IssuedDate;
	private String Period;
	private String FINE;

	public Bookshelf() {
	};

	public Bookshelf(String bID, String bookTitle, String authorLastName, String issuedDate) {
		super();
		BID = bID;
		BookTitle = bookTitle;
		AuthorLastName = authorLastName;
		IssuedDate = issuedDate;
	}

	public Bookshelf(String bID, String uID, String bookTitle, String authorLastName, String authorFirstName,
			String bookGenre, String issuedDate, String period, String fINE) {
		super();
		BID = bID;
		UID = uID;
		BookTitle = bookTitle;
		AuthorLastName = authorLastName;
		AuthorFirstName = authorFirstName;
		BookGenre = bookGenre;
		IssuedDate = issuedDate;
		Period = period;
		FINE = fINE;
	}

	public Bookshelf(String bID, String title, String writerLast, String writerFirst, String genre, String issuedDate) {
		BID = bID;
		BookTitle = title;
		AuthorLastName = writerLast;
		AuthorFirstName = writerFirst;
		BookGenre = genre;
		IssuedDate = issuedDate;
	}

	public String getBID() {
		return BID;
	}

	public void setBID(String bID) {
		BID = bID;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getBookTitle() {
		return BookTitle;
	}

	public void setBookTitle(String bookTitle) {
		BookTitle = bookTitle;
	}

	public String getAuthorLastName() {
		return AuthorLastName;
	}

	public void setAuthorLastName(String authorLastName) {
		AuthorLastName = authorLastName;
	}

	public String getAuthorFirstName() {
		return AuthorFirstName;
	}

	public void setAuthorFirstName(String authorFirstName) {
		AuthorFirstName = authorFirstName;
	}

	public String getBookGenre() {
		return BookGenre;
	}

	public void setBookGenre(String bookGenre) {
		BookGenre = bookGenre;
	}

	public String getIssuedDate() {
		return IssuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		IssuedDate = issuedDate;
	}

	public String getPeriod() {
		return Period;
	}

	public void setPeriod(String period) {
		Period = period;
	}

	public String getFINE() {
		return FINE;
	}

	public void setFINE(String fINE) {
		FINE = fINE;
	}

	@Override
	public String toString() {
		return "Bookshelf [BID=" + BID + ", UID=" + UID + ", BookTitle=" + BookTitle + ", AuthorLastName="
				+ AuthorLastName + ", AuthorFirstName=" + AuthorFirstName + ", BookGenre=" + BookGenre + ", IssuedDate="
				+ IssuedDate + ", Period=" + Period + ", FINE=" + FINE + "]";
	}

}
