package com.persistent.daisy.gen;
		
import com.persistent.daisy.core.browser.BrowserConfig;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import com.persistent.daisy.base.DynamicGenericXpath;
import com.persistent.daisy.base.Report;
import com.persistent.daisy.base.Utility;
import com.persistent.daisy.command.StringCmd;
import com.persistent.daisy.core.ActionData;
import com.persistent.daisy.core.CustomException;
import com.persistent.daisy.core.Executor;
import com.persistent.daisy.core.ExtLoader;
import com.sun.glass.events.KeyEvent;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import org.openqa.selenium.StaleElementReferenceException;
import atu.testrecorder.ATUTestRecorder;
import org.testng.util.Strings;

public class DaisyUtility extends BrowserConfig{
	
	private enum Locators {
		xpath, link, id, className, name, tag, cssSelector, label_name, label_for
	}
	public String random = "";
	public StringCmd stringCmd = null;
	static boolean flagForVerify = false;
	static boolean flagForVerifyCompare= false;
	public String tempGlobalVar = "";
	ATUTestRecorder recorder; 
	
		

	public void ClearText(By locator,Map<String, Object> argumentValues) {
		WebElement webelement = driver.findElement((By)((locator)));
		webelement.clear();
	}
		

	public void RefreshBrowser(Map<String,Object>  argumentValues) {   
		//getcallerMethod1(argumentValues);
		driver.navigate().refresh();
		return;
	}
		

