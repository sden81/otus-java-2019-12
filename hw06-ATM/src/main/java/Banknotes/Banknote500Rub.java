package Banknotes;

import Cassette.Currency;

import static Cassette.Currency.RUB;

public class Banknote500Rub implements Banknote {
    private Currency currency = RUB;
    private Integer nominal = 500;

    public Currency getCurrency() {
        return currency;
    }

    public Integer getNominal() {
        return nominal;
    }
}
