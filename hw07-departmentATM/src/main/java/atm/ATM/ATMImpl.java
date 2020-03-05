package atm.ATM;

import atm.ATM.Command.GetBalanceCommand;
import atm.Cassette.Cassette;
import atm.Dto.BanknotesDto;
import atm.Exceptions.ATMException;

import java.lang.reflect.Constructor;
import java.util.List;

public class ATMImpl implements ATM{
    protected Cassette atmCassette;
    protected String address;
    protected String vendor;
    protected String serialNumber;
    protected GetBalanceCommand getBalanceCommand;
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

    public GetBalanceCommand getGetBalanceCommand() {
        return getBalanceCommand;
    }

    public void setGetBalanceCommand(GetBalanceCommand getBalanceCommand) {
        this.getBalanceCommand = getBalanceCommand;
    }

    public static class Builder {
        private Cassette atmCassette;
        private String address;
        private String vendor;
        private String serialNumber;
        private Boolean isSaveInitState = false;
        private Constructor<atm.ATM.Command.GetBalanceCommand> getBalanceCommandConstructor;

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

        public Builder setGetBalanceCommand(Class<atm.ATM.Command.GetBalanceCommand> getBalanceCommandClass){
            try {
                getBalanceCommandConstructor = getBalanceCommandClass.getConstructor(Cassette.class);
            } catch (NoSuchMethodException e){
                throw new RuntimeException();
            }

            return this;
        }

        public ATM build() {
            var atm = new ATMImpl(atmCassette, serialNumber, vendor, address);
            try {
                atm.setGetBalanceCommand(getBalanceCommandConstructor.newInstance(atmCassette));
            } catch (Exception e){
                throw new RuntimeException("Can't create getBlanceCommand");
            }

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