	public void MixSendKeyMethod1(By locator,Keys firstKey,ActionData actionData, String secKey,
		Map<String, Object> argumentValues) {
		// TODO Auto-generated method stub
		//getcallerMethod1(argumentValues);	
	 	WebElement webelement=driver.findElement((By)((locator)));		
        String mixKeys = Keys.chord(firstKey,secKey);
        webelement.sendKeys(mixKeys);
        Report.getReport().logPass("Sendkey is working for " +actionData.getData()); 
        System.out.println("Mixed sendKey is working for "+actionData.getData());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
		

	public void MixSendKeyMethod2(By locator, Keys firstKey, ActionData actionData, Keys secKey1,
		Map<String, Object> argumentValues) {
		// TODO Auto-generated method stub
		//getcallerMethod1(argumentValues);
		String mixKeys = Keys.chord(firstKey, secKey1 );
		WebElement webelement=driver.findElement((By)((locator)));
		webelement.sendKeys(mixKeys);
        //System.out.println("Mixed sendKey is working ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }         
        Report.getReport().logPass("Sendkey is working for " +actionData.getData());         
        System.out.println("[log] Succesfully run sendKeys for : " +actionData.getData());  	
	}
		

	public void MixSendKeyMethod3(By locator,Keys keyCode,ActionData actionData, Map<String, Object> argumentValues) {
		// TODO Auto-generated method stub
		//getcallerMethod1(argumentValues);
		 WebElement webelement=driver.findElement((By)((locator)));
		 webelement.sendKeys(keyCode);
         //System.out.println("SendKey is working "+keyCode);
         Report.getReport().logPass("Sendkey is working for " +actionData.getData()); 
         System.out.println("[log] Succesfully run sendKeys for  : " +actionData.getData());
         try {
             Thread.sleep(2000);
         } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
	}
		

	public void buttonClick(By locator, Map<String,Object> argumentValues) {
		int count = 0;
		boolean clicked = false;
		while (count < 3 && !clicked)
		{
			try 
			{
				waitForPageLoaded();
				try {
					waitForElementtoBeVisible(locator, argumentValues);
				} catch (CustomException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				waitForElementtoBeClickable(locator, argumentValues);
				System.out.println("*//[log] Inside Try Block of ButtonClick method--->Step1");
				WebElement button = driver.findElement(locator);
				System.out.println("*//[log] Inside Try Block of ButtonClick method--->Step2");
				Actions actions = new Actions(driver);
				System.out.println("*//[log] Inside Try Block of ButtonClick method--->Step3");
				actions.moveToElement(button).click().build().perform();
				System.out.println("*//[log] Inside Try Block of ButtonClick method--->Step4");
				clicked = true;
			} 
			catch (StaleElementReferenceException e)
			{
				e.toString();
				System.out.println("Trying to recover from a stale element :" + e.getMessage());
				count = count+1;
			}
		}
	}
	

	public void listSelect(By locator,String actionData,Map<String, Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		Select select = new Select(driver.findElement(locator));
		select.selectByVisibleText(actionData);		
	}
		

	public void click(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		int count = 0;
		boolean clicked = false;
		while (count < 3 && !clicked)
		{
			System.out.println("*//[log] Click Retry Cout: "+count);
			waitForPageLoaded();
			try {
				waitForElementtoBeVisible(locator, argumentValues);
			} catch (CustomException e1) {
				System.out.println("[log] Error: element not visibe.");
			}
			waitForElementtoBeClickable(locator, argumentValues);

			if(locator.toString().contains("slds-icon-utility-setup")){
				System.out.println("*//[log] Inside the locator.toString().contains(slds-icon-utility-setup) "+locator.toString());
				//this condition was created specifically for Lightning Setup gear. 
				//this makes the syscommands functions lightning compatible in Firefox browser as well.
				clickUsingJavaScript(locator, argumentValues);
				clicked = true;
			}
			else {
				try {
					System.out.println("*//[log] Inside the TRY of else block "+locator.toString());
					clickUsingJavaScript(locator, argumentValues);
					try {
						if(driver.findElement(locator).isDisplayed() && driver.findElement(locator).isEnabled()) {
							System.out.println("*//[log] JS Click did not work, hence trying to click again");
							try {
								driver.findElement(locator).click();
							}
							catch (Exception e){
								System.out.println("*//[log] Exception caught in if: ");
							}
						}}
					catch (Exception e){
						System.out.println("*//[log] Exception caught in if2: ");
					}

					clicked = true;
				} 
				catch (StaleElementReferenceException e){
					e.toString();
					System.out.println("Trying to recover from a stale element :" + e.getMessage());
					count++;
				}
				catch (WebDriverException e) {
					System.out.println("*//[log] Inside the Catch of else block"+e.toString());
					clickUsingJavaScript(locator, argumentValues);
					clicked = true;
				}
			}

		}
	}
		

	public void click(WebElement element, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		waitForElementtoBeVisible(element, argumentValues);
		waitForElementtoBeClickable(element, argumentValues);
		if(! getbrowserName().equals("ie")) {
			try {
				element.click();
			} catch (WebDriverException e) {
				clickUsingJavaScript(element, argumentValues);
			}
		}
		else {
			clickUsingJavaScript(element, argumentValues);
		}
	}
		

	public void ByLabelMethod(String labelName, String str2, Map<String,Object> argumentValues) {
		Actions actions = new Actions(driver);
		String IDe = driver.findElement(By.xpath("//label[contains(.,'" + labelName + "')]")).getAttribute("for");
		WebElement web = driver.findElement(By.id(IDe));
		String typeOfInput = web.getAttribute("type");			
		switch (typeOfInput) {
			case "email": {
						actions.sendKeys(web, str2).build().perform();
						System.out.println("Used ByLabel for email");
						break;
					}
			case "password": {
						actions.sendKeys(web, str2).build().perform();
						System.out.println("Enter text in " + labelName);
						break;
					}
			case "text": {
						if (web.getAttribute("role") != null && web.getAttribute("role").equalsIgnoreCase("combobox")) {
							actions.sendKeys(web, str2).build().perform();
							clickUsingJavaScript(
									By.xpath("//*[contains(@title, '" + str2
											+ "')and contains(@class, 'primaryLabel slds-truncate slds-lookup__result-text')]"),
									argumentValues);
						} else {
							actions.sendKeys(web, str2).build().perform();
						}
						System.out.println("[log] Enter text in " + labelName);
						break;
					}
			case "submit": {
						clickUsingJavaScript(web, argumentValues);
						break;
					}
			
			case "checkbox": {
				          
						clickUsingJavaScript(web, argumentValues);
						System.out.println("[log] Clicked on checkbox " + labelName);
						break;
					}
			case "radio": {
						clickUsingJavaScript(web, argumentValues);
						System.out.println("[log] Selected RadioButton " + labelName);
						break;
					}
			case "list": {
						Select webElement1 = new Select(web);
						webElement1.selectByVisibleText(str2);
						System.out.println("[log] Selected item " + str2);
						break;
					}
			case "select-one": {			
						List<WebElement> options = web.findElements(By.tagName("option"));           
						for (WebElement option : options) {
							if (str2.equals(option.getText().trim()))
			
								option.click();
						}
						System.out.println("[log] Selected item " + str2);
						break;
					}
			default: {
						actions.sendKeys(web, str2).build().perform();
						System.out.println(web.getAttribute("role"));
					}
			}
	}
		

	public void actionSendKey(String data, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		Actions actions = new Actions(driver);
		Keys keyCode = Keys.valueOf(data.toUpperCase());
		
		actions.sendKeys(keyCode).build().perform();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("without xpath sendkey funtion");
	}
		

	public void genRandom(int length, String type) {
		String str = "";
		StringBuilder sb = new StringBuilder(length);
		if (type.equalsIgnoreCase("ALPHA")) {
			str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
		} 
		else if (type.equalsIgnoreCase("NUM")) {
			str = "0123456789";
		} 
		else if (type.equalsIgnoreCase("ALPHANUM")) {
			str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";
		} 
		else {
			random = sb.toString();
			Report.getReport().logPass("Invalid locator value : " + type);
			System.out.println("[log] Invliad locator value : " + type);
		}
			
		if (str.length() > 0) {
			for (int i = 0; i < length; i++) {
				int index = (int) (str.length() * Math.random());
				sb.append(str.charAt(index));
			}
			random = sb.toString();
			Report.getReport().logPass("Random String of length " + length + " and type " + type + " : " + random);
			System.out.println("[log] Random String of length " + length + " and type " + type + " : " + random);
		}
	}
		

	public void rightClick(WebElement elementLocator, Map<String,Object> argumentValues){
		//getcallerMethod1(argumentValues);
			waitForPageLoaded();
		    waitForElementtoBeVisible(elementLocator, argumentValues);
			waitForElementtoBeClickable(elementLocator, argumentValues);
		/*	WebDriverWait wait = new WebDriverWait(driver,0);
			wait.until(ExpectedConditions.elementToBeClickable(elementLocator));*/
			
			try{
			Actions actions = new Actions(driver);
			actions.contextClick(elementLocator).perform();
			/*action.contextClick(elementLocator).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).click().perform();*/
			}
			
			catch(WebDriverException e){
				
			}
		} 
		
		
	public void DoubleClick1(By locator, Map<String, Object> argumentValues) {
		//getcallerMethod1(argumentValues);			
		WebElement webelement = driver.findElement((By) ((locator)));
		Actions actions = new Actions(driver);
		actions.doubleClick(webelement).perform();
	}
		

	public void hoverToElement(WebElement element, Map<String,Object> argumentValues) {

		//getcallerMethod1(argumentValues);
		waitForPageLoaded();
		waitForElementtoBeVisible(element, argumentValues);
		waitForElementtoBeClickable(element, argumentValues);
		
		String ExpectedTooltip = element.getAttribute("title");
		
		try{
		Actions actions = new Actions(driver);
		
		actions.moveToElement(element).clickAndHold(element).build().perform();
		
		String actualTooltip = element.getAttribute("title");
		
		System.out.println("Actual Title of Tool Tip: "+actualTooltip);
		
		if(actualTooltip.equals(ExpectedTooltip)) {	
			System.out.println("Test Case Passed");	
		}
		else{
			System.out.println("Fail : Tooltip NOT matching expected value"); 
			} 
		}
		
		catch(WebDriverException e){
		
		}
		
	}
		

	public void PageDown(Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		Actions actions = new Actions(driver);
		driver.getCurrentUrl();
		actions.sendKeys(Keys.PAGE_DOWN).build().perform();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {	
			e.printStackTrace();
		}
		System.out.println("After thread sleep");
	}
		

	public void PageUp(Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		Actions actions = new Actions(driver);
		driver.getCurrentUrl();
		actions.sendKeys(Keys.PAGE_UP).build().perform();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("After thread sleep");
	}
		

	public void Space() {
		Actions actions = new Actions(driver);
		driver.getCurrentUrl();
		actions.sendKeys(Keys.SPACE).build().perform();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		System.out.println("After thread sleep");
	}
		

	public void ScrollToView(WebElement el, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		waitForPageLoaded();
		waitForElementtoBeVisible(el, argumentValues);
		waitForElementtoBeClickable(el, argumentValues);			
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);", el);
	}
		

	public void clickUsingJavaScript(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waitForElementtoBeClickable(locator, argumentValues);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		WebElement webelement = driver.findElement((By)(locator));
		executor.executeScript("arguments[0].click();", webelement);
	}
		

	public void clickUsingJavaScript(WebElement element, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		waitForElementtoBeClickable(element, argumentValues);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}
		

	public void closeCurrentTab(Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		driver.close();
	}
	

	public void enter(By locator, String txt, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!isElementEnable(locator, argumentValues)) {
			wait(5);
		}
		driver.findElement(locator).clear();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(locator).sendKeys(txt);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(locator).sendKeys(Keys.UP);
	}
	

