package ru.netology.diploma.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TourPage {
    // элементы страницы
    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement buyInCreditButton = $(byText("Купить в кредит"));

    // переход к оплате по карте
    public PaymentPage goToPayment() {
        buyButton.click(); // нажимаем кнопку "Купить"
        return new PaymentPage(); // и переходим на "Оплата по карте"
    }

    // переход к покупке в кредит
    public CreditPage goToCredit() {
        buyInCreditButton.click(); //нажимаем кнопку "Купить в кредит"
        return new CreditPage(); // и переходим на "Кредит по данным карты"
    }
}