package atm.Banknotes;

import atm.Cassette.Currency;

import static atm.Cassette.Currency.RUB;

public class Banknote1000Rub implements Banknote {
    private final Currency currency = RUB;
    private final Integer nominal = 1000;

    public Currency getCurrency() {
        return currency;
    }

    public Integer getNominal() {
        return nominal;
    }
}
