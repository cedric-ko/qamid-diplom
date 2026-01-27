package ru.netology.diploma.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.diploma.data.DataHelper;
import ru.netology.diploma.data.SQLHelper;
import ru.netology.diploma.page.PaymentPage;
import ru.netology.diploma.page.TourPage;

import static com.codeborne.selenide.Condition.visible;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.diploma.data.SQLHelper.cleanAllTables;

public class PaymentTest {
    private PaymentPage paymentPage;

    @BeforeAll
    // открываем страницу продажи тура перед запуском тестов
    public static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        var tourPage = Selenide.open("http://localhost:8080", TourPage.class);
    }

    @BeforeEach
    // перед каждым тестом открываем страницу "Оплата по карте"
    void openPaymentPage() {
        var tourPage = new TourPage();
        paymentPage = tourPage.goToPayment();
    }

    @AfterEach
    // обновляем страницу после каждого теста
    void refreshWebPage() {
        Selenide.refresh();
    }
    // очищаем табицы после теста
    static void tearDown() {
        cleanAllTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("1. Переход к форме покупки «Оплата по карте»")
    void shouldGoToPaymentByCard() {
        paymentPage.displayPaymentHeader();
    }

    @Test
    @DisplayName("3. Отправка формы с валидными данными")
    void shouldSuccessfullyPayWithValidData() {
        paymentPage.fillFormWithValidData();
        paymentPage.clickContinue();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("4. Отправка формы с данными карты DECLINED")
    void shouldGetDenialWithDeclinedCard() {
        paymentPage.setCardNumber(DataHelper.getDeclinedCardNumber());
        paymentPage.fillFormWithValidDataExceptCardNumber();
        paymentPage.clickContinue();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("5. Отправка формы с номером карты, не относящимся к тестовым данным")
    void shouldGetDenialWithRandomCard() {
        paymentPage.setCardNumber(DataHelper.generateCardNumber());
        paymentPage.fillFormWithValidDataExceptCardNumber();
        paymentPage.clickContinue();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("6. Отправка формы с 15 цифрами в номере карты")
    void shouldShowWrongCardFormatNotificationFor15Digits() {
        paymentPage.setCardNumber("111122223333444");
        paymentPage.fillFormWithValidDataExceptCardNumber();
        paymentPage.clickContinue();
        paymentPage.notificationOfCardWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("7. Отправка формы с пустым полем номер карты")
    void shouldShowWrongCardFormatNotificationForEmptyField() {
        paymentPage.setCardNumber(null);
        paymentPage.fillFormWithValidDataExceptCardNumber();
        paymentPage.clickContinue();
        paymentPage.notificationOfCardWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("8. Отправка формы с буквой латиницы в поле номера карты")
    void shouldShowWrongCardFormatNotificationForLatinLetter() {
        paymentPage.setCardNumber("111122223333444a");
        paymentPage.fillFormWithValidDataExceptCardNumber();
        paymentPage.clickContinue();
        paymentPage.notificationOfCardWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("9. Отправка формы с буквой кириллицы в поле номера карты")
    void shouldShowWrongCardFormatNotificationForCyrillicLetter() {
        paymentPage.setCardNumber("111122223333444ф");
        paymentPage.fillFormWithValidDataExceptCardNumber();
        paymentPage.clickContinue();
        paymentPage.notificationOfCardWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("10. Отправка формы со спецсимволом в поле номера карты")
    void shouldShowWrongCardFormatNotificationForSimbol() {
        paymentPage.setCardNumber("111122223333444@");
        paymentPage.fillFormWithValidDataExceptCardNumber();
        paymentPage.clickContinue();
        paymentPage.notificationOfCardWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("11. Отправка формы с нулями в поле месяца")
    void shouldShowWrongMonthFormatNotificationFor00() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth("00");
        paymentPage.clickContinue();
        paymentPage.notificationOfMonthWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("12. Отправка формы с числом «13» в поле месяца")
    void sshouldShowWrongMonthFormatNotificationFor13() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth("13");
        paymentPage.clickContinue();
        paymentPage.notificationOfMonthWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("13. Отправка формы с одной цифрой в поле месяца")
    void shouldShowWrongMonthFormatNotificationForSingleDigit() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth("1");
        paymentPage.clickContinue();
        paymentPage.notificationOfMonthWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("14. Отправка формы с пустым полем месяца")
    void shouldShowWrongMonthFormatNotificationForEmpty() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth(null);
        paymentPage.clickContinue();
        paymentPage.notificationOfMonthWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("15. Отправка формы с латинской буквой в поле месяца")
    void shouldShowWrongMonthFormatNotificationForLatinLetter() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth("a1");
        paymentPage.clickContinue();
        paymentPage.notificationOfMonthWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("16. Отправка формы с кириллической буквой в поле месяца")
    void shouldShowWrongMonthFormatNotificationForCyrillicLetter() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth("ф1");
        paymentPage.clickContinue();
        paymentPage.notificationOfMonthWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("17. Отправка формы со спецсимволом в поле месяца")
    void shouldShowWrongMonthFormatNotificationForSpecialChar() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth("@1");
        paymentPage.clickContinue();
        paymentPage.notificationOfMonthWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("18. Отправка формы с прошедшей датой")
    void shouldShowYearErrorForExpiredTerm() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearMonthField();
        paymentPage.setMonth("12");
        paymentPage.clearYearField();
        paymentPage.setYear("25");
        paymentPage.clickContinue();
        paymentPage.yearErrorNotification("Истёк срок действия карты");
    }

    @Test
    @DisplayName("19. Отправка формы с одной цифрой в поле года")
    void shouldShowYearErrorForSingleDigit() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearYearField();
        paymentPage.setYear("6");
        paymentPage.clickContinue();
        paymentPage.yearErrorNotification("Неверный формат");
    }

    @Test
    @DisplayName("20. Отправка формы с пустым полем года")
    void shouldShowYearErrorForEmptyField() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearYearField();
        paymentPage.setYear(null);
        paymentPage.clickContinue();
        paymentPage.yearErrorNotification("Неверный формат");
    }

    @Test
    @DisplayName("21. Отправка формы с латинской буквой в поле года")
    void shouldShowYearErrorForLatinLetter() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearYearField();
        paymentPage.setYear("a6");
        paymentPage.clickContinue();
        paymentPage.yearErrorNotification("Неверный формат");
    }

        @Test
        @DisplayName("22. Отправка формы с кириллической буквой в поле года")
        void shouldShowYearErrorForCyrillicLetter() {
            paymentPage.fillFormWithValidData();
            paymentPage.clearYearField();
            paymentPage.setYear("ф6");
            paymentPage.clickContinue();
            paymentPage.yearErrorNotification("Неверный формат");
        }

    @Test
    @DisplayName("23. Отправка формы со спецсимволом в поле года")
    void shouldShowYearErrorForSimbol() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearYearField();
        paymentPage.setYear("@6");
        paymentPage.clickContinue();
        paymentPage.yearErrorNotification("Неверный формат");
    }

    @Test
    @DisplayName("24. Отправка формы с одной буквой в поле владельца")
    void shouldSuccessfullyPayWithSingleLetterHolder() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearHolderField();
        paymentPage.setHolder("A");
        paymentPage.clickContinue();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("25. Отправка формы со значением из 31 символа в поле владельца")
    void shouldShowHolderErrorFor31Chars() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearHolderField();
        paymentPage.setHolder("A".repeat(31)); // создание значения из 31 символа
        paymentPage.clickContinue();
        paymentPage.holderErrorNotification("Неверный формат");
    }

    @Test
    @DisplayName("26. Отправка формы с пустым полем владельца")
    void shouldShowHolderRequiredErrorForEmpty() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearHolderField();
        paymentPage.setHolder(null);
        paymentPage.clickContinue();
        paymentPage.holderErrorNotification("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("27. Отправка формы с кириллической буквой в поле владельца")
    void shouldShowHolderErrorForCyrillicLetter() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearHolderField();
        paymentPage.setHolder("Иванов");
        paymentPage.clickContinue();
        paymentPage.holderErrorNotification("Неверный формат");
    }

    @Test
    @DisplayName("28. Отправка формы со спецсимволом в поле владельца")
    void shouldShowHolderErrorForSimbol() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearHolderField();
        paymentPage.setHolder("John Smith@");
        paymentPage.clickContinue();
        paymentPage.holderErrorNotification("Неверный формат");
    }

    @Test
    @DisplayName("29. Отправка формы с цифрой в поле владельца")
    void shouldShowHolderErrorForDigit() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearHolderField();
        paymentPage.setHolder("John Sm1th");
        paymentPage.clickContinue();
        paymentPage.holderErrorNotification("Неверный формат");
    }

    @Test
    @DisplayName("30. Отправка формы с одной цифрой в поле кода валидации")
    void shouldShowCVCWrongFormatNotificationForSingleDigit() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearCvcField();
        Faker faker = new Faker();
        String singleDigit = faker.number().digit();
        paymentPage.setCvc(singleDigit);
        paymentPage.clickContinue();
        paymentPage.notificationOfCvcWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("31. Отправка формы с двумя цифрами в поле кода валидации")
    void shouldShowCVVWrongFormatNotificationForTwoDigits() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearCvcField();
        Faker faker = new Faker();
        String twoDigits = faker.number().digits(2);
        paymentPage.setCvc(twoDigits);
        paymentPage.clickContinue();
        paymentPage.notificationOfCvcWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("32. Отправка формы с пустым полем кода валидации")
    void shouldShowCVVWrongFormatNotificationForEmptyField() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearCvcField();
        paymentPage.setCvc(null);
        paymentPage.clickContinue();
        paymentPage.notificationOfCvcWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("33. Отправка формы с кириллической буквой в поле кода валидации")
    void shouldShowCVVWrongFormatNotificationForCyrillicLetter() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearCvcField();
        paymentPage.setCvc("ф12");
        paymentPage.clickContinue();
        paymentPage.notificationOfCvcWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("34. Отправка формы с латинской буквой в поле кода валидации")
    void shouldShowCVVWrongFormatNotificationForLatinLetter() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearCvcField();
        paymentPage.setCvc("a12");
        paymentPage.clickContinue();
        paymentPage.notificationOfCvcWrongFormat("Неверный формат");
    }

    @Test
    @DisplayName("35. Отправка формы со спецсимволом в поле кода валидации")
    void shouldShowCVVWrongFormatNotificationForSimbol() {
        paymentPage.fillFormWithValidData();
        paymentPage.clearCvcField();
        paymentPage.setCvc("@12");
        paymentPage.clickContinue();
        paymentPage.notificationOfCvcWrongFormat("Неверный формат");
    }
}