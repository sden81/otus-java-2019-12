package Banknotes;

import Cassette.Currency;

import static Cassette.Currency.RUB;

public class Banknote100Rub implements Banknote {
    private Currency currency = RUB;
    private Integer nominal = 100;

    public Currency getCurrency() {
        return currency;
    }

    public Integer getNominal() {
        return nominal;
    }
}
