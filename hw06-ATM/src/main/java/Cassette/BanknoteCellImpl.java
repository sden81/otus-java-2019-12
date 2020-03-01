package Cassette;

import Banknotes.Banknote;
import Exceptions.InvalidCashSum;

public class BanknoteCellImpl implements BanknoteCell{
    public final Integer MAX_BANKNOTE_COUNT = 100;
    private Integer banknoteCount;
    public final Banknote banknote;

    public BanknoteCellImpl(Banknote banknote, Integer banknoteCount) {
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
        if (banknoteCount > MAX_BANKNOTE_COUNT){
            throw new InvalidCashSum("max banknote count exceeded");
        }

        if (banknoteCount < 0) {
            throw new InvalidCashSum("banknote count is less than zero");
        }
        this.banknoteCount = banknoteCount;
    }
}
