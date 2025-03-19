import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import root.vending.VendingMachine;
import root.vending.VendingMachine.Mode;
import root.vending.VendingMachine.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
public class VendingMachineTest {
    private VendingMachine vm;
    private static final long CORRECT_ID = 117345294655382L;
    private static final int DEFAULT_MAX = 40;
    private static final int DEFAULT_NUM = 0;
    private static final int DEFAULT_PRICE = 5;
    private static final int DEFAULT_MAXC1 = 50;
    private static final int DEFAULT_MAXC2 = 50;
    private static final int DEFAULT_COINS1 = 0;
    private static final int DEFAULT_COINS2 = 0;
    private static final int DEFAULT_BALANCE = 0;

    private void putCoins1(int number) {
        for (int i = 0; i < number; i++) {
            vm.putCoin1();
        }
    }

    private void putCoins2(int number) {
        for (int i = 0; i < number; i++) {
            vm.putCoin2();
        }
    }

    @BeforeEach
    public void init() {
        vm = new VendingMachine();
    }

    @Test
    public void getCurrentSumTest() {
        // arrange
        int coins1 = 3, coins2 = 5;
        int correct = VendingMachine.coinval1 * coins1
                + VendingMachine.coinval2 * coins2;

        // act
        int fromOperationModeDefault = vm.getCurrentSum();

        vm.enterAdminMode(CORRECT_ID);
        int fromAdminModeDefault = vm.getCurrentSum();

        vm.fillCoins(coins1, coins2);
        int fromAdminModeChanged = vm.getCurrentSum();

        vm.exitAdminMode();
        int fromOperationModeChanged = vm.getCurrentSum();

        // assert
        assertEquals(0, fromOperationModeDefault);
        assertEquals(0, fromAdminModeDefault);
        assertEquals(correct, fromAdminModeChanged);
        assertEquals(0, fromOperationModeChanged);
    }

    @Test
    public void getCoins1Test() {
        // arrange

        // act
        int fromOperationModeDefault = vm.getCoins1();

        vm.enterAdminMode(CORRECT_ID);
        int fromAdminModeDefault = vm.getCoins1();

        vm.fillCoins(1, DEFAULT_MAXC2);
        int fromAdminModeChanged = vm.getCoins1();

        vm.exitAdminMode();
        int fromOperationModeChanged = vm.getCoins1();

        // assert
        assertEquals(0, fromOperationModeDefault);
        assertEquals(0, fromAdminModeDefault);
        assertEquals(1, fromAdminModeChanged);
        assertEquals(0, fromOperationModeChanged);
    }

    @Test
    public void getCoins2Test() {
        // arrange

        // act
        int fromOperationModeDefault = vm.getCoins2();

        vm.enterAdminMode(CORRECT_ID);
        int fromAdminModeDefault = vm.getCoins2();

        vm.fillCoins(DEFAULT_MAXC1, 1);
        int fromAdminModeChanged = vm.getCoins2();

        vm.exitAdminMode();
        int fromOperationModeChanged = vm.getCoins2();

        // assert
        assertEquals(0, fromOperationModeDefault);
        assertEquals(0, fromAdminModeDefault);
        assertEquals(1, fromAdminModeChanged);
        assertEquals(0, fromOperationModeChanged);
    }

    @Test
    public void fillProductsTest() {
        // arrange

        // act
        Response responseFromOperation = vm.fillProducts();
        int numberFromOperation = vm.getNumberOfProduct();

        vm.enterAdminMode(CORRECT_ID);
        Response responseFromAdmin = vm.fillProducts();
        int numberFromAdmin = vm.getNumberOfProduct();

        // assert
        assertEquals(Response.ILLEGAL_OPERATION, responseFromOperation);
        assertEquals(DEFAULT_NUM, numberFromOperation);

        assertEquals(Response.OK, responseFromAdmin);
        assertEquals(DEFAULT_MAX, numberFromAdmin);
    }

