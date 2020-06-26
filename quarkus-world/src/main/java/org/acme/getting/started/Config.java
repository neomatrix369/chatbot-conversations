package org.acme.getting.started;///////////////////////////////////////////////////////////////////////////////
// Config.java Fall 2018
//
// This file contains constants used within your Eliza program.
// This file will not be handed in, because testing Config.java files will 
// be used to test your program. Your code must reference these constant 
// values by the names defined below and not the values themselves.
//
///////////////////////////////////////////////////////////////////////////////

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Config {
	
    /**  
     * A debugging technique is to add statements like the following
     * at key places in your program:
     *    if (Config.DEBUG) { System.out.println("value=" + value); }
     * Then you can turn on or off all these statements simply by
     * changing the value of DEBUG here.
     */
    static final boolean DEBUG = true;
    
    /**
     * The filename extension for files with lists of key words and responses.
     */
	static final String RESPONSE_FILE_EXTENSION = ".rsp";
    
	/**
	 * If the user input contains any of these words in their own phrase
	 * then Eliza ends.
	 */
	static final String[] QUIT_WORDS = {"bye","goodbye","quit", "seeya"};
	
	/** 
	 * In user input, if the first word is found it is replaced with 
	 * the second word. For example "i'm" is replaced with "i am".
     */
	static final String[][] INPUT_WORD_MAP = {
			//from  => to
			{"dont", "don't"},
			{"cant", "cannot"},
			{"can't", "cannot"},
			{"want", "desire"},
			{"need", "desire"},
			{"noone", "nobody"},
			{"everybody", "everyone"},
			{"wont", "won't"},
			{"recollect", "remember"},
			{"dreamt", "dreamed"},
			{"dreams", "dream"},
			{"maybe", "perhaps"},
			{"how", "what"},
			{"when", "what"},
			{"certainly", "yes"},
			{"machine", "computer"},
			{"computers", "computer"},
			{"were", "was"},
			{"you're", "you are"},
			{"i'm", "i am"},
			{"same", "alike"},
			{"sorry", "apologize"},
			{"aren't", "are not"},
			{"mom", "mother"},
			{"dad", "father"}
	};

	/** 
	 * Pronoun pairs for echoing phrases back to a user.
	 */
	static final String[][] PRONOUN_MAP = {
	    {"am", "are"},
        {"your", "my"},
        {"me", "you"},
        {"myself", "yourself"},
        {"yourself", "myself"},
        {"i", "you"},
        {"you", "I"},
        {"my", "your"},
        {"i'm", "you are"}	    
	};
	
	/**
	 * When the user input doesn't match any of the keyword patterns in the 
	 * response table then this response is selected.
	 */
	static final String NO_MATCH_RESPONSE = "Please go on.";
	
    /** 
     * Some responses are randomly chosen from a list. This seed is used
     * to make those choices reproducible for testing purposes. 
     */	
	static final int SEED = 123;

	public static String getRandomMessage() {
		List<String> messages = Arrays.asList(
				NO_MATCH_RESPONSE, "I rather hear from you first.", "You may have something interesting to say.", "I'm all ears!"
		);
		Random random = new Random();
		int random_int = random.nextInt(messages.size());
		return messages.get(random_int);
	}
}
