package atm.Cassette;

import atm.Dto.BanknotesDto;

import java.util.List;

public interface Cassette extends Cloneable {
    Integer getBalance();

    void depositCash(List<BanknotesDto> banknotes);

    List<BanknotesDto> getCash(int cashSum);

    List<BanknotesDto> convertToBanknoteDtoList();

    Cassette clone();
}
