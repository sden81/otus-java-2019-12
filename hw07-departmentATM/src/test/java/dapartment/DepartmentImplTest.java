package dapartment;


import atm.ATM.ATM;
import atm.ATM.ATMImpl;
import atm.ATM.Command.GetBalanceCommand;
import atm.Banknotes.Banknote100Rub;
import atm.Banknotes.Banknote500Rub;
import atm.Cassette.RubCassette;
import atm.Dto.BanknotesDto;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DepartmentImplTest {
    Department department;


    @Before
    public void init() {
        department = new DepartmentImpl();

        List<BanknotesDto> listBanknotesDtos = new ArrayList<BanknotesDto>();
        listBanknotesDtos.add(new BanknotesDto(new Banknote100Rub(), 3));
        listBanknotesDtos.add(new BanknotesDto(new Banknote500Rub(), 4));

        ATM atm1 = new ATMImpl.Builder().setCassette(RubCassette.createCassette(listBanknotesDtos)).setAddress("atm1 address").setSerialNumber("s/n atm1").saveInitState().setGetBalanceCommand(GetBalanceCommand.class).build();
        ATM atm2 = new ATMImpl.Builder().setCassette(RubCassette.createCassette(listBanknotesDtos)).setAddress("atm2 address").setSerialNumber("s/n atm2").saveInitState().setGetBalanceCommand(GetBalanceCommand.class).build();
        ATM atm3 = new ATMImpl.Builder().setCassette(RubCassette.createCassette(listBanknotesDtos)).setAddress("atm3 address").setSerialNumber("s/n atm3").saveInitState().setGetBalanceCommand(GetBalanceCommand.class).build();
        department.registerATM(atm1);
        department.registerATM(atm2);
        department.registerATM(atm3);
    }

    @Test
    public void resetAllATM() {
        assertEquals("6900", department.getTotalBalance());
        ATM atm = (ATM) ((DepartmentImpl) department).atmSet.toArray()[0];
        var banknotesDtos = atm.getCash(1000);
        assertEquals("5900", department.getTotalBalance());
    }

    @Test
    public void removeATM() {
        assertEquals(3, ((DepartmentImpl) department).atmSet.size());
        ATM removedATM = (ATM) ((DepartmentImpl) department).atmSet.toArray()[0];
        department.removeATM(removedATM);
        assertEquals(2, ((DepartmentImpl) department).atmSet.size());
    }

    @Test
    public void registerATM() {
        assertEquals(3, ((DepartmentImpl) department).atmSet.size());
    }

    @Test
    public void testDecorator(){
        Department departmentDecorator = new DepartmentImplDecorator(department);
        assertEquals("Total balance: 6900", departmentDecorator.getTotalBalance());
    }
}