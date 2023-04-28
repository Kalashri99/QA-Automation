package com.persistent.daisy.gen;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.persistent.daisy.core.browser.SeleniumUtils;

public class Wrap {

	public Map<String, String> variableMap = new HashMap<String, String>();
	public boolean flagForVerify;

	// We haven't stored driver and wait for easy access because driver instance may
	// change with time, due to OpenNewWindow
	private SeleniumUtils utils;

	public Wrap(SeleniumUtils utils) {
		this.utils = utils;
	}

	public void click(WebElement element) {
		element.click();
	}

	public WebDriverWait getWebDriverWaitFor(int seconds) {
		WebDriver driver = utils.getDriver();

		return new WebDriverWait(driver, seconds);
	}

	public void setValues(WebElement element, String str, String... arg) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) utils.getDriver();

		element.clear();
		element.sendKeys(str);

		if (arg.length == 0)
			element.sendKeys(Keys.UP);
		else {
			jsExecutor.executeScript("arguments[0].blur();", element);
		}
	}

	public void waitToLoad(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void waitForPageLoaded() {
		WebDriver driver = utils.getDriver();
		WebDriverWait wait = utils.getWait();

		ExpectedCondition<Boolean> page = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver input) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		wait.until(page);
	}

	public int ordinalIndexOf(String str, String substr, int n) {
		int pos = str.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = str.indexOf(substr, pos + 1);
		return pos;
	}

	public void writeKey(String data, String value, By locator) {
		WebDriver driver = utils.getDriver();

		String vart = null;
		String option = "";

		try {
			// data = filename.keyname
			// value = value to store in key

			// For taking value from given xpath
			if (locator != null) { // if locator type is not given
				value = driver.findElement(locator).getText();
			}

			// For taking variable
			if (value.contains("${Var.")) {
				int index = value.indexOf("${Var.");
				int index2 = value.indexOf("}");
				vart = value.substring(index + 6, index2);
				value = variableMap.get(vart);
			}

			// For checking append or removekey condition
			if (data.contains(";")) {
				option = data.split(";")[1];
				data = data.split(";")[0];
			}

			if (value == null || value.trim().equalsIgnoreCase("null") || value.trim().equals("")) {
				System.out.println("WriteKey Locator Value is missing");

				if (!option.equalsIgnoreCase("removekey")) {
					System.out.println("Value of " + data + " does not exist");
					value = "null";
				}
			}

			String fileName = data.split("\\.")[0];
			String keyName = data.split("\\.")[1];

			String p = System.getProperty("user.dir") + File.separator + "programdata" + File.separator;
			File dir = new File(p);
			dir.mkdirs();

			String path = p + fileName + ".dat";
			File newFile = new File(path);
			Properties props = new Properties();

			if (newFile.createNewFile() && !option.equalsIgnoreCase("removekey")) {
				System.out
						.println("File created: " + newFile.getName() + " with Key: " + keyName + ", Value: " + value);
				props.setProperty(keyName, value);
				FileOutputStream out = new FileOutputStream(path);
				props.store(out, null);
				out.close();
			} else {
				FileInputStream in = new FileInputStream(newFile);
				props.load(in);
				in.close();
				// Condition for removing key
				if (option.equalsIgnoreCase("removekey") && props.containsKey(keyName)) {
					System.out.println("Removing Key : " + keyName + " with value: " + props.getProperty(keyName)
							+ ", from file: " + newFile.getName());
					props.remove(keyName);
					FileOutputStream out = new FileOutputStream(path);
					props.store(out, null);
					out.close();
				}
				// Trying to rempve key which is not in file
				else if (option.equalsIgnoreCase("removekey") && !props.containsKey(keyName)) {
					System.out.println("No key with keyname : " + keyName + "in file: " + newFile.getName());
				}
				// Extra condition to append in list format if append is provided in option
				else if (option.equalsIgnoreCase("append") && props.containsKey(keyName)) {
					String oldValue = props.getProperty(keyName);
					String newValue = "";
					if (oldValue.contains("[") && oldValue.contains("]")) {
						int index = oldValue.indexOf("]");
						newValue = oldValue.substring(0, index) + "," + value + "]";
					} else {
						newValue = "[" + oldValue + "," + value + "]";
					}
					props.setProperty(keyName, newValue);
					System.out.println(
							"Appending value : " + value + " in key: " + keyName + ", from file: " + newFile.getName());
					FileOutputStream out = new FileOutputStream(path);
					props.store(out, null);
					out.close();
				} else if (props.containsKey(keyName)) {
					System.out.println("Key already exists. Replacing key's value in file: " + newFile.getName()
							+ " with Key: " + keyName + ", Value: " + value);
					props.setProperty(keyName, value);
					FileOutputStream out = new FileOutputStream(path);
					props.store(out, null);
					out.close();
				} else {
					props.clear();
					System.out.println("File already exists. Appending to file: " + newFile.getName() + " with Key: "
							+ keyName + ", Value: " + value);
					props.setProperty(keyName, value);
					FileOutputStream out = new FileOutputStream(path, true);
					props.store(out, null);
					out.close();
				}
			}
		} catch (Exception e) {
			// if variable doesn't exist
			if (!variableMap.containsKey(vart)) {
				System.out.println("Variable " + vart + "does not exist");
			} else if (value == null) {
				System.out.println("Value of" + vart + "does not exist");
			} else {
				System.out.println("Exception occured in WriteKey");
			}
		}
	}

	public void readKey(String data, By locator) {
		WebDriver driver = utils.getDriver();

		String option = "";
		String fileName = "";

		try {
			// data = filename.keyname or filename.keyname;Iterate
			String value = "";

			// Condition for checking Iterate or SearchKey
			if (data.contains(";")) {
				option = data.split(";")[1];
				data = data.split(";")[0];
			}

			fileName = data.split("\\.")[0];
			String keyName = data.split("\\.")[1];

			String path = System.getProperty("user.dir") + File.separator + "ProgramData" + File.separator + fileName
					+ ".dat";
			File newFile = new File(path);
			FileInputStream in = new FileInputStream(newFile);
			Properties props = new Properties();

			if (newFile.exists()) {
				props.load(in);
				// Search for given key and returns if value exists
				if (option.equalsIgnoreCase("searchkey") && props.containsKey(keyName)) {
					String expected_value = "";

					if (locator != null) { // if locator type is not given
						expected_value = driver.findElement(locator).getText();
					}

					value = props.getProperty(keyName);
					if (value.contains("[")) { // i.e given is list
						value = value.substring(1, value.length() - 1);
						for (String each : value.split(",")) {
							if (each.equals(expected_value)) {
								value = each;
								break;
							}
						}
					}

					if (value.equals(expected_value)) { // else only put key if its same as expected
						variableMap.put(keyName, value);
						System.out.println("Read file: " + newFile.getName() + " and search Key: " + keyName
								+ ", Expected Value: " + expected_value + " Actual Value: " + value);
					} else {
						System.out.println("Read file: " + newFile.getName() + " and search Key: " + keyName
								+ ", Expected Value: " + expected_value + " Not Found");
					}
				}
				// Extra Condition if user want to retreive value one by one in forloop
				else if (option.equalsIgnoreCase("Iterate") && props.containsKey(keyName)
						&& props.getProperty(keyName).contains("[")) {
					int index = Integer.parseInt(variableMap.getOrDefault(keyName + "_index_", "0"));
					value = props.getProperty(keyName);
					if (index == 0) {
						variableMap.put(keyName, value.substring(1, ordinalIndexOf(value, ",", 1)));
					} else {
						int startIndex = ordinalIndexOf(value, ",", index);
						int endIndex = ordinalIndexOf(value, ",", index + 1);
						if (endIndex == -1)
							endIndex = value.length() - 1;
						variableMap.put(keyName, value.substring(startIndex + 1, endIndex));
					}
					System.out.println(
							"Read file: " + newFile.getName() + " and stored Key: " + keyName + ", Iterating Value: "
									+ variableMap.get(keyName) + " into Variable: " + "${Var." + keyName + "}");
					variableMap.put(keyName + "_index_", String.valueOf(index + 1));
				} else if (props.containsKey(keyName)) {
					value = props.getProperty(keyName);
					variableMap.put(keyName, value);
					System.out.println("Read file: " + newFile.getName() + " and stored Key: " + keyName + ", Value: "
							+ value + " into Variable: " + "${Var." + keyName + "}");
				} else {
					System.out.println("Key: " + keyName + " does not exist in file: " + newFile.getName());
				}
			} else {
				System.out.println("File to be read does not exist: " + fileName + ".dat");
			}

			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("File to be read does not exist: " + fileName + ".dat");
		} catch (Exception e) {
			System.out.println("Wrong value entered for ReadKey");
		}

	}

	public void deleteFile(String data) {

		try {
			String fileName = data; // filename
			String path = System.getProperty("user.dir") + File.separator + "ProgramData" + File.separator + fileName
					+ ".dat";
			File newFile = new File(path);

			if (newFile.exists()) {
				newFile.delete();
				System.out.println("File deleted: " + fileName + ".dat");
			} else {
				System.out.println("File to be deleted does not exist: " + fileName + ".dat");
			}
		} catch (Exception e) {
			System.out.println("Wrong value entered for DeleteFile");
		}
	}

	public Boolean verifyValues(String actual, String expected, String msg) {

		try {
			if (actual.contains("${Var.")) {
				flagForVerify = false;
				return flagForVerify;
			} else if (expected.equals("IfNull")) {
				if (actual.isEmpty() || actual == null) {
					flagForVerify = true;
				} else {
					System.out.println(msg);
					flagForVerify = false;
				}

			} else if (expected.equals("IfNotNull")) {
				if (actual.isEmpty() || actual == null) {
					System.out.println(msg);
					flagForVerify = false;
				} else {
					flagForVerify = true;
				}

			} else {

				if (actual.equals(expected)) {
					flagForVerify = true;
				} else {
					System.out.println(msg);
					flagForVerify = false;
				}
			}

		} catch (Exception e) {
			System.out.println("[log] Wrong value entered for verifyValues");
		}

		return flagForVerify;
	}

	public void downloadPDF(String... typeOfElement) {
		WebDriver driver = utils.getDriver();
		Robot robot = null;

		if (typeOfElement.length == 1 && !typeOfElement[0].equals("")) {
			String fileAddress = "";
			if (typeOfElement[0].equalsIgnoreCase("iframe")) {
				WebElement downloadIcon = driver.findElement(By.tagName("iframe"));
				fileAddress = downloadIcon.getAttribute("src");
			} else if (typeOfElement[0].equalsIgnoreCase("embed")) {
				WebElement downloadIcon = driver.findElement(By.tagName("embed"));
				fileAddress = downloadIcon.getAttribute("src");
			} else if (typeOfElement[0].equalsIgnoreCase("object")) {
				WebElement downloadIcon = driver.findElement(By.tagName("object"));
				fileAddress = downloadIcon.getAttribute("data");
			}
			driver.get(fileAddress);
		}

		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println(e.getMessage());
			return;
		}

		try {
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(10000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);

			// for navigating back to original page
			if (typeOfElement.length == 1 && !typeOfElement[0].equals("")) {
				driver.navigate().back();
				waitForPageLoaded();
			}

			System.out.println("PDF file saved..");
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			System.out.println("PDF file not saved..");
		}
	}

	public void PRINTSCREEN() {

		try {
			Thread.sleep(120);
			Robot r = new Robot();

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

			File dir = new File(System.getProperty("user.dir") + File.separator + "Screenshots");
			dir.mkdirs();

			String filename = "Screenshot_" + formater.format(calendar.getTime()) + ".jpg";
			File file = new File(dir, filename);

			Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage Image = r.createScreenCapture(capture);
			ImageIO.write(Image, "jpg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
		
