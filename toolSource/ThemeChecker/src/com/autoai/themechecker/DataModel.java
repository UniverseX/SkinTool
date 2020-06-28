package com.autoai.themechecker;

interface DataModel {
	void prepareStandData();

	void doCompare(String dstPath);

	String[] getStandardDiffArray();

	String[] getDstDiffArray();
}
