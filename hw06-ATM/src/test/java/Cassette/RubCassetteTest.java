package Cassette;

import Banknotes.Banknote1000Rub;
import Banknotes.Banknote100Rub;
import Banknotes.Banknote5000Rub;
import Banknotes.Banknote500Rub;
import Dto.BanknotesDto;
import Exceptions.CellAddError;
import Exceptions.InvalidBanknote;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
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
    public void testRubCassetteCreation() {
        assertEquals(2, rubCassette.banknoteCells.size());
        assertEquals(5400, rubCassette.getBalance());
    }

    @Test
    public void testDepositCache() {
        var banknotesDtoList = new ArrayList<BanknotesDto>();
        banknotesDtoList.add(new BanknotesDto(new Banknote100Rub(), 1));
        banknotesDtoList.add(new BanknotesDto(new Banknote500Rub(), 1));

        rubCassette.depositCash(banknotesDtoList);
        assertEquals(6000, rubCassette.getBalance());
    }

    @Test
    public void testGetCash() {
        List<BanknotesDto> returnedBanknoteList = rubCassette.getCash(1200);

        assertEquals(2, returnedBanknoteList.size());
        assertEquals(2, (int) returnedBanknoteList.get(0).banknotesCount);
        assertTrue(returnedBanknoteList.get(0).banknote instanceof Banknote500Rub);
        assertEquals(2, (int) returnedBanknoteList.get(1).banknotesCount);
        assertTrue(returnedBanknoteList.get(1).banknote instanceof Banknote100Rub);
    }

    @Test(expected = CellAddError.class)
    public void testFullCassette() {
        rubCassette.addBanknoteCell(new BanknotesDto(new Banknote1000Rub(), 1));
        rubCassette.addBanknoteCell(new BanknotesDto(new Banknote5000Rub(), 1));
    }

    @Test(expected = InvalidBanknote.class)
    public void testInvalidBanknote() {
        var banknotesDtoList = new ArrayList<BanknotesDto>();
        banknotesDtoList.add(new BanknotesDto(new Banknote5000Rub(), 1));
        rubCassette.depositCash(banknotesDtoList);
    }

}