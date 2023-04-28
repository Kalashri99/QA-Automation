package com.persistent.daisy.gen;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;	
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.persistent.daisy.core.browser.SeleniumUtils;
		

public class DaisyEntry {
	
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Actions actions;
	
	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		DaisyEntry.driver = driver;
	}

	public static WebDriverWait getWait() {
		return wait;
	}

	public static void setWait(WebDriverWait wait) {
		DaisyEntry.wait = wait;
	}

	public static Actions getActions() {
		return actions;
	}

	public static void setActions(Actions actions) {
		DaisyEntry.actions = actions;
	}
	
	public static void setup() {
		//String strDriverPath= System.getProperty("user.dir").replace("\\src\\com\\persistent\\daisy\\gen", "");
		System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        setDriver(driver);
      	WebDriverWait wait = new WebDriverWait(driver, 100); 
        setWait(wait);
        Actions actions = new Actions(driver);
        setActions(actions);
   }
		

	public static void testSuite() {
				
	}
		

	public static void main(String[] args){
		setup();
		ResourceManager.readProperty();
		testSuite();
	}
}
		
