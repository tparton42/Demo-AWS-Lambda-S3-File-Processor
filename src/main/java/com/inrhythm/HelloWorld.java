package com.inrhythm;

/**
 * Hello world!
 *
 */
public class HelloWorld {
	private static final String HELLO_WORLD = "Hello World!";

	public String hello() {
		return HELLO_WORLD;
	}

	public String hello(String strIn) {

		if (strIn != null) {
			strIn = strIn.trim();

			if (strIn.length() != 0) {
				return "Hello " + strIn;
			}
		}

		return HELLO_WORLD;
	}
}

