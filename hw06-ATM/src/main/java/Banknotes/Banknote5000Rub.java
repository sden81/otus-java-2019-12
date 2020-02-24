package Banknotes;

import Cassette.Currency;

import static Cassette.Currency.RUB;

public class Banknote5000Rub implements Banknote {
    private final Currency currency = RUB;
    private final Integer nominal = 5000;

    public Currency getCurrency() {
        return currency;
    }

    public Integer getNominal() {
        return nominal;
    }
}
