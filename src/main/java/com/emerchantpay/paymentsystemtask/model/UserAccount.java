package com.emerchantpay.paymentsystemtask.model;


import jakarta.persistence.*;

@Entity
@Table(name="USER_ACCOUNT")
public class UserAccount {
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "S_USER_ACCOUNT", sequenceName="S_USER_ACCOUNT", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="S_USER_ACCOUNT")
	private Long identity;
	
	@Column(name="USER_NAME")
	private String userName;
	
	private String email;
	private String role;
    private String password;
    
    public UserAccount() {}

	public UserAccount(String userName, String email, String role, String password) {
		this.userName = userName;
		this.email = email;
		this.role = role;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getIdentity() {
		return identity;
	}
	
}
