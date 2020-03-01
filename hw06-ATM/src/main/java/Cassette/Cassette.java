package Cassette;

import Dto.BanknotesDto;

import java.util.List;

public interface Cassette {
    int getBalance();

    void depositCash(List<BanknotesDto> banknotes);

    List<BanknotesDto> getCash(int cashSum);
}
