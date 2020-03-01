package dapartment;

import atm.ATM.ATM;

import java.util.HashSet;
import java.util.Set;

public class DepartmentImpl implements Department {
    public Set<ATM> atmSet = new HashSet<>();

    @Override
    public String getTotalBalance() {
        Integer totalBalance = 0;
        for (ATM atm : atmSet) {
            totalBalance += atm.getBalance();
        }

        return totalBalance.toString();
    }

    @Override
    public void resetAllATM() {
        for (ATM atm : atmSet) {
            atm.restoreInitState();
        }
    }

    @Override
    public void removeATM(ATM atm) {
        if (!atmSet.contains(atm)) {
            throw new RuntimeException("ATM not added");
        }
        atmSet.remove(atm);
    }

    @Override
    public void registerATM(ATM atm) {
        if (atmSet.contains(atm)) {
            throw new RuntimeException("ATM already added");
        }

        atmSet.add(atm);
    }
}
