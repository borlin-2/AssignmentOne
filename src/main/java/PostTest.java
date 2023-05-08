import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

// This is the class "PostTest" that includes a main method for testing the post function on Facebook.
public class PostTest {

    // Here I create a logger instance for this class using the imported LoggerFactory.
    public static final Logger logger = LoggerFactory.getLogger(PostTest.class);

    // The main method which gets executed when the program runs
    public static void main(String[] args) throws InterruptedException {
        logger.info("Info: Starting post test on Facebook.");

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

        // Wait for the page to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the "What are you doing" field on the user's homepage
        WebElement createPostButton = driver.findElement(By.xpath("//span[contains(text(), \"Vad gör du just nu\")]"));
        createPostButton.click();
        Thread.sleep(3000);
        logger.info("Info: Preparing to post.");

        // Wait until the text field is visible
        WebDriverWait wait = new WebDriverWait(driver, 10);
        By textFieldLocator = By.xpath("//div[@class='xzsf02u x1a2a7pz x1n2onr6 x14wi4xw x9f619 x1lliihq x5yr21d xh8yej3 notranslate']");
        WebElement textField = wait.until(ExpectedConditions.visibilityOfElementLocated(textFieldLocator));

        // Inserting the text
        textField.sendKeys("Test post.");
        Thread.sleep(3000);
        logger.info("Info: You have entered your post.");

        // Clicking the button to post the text
        WebElement postButton = driver.findElement(By.cssSelector("[aria-label='Publicera']"));
        postButton.click();
        logger.info("Info: You have published your post on Facebook.");

        // Verifying that the post is successfully published and displayed on the user's timeline
        WebElement post = driver.findElement(By.xpath("//span[contains(text(), \"Test post\")]"));
        String postTextDisplayed = post.getText();

        if (postTextDisplayed.equals("Test post.")) {
            System.out.println("Post successfully published and displayed on the user's timeline.");
            logger.info("Info: Post published on Facebook.");
        } else {
            System.out.println("Post not published or displayed on the user's timeline.");
            logger.error("Error: Post not published on Facebook.");
        }
    }
}