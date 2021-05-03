package me.joost.Physonomy.eco.bank;

import me.joost.Physonomy.Physonomy;
import me.joost.Physonomy.api.exception.credit.CreditDebtConclusiveException;
import me.joost.Physonomy.api.exception.credit.CreditMaxCreditException;
import me.joost.Physonomy.api.exception.funds.NoBankAccountException;
import me.joost.Physonomy.api.exception.funds.NotEnoughFundsException;
import me.joost.Physonomy.util.DateUtil;
import me.joost.Physonomy.util.TimeUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Credit {

    private Bank bank;
    private String account;
    private HashMap<Double, Date> debt;
    private boolean debtConclusive;

    public Credit(Bank bank, String account, HashMap<Double, Date> debt, boolean debtConclusivee){
        this.bank = bank;
        this.account = account;
        this.debt = debt;
        this.debtConclusive = debtConclusivee;
        Credit c = this;

        int time = TimeUtil.StringToTime(bank.getCreditTime());
        new BukkitRunnable(){
            public void run(){
                // Per 30 seconds
                for(Map.Entry<Double, Date> d : debt.entrySet()){
                    Date date = d.getValue();
                    Date now = new Date();

                    long diff = now.getTime()-date.getTime();
                    if(TimeUnit.MILLISECONDS.toSeconds(diff)>=time){
                        try {
                            bank.withdrawFromUser(bank.getCredit(c), d.getKey());
                        } catch (NotEnoughFundsException | NoBankAccountException e) {
                            this.cancel();
                            debtConclusive = true;
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Physonomy.getPlugin(Physonomy.class), 600L, 600L);
    }

    public boolean canPay(double amount){
        if(!isDebtConclusive()){
            return amount+getTotalDebt()<=bank.getMaxCredit();
        }
        return false;
    }

    private double getTotalDebt() {
        double totalDebt = 0;

        if(!getDebt().isEmpty()){
            for(Double d : getDebt().keySet()){
                totalDebt += d;
            }
        }

        return totalDebt;
    }

    public Bank getBank(){
        return bank;
    }

    public String getAccount(){
        return account;
    }

    public HashMap<Double, Date> getDebt(){
        return debt;
    }

    public boolean isDebtConclusive(){
        return debtConclusive;
    }

    public void payViaCredit(double amount) throws CreditMaxCreditException, CreditDebtConclusiveException {
        if(!debtConclusive){
            double totalDebt = getTotalDebt();

            if(totalDebt+amount>bank.getMaxCredit()){
                throw new CreditMaxCreditException("Max credit is " + bank.getMaxCredit() + ", debt would be" + (totalDebt+amount));
            }

            else{
               getDebt().put(amount, DateUtil.getDateNow()); // Add debt
            }
        }

        else{
            throw new CreditDebtConclusiveException("Debt is conclusive, user must first pay open debt");
        }
    }
}
