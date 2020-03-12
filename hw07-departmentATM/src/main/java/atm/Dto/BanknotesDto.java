package atm.Dto;

import atm.Banknotes.Banknote;

public class BanknotesDto {
    public final Banknote banknote;
    public final Integer banknotesCount;

    public BanknotesDto(Banknote banknote, Integer banknotesCount) {
        this.banknote = banknote;
        this.banknotesCount = banknotesCount;
    }
}
