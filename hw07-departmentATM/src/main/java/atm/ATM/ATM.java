package atm.ATM;

import atm.Cassette.Cassette;
import atm.Dto.BanknotesDto;

import java.util.List;

public interface ATM {
    void depositCash(List<BanknotesDto> banknotes);

    List<BanknotesDto> getCash(int cashSum);

    String getAddress();
    String getSerialNumber();
    String getVendor();
    Cassette getAtmCassette();

    void saveInitState();
    void restoreInitState();

    Integer getBalance();
}
