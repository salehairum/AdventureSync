package accountAndPersonModels;

import java.time.LocalDate;

public class Person {
	private String name;
	private LocalDate dob;
	private String cnic;
	private Account account;
	
	public Person()
	{
		name="";
		dob = null;
		cnic = "";
		account = null;
	}
	//constructor without assigning any account
	public Person(String name, LocalDate dob, String cnic)
	{
		this.name = name;
		this.dob = dob;
		this.cnic = cnic;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public String getCnic() {
		return cnic;
	}
	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
