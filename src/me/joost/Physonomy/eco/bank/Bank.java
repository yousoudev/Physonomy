package me.joost.Physonomy.eco.bank;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import me.joost.Physonomy.api.exception.funds.NoBankAccountException;
import me.joost.Physonomy.api.exception.funds.NotEnoughFundsException;
import org.bukkit.scheduler.BukkitRunnable;

import me.joost.Physonomy.Physonomy;
import me.joost.Physonomy.eco.bank.subaccount.BankAccount;
import me.joost.Physonomy.util.TimeUtil;

public class Bank {
	private Physonomy api = Physonomy.getPlugin(Physonomy.class);

	private List<UUID> owner;
	private HashMap<UUID, BankAccount> accounts;
	private HashMap<UUID, Credit> creditAccounts;
	private HashMap<UUID, Double> loans;
	private String bankName;
	private double bankBalance;
	
	private double interest;
	private boolean allowSavings;
	
	private boolean allowCredit;
	private String creditTime;
	private int creditInterest;
	private double maxDebt;
	private double maxCredit;
	
	public Bank(List<UUID> owner, String bankName, HashMap<UUID, Credit> creditAccounts, double bankBalance, HashMap<UUID, BankAccount> accounts, HashMap<UUID, Double> loans, double interest, boolean allowSavings, boolean allowCredit, String creditTime, int creditInterest, double maxDebt, double maxCredit) {
		this.setOwner(owner);
		this.setBankName(bankName);
		this.setBankBalance(bankBalance);
		this.setAccounts(accounts);
		this.creditAccounts = creditAccounts;
		this.loans = loans;
		this.setInterest(interest);
		this.setAllowSavings(allowSavings);
		this.setAllowCredit(allowCredit);
		this.setCreditTime(creditTime);
		this.setCreditInterest(creditInterest);
		this.setMaxDebt(maxDebt);
		this.setMaxCredit(maxCredit);
		
		int time = TimeUtil.StringToTime(creditTime);
		if(time > 0) {

			new BukkitRunnable() {
				public void run() {
					if(!loans.isEmpty()) {
						updateLoans();
					}
				}
			}.runTaskTimerAsynchronously(api, time, time);
			
		}
	}
	
	private void updateLoans() {
		HashMap<UUID, Double> newLoans = new HashMap<UUID, Double>();
		for(Entry<UUID, Double> loan : loans.entrySet()) {
			double newAmount = loan.getValue()/100*(100+interest);
			newLoans.put(loan.getKey(), newAmount);
		}
		
		loans.clear();
		loans = newLoans;
	}

	public UUID getCredit(Credit credit){
		for(Entry<UUID, Credit> cre : creditAccounts.entrySet()){
			if(cre.getValue().equals(credit)){
				return cre.getKey();
			}
		}
		return null;
	}

	public double getBalance(UUID uuid) throws NoBankAccountException{
		if(accounts.containsKey(uuid)){
			BankAccount account = accounts.get(uuid);
			return account.getBalance();
		}else{
			throw new NoBankAccountException("User does not have a bank account with " + bankName);
		}
	}

	public double getBalanceMinusDebt(UUID uuid) throws NoBankAccountException{
		return getBalance(uuid)-getTotalDebt(uuid);
	}

	public double getTotalDebt(UUID uuid) throws NoBankAccountException{
		double total = 0;
		if(accounts.containsKey(uuid)){
			BankAccount account = accounts.get(uuid);
			if(creditAccounts.containsKey(uuid)){
				Set<Double> debt = creditAccounts.get(uuid).getDebt().keySet();

				for(Double d : debt){
					total += d;
				}
			}

			return total;
		}else{
			throw new NoBankAccountException("User does not have a bank account with " + bankName);
		}
	}

	public void withdrawFromUser(UUID uuid, double amount) throws NotEnoughFundsException, NoBankAccountException{
		if(accounts.containsKey(uuid)){
			BankAccount account = accounts.get(uuid);

			if(account.getBalance()<amount){
				throw new NotEnoughFundsException("User is " + (amount-account.getBalance()) + " short");
			}else{
				account.setBalance(account.getBalance()-amount);
			}
		}else{
			throw new NoBankAccountException("User does not have a bank account with " + bankName);
		}
	}

	public void depositToPlayer(UUID uuid, double amount) throws NoBankAccountException{
		if(accounts.containsKey(uuid)){
			BankAccount account = accounts.get(uuid);

			account.setBalance(account.getBalance()+amount);
		}else{
			throw new NoBankAccountException("User does not have a bank account with " + bankName);
		}
	}

	public List<UUID> getOwner() {
		return owner;
	}

	public void setOwner(List<UUID> owner) {
		this.owner = owner;
	}

	public HashMap<UUID, BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(HashMap<UUID, BankAccount> accounts) {
		this.accounts = accounts;
	}

	public HashMap<UUID, Double> getLoans() {
		return loans;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public boolean isAllowSavings() {
		return allowSavings;
	}

	public void setAllowSavings(boolean allowSavings) {
		this.allowSavings = allowSavings;
	}

	public boolean isAllowCredit() {
		return allowCredit;
	}

	public void setAllowCredit(boolean allowCredit) {
		this.allowCredit = allowCredit;
	}

	public String getCreditTime() {
		return creditTime;
	}

	public void setCreditTime(String creditTime) {
		this.creditTime = creditTime;
	}

	public int getCreditInterest() {
		return creditInterest;
	}

	public void setCreditInterest(int creditInterest) {
		this.creditInterest = creditInterest;
	}
	
	public boolean hasLoan(UUID uuid) {
		return loans.containsKey(uuid);
	}
	
	public void addLoan(UUID uuid, double amount) {
		bankBalance -= amount;
		double am = amount;
		if(hasLoan(uuid)) {
			am += loans.get(uuid);
		}
		
		loans.put(uuid, am);
	}
	
	public void loanPayoff(UUID uuid, double amount) {
		if(hasLoan(uuid)) {
			double am = loans.get(uuid);
			am-=amount;
			if(am<0) {bankBalance+=am;}else {bankBalance+=amount;}
			
			if(am>0) {
				loans.put(uuid, am);
			}else {
				loans.remove(uuid);
			}
		}
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public double getMaxDebt() {
		return maxDebt;
	}

	public void setMaxDebt(double maxDebt) {
		this.maxDebt = maxDebt;
	}

	public double getBankBalance() {
		return bankBalance;
	}

	public void setBankBalance(double bankBalance) {
		this.bankBalance = bankBalance;
	}

	public double getMaxCredit(){
		return maxCredit;
	}

	public void setMaxCredit(double maxCredit){
		this.maxCredit = maxCredit;
	}

	public HashMap<UUID, Credit> getCreditAccounts() {
		return creditAccounts;
	}

	public void setCreditAccounts(HashMap<UUID, Credit> creditAccounts) {
		this.creditAccounts = creditAccounts;
	}
}
