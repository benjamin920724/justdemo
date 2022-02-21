package com.example.assignment;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.assignment.biz.placesearch.common.exception.CustomException;

@SpringBootTest
public class ExceptionTest {

	@Test
	public void customExceptionTest() {
		
		Assertions.assertThrows(CustomException.class, () -> new TestClass(-1));
	
    };
    
    public class TestClass {
    	public TestClass(int value) throws CustomException {
            if(value < 0){
                throw new CustomException("Exception!!!");
            }
        }
    }
}


