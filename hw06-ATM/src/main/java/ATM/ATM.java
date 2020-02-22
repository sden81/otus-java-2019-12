package ATM;

import Cassette.Cassette;
import Dto.BanknotesDto;

import java.util.List;
import java.util.Map;

public class ATM implements ATMInterface {
    protected Cassette atmCassette;

    public ATM(Cassette atmCassette) {
        this.atmCassette = atmCassette;
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
}
