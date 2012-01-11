package com.jatayu.mnknowt;

public class QBookmark {

	private static QBookmark	instance;
	private int[]			bookmarks;

	private QBookmark() {
		bookmarks = new int[160];
	}

	public static QBookmark getInstance() {
		if (instance == null)
			instance = new QBookmark();
		return instance;
	}

	public int[] getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(int index, int value) {
		bookmarks[index] = value;
	}

	public int getIndividualBookMark(int i) {
		return bookmarks[i];
	}

	public void testValues() {
		for (int i = 0; i < 25; i++) {
			bookmarks[i] = 1;
		}
	}

}
