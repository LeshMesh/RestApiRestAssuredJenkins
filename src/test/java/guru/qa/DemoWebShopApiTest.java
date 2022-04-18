package guru.qa;

import com.codeborne.selenide.Condition;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static listeners.CustomAllureListener.withCustomTemplates;
import static org.hamcrest.core.Is.is;

public class DemoWebShopApiTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(withCustomTemplates());
    }

    @Test
    void addWishlistTest() {
        given()
                .log().uri()
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/66/2")
                .then()
                .statusCode(200)
                .log().status()
                .log().body()
                .body("success", is(true))
                .body("updatetopwishlistsectionhtml", is("(1)"))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"));
    }

    @Test
    void noHelpfulReviewTest() {
        given()
                .cookie("ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; NOPCOMMERCE.AUTH=B1DA4AB8F6A2FF6661A3D48C7622862FA79F05B2188334C9325C567FE55BEA26AD41F8095A27CFC561F326FAE00D2E1FE6EB8C1A80A5D4A500B0520BA714420160F860EBA4E2E9C292DAABC008CB7F99213DE2CAC6E987632B8B587577F812596B24C440BAEC60C7CAAE904AAEF8C9FBEDDE355C99BF93C65F4AF6DF5692362F3EA79D649274051F3EAEA6E1AF2CF5C7711382F153BB6EA5D2C5DACF717680BC; Nop.customer=79b6435b-06f4-48aa-ac83-07973d9d3936")
                .formParam("productReviewId", 41)
                .formParam("washelpful", false)
                .log().params()
                .when()
                .post("http://demowebshop.tricentis.com/setproductreviewhelpfulness")
                .then()
                .statusCode(200)
                .log().status()
                .log().body()
                .body("Result", is("Successfully voted"));
    }

    @Test
    void yesHelpfulReviewTest() {
        given()
                .cookie("ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; NOPCOMMERCE.AUTH=B1DA4AB8F6A2FF6661A3D48C7622862FA79F05B2188334C9325C567FE55BEA26AD41F8095A27CFC561F326FAE00D2E1FE6EB8C1A80A5D4A500B0520BA714420160F860EBA4E2E9C292DAABC008CB7F99213DE2CAC6E987632B8B587577F812596B24C440BAEC60C7CAAE904AAEF8C9FBEDDE355C99BF93C65F4AF6DF5692362F3EA79D649274051F3EAEA6E1AF2CF5C7711382F153BB6EA5D2C5DACF717680BC; Nop.customer=79b6435b-06f4-48aa-ac83-07973d9d3936")
                .formParam("productReviewId", 41)
                .formParam("washelpful", true)
                .log().params()
                .when()
                .post("http://demowebshop.tricentis.com/setproductreviewhelpfulness")
                .then()
                .statusCode(200)
                .log().status()
                .log().body()
                .body("Result", is("Successfully voted"));
    }

    @Test
    void logInUserTest() {
        String authorizationCookie =

                given()
                        .formParam("Email", "testsvsv@test.test")
                        .formParam("Password", 111111)
                        .formParam("RememberMe", false)
                        .log().params()
                        .when()
                        .post("http://demowebshop.tricentis.com/login")
                        .then()
                        .statusCode(302)
                        .log().status()
                        .log().body()
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");

        open("http://demowebshop.tricentis.com/");
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));
        open("http://demowebshop.tricentis.com/");
        $(".account").shouldHave(Condition.text("testsvsv@test.test"));
        $(".ico-logout").shouldBe(Condition.visible);
    }
}
