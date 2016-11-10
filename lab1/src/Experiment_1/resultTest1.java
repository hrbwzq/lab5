package Experiment_1;

import static org.junit.Assert.*;

import org.junit.Test;

public class resultTest1 {

	@Test
	public void testDuff() {
		result test=new result();  
		String str="3+2*xy";
		str=test.dispose(str);
        String result=test.duff(str,"!d/d xy"); 
        assertEquals("2",result); 
		
	}
}
