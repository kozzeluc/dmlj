package org.lh.dmlj.schema.editor.preference;

import junit.framework.TestCase;

public class PrintPreferencesPreferencePageTest extends TestCase {

	public void testToMargin() {
		
		// pels
		for (int pels = 0 ; pels <= 72; pels++) {
			assertEquals("pels=" + pels + "; ", pels, 
						 PrintPreferencesPreferencePage.toMargin(pels, Unit.PIXELS));
		}
			
		// tenths of an inch; we support margins with a width of maximum 1 inch (i.e. 10 tenths)
		for (int pels = 0; pels <= 72; pels++) {			
			int tenthsOfAnInch = (int) Math.round(((double) pels) / 7.2d);			
			assertEquals("pels=" + pels + "; ", tenthsOfAnInch, 
						 PrintPreferencesPreferencePage.toMargin(pels, Unit.INCHES));
		}
		
		// millimeters		
		for (int pels = 0; pels <= 72; pels++) {
			double tenthsOfAnInch = ((double) pels) / 7.2d;
			int millimeters = (int) Math.round(tenthsOfAnInch * 2.54d);
			assertEquals("pels=" + pels + "; ", millimeters, 
					 	 PrintPreferencesPreferencePage.toMargin(pels, Unit.CENTIMETERS));
		}
	}
	
	public void testToPels() {
		
		// pels
		for (int pels = 0 ; pels <= 72; pels++) {
			assertEquals("pels=" + pels + "; ", pels, 
						 PrintPreferencesPreferencePage.toPels(pels, Unit.PIXELS));
		}
				
		// tenths of an inch
		for (int tenthsOfAnInch = 0 ; tenthsOfAnInch <= 10; tenthsOfAnInch++) {
			int pels = (int) Math.round(((double) tenthsOfAnInch) * 7.2d);
			assertEquals("tenthsOfAnInch=" + tenthsOfAnInch + "; ", pels, 
					 	 PrintPreferencesPreferencePage.toPels(tenthsOfAnInch, Unit.INCHES));
		}
	
		// millimeters
		for (int millimeters = 0 ; millimeters <= 25; millimeters++) {
			double tenthsOfAnInch = ((double) millimeters / 2.54d);
			int pels = (int) Math.round(tenthsOfAnInch * 7.2d);
			assertEquals("millimeters=" + millimeters + "; ", pels, 
					 	 PrintPreferencesPreferencePage.toPels(millimeters, Unit.CENTIMETERS));
		}
	}
	
	
}