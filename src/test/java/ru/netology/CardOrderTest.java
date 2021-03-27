package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Generate.generateDeliveryDate;


public class CardOrderTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldAcceptOrder() {
        CardDelivery user = DataGenerator.Generate.generateUserData("ru");
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] [value]").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] [value]").setValue(generateDeliveryDate(4));
        $("[name='name']").setValue(user.getName());
        $("[name='phone']").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $("[class='button__text']").click();
        $("[data-test-id=success-notification]").waitUntil(Condition.visible, 15000)
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDeliveryDate(4)));
        $("[data-test-id=date] [value]").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] [value]").setValue(generateDeliveryDate(5));
        $("[class='button__text']").click();
        $(byText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(".notification_has-closer .button").click();
        $("[data-test-id=success-notification]").waitUntil(Condition.visible, 15000)
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDeliveryDate(5)));
    }

   @Test
    void shouldNotAcceptOrder() {
        CardDelivery user = DataGenerator.Generate.generateUserData("ru");
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] [value]").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] [value]").setValue(generateDeliveryDate(4));
        $("[name='name']").setValue(user.getName());
        $("[name='phone']").setValue(DataGenerator.Generate.generateInvalidPhoneNumber());
        $("[data-test-id=agreement]").click();
        $("[class='button__text']").click();
        $(".input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
}



