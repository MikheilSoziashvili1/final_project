import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.http.ContentType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class Tests {

    WebDriver driver;
    static String userName = "ms" + (int) (Math.random() * (9999 - 1000)) + 1000;
    static String password = "Aa1234_^5Zz";

    @FindBy(id = "userName")
    WebElement webSiteUsername;

    @FindBy(id = "password")
    WebElement webSitePassword;

    @FindBy(id = "login")
    WebElement login;

    @FindBy(id = "userName-value")
    WebElement userNameValue;

    @FindBy(id = "gotoStore")
    WebElement gotoStore;

    @FindBy(xpath = "//span[starts-with(@id, 'see-book-You')]")
    WebElement bookName;

    @FindBy(xpath = "//button[text()='Add To Your Collection']")
    WebElement addToCollection;

    @FindBy(xpath = "//li[@id = 'item-3']/span[text()='Profile']")
    WebElement profile;

    @BeforeClass
    public void config() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(priority = 0)
    public static void createUser() {
        given().contentType(ContentType.JSON)
                .body("{\"userName\": \"" + userName + "\",\n \"password\": \"" + password + "\"\n }")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/User")
                .then()
                .assertThat()
                .body(containsString("[]"));
    }

    @Test(priority = 1)
    public static void generateToken() {
        given().contentType(ContentType.JSON)
                .body("{\"userName\": \"" + userName + "\",\n \"password\": \"" + password + "\"\n }")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/GenerateToken")
                .then()
                .assertThat()
                .body(containsString("\"User authorized successfully.\""))
                .and()
                .body(containsString("Success"));
    }

    @Test(priority = 2)
    public static void checkAuthorized() {
        given().contentType(ContentType.JSON)
                .body("{\"userName\": \"" + userName + "\",\n \"password\": \"" + password + "\"\n }")
                .when()
                .post("https://bookstore.toolsqa.com/Account/v1/Authorized")
                .then()
                .assertThat()
                .body(containsString("true"));
    }

    @Test(priority = 3)
    public void test() {
        driver.get("https://demoqa.com/login");
        driver.manage().window().maximize();

        webSiteUsername.sendKeys(userName);
        webSitePassword.sendKeys(password);
        login.click();
        Assert.assertEquals(userNameValue.getText(), userName);
        gotoStore.click();
        bookName.click();
        addToCollection.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", profile);
        profile.click();

        Assert.assertEquals(bookName.getText(), "You Don't Know JavaScript");
    }
}
