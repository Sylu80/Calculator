package com.easypay.suite.AddEp;

import java.awt.AWTException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.easypay.util.TestUtil;

public class VerifyCalculation extends TestSuiteBase{
	
	String runmodes[]=null;
	static int count=-1;
	//static boolean pass=false;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	WebDriver driver = null;
	Connection conn = null;
	DatabaseMetaData meta = null;
	PreparedStatement pstmt = null;
	ResultSet rset = null;
	
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip(){
		System.out.println("checkTestSkip in VerifyCalculation");
		System.out.println("this.getClass().getSimpleName()))" +this.getClass().getSimpleName());
		System.out.println("calOpnXls" + calOpnXls);
		if(! TestUtil.isTestCaseRunnable(calOpnXls,this.getClass().getSimpleName() )){
			System.out.println("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		// load the runmodes of the tests
		runmodes=TestUtil.getDataSetRunmodes(calOpnXls, this.getClass().getSimpleName());
	}
	
	

	@Test(dataProvider="getTestData")
	public void TestCalculator(String N1, String N2,
			                          String operation, String expectedAmount,
			                          String browserType) throws InterruptedException, AWTException{
		// test the runmode of current dataset
		System.out.println("start dataProvider=getTestData");
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")){
			System.out.println("start dataProvider=getTestData");
			skip=true;
			throw new SkipException("Runmode for test set data set to NOOOOOO "+count);
		}
		
		System.out.println(" Executing VerifyCalculation");
		System.out.println(" ******************** Executing VerifyCalculation");
		
		
		//String initialUrl = CONFIG.getProperty("initialUrl");
		
		
		System.out.println("THE BROWSER TYPE IS " +browserType);
		System.out.println("************* VerifyCalculation");
		
		/*Opening browser based on the the browser type
		 * and url and the passtype stored in
		 * CONFIG file
		 * 
		 */
		String initialUrl = "http://web2.0calc.com/";
		if (browserType.equalsIgnoreCase("Mozilla")) {
			driver  = openBrowserMozilla();
			
			driver.get(initialUrl);
		}
		else if(browserType.equalsIgnoreCase("IE"))  {
			driver = openBrowserIE(initialUrl);
	    }
		
		else if(browserType.equalsIgnoreCase("Chrome"))  {
			driver = openBrowserChrome();
			driver.get(initialUrl);
			
		}
		//Thread.sleep(3000);
		
		for ( int i = 0; i < N1.length(); ++i ) {
            char c = N1.charAt( i );
            String xpath1 = "//button[@id='Btn";
            String xpath2 = String.valueOf(c);
            String xpath3 ="']";
            String xpath = xpath1+xpath2+xpath3;
            driver.findElement(By.xpath(xpath)).click();
        }
        if (operation.equals("Addition")) {
		 driver.findElement(By.xpath(".//*[@id='BtnPlus']")).click();
        } else if(operation.equals("Multiplication")) {
        	driver.findElement(By.xpath(".//*[@id='BtnMult']")).click();
        }
		
		for ( int i = 0; i < N2.length(); ++i ) {
            char c = N2.charAt( i );
            String xpath1 = "//button[@id='Btn";
            String xpath2 = String.valueOf(c);
            String xpath3 ="']";
            driver.findElement(By.xpath(xpath1+xpath2+xpath3)).click();
        }
		
		driver.findElement(By.xpath(".//*[@id='BtnCalc']")).click();
		Thread.sleep(1000);
		
		String actualAmount = driver.findElement(By.xpath(".//input[@id='input']")).getText();
		Assert.assertEquals(actualAmount,expectedAmount , "Sum amount not equal");
		
		
		 Thread.sleep(5000);
		
		 
		 
		
		//driver.quit();
		
		/**********************************/
		
		System.out.println("************** End of executing VerifyCalculation");
		
	}
	
	@AfterMethod
	public void reportDataSetResult(){
		System.out.println("Inside After Mathod");
		if(skip) {
			TestUtil.reportDataSetResult(calOpnXls, this.getClass().getSimpleName(), count+2, "SKIP");
			System.out.println("Inside After Mathod, Skipping");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(calOpnXls, this.getClass().getSimpleName(), count+2, "FAIL");
			System.out.println("Inside After Mathod,Fail");
		}
		else {
			TestUtil.reportDataSetResult(calOpnXls, this.getClass().getSimpleName(), count+2, "PASS");
			System.out.println("Inside After Mathod, Pass");
		}
		skip=false;
		fail=false;
		

	}
	
	
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(calOpnXls, "Test Cases", TestUtil.getRowNum(calOpnXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(calOpnXls, "Test Cases", TestUtil.getRowNum(calOpnXls,this.getClass().getSimpleName()), "FAIL");
	
	}

	@DataProvider
	public Object[][] getTestData(){
		System.out.println("start getTestData");
		return TestUtil.getData(calOpnXls, this.getClass().getSimpleName()) ;
		
	}

}
