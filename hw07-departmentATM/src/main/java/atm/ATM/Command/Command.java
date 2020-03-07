package atm.ATM.Command;

import atm.ATM.ATM;

public interface Command {
    String execute(ATM atm);
}
