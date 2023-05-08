import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

// This is the class "LoginTest" that includes a main method for testing the login function on Facebook.
public class LoginTest {

    // Here I create a logger instance for this class using the imported LoggerFactory.
    public static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    // The main method which gets executed when the program runs
    public static void main(String[] args) {
        logger.info("Info: Starting login test on Facebook.");

        // Setting the path to the chromedriver.exe
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\IdeaProjects\\Test\\src\\main\\java\\chromedriver.exe");

        // Creating an instance of ChromeOptions and adding desired options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

        // Creating an instance of ChromeDriver with the options
        ChromeDriver driver = new ChromeDriver(options);

        try {
            // Opening the Facebook website
            driver.get("https://www.facebook.com/");
            logger.info("Info: Opening Facebook.");
        } catch (Exception e) {
            logger.error("Error: Error opening Facebook website: {}", e.getMessage());
            driver.quit();
            return;
        }

        // Creating an instance of File with the pathname to the login credentials
        File jsonFile = new File("C:\\temp\\facebook.json");

        String email = null;
        String password = null;

        try {
            // Creating an instance of ObjectMapper to read the JSON file
            ObjectMapper objectMapper = new ObjectMapper();

            // Using a FileInputStream to read the JSON file into a JsonNode object
            JsonNode jsonNode = objectMapper.readTree(jsonFile);

            // Retrieve the username and password from the JsonNode object
            email = jsonNode.get("facebookCredentials").get("email").asText();
            password = jsonNode.get("facebookCredentials").get("password").asText();

        } catch (IOException i) {
            // Print the stack trace of the exception if an error occurs
            i.printStackTrace();
        }

        try {
            // Here we are finding the fields for the email(username) and password, and then we enter the login credentials
            WebElement emailInput = driver.findElement(By.id("email"));
            emailInput.sendKeys(email);
            logger.info("Info: Email entered.");

            WebElement passwordInput = driver.findElement(By.id("pass"));
            passwordInput.sendKeys(password);
            logger.info("Info: Password entered.");

            // Here we click the login button to log in
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            loginButton.click();
            logger.info("Info: You have tried to log in to Facebook.");
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage());
        }

        // Here we verify that the user is logged in to the Facebook homepage.
        String expectedUrl = "https://www.facebook.com/";
        String actualUrl = driver.getCurrentUrl();

        if (actualUrl.equals(expectedUrl)) {
            System.out.println("Login successful");
            logger.info("Info: Login successful.");
        } else {
            System.out.println("Login failed");
            logger.error("Error: Login failed.");
        }
    }
}