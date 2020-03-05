package atm.Cassette;

import atm.Dto.BanknotesDto;

import java.util.List;

import static atm.Cassette.Currency.RUB;

public class RubCassette extends BaseCassette {
    public RubCassette(int cellsCount) {
        super(RUB, cellsCount);
    }

    public static Cassette createCassette(List<BanknotesDto> listBanknotesDto, int cellsCount) {
        return BaseCassette.createCassette(RUB, listBanknotesDto, cellsCount);
    }

    public static Cassette createCassette(List<BanknotesDto> listBanknotesDto) {
        return BaseCassette.createCassette(RUB, listBanknotesDto, BaseCassette.CELLS_COUNT);
    }

    @Override
    public List<BanknotesDto> convertToBanknoteDtoList() {
        return super.convertToBanknoteDtoList();
    }
}
