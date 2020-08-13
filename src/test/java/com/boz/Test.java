package test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Test {
	private static final Logger LOGGER  = LoggerFactory.getLogger(Test.class);
	
	
	
	public static void main(String[] args) {
		
		
		String err = "err";
		String info = "info";
		String debug = "debug";
		LOGGER.error("loglevel:{}",err);
		LOGGER.info("loglevel:{}",info);
		LOGGER.debug("loglevel:{}",debug);
		LOGGER.trace("loglevel:TRACE");
		LOGGER.warn("LOGLEVEL:WARN");
		
		
	}
}
