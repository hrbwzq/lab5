package Experiment_1;

import static org.junit.Assert.*;

import org.junit.Test;

public class resultTest3 {
	@Test
	public void testDuff() {
		result test=new result();  
		String str="3+2*xy*zyy";
		str=test.dispose(str);
        String result=test.duff(str,"!d/d kk");
        //System.out.println(result);
        assertEquals("",result);
	}

}
