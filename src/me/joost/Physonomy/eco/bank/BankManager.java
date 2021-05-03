package me.joost.Physonomy.eco.bank;

import me.joost.Physonomy.api.exception.funds.NoBankAccountException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BankManager {

    private List<Bank> banks = new ArrayList<Bank>();
    private HashMap<String, UUID> cache = new HashMap<String, UUID>();

    public List<Bank> getBanks(){
        return banks;
    }

    public Bank getBankFor(UUID uuid) {
        for(Bank b : banks){
            if(b.getAccounts().containsKey(uuid)){
                return b;
            }
        }
        return null;
    }

    public UUID getFromString(String player){
        return cache.get(player);
    }
}
