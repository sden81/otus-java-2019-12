package dapartment;

import atm.ATM.ATM;

import java.util.HashSet;
import java.util.Set;

public class DepartmentImpl implements Department {
    public Set<ATM> atmSet = new HashSet<>();

    @Override
    public String getTotalBalance() {
        var totalBalance = atmSet.stream().mapToInt(ATM::getBalance).sum();

        return String.valueOf(totalBalance);
    }

    @Override
    public void resetAllATM() {
        atmSet.forEach(atm ->resetAllATM());
    }

    @Override
    public void removeATM(ATM atm) {
        if (!atmSet.remove(atm)) {
            throw new RuntimeException("ATM not added");
        }
    }

    @Override
    public void registerATM(ATM atm) {
        if (atmSet.contains(atm)) {
            throw new RuntimeException("ATM already added");
        }

        atmSet.add(atm);
    }
}
