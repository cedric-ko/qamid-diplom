package ru.netology.diploma.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    // конструктор объекта класса
    private DataHelper() {
    }

    public static final Faker faker = new Faker(new Locale("en"));

    // вложенный класс банковской карты
    @Value
    public static class CreditCard {
        private String cardNumber;
        private String expMonth;
        private String expYear;
        private String cardHolder;
        private String cvc;
    }

    // получение номера одобренной карты
    public static String getApprovedCardNumber() {
        return ("1111 2222 3333 4444");
    }

    // получение номера отклонённой карты
    public static String getDeclinedCardNumber() {
        return ("5555666677778888");
    }

    // получение случайного номера карты
    public static String generateCardNumber() {
        return faker.business().creditCardNumber();
    }

    // получение текущего месяца
    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    // получение текущего года
    public static String getCurrentYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    // получение валидного владельца
    public static String generateValidHolderName() {
        String holderName = faker.name().fullName();
        return holderName;
    }

    // получение валидного CVC/CVV
    public static String generateValidCvc() {
        return faker.number().digits(3);
    }
}