    @Test
    public void fillCoinsObviousCases() {
        // arrange
        int coins1correct = 3, coins2correct = 5;

        // act
        Response fromOperation = vm.fillCoins(coins1correct, coins2correct);

        vm.enterAdminMode(CORRECT_ID);

        int coins1FromOperation = vm.getCoins1();
        int coins2FromOperation = vm.getCoins2();

        Response responseOk = vm.fillCoins(coins1correct, coins2correct);
        int numberCoins1Ok = vm.getCoins1();
        int numberCoins2Ok = vm.getCoins2();

        // assert
        assertEquals(Response.ILLEGAL_OPERATION, fromOperation);
        assertEquals(DEFAULT_COINS1, coins1FromOperation);
        assertEquals(DEFAULT_COINS2, coins2FromOperation);

        assertEquals(Response.OK, responseOk);
        assertEquals(coins1correct, numberCoins1Ok);
        assertEquals(coins2correct, numberCoins2Ok);
    }

    @Test
    public void fillCoinsFixedCoin2Test() {
        // arrange
        int coins1correct = 3, coins2correct = 5;
        int coins1UnderZero = -3;
        int coins1BiggerThanMax = DEFAULT_MAXC1 + 1;

        // act
        vm.enterAdminMode(CORRECT_ID);
        vm.fillCoins(coins1correct, coins2correct);

        Response responseCoins1UnderZero = vm.fillCoins(coins1UnderZero, coins2correct);
        int numberCoins1UnderZero = vm.getCoins1();
        Response responseCoins1BiggerThanMax = vm.fillCoins(coins1BiggerThanMax, coins2correct);
        int numberCoins1BiggerThanMax = vm.getCoins1();
        Response responseCoins1Zero = vm.fillCoins(0, coins2correct);
        int numberCoins1Zero = vm.getCoins1();

        // assert
        assertEquals(Response.INVALID_PARAM, responseCoins1UnderZero);
        assertEquals(Response.INVALID_PARAM, responseCoins1BiggerThanMax);
        assertEquals(Response.INVALID_PARAM, responseCoins1Zero);
        assertEquals(coins1correct, numberCoins1UnderZero);
        assertEquals(coins1correct, numberCoins1BiggerThanMax);
        assertEquals(coins1correct, numberCoins1Zero);
    }

    @Test
    public void fillCoinsFixedCoin1Test() {
        // arrange
        int coins1correct = 3, coins2correct = 5;
        int coins2UnderZero = -5;
        int coins2BiggerThanMax = DEFAULT_MAXC2 + 1;

        // act
        vm.enterAdminMode(CORRECT_ID);
        vm.fillCoins(coins1correct, coins2correct);

        Response responseCoins2UnderZero = vm.fillCoins(coins1correct, coins2UnderZero);
        int numberCoins2UnderZero = vm.getCoins2();
        Response responseCoins2BiggerThanMax = vm.fillCoins(coins1correct, coins2BiggerThanMax);
        int numberCoins2BiggerThanMax = vm.getCoins2();
        Response responseCoins2Zero = vm.fillCoins(coins1correct, 0);
        int numberCoins2Zero = vm.getCoins2();

        // assert
        assertEquals(Response.INVALID_PARAM, responseCoins2UnderZero);
        assertEquals(Response.INVALID_PARAM, responseCoins2BiggerThanMax);
        assertEquals(Response.INVALID_PARAM, responseCoins2Zero);
        assertEquals(coins2correct, numberCoins2UnderZero);
        assertEquals(coins2correct, numberCoins2BiggerThanMax);
        assertEquals(coins2correct, numberCoins2Zero);
    }

    @Test
    public void enterAdminModeTest() {
        // arrange
        long incorrectId = CORRECT_ID + 1L;

        // act
        Response responseOk = vm.enterAdminMode(CORRECT_ID);
        Mode modeOk = vm.getCurrentMode();

        Response responseOkFromAdmin = vm.enterAdminMode(CORRECT_ID);
        Mode modeOkFromAdmin = vm.getCurrentMode();

        vm.exitAdminMode();

        Response responseInvalid = vm.enterAdminMode(incorrectId);
        Mode modeInvalid = vm.getCurrentMode();

        vm.putCoin1();

        Response responseWithBalance = vm.enterAdminMode(CORRECT_ID);
        Mode modeWithBalance = vm.getCurrentMode();

        // assert
        assertEquals(Response.OK, responseOk);
        assertEquals(Mode.ADMINISTERING, modeOk);

        assertEquals(Response.OK, responseOkFromAdmin);
        assertEquals(Mode.ADMINISTERING, modeOkFromAdmin);

        assertEquals(Response.INVALID_PARAM, responseInvalid);
        assertEquals(Mode.OPERATION, modeInvalid);

        assertEquals(Response.CANNOT_PERFORM, responseWithBalance);
        assertEquals(Mode.OPERATION, modeWithBalance);
    }

