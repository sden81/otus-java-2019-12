package atm.ATM;

import atm.Dto.BanknotesDto;

import java.util.List;

public interface ATM {
    void depositCash(List<BanknotesDto> banknotes);

    List<BanknotesDto> getCash(int cashSum);

    String getAddress();
    String getSerialNumber();
    String getVendor();

    void saveInitState();
    void restoreInitState();

    Integer getBalance();
}
