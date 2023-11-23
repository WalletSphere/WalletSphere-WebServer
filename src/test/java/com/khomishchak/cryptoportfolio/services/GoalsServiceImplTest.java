package com.khomishchak.cryptoportfolio.services;

import com.khomishchak.cryptoportfolio.model.User;
import com.khomishchak.cryptoportfolio.model.goals.CryptoGoalsTable;
import com.khomishchak.cryptoportfolio.model.goals.CryptoGoalsTableRecord;
import com.khomishchak.cryptoportfolio.repositories.CryptoGoalsTableRepository;
import com.khomishchak.cryptoportfolio.repositories.SelfGoalRepository;
import com.khomishchak.cryptoportfolio.services.exchangers.ExchangerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalsServiceImplTest {

    private static final long USER_ID = 1L;
    private static final long CRYPTO_GOALS_TABLE_ID = 1L;

    @Mock
    private CryptoGoalsTableRepository cryptoGoalsTableRepository;
    @Mock
    private UserService userService;
    @Mock
    private SelfGoalRepository selfGoalRepository;
    @Mock
    private ExchangerService exchangerService;

    private GoalsServiceImpl goalsService;

    User testUser;

    @BeforeEach
    void setUp() {
        goalsService = new GoalsServiceImpl(cryptoGoalsTableRepository, userService, selfGoalRepository, exchangerService);
    }

    @Test
    void shouldCreateCryptoGoalsTable() {
        // given
        CryptoGoalsTableRecord record = CryptoGoalsTableRecord.builder()
                .goalQuantity(BigDecimal.TEN)
                .quantity(BigDecimal.ONE)
                .build();

        CryptoGoalsTable cryptoGoalsTableToBeCreated = CryptoGoalsTable.builder()
                .tableRecords(List.of(record))
                .build();

        CryptoGoalsTable cryptoGoalsTable = CryptoGoalsTable.builder()
                .id(CRYPTO_GOALS_TABLE_ID)
                .tableRecords(List.of(record))
                .build();

        testUser = User.builder().id(USER_ID).build();

        when(userService.getUserById(eq(USER_ID))).thenReturn(testUser);
        when(cryptoGoalsTableRepository.save(eq(cryptoGoalsTableToBeCreated))).thenReturn(cryptoGoalsTable);

        // when
        CryptoGoalsTable actualTable = goalsService.createCryptoGoalsTable(USER_ID, cryptoGoalsTableToBeCreated);

        // then
        assertThat(actualTable.getTableRecords()).isNotNull();

        CryptoGoalsTableRecord calculatedRecord =  actualTable.getTableRecords().get(0);
        assertThat(calculatedRecord.getLeftToBuy()).isEqualTo(new BigDecimal(9));
        assertThat(calculatedRecord.getDonePercentage()).isEqualTo(new BigDecimal("10.0"));
        assertThat(calculatedRecord.isFinished()).isEqualTo(false);
    }

    @Test
    void shouldReturnCryptoGoalsTable() {
        // given
        CryptoGoalsTableRecord record = CryptoGoalsTableRecord.builder()
                .goalQuantity(BigDecimal.TEN)
                .quantity(BigDecimal.ONE)
                .build();

        CryptoGoalsTable cryptoGoalsTable = CryptoGoalsTable.builder()
                .id(CRYPTO_GOALS_TABLE_ID)
                .tableRecords(List.of(record))
                .build();
        testUser = User.builder().cryptoGoalsTable(cryptoGoalsTable).build() ;

        when(userService.getUserById(USER_ID)).thenReturn(testUser);

        // when
        CryptoGoalsTable actualTable = goalsService.getCryptoGoalsTable(USER_ID);

        // then
        assertThat(actualTable.getTableRecords()).isNotNull();

        CryptoGoalsTableRecord calculatedRecord =  actualTable.getTableRecords().get(0);
        assertThat(calculatedRecord.getLeftToBuy()).isEqualTo(new BigDecimal(9));
        assertThat(calculatedRecord.getDonePercentage()).isEqualTo(new BigDecimal("10.0"));
        assertThat(calculatedRecord.isFinished()).isEqualTo(false);
    }

    @Test
    void shouldUpdateCryptoGoalsTable() {
        // given
        CryptoGoalsTableRecord newRecord = CryptoGoalsTableRecord.builder()
                .name("BTC")
                .goalQuantity(BigDecimal.TEN)
                .quantity(BigDecimal.TEN)
                .build();

        CryptoGoalsTable newCryptoGoalsTable = CryptoGoalsTable.builder()
                .tableRecords(List.of(newRecord))
                .build();

        when(cryptoGoalsTableRepository.save(eq(newCryptoGoalsTable))).thenReturn(newCryptoGoalsTable);

        // when
        CryptoGoalsTable actualTable = goalsService.updateCryptoGoalsTable(newCryptoGoalsTable);

        // then
        assertThat(actualTable.getTableRecords()).isNotNull();

        CryptoGoalsTableRecord calculatedRecord =  actualTable.getTableRecords().get(0);
        assertThat(calculatedRecord.getLeftToBuy()).isEqualTo(BigDecimal.ZERO);
        assertThat(calculatedRecord.getDonePercentage()).isEqualTo(new BigDecimal("100.0"));
        assertThat(calculatedRecord.isFinished()).isEqualTo(true);
    }
}