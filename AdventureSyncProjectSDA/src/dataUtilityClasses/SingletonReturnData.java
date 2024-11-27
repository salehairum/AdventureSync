package dataUtilityClasses;

import java.time.LocalDate;

import accountAndPersonModels.Account;

public class SingletonReturnData {
	private int id;
	private String name;
	private LocalDate dob;
	private String cnic;
	private Account acc;
	
	public SingletonReturnData(int id, String n, LocalDate d, String c) {
		this.id=id;
		name=n;
		dob=d;
		cnic=c;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Account getAcc() {
		return acc;
	}

	public void setAcc(Account acc) {
		this.acc = acc;
	}
	
}
