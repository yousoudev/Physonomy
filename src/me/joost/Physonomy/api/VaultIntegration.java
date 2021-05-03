package me.joost.Physonomy.api;

import java.util.List;
import java.util.UUID;

import me.joost.Physonomy.Physonomy;
import me.joost.Physonomy.api.exception.funds.NoBankAccountException;
import me.joost.Physonomy.api.exception.funds.NotEnoughFundsException;
import me.joost.Physonomy.eco.bank.Bank;
import me.joost.Physonomy.eco.bank.BankManager;
import org.bukkit.OfflinePlayer;

import me.joost.Physonomy.data.Configuration;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultIntegration implements Economy{

	private BankManager bm = Physonomy.getPlugin(Physonomy.class).banks;
	private UUID fromString(String player){
		return bm.getFromString(player);
	}

	@Override
	public EconomyResponse bankBalance(String player) {
		UUID u = fromString(player);
		if(u==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist"); }
		Bank b = bm.getBankFor(u);
		if(b==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have a bankaccount"); }
		try {
			return new EconomyResponse(0, b.getBalance(u), EconomyResponse.ResponseType.SUCCESS, "");
		} catch (NoBankAccountException e) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have a bankaccount");
		}
	}

	@Override
	public EconomyResponse bankDeposit(String player, double amount) {
		UUID u = fromString(player);
		if(u==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist"); }
		Bank b = bm.getBankFor(u);
		if(b==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have a bankaccount"); }
		try {
			b.depositToPlayer(u, amount);
			return new EconomyResponse(amount, b.getBalance(u), EconomyResponse.ResponseType.SUCCESS, "");
		} catch (NoBankAccountException e) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have a bankaccount");
		}
	}

	@Override
	public EconomyResponse bankHas(String player, double amount) {
		UUID u = fromString(player);
		if(u==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist"); }
		Bank b = bm.getBankFor(u);
		if(b==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have a bankaccount"); }
		try {
			return new EconomyResponse(amount, b.getBalance(u), EconomyResponse.ResponseType.SUCCESS, "");
		} catch (NoBankAccountException e) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "");
		}
	}

	@Override
	public EconomyResponse bankWithdraw(String player, double amount) {
		UUID u = fromString(player);
		if(u==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist"); }
		Bank b = bm.getBankFor(u);
		if(b==null) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have a bankaccount"); }
		try {
			b.withdrawFromUser(u, amount);
			return new EconomyResponse(amount, b.getBalance(u), EconomyResponse.ResponseType.SUCCESS, "");
		} catch (NotEnoughFundsException e) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have enough funds");
		} catch (NoBankAccountException e) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have a bankaccount");
		}
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createPlayerAccount(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String currencyNamePlural() {
		return Configuration.CURRENCY_NAME_PLURAL;
	}

	@Override
	public String currencyNameSingular() {
		return Configuration.CURRENCY_NAME_SINGULAR;
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String format(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int fractionalDigits() {
		return 2;
	}

	@Override
	public double getBalance(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getBalance(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getBalance(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getBalance(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getBanks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean has(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean has(OfflinePlayer arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean has(String arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasBankSupport() {
		return true;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		return Configuration.INTEGRATE_TO_VAULT;
	}

	@Override
	public EconomyResponse withdrawPlayer(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
