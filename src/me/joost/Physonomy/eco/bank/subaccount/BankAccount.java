package me.joost.Physonomy.eco.bank.subaccount;

import java.util.UUID;

public class BankAccount {

	private UUID owner;
	private String account;
	private boolean savings;
	private double balance;
	
	public BankAccount(UUID owner, String account, boolean savings, double balance) {
		this.owner = owner;
		this.account = account;
		this.savings = savings;
		this.balance = balance;
	}
	
	public UUID getUniqueId() {
		return owner;
	}
	
	public String getAccount() {
		return account;
	}
	
	public boolean isSavingAccount() {
		return savings;
	}
	
	public double getBalance() {
		return balance;
	}
}
