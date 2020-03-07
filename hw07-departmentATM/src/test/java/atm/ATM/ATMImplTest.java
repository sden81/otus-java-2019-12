package atm.ATM;

import atm.ATM.Command.GetBalanceCommand;
import atm.Banknotes.Banknote100Rub;
import atm.Banknotes.Banknote500Rub;
import atm.Cassette.RubCassette;
import atm.Dto.BanknotesDto;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ATMImplTest {
    private ATM atm;

    @Before
    public void initAtm() {
        List<BanknotesDto> listBanknotesDtos = new ArrayList<>();
        listBanknotesDtos.add(new BanknotesDto(new Banknote100Rub(), 3));
        listBanknotesDtos.add(new BanknotesDto(new Banknote500Rub(), 4));

        atm = new ATMImpl.Builder()
                .setCassette(RubCassette.createCassette(listBanknotesDtos))
                .setAddress("NN")
                .setSerialNumber("123")
                .saveInitState()
                .setGetBalanceCommand(new GetBalanceCommand())
                .build();
    }

    @Test
    public void testBuilder(){
        assertEquals("NN", atm.getAddress());
        assertEquals("123", atm.getSerialNumber());
        assertEquals(null, atm.getVendor());
    }

    @Test
    public void testMomento(){
        assertEquals((Integer)2300, atm.getBalance());
        var banknotesList = atm.getCash(2300);
        assertEquals((Integer)0, atm.getBalance());
        atm.restoreInitState();
        assertEquals((Integer)2300, atm.getBalance());
    }
}