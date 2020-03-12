package dapartment;

import atm.ATM.ATM;

public interface Department {
    String getTotalBalance();
    void resetAllATM();
    void registerATM(ATM atm);
    void removeATM(ATM atm);
}
