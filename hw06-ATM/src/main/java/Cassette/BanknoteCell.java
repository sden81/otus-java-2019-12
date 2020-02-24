package Cassette;

import Banknotes.Banknote;

public class BanknoteCell {
    public final Banknote banknote;
    private Integer banknoteCount;

    public BanknoteCell(Banknote banknote, Integer banknoteCount) {
        this.banknote = banknote;
        this.banknoteCount = banknoteCount;
    }

    public Integer getBanknoteNominal() {
        return banknote.getNominal();
    }

    public Integer getBanknoteCount() {
        return banknoteCount;
    }

    public void setBanknoteCount(Integer banknoteCount) {
        this.banknoteCount = banknoteCount;
    }
}
