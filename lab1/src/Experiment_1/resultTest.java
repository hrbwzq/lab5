package Experiment_1;

import static org.junit.Assert.*;

import org.junit.Test;

public class resultTest {

	@Test
	public void testDuff() {
		result test=new result();  
		String str="3+2*x";
		str=test.dispose(str);
        String result=test.duff(str,"!d/d x");          
        assertEquals("2",result); 
		
	}

}
