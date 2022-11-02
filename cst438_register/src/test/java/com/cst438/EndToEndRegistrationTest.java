package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndRegistrationTest {
    public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/dante/Downloads/chromedriver";
    public static final String URL = "http://localhost:3000";
    public static final String TEST_USER_EMAIL = "dantemoon@csumb.edu";
    public static final String TEST_USER_NAME = "Dante Moon TEST";
    public static final int SLEEP_DURATION = 1000; // 1 second.

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void addStudent() throws Exception{
        Student s = null;
        do{
            s = studentRepository.findByEmail(TEST_USER_EMAIL);
            if(s!=null){
                studentRepository.delete(s);
            }
        } while (s!=null);

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        // Puts an Implicit wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try{
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            //click the add student popup button
            driver.findElement(By.xpath("//button[@id='addstudentpopup']")).click();
            Thread.sleep(SLEEP_DURATION);

            //fill out the info
            driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);
            Thread.sleep(SLEEP_DURATION); //not necessary but I wanted to see the interaction happen on screen slower
            driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
            Thread.sleep(SLEEP_DURATION);
            driver.findElement(By.xpath("//button[@id='Add']")).click();
            Thread.sleep(SLEEP_DURATION);

            //verify the student was actually added by checking the database, could also do this by reading the toast or checking another way
            Student checkStudent = studentRepository.findByEmail(TEST_USER_EMAIL);
            assertNotNull(checkStudent, "Error the student was not added");

        } catch (Exception ex){
            throw ex;
        } finally {
            Student deleteStudent = studentRepository.findByEmail(TEST_USER_EMAIL);
        if(deleteStudent!=null){
            studentRepository.delete(deleteStudent);
        }
            driver.quit();
        }
    }

}