	public void getUrl(String url, Map<String,Object> argumentValues) {		
		//getcallerMethod1(argumentValues);
		driver.manage().window().maximize();
		driver.get(url);
	}
		

	public WebElement findElement(By locator, Map<String,Object> argumentValues) {
		waitForElementtoBePresent(locator, argumentValues);
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver.findElement(locator);
	}
	

	public String getText(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		waitForElementtoBePresent(locator, argumentValues);
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		return driver.findElement(locator).getText();
	}
		

	public String getAttributeValue(By locator, String attribute, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
			e.printStackTrace();
		}
			waitForElementtoBePresent(locator, argumentValues);
			return driver.findElement(locator).getAttribute(attribute);
	}
	

	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}
	

	public void isDataMissing(ActionData actionData, String location) throws CustomException {
		if (Utility.isStringEmpty(actionData.getData())) {
			System.out.println(location + " Data is missing");
			throw new CustomException(location + " Data is missing");
		}
	}
		

	public void switchToWindow(String windowHandle, Map<String,Object> argumentValues) {
		//getcallerMethod(argumentValues);
		driver.switchTo().window(windowHandle);
	}
	

	public boolean isElementEnable(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod(argumentValues);
		return driver.findElement(locator).isEnabled();
	}
		

	public void moveToElement(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		Actions actions = new Actions(driver);
		WebElement move = driver.findElement(locator);
		actions.moveToElement(move).build().perform();
	}
	

	public void moveToElement(WebElement element, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).build().perform();
	}
	

	public void selectByVisibleText(By locator, String value, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		WebElement dropdown = driver.findElement(locator);
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
	}
	

	public void deSelectByVisibleText(By locator, String value, Map<String,Object> argumentValues) throws CustomException {
		//getcallerMethod1(argumentValues);
		waitForElementtoBeVisible(locator, argumentValues);
		WebElement webelement  = driver.findElement(locator);
		Select select = new Select(webelement);
		select.deselectByVisibleText(value);
	}
	

	public void selectCheckBox(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		waitForPageLoaded();
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		if (!(driver.findElement(locator).isSelected())) {
			driver.findElement(locator).click();
		}
	}
	

	public void deselectCheckBox(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		waitForPageLoaded();
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (driver.findElement(locator).isSelected()) {
			driver.findElement(locator).click();
		}
	}
	

	public void isSelected(By locator,Map<String,Object> argumentValues) throws CustomException{
		//getcallerMethod1(argumentValues);
		try {
			waitForElementtoBeVisible(locator, argumentValues);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.findElement(locator).isSelected();
	}
	

	public void switchToFrame(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	

	public void switchToDefaultContent(Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		
		driver.switchTo().defaultContent();
	}
	

	public void switchToAlertAndAccept() {
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
	}
	

	public void switchToAlertAndDismiss() {
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().dismiss();
	}
	

	public boolean verifyValues(String actual, String expected, String msg) {
		if(actual.equals(expected)) {
         flagForVerify=true;
         System.out.println("Verification passed");
     }
     else {
         System.out.println(msg);
         flagForVerify = false;
     }
     return flagForVerify;
	}
	

	public boolean VerifyCompareValues(String actual, String expected, String msg) {

	     if(actual.equals(expected)) {
	         flagForVerifyCompare=true;
	         System.out.println("Verification passed");
	     }
	     else {
	         System.out.println(msg);
	         flagForVerifyCompare = false;
	     }
	     return flagForVerifyCompare;
	 }
	 

 	public boolean skipIfVerifyFailed() {
    	 return flagForVerify;
	 }
 	

 	public boolean SkifIfVerifyCompareFailed() {
     	return flagForVerifyCompare;
 	}
 	

	public void verifyRegExValues(String actual, String regex, String msg) {
		Assert.assertTrue(stringCmd.stringContainsRegex(actual, regex), msg);
	}
	

	public void verifyValues(boolean actual, boolean expected, String msg) {
		Assert.assertEquals(actual, expected, msg);
	}
	

	public void fail(String message) {
		Assert.fail(message);
	}
	

	public void waitForElementtoBeClickable(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (TimeoutException e) {
			Assert.fail("Element is not clickable : " + locator);
		}
	}
	

	public void waitForElementtoBeClickable(WebElement element, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (TimeoutException e) {
			Assert.fail("Element is not clickable : " + element);
		}
	}
	

	public void waitForElementtoBeVisible(WebElement element, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			if (!isWebElementVisible(element)) {
				try {
					moveToElement(element, argumentValues);
				} catch (Exception e) {
				}
			}
		} catch (TimeoutException e) {
			Assert.fail("Element is not visible : " + element);
		}
	}
	

	public void waitForElementtoBeVisible(By locator, Map<String,Object> argumentValues) throws CustomException {
		//getcallerMethod1(argumentValues);
		
		try {
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			} catch (Exception e) {
				throw new CustomException(e.getMessage());
			}
			if (!isWebElementVisible(driver.findElement(locator))) {
				try {
					moveToElement(locator, argumentValues);
				} catch (Exception e) {
				}
			}
		} catch (TimeoutException e) {
			Assert.fail("Element is not visible : " + locator);
		}
	}
	

	public void waitForElementtoBePresent(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (TimeoutException e) {
			Assert.fail("Element is not present : " + locator);
		}
	}
	

	public boolean isWebElementVisible(WebElement w) {
		Dimension weD = w.getSize();
		Point weP = w.getLocation();
		Dimension d = driver.manage().window().getSize();
		int x = d.getWidth();
		int y = d.getHeight();
		int x2 = weD.getWidth() + weP.getX();
		int y2 = weD.getHeight() + weP.getY();
		return x2 <= x && y2 <= y;
	}
	

	public void performActions(String type, ArrayList<By> locator, String data, String field, String locator_value,
	ExtLoader loader) throws  ClassNotFoundException, NoSuchMethodException,
	SecurityException, InstantiationException, IllegalAccessException, CustomException {
		ActionData actionData = new ActionData(type, locator, data, field, locator_value, null);
		loader.findCommandInChain(actionData);
	}
	

	public ArrayList<By> toLocator(String locator_type, ArrayList<String> list, String type,
		Map<String,Object> argumentValues) {
		//getcallerMethod(argumentValues);
		ArrayList<By> l = new ArrayList<By>();
		if (list.size() > 1) {
			for (int i = 0; i < list.size(); i++) {
				String locator_value = list.get(i);
				By loc = toLocator(locator_type, locator_value, type, argumentValues);
				l.add(i, loc);
			}
			return l;
		} else {
			String locator_value = list.get(0);
			By loc = toLocator(locator_type, locator_value, type, argumentValues);
			l.add(0, loc);
			return l;
		}
	}		
	

	public By toLocator(String locator_type, String locator_value, String type, Map<String,Object> argumentValues) {
		//getcallerMethod(argumentValues);
		if (locator_type.trim().isEmpty()) {
			return null;
		}
		Locators loc = Locators.valueOf(locator_type.trim());
		switch (loc) {
			case xpath:
					return By.xpath(locator_value);
			case link:
					return By.linkText(locator_value);
			case id:
					return By.id(locator_value);
			case name:
					return By.name(locator_value);
			case tag:
					return By.tagName(locator_value);
			case cssSelector:
					return By.cssSelector(locator_value);
			case className:
					return By.className(locator_value);
			case label_name:
					return null;
			case label_for:
					return By.xpath(new DynamicGenericXpath().getXpathByAttributeFOR(locator_value, type));
		}
		return null;
	}
	

	public void waitForTexttoBePresent(By locator, String text, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
		} catch (TimeoutException e) {
			Assert.fail("Text is not present in the element : " + locator + " and text " + text);
		}
	}
	

	public void waitForElementToDisappear(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			wait(1);
		} catch (TimeoutException e) {
		}
	}
	

	public void setVariableMethod(Executor exec,String varName,By locator1,Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		List<WebElement> list = driver.findElements(locator1);
		for(WebElement wb : list){
			if(wb.isDisplayed())
			{
				String attr = wb.getAttribute("value");
				if(Strings.isNullOrEmpty(attr)){
					attr = wb.getText();
				}
				exec.variableMap.put(varName, attr);
				System.out.println("[log] Stored data in varName:" + varName + " and data :" +attr); 
				Report.getReport().logPass("Stored data in varName:" + varName + " and data :" +attr);
				//return;
				break;
			}
		}
	}
	

	public void getTempVarMethod(Executor exec,By locator2,Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		List<WebElement> list = driver.findElements(locator2);
		for(WebElement wb : list){
			if(wb.isDisplayed())
			{
				String attr = wb.getAttribute("value");
				if(attr!=null && attr!="")
					tempGlobalVar = wb.getAttribute("value");
				else
					tempGlobalVar = wb.getText();
				
				exec.variableMap.put("TempVar", tempGlobalVar);
				//return;
				break;
			}
		}
	}
	

	public void getURLMethod(Executor exec,Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		String storeURL= driver.getCurrentUrl();
		exec.variableMap.put("TempURL", storeURL);
	}
	

	public void SkipIfElementMissingMethod(Executor exec,By locator,ActionData actionData,Map<String,Object> argumentValues){
    	//getcallerMethod1(argumentValues);
        List<WebElement> list = driver.findElements(locator);
        boolean b = true;
        for (WebElement wb : list) {
            if (wb.isDisplayed()) {
                b = false;
                break;
            }
        }
        String lineC = actionData.getData();
        System.out.println("[log] Checking if XPath element with name " + actionData.getLocatorTypeData().toString()
                + " exist");
        if (b) {
            try {
                exec.skipCount = Integer.parseInt(lineC);
            } catch (Exception ex) {
                System.out.println("[log] Unable to read line to skip, resuming to next line");
                exec.skipCount = 0;
            }
            System.out.println("[log] Skipping " + lineC + " Lines");
        } else {
            System.out.println("[log] Element exist");
        }
        System.out.println("[log] Successfully executed SkipIfElementMissing");
        Report.getReport().logPass("Successfully executed SkipIfElementMissing");
    }
	

	public void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			System.out.println("[log] Failed to wait for :" + seconds + " seconds");
		}
	}
	

	public void verifyTableValue(By locator, int row, int col, String expectedTxt, String msg,
		Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		WebElement ele = driver.findElement(locator);
		List<WebElement> rows = ele.findElements(By.tagName("tr"));
		if (rows.size() > row) {
			List<WebElement> cols = rows.get(row).findElements(By.tagName("tr"));
			String output = cols.get(col).getText();
			verifyValues(output, expectedTxt, msg);
		}
	}
			
	

	public void verifyListValue(By locator, String expectedTxt, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		Select drpCountry = new Select(driver.findElement(locator));
      drpCountry.selectByVisibleText(expectedTxt);
	}
	

	public String readPDF(String pdfLocation) {
		String pdfContents = null;
		try {
			FileInputStream pdfFile = new FileInputStream(pdfLocation);
			PDDocument pdfDocument = PDDocument.load(pdfFile);
			PDFTextStripper pdfTextStrip = new PDFTextStripper();
			pdfContents = pdfTextStrip.getText(pdfDocument);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pdfContents;
	}
	

	public boolean validatePDFContents(String pdfContents, String values) {
		return pdfContents.contains(values);
	}
	

	public void switchToTab() {
		String currentScreen = driver.getWindowHandle();
		Set<String> tabSet = driver.getWindowHandles();
		for (String tab : tabSet) {
			if (!tab.equals(currentScreen)) {
				driver.switchTo().window(tab);
				System.out.println(driver.getCurrentUrl());
			}
		}
	}
	

	public void downloadPDF() {
		System.out.println(driver.getCurrentUrl());
		Robot robot = null;
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(4000);
			robot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void Delete(By elementLocator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		waitForPageLoaded();
		try {
			waitForElementtoBeVisible(elementLocator, argumentValues);
		} catch (CustomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		waitForElementtoBeClickable(elementLocator, argumentValues);
		try {
			Actions actions = new Actions(driver);
			// actions.sendKeys(Keys.DELETE).build().perform();
			actions.sendKeys(Keys.CONTROL, "a", Keys.DELETE).build().perform();
		}
		catch (WebDriverException e) {
		}
	}
	

	public Date dateModification(Date startDate, int day2, int month2, int year2) {
		Date date = startDate;
		int Day = day2;
		int month = month2;
		int year = year2;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, Day);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.YEAR, year);
		Date result = cal.getTime();
		return result;
	}
	

	public void startVideoRecording() throws ATUTestRecorderException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		File file1 = new File(System.getProperty("user.dir") + "\\ScriptVideos");
//         String workingDirectory = System.getProperty("user.dir");

		if (!file1.exists()) {
			if (file1.mkdir()) {

				System.out.println("ScriptVideos Folder is creating in process....................");
				System.out.println("ScriptVideos Folder is created!");
			}
		}
//		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd  HH-mm-ss");
//		Date date = new Date();
		recorder = new ATUTestRecorder("ScriptVideos", "Video"+formater.format(calendar.getTime()), false);

		recorder.start();
	}
	

	public void stopVideoRecording() throws ATUTestRecorderException {
		recorder.stop();
	}
	

	public void PRINTSCREEN() {
		try {
			Thread.sleep(120);
			Robot r = new Robot();
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			File file1 = new File(System.getProperty("user.dir") + "\\Screenshot");
			String workingDirectory = System.getProperty("user.dir");
			if (!file1.exists()) {
				if (file1.mkdir()) {
					System.out.println("Screenshot Folder is creating in process....................");
					System.out.println("Screenshot Folder is created!");
				}
			}
			String filename = "\\Screenshot\\Screenshot_" + formater.format(calendar.getTime()) + ".jpg";
			// String workingDirectory = System.getProperty("user.dir");
			File file = new File(workingDirectory, filename);
			System.out.println("Final filepath : " + file.getAbsolutePath());
			Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage Image = r.createScreenCapture(capture);
			ImageIO.write(Image, "jpg", file);
			System.out.println("Screenshot is saved in screenshot folder");
			Report.getReport().logPass("Screenshot is saved in screenshot folder");
		}
		catch (AWTException | IOException | InterruptedException ex) {
			System.out.println(ex);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	

	public void iframe(String iframeXpath, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(iframeXpath)));
	}
	

	public void selectCheck(By locator, Map<String,Object> argumentValues) {
		//getcallerMethod1(argumentValues);
		if (driver.findElement(locator).getAttribute("innerHTML").isEmpty()) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		click(locator, argumentValues);
		}
	}
	

	public void mixedSendKey(String actionData,By actionData1, Map<String,Object> argumentValues)
	{}
	

	public void waitForPageLoaded() {
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState===\"complete\""));
	}
		
}