    @Test
    public void setPricesTest() {
        // arrange
        int correctPrice = 7, incorrectPrice = -3;

        // act
        Response responseFromOperation = vm.setPrices(correctPrice);
        int priceFromOperation = vm.getPrice();

        vm.enterAdminMode(CORRECT_ID);

        Response responseOk = vm.setPrices(correctPrice);
        int priceOk = vm.getPrice();

        Response responseIncorrect = vm.setPrices(incorrectPrice);
        int priceIncorrect = vm.getPrice();

        Response responseZero = vm.setPrices(0);
        int priceZero = vm.getPrice();

        // assert
        assertEquals(Response.ILLEGAL_OPERATION, responseFromOperation);
        assertEquals(DEFAULT_PRICE, priceFromOperation);

        assertEquals(Response.OK, responseOk);
        assertEquals(correctPrice, priceOk);

        assertEquals(Response.INVALID_PARAM, responseIncorrect);
        assertEquals(correctPrice, priceIncorrect);

        assertEquals(Response.INVALID_PARAM, responseZero);
        assertEquals(correctPrice, priceZero);

    }

    @Test
    public void putCoin1Test() {
        // arrange

        // act
        Response responseOk = vm.putCoin1();
        int balanceOk = vm.getCurrentBalance();

        vm.returnMoney();
        vm.enterAdminMode(CORRECT_ID);

        Response responseFromAdmin = vm.putCoin1();
        int balanceFromAdmin = vm.getCurrentBalance();

        vm.fillCoins(DEFAULT_MAXC1, 1);

        vm.exitAdminMode();

        Response responseHigherThanMax = vm.putCoin1();
        int balanceHigherThanMax = vm.getCurrentBalance();

        // assert
        assertEquals(Response.OK, responseOk);
        assertEquals(VendingMachine.coinval1, balanceOk);

        assertEquals(Response.ILLEGAL_OPERATION, responseFromAdmin);
        assertEquals(0, balanceFromAdmin);

        assertEquals(Response.CANNOT_PERFORM, responseHigherThanMax);
        assertEquals(0, balanceHigherThanMax);
    }

    @Test
    public void putCoin2Test() {
        // arrange

        // act
        Response responseOk = vm.putCoin2();
        int balanceOk = vm.getCurrentBalance();

        vm.returnMoney();
        vm.enterAdminMode(CORRECT_ID);

        Response responseFromAdmin = vm.putCoin2();
        int balanceFromAdmin = vm.getCurrentBalance();

        vm.fillCoins(1, DEFAULT_MAXC2);

        vm.exitAdminMode();

        Response responseHigherThanMax = vm.putCoin2();
        int balanceHigherThanMax = vm.getCurrentBalance();

        // assert
        assertEquals(Response.OK, responseOk);
        assertEquals(VendingMachine.coinval2, balanceOk);

        assertEquals(Response.ILLEGAL_OPERATION, responseFromAdmin);
        assertEquals(0, balanceFromAdmin);

        assertEquals(Response.CANNOT_PERFORM, responseHigherThanMax);
        assertEquals(0, balanceHigherThanMax);
    }

    @Test
    public void returnMoneyObviousCasesTest() {
        // arrange

        // act
        Response responseZeroBalance = vm.returnMoney();
        int balanceZeroBalance = vm.getCurrentBalance();

        vm.enterAdminMode(CORRECT_ID);
        Response responseFromAdmin = vm.returnMoney();
        int balanceFromAdmin = vm.getCurrentBalance();
        vm.exitAdminMode();

        vm.putCoin2();
        VendingMachine.coinval2 = 0;
        Response responseTooBigBalance = vm.returnMoney();
        int balanceTooBigBalance = vm.getCurrentBalance();
        VendingMachine.coinval2 = 2;

        // assert
        assertEquals(Response.ILLEGAL_OPERATION, responseFromAdmin);
        assertEquals(DEFAULT_BALANCE, balanceFromAdmin);

        assertEquals(Response.OK, responseZeroBalance);
        assertEquals(DEFAULT_BALANCE, balanceZeroBalance);

        assertEquals(Response.TOO_BIG_CHANGE, responseTooBigBalance);
        assertEquals(VendingMachine.coinval2, balanceTooBigBalance);
    }

