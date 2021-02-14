import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FreeCRMTest {

    //This project is for failed test cases with attach screenshort in the extent report.

    public WebDriver driver;
    public ExtentReports extent;
    public ExtentTest extentTest;


    @BeforeTest
    public void setExtent() {
        extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/ExtentReport.html", true);
        extent.addSystemInfo("Host Name", "Parita win");
        extent.addSystemInfo("User Name", "Parita AutomationLabs");
        extent.addSystemInfo("Environment", "QA");
        //user.dir means project directory,where project is available.means project path
        //Any previous report is there ,plz replace with new if we put true
    }

    @AfterTest
    public void endReport() {
        extent.flush();//means close the connection with it.
        extent.close();
    }

    public static String getScreenShort(WebDriver driver, String screenshortname) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        //after execution,you

        String destination = System.getProperty("user.dir") + "/FailedTestsScreenShorts/" + screenshortname + dateName
                + ".png";
        File finalDestination = new File(destination);
        //Fileutils from common io apache poi
        FileUtils.copyFile(source, finalDestination);
        return destination;

    }


    @BeforeMethod
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Parita Patel\\Desktop\\WebDrivers\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get("https://freecrm.com");
       // driver.findElement(By.xpath("//div[@aria-label='Dismiss']")).click();
    }

    @Test
    public void freeCRMTitleTest() {
        extentTest = extent.startTest("freeCRMTest");//This line we have to write in each n every test cases
        // after test case starts
        String title = driver.getTitle();
        System.out.println(title);
        Assert.assertEquals(title, "#1 Free CRM customer relationship management software cloud123");
    }

    @Test
    public void freeCRMLoginTest(){
        extentTest = extent.startTest("freeCRMLoginTest");
        boolean enabled = driver.findElement(By.xpath("//*[@class='btn btn-primary btn-xs-2 btn-shadow btn-rect btn-icon btn-icon-left123']")).isEnabled();
        Assert.assertTrue(enabled);


    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        //ITTestresult will stores all pass/fail testcases
        //pick those testcases which are failed and attach it.
        if (result.getStatus()== ITestResult.FAILURE){
            extentTest.log(LogStatus.FAIL,"TEST CASE FAILED IS"+result.getName());//to add name in extent report
            extentTest.log(LogStatus.FAIL,"TEST CASE FAILED IS"+result.getThrowable());//to add error/exception in extent report
            //super class of error and exception is throwable
            //next step is to attach screen short

            String screenShortPath = FreeCRMTest.getScreenShort(driver,result.getName());
            extentTest.log(LogStatus.FAIL,extentTest.addScreenCapture(screenShortPath));//to add screenshort in extent report
            //In IQ they can ask which method is use to add screenshort in extent report ANS is :: addscreencapture

        }else if (result.getStatus()==ITestResult.SKIP){
            extentTest.log(LogStatus.SKIP,"TEST CASES SKIPPED IS"+result.getName());
        }
        else  if (result.getStatus()==ITestResult.SUCCESS){
            extentTest.log(LogStatus.PASS,"TEST CASE PASSES IS"+ result.getName());
        }
        extent.endTest(extentTest);//ending test and ends the current test and prepare to create html report
        driver.quit();


    }
}

