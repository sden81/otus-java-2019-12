package ATM;

import Cassette.Cassette;
import Dto.BanknotesDto;

import java.util.List;

public class ATMImpl implements ATM {
    protected Cassette atmCassette;
    protected String address;
    protected String vendor;
    protected String serialNumber;

    public ATMImpl(
            Cassette atmCassette,
            String serialNumber,
            String vendor
    ) {
        this.atmCassette = atmCassette;
        this.serialNumber = serialNumber;
        this.vendor = vendor;
    }

    /**
     * @return
     */
    public int getBalance() {
        return atmCassette.getBalance();
    }

    /**
     * @param banknotes
     */
    public void depositCash(List<BanknotesDto> banknotes) {
        atmCassette.depositCash(banknotes);
    }

    /**
     * @param cashSum
     * @return
     */
    public List<BanknotesDto> getCash(int cashSum) {
        return atmCassette.getCash(cashSum);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
