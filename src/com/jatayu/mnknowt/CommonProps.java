package com.jatayu.mnknowt;

/**
 * Properties common to the entire application
 * 
 * @author Neeraj Sharma
 * 
 */
public class CommonProps {

	public static final int		TOTAL_QUIZ_QUESTIONS	= 10;

	public static final boolean	LOG_ENABLED		= false;

	public static final String	COPYRIGHT_SYMBOL	= " \u00a9 ";
	public static final String	COMPANY_NAME		= " Code 83 Lab ";
	public static final String	APP_RELEASE_YEAR	= " 2011 ";
	public static final String	CONTACT_EMAIL		= "mnknowt@gmail.com";

	public static final String	HELP_FLASH_CARDS	= "Flash Cards: Browse through 150 flash cards. "
										+ "Each Flash Card presents a question "
										+ "with answer choices. Select 'Peek' button "
										+ "at the bottom of the card to view the correct "
										+ "answer choice. At any stage you can shuffle "
										+ "through the 150 flash cards by selecting the "
										+ "'shuffle' button at the top right corner."
										+ "Simply swipe across the screen to view the next question.\n\n";

	public static final String	HELP_QUIZ		= "Quiz: Take a rapid quiz comprising of questions randomly "
										+ "selected from the flash card pool. "
										+ "Each quiz contains 10 questions."
										+ "If you answered a question incorrectly"
										+ "you will be prompted with the correct answer at the bottom."
										+ "After completing the quiz you have the"
										+ "option to re-take a new quiz again!\n\n";

	public static final String	HELP_STATS		= "Stats: A simple graph that shows the total questions answered,"
										+ "number of correct"
										+ " and incorrect answers, "
										+ "along with best and worst score\n\n";
}
