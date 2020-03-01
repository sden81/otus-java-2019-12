package atm.Cassette;

import atm.Dto.BanknotesDto;

import java.util.List;

import static atm.Cassette.Currency.RUB;

public class RubCassette extends AbstractCassette {
    public RubCassette(int cellsCount) {
        super(RUB, cellsCount);
    }

    public static Cassette createCassette(List<BanknotesDto> listBanknotesDto, int cellsCount) {
        return AbstractCassette.createCassette(RUB, listBanknotesDto, cellsCount);
    }

    public static Cassette createCassette(List<BanknotesDto> listBanknotesDto) {
        return AbstractCassette.createCassette(RUB, listBanknotesDto, AbstractCassette.CELLS_COUNT);
    }

    @Override
    public List<BanknotesDto> convertToBanknoteDtoList() {
        return super.convertToBanknoteDtoList();
    }
}
