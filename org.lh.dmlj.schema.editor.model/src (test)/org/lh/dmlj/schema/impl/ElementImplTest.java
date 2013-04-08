package org.lh.dmlj.schema.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.Usage;

public class ElementImplTest extends TestCase {

	private static File[] getTestdata() {
		return new File[] {new File("testdata/testdata1.txt"),
						   new File("testdata/testdata2.txt"),
						   new File("testdata/testdata3.txt")};
	}
	
	private int getExpectedLength(String line) {
		int i = line.lastIndexOf("::");
		if (i == -1) {
			throw new Error("expected length not found: " + line);
		}
		return Integer.valueOf(line.substring(i + 2)).intValue();
	}

	private String getPicture(String line) {		
		int i = line.indexOf("::");
		int j = line.lastIndexOf("::");
		if (i == -1 || j == -1 || i == j) {
			throw new Error("picture not found: " + line);
		}
		return line.substring(i + 2, j);
	}

	private static Usage getUsage(String line) {
		int i = line.indexOf("::");
		String value = line.substring(0, i);
		if (value.equals("0")) {
			return Usage.DISPLAY;
		} else if (value.equals("1")) {
			return Usage.COMPUTATIONAL;
		} else if (value.equals("4")) {
			return Usage.COMPUTATIONAL_3;
		} else if (value.equals("5")) {
			return Usage.BIT;
		} else {
			throw new Error("no Usage for value '" + value + "'");
		}
	}

	public void testPictureBytes() throws IOException {
		for (File file : getTestdata()) {
			BufferedReader in = new BufferedReader(new FileReader(file));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				Usage usage = getUsage(line);
				String picture = getPicture(line);			
				int expectedLength = getExpectedLength(line);
				int expectedPictureBytes;
				if (usage == Usage.DISPLAY) {
					expectedPictureBytes = expectedLength;
				} else if (usage == Usage.COMPUTATIONAL) {
					expectedPictureBytes = -1; // hard to check here
				} else if (usage == Usage.COMPUTATIONAL_3) {
					expectedPictureBytes = -1; // hard to check here
				} else if (usage == Usage.BIT) {
					expectedPictureBytes = expectedLength;
				} else {
					in.close();
					throw new Error("unexpected Usage: " + usage);
				}
				if (expectedPictureBytes > -1) {
					assertEquals("wrong calculated picture bytes; " + picture + " " + usage, 
							 	 expectedPictureBytes, ElementImpl.getPictureBytes(picture));
				}
			}
			in.close();			
		}
	}	
	
	public void testGetLength() throws IOException {
		for (File file : getTestdata()) {
			BufferedReader in = new BufferedReader(new FileReader(file));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				Usage usage = getUsage(line);
				String picture = getPicture(line);			
				int expectedLength = getExpectedLength(line);
				if (usage == Usage.BIT) {
					int i = expectedLength / 8;
					if (i == 0) {
						i = 1;
					}
					expectedLength = i;
				}
				Element element = SchemaFactory.eINSTANCE.createElement();
				element.setName("unknown");
				element.setUsage(usage);
				element.setPicture(picture);
				assertEquals("wrong calculated length; " + picture + " " + usage, expectedLength, 
							 element.getLength());
						
			}
			in.close();			
		}
	}

}
