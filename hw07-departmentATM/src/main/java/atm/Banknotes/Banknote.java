package atm.Banknotes;

import atm.Cassette.Currency;

public interface Banknote {
    Currency getCurrency();

    Integer getNominal();
}