    @Test
    public void returnMoneyFullTest() {
        // act
        int coins1 = 3, coins2 = 5;
        putCoins1(coins1);
        putCoins2(coins2);

        // act
        Response response = vm.returnMoney();
        int balance = vm.getCurrentBalance();
        vm.enterAdminMode(CORRECT_ID);
        int currentCoins1 = vm.getCoins1();
        int currentCoins2 = vm.getCoins2();

        // assert
        assertEquals(Response.OK, response);
        assertEquals(0, balance);
        assertEquals(0, currentCoins1);
        assertEquals(0, currentCoins2);
    }

    @Test
    public void returnMoneyHigherThanCoin2AmountTest() {
        // arrange
        int startCoins1 = 1, startCoins2 = 1;
        int balanceCoins1 = 3, balanceCoins2 = 1;
        int correctCoins1 = 3, correctCoins2 = 0;

        vm.enterAdminMode(CORRECT_ID);
        vm.fillCoins(startCoins1, startCoins2);
        vm.exitAdminMode();
        putCoins1(balanceCoins1);
        putCoins2(balanceCoins2);

        // act
        Response response = vm.returnMoney();
        int balance = vm.getCurrentBalance();
        vm.enterAdminMode(CORRECT_ID);
        int currentCoins1 = vm.getCoins1();
        int currentCoins2 = vm.getCoins2();

        // assert
        assertEquals(Response.OK, response);
        assertEquals(0, balance);
        assertEquals(correctCoins1, currentCoins1);
        assertEquals(correctCoins2, currentCoins2);
    }

    @Test
    public void returnMoneyEvenBalanceTest() {
        // arrange
        int startCoins1 = 1, startCoins2 = 4;
        int balanceCoins1 = 6, balanceCoins2 = 0;
        int correctCoins1 = 7, correctCoins2 = 1;

        vm.enterAdminMode(CORRECT_ID);
        vm.fillCoins(startCoins1, startCoins2);
        vm.exitAdminMode();
        putCoins1(balanceCoins1);
        putCoins2(balanceCoins2);

        // act
        Response response = vm.returnMoney();
        int balance = vm.getCurrentBalance();
        vm.enterAdminMode(CORRECT_ID);
        int currentCoins1 = vm.getCoins1();
        int currentCoins2 = vm.getCoins2();

        // assert
        assertEquals(Response.OK, response);
        assertEquals(0, balance);
        assertEquals(correctCoins1, currentCoins1);
        assertEquals(correctCoins2, currentCoins2);
    }

    @Test
    public void returnMoneyOddBalanceWithCoins1() {
        // arrange
        int startCoins1 = 1, startCoins2 = 2;
        int balanceCoins1 = 3, balanceCoins2 = 2;
        int correctCoins1 = 3, correctCoins2 = 1;

        vm.enterAdminMode(CORRECT_ID);
        vm.fillCoins(startCoins1, startCoins2);
        vm.exitAdminMode();
        putCoins1(balanceCoins1);
        putCoins2(balanceCoins2);

        // act
        Response response = vm.returnMoney();
        int balance = vm.getCurrentBalance();
        vm.enterAdminMode(CORRECT_ID);
        int currentCoins1 = vm.getCoins1();
        int currentCoins2 = vm.getCoins2();

        // assert
        assertEquals(Response.OK, response);
        assertEquals(0, balance);
        assertEquals(correctCoins1, currentCoins1);
        assertEquals(correctCoins2, currentCoins2);
    }

