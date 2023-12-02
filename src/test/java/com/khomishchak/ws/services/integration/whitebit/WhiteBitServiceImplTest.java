package com.khomishchak.ws.services.integration.whitebit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WhiteBitServiceImplTest {
//
//    private static final long USER_ID = 1L;
//    private static final long BALANCE_ID = 2L;
//    private static final int RETRY_MAX_ATTEMPTS = 2;
//    private static final int RETRY_MIN_BACKOFF_SECONDS = 2;
//    private static final String PRIVATE_KEY = "privateKey";
//    private static final String PUBLIC_KEY = "publicKey";
//
//    private MockWebServer mockWebServer;
//
//    private WebClient webClient;
//
//    @Mock
//    private ApiKeySettingRepositoryAdapter apiKeySettingRepositoryAdapter;
//    @Mock
//    private BalanceRepository balanceRepository;
//    @Mock
//    private  WhiteBitResponseMapper responseMapper;
//    @Mock
//    private DepositWithdrawalTransactionsHistoryRepository depositWithdrawalTransactionsHistoryRepository;
//
//    private WhiteBitServiceImpl whiteBitService;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//
//        webClient = WebClient.builder().build();
//
//        whiteBitService = new WhiteBitServiceImpl(balanceRepository, webClient, apiKeySettingRepositoryAdapter,
//                RETRY_MAX_ATTEMPTS, RETRY_MIN_BACKOFF_SECONDS, responseMapper, depositWithdrawalTransactionsHistoryRepository);
//    }
//
//    @Test
//    void shouldReturnMainBalance() {
//        // given
//        Currency bsv = new Currency("BSV", 1.3);
//        Currency btc = new Currency("BTC", 22.11);
//        Currency xlm = new Currency("XLM", 36.48);
//        List<Currency> currencies = List.of(bsv, btc, xlm);
//
//        List<DecryptedApiKeySettingDTO> decryptedKeysPair = List.of(DecryptedApiKeySettingDTO.builder()
//                        .publicKey(PUBLIC_KEY).privateKey(PRIVATE_KEY).code(ExchangerCode.WHITE_BIT).build());
//
//        Balance balance = Balance.builder().id(BALANCE_ID).build();
//
//        when(apiKeySettingRepositoryAdapter.findAllByUserId(USER_ID)).thenReturn(decryptedKeysPair);
//        when(responseMapper.mapToCurrencies(any(WhiteBitBalanceResp.class))).thenReturn(currencies);
//        when(balanceRepository.findByCodeAndUser_Id(ExchangerCode.WHITE_BIT, USER_ID)).thenReturn(Optional.of(balance));
//
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody(getBalanceRespJson())
//                .addHeader("Content-Type", "application/json"));
//
//        // when
//        Balance result = whiteBitService.getAccountBalance(USER_ID);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getCurrencies()).isEqualTo(currencies);
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        mockWebServer.shutdown();
//    }
//
//    private WhiteBitBalanceResp generateWhiteBitBalanceResp(Map<String, Map<String, String>> currencies) {
//        WhiteBitBalanceResp resp = new WhiteBitBalanceResp();
//        for(Map.Entry<String, Map<String, String>> entry: currencies.entrySet()) {
//            resp.setCurrencies(entry.getKey(), entry.getValue());
//        }
//        return resp;
//    }
//
//    private String getBalanceRespJson() {
//         return """
//        {
//            "BSV": {
//            "main_balance": "1.3"
//        },
//            "BTC": {
//            "main_balance": "22.11"
//        },
//            "BTG": {
//            "main_balance": "0"
//        },
//            "BTT": {
//            "main_balance": "0"
//        },
//            "XLM": {
//            "main_balance": "36.48"
//        }""";
//    }
}