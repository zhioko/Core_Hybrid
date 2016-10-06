package com.selenium.test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.selenium.xls.read.Xls_Reader;

public class DriverScript {
	
public static Logger APP_LOGS;
	
	//Suite
	public Xls_Reader suiteXLS;
	public int currentSuiteID;
	public String currenTestSuite;
	
	//Test Case
	public int currentTestCaseID;
	public Xls_Reader currentTestSuiteXLS;
	public String currentTestCaseName;
	
	//Test Step 
	public int currentTestStepID;
	public String currentKeyword;
	public int currentTestDataSetID;
	
	public Keywords keywords;
	Method [] method;
	public String keyword_execution_result;
	public ArrayList<String> resultSet;
	public String data;
	public String object;
	
	public static Properties CONFIG;
	public static Properties OR;
	
	public DriverScript(){
		APP_LOGS = Logger.getLogger("myAppLogger");
		keywords = new Keywords();
		method = keywords.getClass().getMethods();
	}
	
	public static void main (String [] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//com//selenium//config//config.properties");
		CONFIG = new Properties();
		CONFIG.load(fs);
		
		fs = new FileInputStream(System.getProperty("user.dir")+"//src//com//selenium//config//or.properties");
		OR = new Properties();
		OR.load(fs);
		
		System.out.println("Test site url "+CONFIG.getProperty("testSuitePiggo"));
		
		
		DriverScript test = new DriverScript();
		test.start();
	}
	
	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		//Initialize app logs
		APP_LOGS = Logger.getLogger("myAppLogger");
		APP_LOGS.debug("Logger Initialized");
		
		APP_LOGS.debug("Initialize suite xls");
		suiteXLS = new Xls_Reader(System.getProperty("user.dir")+"//src//com//selenium//xls//Suite.xlsx");
		
		for(currentSuiteID=2 ; currentSuiteID<=suiteXLS.getRowCount(Constants.TEST_SUITE_SHEET) ; currentSuiteID++){
			APP_LOGS.debug(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID)+"----"+suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID));
		
			String runmode = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID);
			
			currenTestSuite = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID);
			if(runmode.equals(Constants.RUNMODE_YES)){
				APP_LOGS.debug("Executing suite "+suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID));
				currentTestSuiteXLS = new Xls_Reader(System.getProperty("user.dir")+"//src//com//selenium//xls//"+currenTestSuite+".xlsx");
				
				//Tarea:iterar todos los testcases de la suite
				for(currentTestCaseID=2 ; currentTestCaseID<=currentTestSuiteXLS.getRowCount(Constants.TEST_CASES_SHEET) ; currentTestCaseID++ ){
					APP_LOGS.debug(currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "TCID", currentTestCaseID)+"----"+currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestCaseID));                                          
					
					//Iterando todos los testcases de la hoja test steps
					currentTestCaseName = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "TCID", currentTestCaseID);
					
					
					
					if(currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestCaseID).equals(Constants.RUNMODE_YES)){
						APP_LOGS.debug("Executing test case: "+currentTestCaseName);
						
						if(currentTestSuiteXLS.isSheetExist(currentTestCaseName)){
							
							//Iterar las veces necesarias igual al numero de Y s en dataset
							for(currentTestDataSetID=2 ; currentTestDataSetID<=currentTestSuiteXLS.getRowCount(currentTestCaseName) ; currentTestDataSetID++ ){
								resultSet = new ArrayList<String>();
								APP_LOGS.debug("Numero de iteracion "+(currentTestDataSetID-1));
								
								if(currentTestSuiteXLS.getCellData(currentTestCaseName, Constants.RUNMODE, currentTestDataSetID).equals(Constants.RUNMODE_YES)){
									executeKeywords();
								}
								createXLSReport();
							}	
						}else{
//							currentTestDataSetID = 2;
							resultSet = new ArrayList<String>();
							executeKeywords();
							createXLSReport();
						}
					}
				}
			}
		}
		
	}
	
	public void executeKeywords() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		for( currentTestStepID=2 ; currentTestStepID<=currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET) ; currentTestStepID++){
			data = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID);
			
			//Check if data start with col
			if(data.startsWith(Constants.DATA_STARTS_COL)){
				data = currentTestSuiteXLS.getCellData(currentTestCaseName, data.split(Constants.DATA_SPLIT)[1], currentTestDataSetID);
			}else if (data.startsWith(Constants.CONFIG)){
				//check is data start with config
				data= CONFIG.getProperty(data.split(Constants.DATA_SPLIT)[1]);
				
			}else{
				//By default get the object from Or
				data = OR.getProperty(data);
				
			}
			
			
			object = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT, currentTestStepID);
			
			if (currentTestCaseName.equals(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, currentTestStepID))) {
				APP_LOGS.debug(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD, currentTestStepID));
				currentKeyword = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD, currentTestStepID);
				//Codigo para ejecutar keywords
				
				for(int i=0 ; i<method.length ; i++){
				
					if(method[i].getName().equals(currentKeyword)){
						keyword_execution_result = (String)method[i].invoke(keywords, object, data);
						APP_LOGS.debug(keyword_execution_result);
						resultSet.add(keyword_execution_result);
					}
				}
			
			}
		}
	}
	
	public void createXLSReport(){
		
		String colName=Constants.RESULT +(currentTestDataSetID-1);
		boolean isColExist=false;
		
		for(int c=0;c<currentTestSuiteXLS.getColumnCount(Constants.TEST_STEPS_SHEET);c++){
//			System.out.println(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,c , 2));
			if(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET,c , 1).equals(colName)){
				isColExist=true;
				break;
			}
		}
		
		if(!isColExist)
			currentTestSuiteXLS.addColumn(Constants.TEST_STEPS_SHEET, colName);
		int index=0;
		for(int i=2;i<=currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET);i++){
			
			if(currentTestCaseName.equals(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, i))){
				if(resultSet.size()==0)
					currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, i, Constants.KEYWORD_SKIP);
				else	
					currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, i, resultSet.get(index));
				index++;
			}
			
			
		}
		
		if(resultSet.size()==0){
			// skip
			currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID, Constants.KEYWORD_SKIP);
			return;
		}else{
			for(int i=0;i<resultSet.size();i++){
				if(!resultSet.get(i).equals(Constants.KEYWORD_PASS)){
					currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID, resultSet.get(i));
					return;
				}
			}
		}
		currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID, Constants.KEYWORD_PASS);
	//	if(!currentTestSuiteXLS.getCellData(currentTestCaseName, "Runmode",currentTestDataSetID).equals("Y")){}
		
	}

}
