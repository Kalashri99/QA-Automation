package com.persistent.daisy.gen;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import com.persistent.daisy.base.Logs;
import com.persistent.daisy.base.Report;
import com.persistent.daisy.base.Configurations;
import com.persistent.daisy.core.browser.SeleniumUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

//import com.persistent.daisy.core.browser.SeleniumUtilIntf;

public class TestUtility {

	static {
		System.setProperty("config", "Default");
		System.setProperty("is_running_extractedcode", "true");
		Configurations.getInstance().initialize();		
	}

	private SeleniumUtils createExecutor() {
		SeleniumUtils ex1 = new SeleniumUtils();

		String target = Configurations.getInstance().getAttribute("target");
		String browser = Configurations.getInstance().getAttribute("browser");
		int timeout = Configurations.getInstance().getAttributeInt("timeout");
		String platform = Configurations.getInstance().getAttribute("platform");
		String saucelabsUser = Configurations.getInstance().getAttribute("saucelabs_user");
		String sauceLabsAccessKey = Configurations.getInstance().getAttribute("saucelabs_access_key");
		String browserVersion = Configurations.getInstance().getAttribute("browserVersion");

		if (target.equalsIgnoreCase("saucelabs")) {
			ex1.setSauceLabsBrowser(browser, timeout, platform, saucelabsUser, sauceLabsAccessKey, browserVersion);
		} else if (target.equalsIgnoreCase("remote")) {
			ex1.setRemoteBrowser(browser, timeout, platform);
		} else {
			ex1.setBrowser(browser, timeout);
		}

		return ex1;
	}

// In case you want to remove SeleniumUtils from extraction just uncomment this code and use.
//	public WebDriver getDriver(String browser) {
//
//		if (browser.equalsIgnoreCase("IE")) {
//			WebDriverManager.iedriver().setup();
//			return new InternetExplorerDriver();
//		} else if (browser.equalsIgnoreCase("CHROME")) {
//			WebDriverManager.chromedriver().setup();
//			return new ChromeDriver();
//		} else if (browser.equalsIgnoreCase("EDGE")) {
//			WebDriverManager.edgedriver().setup();
//			return new EdgeDriver();
//		} else if (browser.equalsIgnoreCase("HTMLUNIT")) {
//			return new HtmlUnitDriver();
//		} else {
//			WebDriverManager.firefoxdriver().setup();
//			return new FirefoxDriver();
//		}
//	}

	@DataProvider(name = "getDrivers")
	public Object[][] getDrivers() {

		SeleniumUtils utils = createExecutor();

		String filename = this.getClass().getSimpleName();		
		Logs.getLog().setScriptName(filename);   
		Report.getReport().logTestName(filename);
		
//		In case you want to remove SeleniumUtils from extraction just uncomment this code and change accordingly.
//		WebDriver driver = getDriver("CHROME");
//		WebDriverWait wait = new WebDriverWait(driver, 100);
//		Actions actions = new Actions(driver);

		return new Object[][] { { utils } };
	}

}
	
