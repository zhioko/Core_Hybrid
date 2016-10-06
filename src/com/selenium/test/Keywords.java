package com.selenium.test;

import static com.selenium.test.DriverScript.APP_LOGS;
import static com.selenium.test.DriverScript.CONFIG;
import static com.selenium.test.DriverScript.OR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Keywords {
	
public WebDriver driver;


	
	public String openBrowser(String object,String data){		
		APP_LOGS.debug("Opening browser");
		if(data.equals("Mozilla"))
			driver=new FirefoxDriver();
		else if(data.equals("IE"))
			driver=new InternetExplorerDriver();
		else if(data.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver","/Users/Raul/Documents/workspaceSTS2/chromedriver_win32/chromedriver.exe");
			driver=new ChromeDriver();
		}
		driver.manage().window().maximize();
		
		long implicitWaitTime=Long.parseLong(CONFIG.getProperty("implicitwait"));
		driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
		return Constants.KEYWORD_PASS;

	}
	
	public String navigate(String object,String data){		
		APP_LOGS.debug("Navigating to URL");
		try{
		driver.navigate().to(data);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}
	
	public String clickLink(String object,String data){
        APP_LOGS.debug("Clicking on link ");
        try{
        driver.findElement(By.xpath(OR.getProperty(object))).click();
        }catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();
        }
     
		return Constants.KEYWORD_PASS;
	}
	public String clickLink_linkText(String object,String data){
        APP_LOGS.debug("Clicking on link ");
        driver.findElement(By.linkText(OR.getProperty(object))).click();
     
		return Constants.KEYWORD_PASS;
	}
	
	
	
	public  String verifyLinkText(String object,String data){
        APP_LOGS.debug("Verifying link Text");
        try{
        	String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
        	String expected=data;
        	
        	if(actual.equals(expected))
        		return Constants.KEYWORD_PASS;
        	else
        		return Constants.KEYWORD_FAIL+" -- Link text not verified";
        	
        }catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Link text not verified"+e.getMessage();

        }
        
	}
	
	
	public  String clickButton(String object,String data){
        APP_LOGS.debug("Clicking on Button");
        try{
            driver.findElement(By.xpath(OR.getProperty(object))).click();
            }catch(Exception e){
    			return Constants.KEYWORD_FAIL+" -- Not able to click on Button"+e.getMessage();
            }
        
        
		return Constants.KEYWORD_PASS;
	}
	
	public  String verifyButtonText(String object,String data){
		APP_LOGS.debug("Verifying the button text");
		try{
		String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
    	String expected=data;

    	if(actual.equals(expected))
    		return Constants.KEYWORD_PASS;
    	else
    		return Constants.KEYWORD_FAIL+" -- Button text not verified "+actual+" -- "+expected;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Object not found "+e.getMessage();
		}
		
	}
	
	public  String selectList(String object, String data){
		APP_LOGS.debug("Selecting from list");
		try{
			if(!data.equals(Constants.RANDOM_VALUE)){
			  driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			}else{
				// logic to find a random value in list
				WebElement droplist= driver.findElement(By.xpath(OR.getProperty(object))); 
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random num = new Random();
				int index=num.nextInt(droplist_cotents.size());
				String selectedVal=droplist_cotents.get(index).getText();
				
			  driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(selectedVal);
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL +" - Could not select from list. "+ e.getMessage();	

		}
		
		return Constants.KEYWORD_PASS;	
	}
	
	public String verifyAllListElements(String object, String data){
		APP_LOGS.debug("Verifying the selection of the list");
	try{	
		WebElement droplist= driver.findElement(By.xpath(OR.getProperty(object))); 
		List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
		
		// extract the expected values from OR. properties
		String temp=data;
		String allElements[]=temp.split(",");
		// check if size of array == size if list
		if(allElements.length != droplist_cotents.size())
			return Constants.KEYWORD_FAIL +"- size of lists do not match";	
		
		for(int i=0;i<droplist_cotents.size();i++){
			if(!allElements[i].equals(droplist_cotents.get(i).getText())){
					return Constants.KEYWORD_FAIL +"- Element not found - "+allElements[i];
			}
		}
	}catch(Exception e){
		return Constants.KEYWORD_FAIL +" - Could not select from list. "+ e.getMessage();	

	}
		
		
		return Constants.KEYWORD_PASS;	
	}
	
	public  String verifyListSelection(String object,String data){
		APP_LOGS.debug("Verifying all the list elements");
		try{
			String expectedVal=data;
			//System.out.println(driver.findElement(By.xpath(OR.getProperty(object))).getText());
			WebElement droplist= driver.findElement(By.xpath(OR.getProperty(object))); 
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
			String actualVal=null;
			for(int i=0;i<droplist_cotents.size();i++){
				String selected_status=droplist_cotents.get(i).getAttribute("selected");
				if(selected_status!=null)
					actualVal = droplist_cotents.get(i).getText();			
				}
			
			if(!actualVal.equals(expectedVal))
				return Constants.KEYWORD_FAIL + "Value not in list - "+expectedVal;

		}catch(Exception e){
			return Constants.KEYWORD_FAIL +" - Could not find list. "+ e.getMessage();	

		}
		return Constants.KEYWORD_PASS;	

	}
	
	public  String selectRadio(String object, String data){
		APP_LOGS.debug("Selecting a radio button");
		try{
			String temp[]=object.split(Constants.DATA_SPLIT);
			driver.findElement(By.xpath(OR.getProperty(temp[0])+data+OR.getProperty(temp[1]))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL +"- Not able to find radio button";	

		}
		
		return Constants.KEYWORD_PASS;	

	}
	
	public  String verifyRadioSelected(String object, String data){
		APP_LOGS.debug("Verify Radio Selected");
		try{
			String temp[]=object.split(Constants.DATA_SPLIT);
			String checked=driver.findElement(By.xpath(OR.getProperty(temp[0])+data+OR.getProperty(temp[1]))).getAttribute("checked");
			if(checked==null)
				return Constants.KEYWORD_FAIL+"- Radio not selected";	

				
		}catch(Exception e){
			return Constants.KEYWORD_FAIL +"- Not able to find radio button";	

		}
		
		return Constants.KEYWORD_PASS;	

	}
	
	
	public  String checkCheckBox(String object,String data){
		APP_LOGS.debug("Checking checkbox");
		try{
			// true or null
			String checked=driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if(checked==null)// checkbox is unchecked
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" - Could not find checkbo";
		}
		return Constants.KEYWORD_PASS;
		
	}
	
	public String unCheckCheckBox(String object,String data){
		APP_LOGS.debug("Unchecking checkBox");
		try{
			String checked=driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if(checked!=null)
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" - Could not find checkbox";
		}
		return Constants.KEYWORD_PASS;
		
	}
	
	
	public  String verifyCheckBoxSelected(String object,String data){
		APP_LOGS.debug("Verifying checkbox selected");
		try{
			String checked=driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("checked");
			if(checked!=null)
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " - Not selected";
			
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" - Could not find checkbox";

		}
		
		
	}
	
	
	public String verifyText(String object, String data){
		APP_LOGS.debug("Verifying the text");
		try{
			String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
	    	String expected=data;

	    	if(actual.equals(expected))
	    		return Constants.KEYWORD_PASS;
	    	else
	    		return Constants.KEYWORD_FAIL+" -- text not verified "+actual+" -- "+expected;
			}catch(Exception e){
				return Constants.KEYWORD_FAIL+" Object not found "+e.getMessage();
			}
		
	}
	
	public  String writeInInput(String object,String data){
		APP_LOGS.debug("Writing in text box");
		
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to write "+e.getMessage();

		}
		return Constants.KEYWORD_PASS;
		
	}
	
	public  String verifyTextinInput(String object,String data){
       APP_LOGS.debug("Verifying the text in input box");
       try{
			String actual = driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("value");
			String expected=data;

			if(actual.equals(expected)){
				return Constants.KEYWORD_PASS;
			}else{
				return Constants.KEYWORD_FAIL+" Not matching ";
			}
			
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to find input box "+e.getMessage();

		}
	}
	
	public  String clickImage(){
	       APP_LOGS.debug("Clicking the image");
			
			return Constants.KEYWORD_PASS;
	}
	
	public  String verifyFileName(){
	       APP_LOGS.debug("Verifying inage filename");
			
			return Constants.KEYWORD_PASS;
	}
	
	
	
	
	public  String verifyTitle(String object, String data){
	       APP_LOGS.debug("Verifying title");
	       try{
	    	   String actualTitle= driver.getTitle();
	    	   String expectedTitle=data;
	    	   if(actualTitle.equals(expectedTitle))
		    		return Constants.KEYWORD_PASS;
		    	else
		    		return Constants.KEYWORD_FAIL+" -- Title not verified "+expectedTitle+" -- "+actualTitle;
			   }catch(Exception e){
					return Constants.KEYWORD_FAIL+" Error in retrieving title";
			   }		
	}
	
	public String exist(String object,String data){
	       APP_LOGS.debug("Checking existance of element");
	       try{
	    	   driver.findElement(By.xpath(OR.getProperty(object)));
			   }catch(Exception e){
					return Constants.KEYWORD_FAIL+" Object doest not exist";
			  }
	       
	       
			return Constants.KEYWORD_PASS;
	}
	
	public  String click(String object,String data){
	       APP_LOGS.debug("Clicking on any element");
	       try{
	    	   driver.findElement(By.xpath(OR.getProperty(object))).click();
			   }catch(Exception e){
					return Constants.KEYWORD_FAIL+" Not able to click";
			  }
			return Constants.KEYWORD_PASS;
	}
	
	public  String synchronize(String object,String data){
		APP_LOGS.debug("Waiting for page to load");
		((JavascriptExecutor) driver).executeScript(
        		"function pageloadingtime()"+
        				"{"+
        				"return 'Page has completely loaded'"+
        				"}"+
        		"return (window.onload=pageloadingtime());");
        
		return Constants.KEYWORD_PASS;
	}
	
	public  String waitForElementVisibility(String object,String data){
		APP_LOGS.debug("Waiting for an element to be visible");
		int start=0;
		int time=(int)Double.parseDouble(data);
		try{
		 while(time == start){
			if(driver.findElements(By.xpath(OR.getProperty(object))).size() == 0){
				Thread.sleep(1000L);
				start++;
			}else{
				break;
			}
		 }
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+"Unable to close browser. Check if its open"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}
	
	public  String closeBroswer(String object, String data){
		APP_LOGS.debug("Closing the browser");
		try{
			driver.quit();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+"Unable to close browser. Check if its open"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}
	
	public String pause(String object, String data) throws NumberFormatException, InterruptedException{
//		long time = (long)Double.parseDouble(object);
		Thread.sleep(5000);
		return Constants.KEYWORD_PASS;
	}
	
	//expli apllication
//	public String explicit(String object, String data) {
//		WebDriverWait myWaitVar;
//		
//		myWaitVar = new WebDriverWait(driver, 20);
//		
//		myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[7]/div/div/form/div/div[1]/button")));
//		myWaitVar.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[7]/div/div/form/div/div[1]/button")));
//		
//		
//		return Constants.KEYWORD_PASS;
//	}
//	public String datePicker(String object, String data)  {
//		
//		
//		try {
//			WebElement yearTable = driver.findElement(By.xpath(OR.getProperty(object)));
//			List<WebElement> listaAnios = driver.findElements(By.tagName("button"));
//			System.out.println("Numero botones "+listaAnios.size());
//			listaAnios.get(20).click();
//			for (WebElement col : listaAnios) {
//				if (col.getSize().equals(27)) {
//				System.out.println("equals "+listaAnios.size());
//					break;
//				}
//			}
//			
//			List<WebElement> listaMeses = driver.findElements(By.tagName("button"));
//			System.out.println("Numero botones "+listaMeses.size());
//			
//
//			for (WebElement col : listaAnios) {
//				if (col.getSize().equals(27)) {
//				System.out.println("equals "+listaAnios.size());
//					break;
//				}
//			}
//		
//			
//			List<WebElement> listaDias = driver.findElements(By.tagName("button"));
//			System.out.println("Numero botones "+listaDias.size());
//			
//
//			for (WebElement col : listaAnios) {
//				if (col.getSize().equals(27)) {
//				System.out.println("equals "+listaAnios.size());
//					break;
//				}
//			}
//			
//		} catch (Exception e) {
//			
//			return Constants.KEYWORD_FAIL+"Unable to close browser. Check if its open"+e.getMessage();
//		}
//	
//		
//		return Constants.KEYWORD_PASS;
//	}
	
	public String dataPicker(String object, String data) throws InterruptedException{
	       
        APP_LOGS.debug("Closing the browser");

        WebElement calendario = driver.findElement(
                By.xpath("html/body/div[7]"));

        List<WebElement> rows = calendario.findElements(By.tagName("span"));
        for (WebElement cell : rows) {
          

            if (cell.getText().equals("2017")) {
                System.out.println(cell.getText());
                String anio = "2017";
                cell.findElement(By.xpath("//span[contains(., \"" + anio + "\")]")).click();

                break;
            }

        }

        Thread.sleep(2000);
        List<WebElement> rows1 = calendario.findElements(By.tagName("span"));

        for (WebElement cell1 : rows1) {
          
            if (cell1.getText().equals("Oct")) {
                String mes = "Oct";
                cell1.findElement(By.xpath("//span[contains(., \"" + mes + "\")]")).click();

                break;
            }

        }

        Thread.sleep(2000);
        List<WebElement> rows2 = calendario.findElements(By.tagName("tr"));
        List<WebElement> columns2 = calendario.findElements(By.tagName("td"));

        for (WebElement cell2 : columns2) {


            if (cell2.getText().equals("13")) {
                String dia = "13";
                cell2.findElement(By.xpath("//td[contains(., \"" + dia + "\")]")).click();

                break;
            }

        }

        return Constants.KEYWORD_PASS;

    } 
	
}

