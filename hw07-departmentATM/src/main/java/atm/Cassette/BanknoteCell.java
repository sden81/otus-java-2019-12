package atm.Cassette;

import atm.Banknotes.Banknote;

public interface BanknoteCell {
    Integer getBanknoteNominal();
    Integer getBanknoteCount();
    void setBanknoteCount(Integer banknoteCount);
    Banknote getBanknote();
}
