package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefectionTest2 {
	
	public static void main (String [] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		
		RefectionTest2 t = new RefectionTest2();
		
		Method [] method = t.getClass().getMethods();
		
		for(int i=0 ; i<method.length ; i++){
			System.out.println(method[i].getName());
		
			if(method[i].getName().equals("abc")){
				method[i].invoke(t);
			}
			
		}
		
	}
	
	public void abc(){
		System.out.print("ABC method");
	}
	
	public void xyz(){
		System.out.print("XYZ method");
	}

}
