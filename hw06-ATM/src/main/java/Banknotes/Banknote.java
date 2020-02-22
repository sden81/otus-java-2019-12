package Banknotes;

import Cassette.Currency;

public interface Banknote {
    Currency getCurrency();

    Integer getNominal();
}
