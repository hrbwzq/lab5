package Experiment_1;

import static org.junit.Assert.*;

import org.junit.Test;

public class resultTest4 {

	@Test
	public void testDuff() {
		result test=new result();  
		String str="3 + 1* xy";
		str=test.dispose(str);
        String result=test.duff(str,"!d/d xy");
        
        assertEquals("1",result);
	}

}
