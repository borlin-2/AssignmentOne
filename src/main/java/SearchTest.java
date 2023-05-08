import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

// This is the class "SearchTest" that includes a main method for testing the search function on Facebook.
public class SearchTest {

    // Here I create a logger instance for this class using the imported LoggerFactory.
    public static final Logger logger = LoggerFactory.getLogger(SearchTest.class);

    // The main method which gets executed when the program runs
    public static void main(String[] args) throws InterruptedException {
        logger.info("Info: Starting search test on Facebook.");

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

        // Enter the search query in the search bar
        try {
            WebElement searchBox = driver.findElement(By.xpath("//input[@aria-label='Sök på Facebook:']"));
            searchBox.sendKeys("Luleå tekniska universitet");
            logger.info("Info: Searching on Facebook.");
            Thread.sleep(3000);

            // Click on the "Search" button
            searchBox.sendKeys(Keys.RETURN);

            String result = "Luleå tekniska universitet";

            // Verify that the search results are accurate and relevant
            boolean isResultPresent = driver.getPageSource().contains(result);
            if (isResultPresent) {
                System.out.println("Search results are accurate and relevant.");
                logger.info("Info: The search was a success!");
            } else {
                System.out.println("Search results are not accurate and relevant.");
                logger.error("Error: The search was not a success!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
