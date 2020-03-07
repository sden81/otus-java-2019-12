package atm.ATM.Command;

import atm.ATM.ATM;

public class GetBalanceCommand implements Command{
    @Override
    public String execute(ATM atm) {
        return atm.getAtmCassette().getBalance().toString();
    }
}
