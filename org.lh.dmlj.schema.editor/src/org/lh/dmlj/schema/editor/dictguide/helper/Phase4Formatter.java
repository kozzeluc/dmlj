package org.lh.dmlj.schema.editor.dictguide.helper;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Phase4Formatter {
	
	private static void processFile(File file, File outputFolder) 
		throws IOException{
		
		PrintWriter out = 
			new PrintWriter(new File(outputFolder, file.getName()));
		boolean inField = false;
		boolean inList1 = false;
		boolean inList2 = false;
		BufferedReader in = new BufferedReader(new FileReader(file));
		String p = in.readLine();
		while (p != null) {			
			if (p.length() == 80 && p.startsWith("/* Field ") && 
				p.indexOf("*/ -") != -1 && p.endsWith("-")) {
				
				inField = true;
				inList1 = false;
				inList2 = false;
				out.println(p);
			} else if (inField && p.startsWith("¦ ")) {
				inList1 = true;
				inList2 = false;
				out.println();
				out.println("* " + p.substring(2));
			} else if (inField && p.startsWith("– ")) {
				inList2 = true;
				out.println();
				out.println("  " + p);
			} else if (inField) {
				if (inList2) {
					out.println("    " + p);
				} else if (inList1) {
					out.println("  " + p);
				} else {
					out.println(p);
				}
			} else if (!inField && p.startsWith("")) {
				out.println(">>" + p.substring(2));
			} else {
				out.println(p);
			}
			p = in.readLine();
		}
		in.close();
		out.flush();
		out.close();
	}
	
	public static void performTask(File inputFolder, File outputFolder) 
		throws IOException {
		
		File[] file = inputFolder.listFiles();
		for (int i = 0; i < file.length; i++) {
			processFile(file[i], outputFolder);
		}
	}	
}