package Banknotes;

import Cassette.Currency;

import static Cassette.Currency.RUB;

public class Banknote100Rub implements Banknote {
    private final Currency currency = RUB;
    private final Integer nominal = 100;

    public Currency getCurrency() {
        return currency;
    }

    public Integer getNominal() {
        return nominal;
    }
}
