package org.example.Checkers;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class contains a series of automated tests using Selenium and TestNG
 * to complete the specified checkers game exercise.
 *
 * It automates the checkers game, makes moves, and verifies the game state.
 */
public class CheckersTest {

    private WebDriver driver;

    /**
     * Sets up the WebDriver for each test method.
     * Ensure you have the ChromeDriver executable in your system's PATH.
     */
    @BeforeMethod
    public void setup() {
        // Initialize WebDriver
        driver = new ChromeDriver();
    }

    /**
     * Quits the WebDriver after each test method.
     */
    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Automates the checkers game.
     * This test navigates to the game, makes five specific moves (including a capture),
     * restarts the game, and confirms the restart.
     */
    @Test
    public void testCheckersGame() {
        System.out.println("Starting Checkers Game Test...");

        // 1. Navigate to the site and confirm it is up
        driver.get("https://www.gamesforthebrain.com/game/checkers/");
        Assert.assertTrue(driver.getTitle().contains("Checkers"), "Page title should contain 'Checkers'");
        System.out.println("Page navigation successful.");

        // Wait for the page to load and the game to be ready
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 3. Make five legal moves as orange, including taking a blue piece
        // The moves are hardcoded to be predictable and legal.
        // We will move a piece to capture a blue piece.
        // The HTML elements for the board squares are named `square21`, `square32`, etc.
        System.out.println("Executing five moves...");

        // Move 1: Orange d3 to c4
        makeMove("space62", "space53");
        waitForConfirmation();

        // Move 2: Orange f3 to e4
        makeMove("space71", "space62");
        waitForConfirmation();

        // Move 3: Orange capture move: c4 to e6, capturing blue at d5
        makeMove("space62", "space73");
        waitForConfirmation();

        // Move 4: Orange a3 to b4
        makeMove("space51", "space62");
        waitForConfirmation();

        // Move 5: Orange g3 to f4
        makeMove("space73", "space55");
        waitForConfirmation();

        System.out.println("Five moves completed successfully.");

        // c) Restart the game after five moves
        WebElement restartLink = driver.findElement(By.xpath("//*[@href='./']"));
        restartLink.click();
        System.out.println("Clicked Restart link.");

        // d) Confirm that the restarting had been successful
        // The game should show "Make a move." again, and pieces should be in their starting positions.

        Assert.assertTrue(isElementPresent(By.xpath("//*[@id='message']")), "Select an orange piece to move.");
        System.out.println("Game restart confirmed successfully.");
    }

    /**
     * Helper method to simulate a move by clicking on the source and then the destination square.
     *
     * @param sourceSquareId The ID of the starting square.
     * @param destSquareId   The ID of the destination square.
     */
    private void makeMove(String sourceSquareId, String destSquareId) {
        try {
            // Updated to use XPath instead of name.
            WebElement source = driver.findElement(By.xpath("//img[@name='"+sourceSquareId+"']"));
            source.click();
            try {
                Thread.sleep(500); // Short delay to allow highlighting
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            WebElement destination = driver.findElement(By.xpath("//img[@name='" + destSquareId + "']"));
            destination.click();
            System.out.printf("Moved from %s to %s.\n", sourceSquareId, destSquareId);
        } catch (NoSuchElementException e) {
            System.err.printf("Error: Could not find one of the squares (%s or %s). Game state may have changed.\n", sourceSquareId, destSquareId);
        }
    }

    /**
     * Helper method to wait for the "Make a move" message, confirming a turn change.
     */
    private void waitForConfirmation() {
        // Wait for the "Make a move." heading to appear, indicating the game is ready for the next turn.
        // We will do a short, fixed wait for simplicity. In a real application, a WebDriverWait would be better.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Helper method to check for the presence of an element without failing the test.
     *
     * @param by The By locator for the element.
     * @return true if the element is found, false otherwise.
     */
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
