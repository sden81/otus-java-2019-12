package atm.ATM.Command;

import atm.Cassette.Cassette;

public class GetBalanceCommand implements Command{
    private Cassette cassette;

    public GetBalanceCommand(Cassette cassette) {
        this.cassette = cassette;
    }

    @Override
    public String execute() {
        return cassette.getBalance().toString();
    }
}
