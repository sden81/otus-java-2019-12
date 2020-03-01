package atm.Cassette;

import atm.Banknotes.Banknote100Rub;
import atm.Banknotes.Banknote500Rub;
import atm.Dto.BanknotesDto;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RubCassetteTest {
    protected AbstractCassette rubCassette;

    @Before
    public void initCassette() {
        rubCassette = new RubCassette(3);
        rubCassette.addBanknoteCell(new BanknotesDto(new Banknote100Rub(), 3));
        rubCassette.addBanknoteCell(new BanknotesDto(new Banknote100Rub(), 1));
        rubCassette.addBanknoteCell(new BanknotesDto(new Banknote500Rub(), 10));
    }

    @Test
    public void testFactoryMethod(){
        List<BanknotesDto> listBanknotesDtos = new ArrayList<>();
        listBanknotesDtos.add(new BanknotesDto(new Banknote100Rub(), 3));
        listBanknotesDtos.add(new BanknotesDto(new Banknote500Rub(), 4));
        Cassette anotherRubCassette = RubCassette.createCassette(listBanknotesDtos);
        assertEquals((Integer) 2300, anotherRubCassette.getBalance());
    }
}