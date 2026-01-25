package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.diploma.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    // заголовок страницы
    private SelenideElement paymentHeader = $(byText("Оплата по карте"));

    // поля для заполнения
    private SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement holderField = $(byText("Владелец")).parent().$(".input__control");
    private SelenideElement cvcField = $("[placeholder='999']");

    // кнопка "Продолжить"
    private SelenideElement continueButton = $(byText("Продолжить"));

    // метод, определяющий видимость заголовка страницы
    public void displayPaymentHeader() {
        paymentHeader.should(Condition.visible);
    }

    // подстрочные уведомления об ошибках заполнения полей
    private SelenideElement cardNumberWrongFormat = $(byText("Номер карты")).parent().$(".input__sub");
    private SelenideElement monthWrongFormat = $(byText("Месяц")).parent().$(".input__sub");
    private SelenideElement yearError = $(byText("Год")).parent().$(".input__sub");
    private SelenideElement holderError = $(byText("Владелец")).parent().$(".input__sub");
    private SelenideElement cvcWrongFormat = $(byText("CVC/CVV")).parent().$(".input__sub");

    // уведомления о результате платежа
    private SelenideElement errorNotification = $(".notification_status_error [class='notification__title']");
    private SelenideElement successNotification = $(".notification_status_ok [class='notification__title']");

    // заполнение формы валидными данными
    public void fillFormWithValidData() {
        setCardNumber(DataHelper.getApprovedCardNumber());
        setMonth(DataHelper.getCurrentMonth());
        setYear(DataHelper.getCurrentYear());
        setHolder(DataHelper.generateValidHolderName());
        setCvc(DataHelper.generateValidCvc());
    }

    // заполнение всех полей, кроме поля "Номер карты", валидными данными
    public void fillFormWithValidDataExceptCardNumber() {
        setMonth(DataHelper.getCurrentMonth());
        setYear(DataHelper.getCurrentYear());
        setHolder(DataHelper.generateValidHolderName());
        setCvc(DataHelper.generateValidCvc());
    }

    public void setCardNumber(String cardNumber) {
        cardNumberField.setValue(cardNumber);
    }

    public void setMonth(String month) {
        monthField.setValue(month);
    }

    public void setYear(String year) {
        yearField.setValue(year);
    }

    public void setHolder(String holder) {
        holderField.setValue(holder);
    }

    public void setCvc(String cvc) {
        cvcField.setValue(cvc);
    }

    public void clickContinue() {
        continueButton.click();
    }

    public void notificationOfCardWrongFormat(String expectedText) {
        cardNumberWrongFormat.should(Condition.visible).shouldHave(Condition.text(expectedText));
    }

    public void notificationOfMonthWrongFormat(String expectedText) {
        monthWrongFormat.should(Condition.visible).shouldHave(Condition.text(expectedText));
    }

    public void yearErrorNotification(String expectedText) {
        yearError.should(Condition.visible).shouldHave(Condition.text(expectedText));
    }

    public void holderErrorNotification(String expectedText) {
        holderError.should(Condition.visible).shouldHave(Condition.text(expectedText));
    }

    public void notificationOfCvcWrongFormat(String expectedText) {
        cvcWrongFormat.should(Condition.visible).shouldHave(Condition.text(expectedText));
    }

    public void notificationOfError(String expectedText) {
        errorNotification.should(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.text(expectedText));
    }

    public void notificationOfSuccess(String expectedText) {
        successNotification.should(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.text(expectedText));
    }

    // метод очистки поля, так как заполненные поля не принимают новые данные
    private void clearField(SelenideElement field) {
        field.click();
        field.sendKeys(Keys.SHIFT, Keys.HOME);
        field.sendKeys(Keys.DELETE);
    }

    // очистка всех полей
    public void clearAllFields() {
        clearField(cardNumberField);
        clearField(monthField);
        clearField(yearField);
        clearField(holderField);
        clearField(cvcField);
    }

    // очистка поля "Месяц"
    public void clearMonthField() {
        clearField(monthField);
    }

    // очистка поля "Год"
    public void clearYearField() {
        clearField(yearField);
    }

    // очистка поля "Владелец"
    public void clearHolderField() {
        clearField(holderField);
    }

    // очистка поля "CVC/CVV"
    public void clearCvcField() {
        clearField(cvcField);
    }
}