    @Test
    public void returnMoneyOddBalanceWithoutCoins1() {
        // arrange
        VendingMachine.coinval2 = 1;
        vm.putCoin2();
        VendingMachine.coinval2 = 2;

        // act
        Response response = vm.returnMoney();
        int balance = vm.getCurrentBalance();
        vm.enterAdminMode(CORRECT_ID);

        // assert
        assertEquals(Response.UNSUITABLE_CHANGE, response);
        assertEquals(1, balance);
    }

    @Test
    public void giveProductBoundersOfProductTest() {
        // arrange
        vm.enterAdminMode(CORRECT_ID);
        vm.fillProducts();
        vm.exitAdminMode();

        putCoins1(40);
        putCoins2(40);
        int startBalance = vm.getCurrentBalance();

        // act
        Response responseZeroProductRequest = vm.giveProduct(0);
        int balanceZeroProductRequest = vm.getCurrentBalance();
        int productsZeroProductRequest = vm.getNumberOfProduct();

        Response responseUnderZeroProductRequest = vm.giveProduct(-1);
        int balanceUnderZeroProductRequest = vm.getCurrentBalance();
        int productsUnderZeroProductRequest = vm.getNumberOfProduct();

        Response responseOverMaxProductRequest = vm.giveProduct(DEFAULT_MAX + 1);
        int balanceOverMaxProductRequest = vm.getCurrentBalance();
        int productsOverMaxProductRequest = vm.getNumberOfProduct();

        // assert
        assertEquals(Response.INVALID_PARAM, responseZeroProductRequest);
        assertEquals(startBalance, balanceZeroProductRequest);
        assertEquals(DEFAULT_MAX, productsZeroProductRequest);

        assertEquals(Response.INVALID_PARAM, responseUnderZeroProductRequest);
        assertEquals(startBalance, balanceUnderZeroProductRequest);
        assertEquals(DEFAULT_MAX, productsUnderZeroProductRequest);

        assertEquals(Response.INVALID_PARAM, responseOverMaxProductRequest);
        assertEquals(startBalance, balanceOverMaxProductRequest);
        assertEquals(DEFAULT_MAX, productsOverMaxProductRequest);
    }

    @Test
    public void giveProductAccessTest() {
        // arrange
        vm.enterAdminMode(CORRECT_ID);
        vm.fillProducts();

        // act
        Response responseFromAdmin = vm.giveProduct(1);
        int balanceFromAdmin = vm.getCurrentBalance();
        int productsFromAdmin = vm.getNumberOfProduct();

        // assert
        assertEquals(Response.ILLEGAL_OPERATION, responseFromAdmin);
        assertEquals(DEFAULT_BALANCE, balanceFromAdmin);
        assertEquals(DEFAULT_MAX, productsFromAdmin);
    }

    @Test
    public void giveProductBoundersOfMoneyTest() {
        // arrange
        vm.enterAdminMode(CORRECT_ID);
        vm.fillProducts();
        vm.exitAdminMode();

        putCoins2(50);

        // act
        Response responseOk = vm.giveProduct(20);
        int balanceOk = vm.getCurrentBalance();
        int productsOk = vm.getNumberOfProduct();

        putCoins2(1);
        int startBalanceNotEnoughMoney = vm.getCurrentBalance();

        Response responseNotEnoughMoney = vm.giveProduct(1);
        int balanceNotEnoughMoney = vm.getCurrentBalance();
        int productsNotEnoughMoney = vm.getNumberOfProduct();

        putCoins2(30);
        int startBalanceOverThanExist = vm.getCurrentBalance();

        Response responseOverThanExist = vm.giveProduct(30);
        int balanceOverThanExist = vm.getCurrentBalance();
        int productsOverThanExist = vm.getNumberOfProduct();

        // assert

        assertEquals(Response.OK, responseOk);
        assertEquals(0, balanceOk);
        assertEquals(20, productsOk);

        assertEquals(Response.INSUFFICIENT_MONEY, responseNotEnoughMoney);
        assertEquals(startBalanceNotEnoughMoney, balanceNotEnoughMoney);
        assertEquals(20, productsNotEnoughMoney);

        assertEquals(Response.INSUFFICIENT_PRODUCT, responseOverThanExist);
        assertEquals(startBalanceOverThanExist, balanceOverThanExist);
        assertEquals(20, productsOverThanExist);
    }
}
