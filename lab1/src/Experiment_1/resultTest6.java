package Experiment_1;

import static org.junit.Assert.*;

import org.junit.Test;

public class resultTest6 {

	@Test
	public void testDuff() {
		result test=new result();  
		String str="3 + 1* xy^2 -xy^3";
		str=test.dispose(str);
        String result=test.duff(str,"!d/d xy");
        
        assertEquals("2*xy-3*xy^2",result);
	}
}
