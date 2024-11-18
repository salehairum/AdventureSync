package accountAndPersonModels;

public class Account {
	private int accountID;
	private String username;
	private String password;
	private String email;
	private float balance;
	
	public Account(int accountID, String username, String password, String email, float balance) {
	    this.accountID = accountID;
	    this.username = username;
	    this.password = password;
	    this.email = email;
	    this.balance = balance;
	}
	
	//getters and setters
	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
}
