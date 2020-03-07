package atm.ATM;

import atm.ATM.Command.Command;
import atm.Cassette.Cassette;
import atm.Dto.BanknotesDto;
import atm.Exceptions.ATMException;

import java.util.List;

public class ATMImpl implements ATM{
    protected Cassette atmCassette;
    protected String address;
    protected String vendor;
    protected String serialNumber;
    protected Command getBalanceCommand;
    protected Momento initState;

    public ATMImpl(
            Cassette atmCassette,
            String serialNumber,
            String vendor,
            String address
    ) {
        this.atmCassette = atmCassette;
        this.serialNumber = serialNumber;
        this.vendor = vendor;
        this.address = address;
    }

    /**
     * @return
     */
    public Integer getBalance() {
        return Integer.parseInt(execute(getBalanceCommand));
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

    public void setGetBalanceCommand(Command getBalanceCommand) {
        this.getBalanceCommand = getBalanceCommand;
    }

    public String execute(Command cmd) {
        return cmd.execute(this);
    }

    public Cassette getAtmCassette() {
        return atmCassette;
    }

    public static class Builder {
        private Cassette atmCassette;
        private String address;
        private String vendor;
        private String serialNumber;
        private Boolean isSaveInitState = false;
        private Command getBalanceCommand;

        public Builder setCassette(Cassette atmCassette) {
            this.atmCassette = atmCassette;

            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;

            return this;
        }

        public Builder setVendor(String vendor) {
            this.vendor = vendor;

            return this;
        }

        public Builder setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;

            return this;
        }

        public Builder saveInitState() {
            this.isSaveInitState = true;

            return this;
        }

        public Builder setGetBalanceCommand(Command getBalanceCommand){
           this.getBalanceCommand = getBalanceCommand;

            return this;
        }

        public ATM build() {
            var atm = new ATMImpl(atmCassette, serialNumber, vendor, address);
            atm.setGetBalanceCommand(this.getBalanceCommand);

            if (this.isSaveInitState){
                atm.saveInitState();
            }

            return atm;
        }
    }

    public void saveInitState(){
        initState = new Momento();
    }

    public void restoreInitState(){
        if (initState == null){
            throw new ATMException("initState not created");
        }

        atmCassette = initState.getInitCassette();
        address = initState.getInitAddress();
        vendor = initState.getInitVendor();
        serialNumber = initState.getInitVendor();
    }

    private class Momento {
        private Cassette initCassette;
        private String initAddress;
        private String initVendor;
        private String initSerialNumber;

        public Momento(){
            this.initCassette = atmCassette.clone();
            this.initAddress = address;
            this.initVendor = vendor;
            this.initSerialNumber = serialNumber;
        }

        public Cassette getInitCassette() {
            return initCassette;
        }

        public String getInitAddress() {
            return initAddress;
        }

        public String getInitVendor() {
            return initVendor;
        }

        public String getInitSerialNumber() {
            return initSerialNumber;
        }
    }
}
