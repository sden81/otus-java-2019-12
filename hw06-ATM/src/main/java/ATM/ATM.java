package ATM;

import Dto.BanknotesDto;

import java.util.List;

public interface ATM {
    int getBalance();

    void depositCash(List<BanknotesDto> banknotes);

    List<BanknotesDto> getCash(int cashSum);
}
