package atm.Cassette;

import atm.Dto.BanknotesDto;
import atm.Exceptions.CellAddError;
import atm.Exceptions.InvalidBanknote;
import atm.Exceptions.InvalidCashSum;
import atm.Exceptions.InvalidCurrency;

import java.util.*;

public class BaseCassette implements Cassette, Cloneable {
    protected Map<Integer, BanknoteCellImpl> banknoteCells;
    protected static final int CELLS_COUNT = 5;
    private final Currency currency;
    private int availableCells;

    static class BanknoteCellsComparator implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public BaseCassette(Currency currency, int cellsCount) {
        banknoteCells = new TreeMap<Integer, BanknoteCellImpl>(new BanknoteCellsComparator());
        this.currency = currency;
        this.availableCells = cellsCount;
    }

    /**
     * @return
     */
    public Integer getBalance() {
        Integer balance = 0;
        for (Map.Entry<Integer, BanknoteCellImpl> banknoteCell : banknoteCells.entrySet()) {
            balance += banknoteCell.getValue().banknote.getNominal() * banknoteCell.getValue().getBanknoteCount();
        }

        return balance;
    }

    /**
     * @param banknotes
     */
    public void depositCash(List<BanknotesDto> banknotes) {
        banknotesDtoValidation(banknotes);

        for (BanknotesDto banknotesDto : banknotes) {
            addBanknotesToCell(banknotesDto);
        }
    }

    /**
     * @param cashSum
     * @return
     */
    public List<BanknotesDto> getCash(int cashSum) {
        if (cashSum < 1 || cashSum > getBalance()) {
            throw new InvalidCashSum("Invalid CashSum: " + cashSum);
        }

        var returnedBanknotesDto = tryGetCash(cashSum);
        for (BanknotesDto banknotesDto : returnedBanknotesDto) {
            removeBanknoteFromCell(banknotesDto);
        }

        return returnedBanknotesDto;
    }

    private void banknotesDtoValidation(List<BanknotesDto> banknotes) {
        for (BanknotesDto banknoteDto : banknotes) {
            if (!banknoteCells.containsKey(banknoteDto.banknote.getNominal())) {
                throw new InvalidBanknote("Invalid banknote nominal: " + banknoteDto.banknote.getNominal().toString());
            }
            if (banknoteDto.banknote.getCurrency() != currency) {
                throw new InvalidCurrency("Invalid banknote currency: " + banknoteDto.banknote.getCurrency().toString());
            }
        }
    }

    public void addBanknoteCell(BanknotesDto banknotesDto) {
        if (currency != banknotesDto.banknote.getCurrency()) {
            throw new InvalidCurrency("Invalid currency: " + banknotesDto.banknote.getCurrency().toString());
        }

        if (!banknoteCells.containsKey(banknotesDto.banknote.getNominal())) {
            availableCells--;
        }

        if (availableCells < 0) {
            throw new CellAddError("All cell is full");
        }

        addBanknotesToCell(banknotesDto);
    }

    protected void addBanknotesToCell(BanknotesDto banknotesDto) {
        Integer banknotesCount;
        if (!banknoteCells.containsKey(banknotesDto.banknote.getNominal())) {
            banknotesCount = banknotesDto.banknotesCount;
        } else {
            banknotesCount = banknotesDto.banknotesCount + banknoteCells.get(banknotesDto.banknote.getNominal()).getBanknoteCount();
        }
        banknoteCells.put(
                banknotesDto.banknote.getNominal(),
                new BanknoteCellImpl(banknotesDto.banknote, banknotesCount)
        );
    }

    protected void removeBanknoteFromCell(BanknotesDto banknotesDto) {
        if (!banknoteCells.containsKey(banknotesDto.banknote.getNominal())) {
            throw new InvalidBanknote("Invalid banknote");
        }

        if (banknoteCells.get(banknotesDto.banknote.getNominal()).getBanknoteCount() < banknotesDto.banknotesCount) {
            throw new InvalidBanknote("Try remove invalid banknote count from ATM for banknote " + banknotesDto.banknote.getNominal().toString());
        }

        int resultBanknoteCountInCell = banknoteCells.get(banknotesDto.banknote.getNominal()).getBanknoteCount() - banknotesDto.banknotesCount;
        banknoteCells.get(banknotesDto.banknote.getNominal()).setBanknoteCount(resultBanknoteCountInCell);
    }

    protected List<BanknotesDto> tryGetCash(int cashSum) {
        var returnedBanknotesDto = new ArrayList<BanknotesDto>();
        double needBanknotes;
        int currentCashSum = cashSum;
        int wholePartNeedBanknotes;
        double fractionalPartNeedBanknotes;

        for (Map.Entry<Integer, BanknoteCellImpl> banknoteCell : banknoteCells.entrySet()) {
            if (banknoteCell.getValue().getBanknoteCount() == 0) {
                continue;
            }

            if (banknoteCell.getValue().getBanknoteNominal() == 0) {
                throw new InvalidBanknote("Invalid nominal");
            }

            needBanknotes = (double) currentCashSum / banknoteCell.getValue().getBanknoteNominal();
            wholePartNeedBanknotes = (int) needBanknotes;
            if (wholePartNeedBanknotes < 1){
                continue;
            }
            fractionalPartNeedBanknotes = needBanknotes - wholePartNeedBanknotes;

            if (fractionalPartNeedBanknotes == 0) {
                returnedBanknotesDto.add(new BanknotesDto(banknoteCell.getValue().banknote, wholePartNeedBanknotes));

                return returnedBanknotesDto;
            }

            if (wholePartNeedBanknotes > banknoteCell.getValue().getBanknoteCount()) {
                returnedBanknotesDto.add(new BanknotesDto(banknoteCell.getValue().banknote, banknoteCell.getValue().getBanknoteCount()));
            } else {
                returnedBanknotesDto.add(new BanknotesDto(banknoteCell.getValue().banknote, wholePartNeedBanknotes));
            }
            currentCashSum -= wholePartNeedBanknotes * banknoteCell.getValue().getBanknoteNominal();

            if (currentCashSum < 0) {
                throw new InvalidCashSum("Invalid cashSum: " + cashSum);
            }

            if (currentCashSum == 0) {
                return returnedBanknotesDto;
            }
        }

        throw new InvalidCashSum("Invalid cashSum: " + cashSum);
    }

    public int getAvailableCells() {
        return availableCells;
    }

    public List<BanknotesDto> convertToBanknoteDtoList() {
        var listBanknotesDto = new ArrayList<BanknotesDto>();
        for (Map.Entry<Integer, BanknoteCellImpl> banknoteCell : banknoteCells.entrySet()) {
            listBanknotesDto.add(new BanknotesDto(banknoteCell.getValue().getBanknote(), banknoteCell.getValue().getBanknoteCount()));
        }

        return listBanknotesDto;
    }

    public static Cassette createCassette(Currency currency, List<BanknotesDto> listBanknotesDto, int cellsCount){
        var cassette = new BaseCassette(currency, cellsCount);
        for (BanknotesDto banknotesDto : listBanknotesDto){
            cassette.addBanknoteCell(banknotesDto);
        }

        return cassette;
    }

    public Cassette clone() {
        return BaseCassette.createCassette(
                this.currency,
                this.convertToBanknoteDtoList(),
                this.getAvailableCells()
                );
    }
}
