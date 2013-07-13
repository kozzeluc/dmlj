package org.lh.dmlj.schema.editor.dictguide;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dictguide.helper.Phase1Extractor;
import org.lh.dmlj.schema.editor.dictguide.helper.Phase1ExtractorCatalog;
import org.lh.dmlj.schema.editor.dictguide.helper.Phase2Extractor;
import org.lh.dmlj.schema.editor.dictguide.helper.Phase2ExtractorCatalog;
import org.lh.dmlj.schema.editor.dictguide.helper.Phase3Checker;
import org.lh.dmlj.schema.editor.dictguide.helper.Phase4Formatter;
import org.lh.dmlj.schema.editor.property.RecordInfoValueObject;
import org.lh.dmlj.schema.editor.service.ServicesPlugin;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class DictguidesRegistry {
	
	private static final String ALPHABET_LC = "abcdefghijklmnopqrstuvwxyz";
	private static final String ALPHABET_UC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL_CHARS = " -_.";
	
	public static final DictguidesRegistry INSTANCE = new DictguidesRegistry();
	
	private String	    activeId;
	private File 	    folder;
	private File	    manifestFile;
	private Set<String> entries = new LinkedHashSet<>();
	
	private static BufferedReader createBufferedReader(ZipFile file,
													   ZipEntry entry) {
		if (entry != null) {
			InputStream is;
			try {
				is = file.getInputStream(entry);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			InputStreamReader isr = new InputStreamReader(is);
			return new BufferedReader(isr);
		}
		return null;
	}	
	
	private static void dumpPdfExtract(String pdfExtract, File file) 
		throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(file));
		out.println(pdfExtract);
		out.flush();
		out.close();
	}

	private DictguidesRegistry() {
		super();
		folder = new File(Plugin.getDefault().getStateLocation().toFile(), "dictguides");
		if (!folder.exists()) {
			if (!folder.mkdir()) {
				throw new RuntimeException("cannot create dictguides folder");
			}
		}
		manifestFile = new File(folder, "MANIFEST.MF");
		if (manifestFile.exists()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(manifestFile));
				for (String line = in.readLine(); line != null; 
					 line = in.readLine()) {
					
					String entry = line.trim();
					if (!entry.equals("")) {
						// ignore blank lines
						if (entry.startsWith("*")) {
							entries.add(entry.substring(1));
							// get the active id:
							activeId = entry.substring(1, line.indexOf(","));
						} else {
							entries.add(entry);
						}
					}
				}
				in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void createEntry(File dictionaryStructureFile,
					   	    String dictionaryStructureTitle,
					   	    File sqlFile,
					   	    String sqlTitle,
					   	    String id,
					   	    boolean defaultForInfoTab) {
		
		if (!isValid(id)) {
			throw new IllegalArgumentException("id is invalid: " + id);
		}
		
		if (entryExists(id)) {
			throw new IllegalArgumentException("Id is already in use: " + id);
		}
		
		if (dictionaryStructureTitle.indexOf(",") > -1 ||
			dictionaryStructureTitle.indexOf("\n") > -1) {
			
			String message = 
				"comma's and new line characters are not allowed in a " +
				"document title: " + dictionaryStructureTitle;
			throw new IllegalArgumentException(message);
		}
		
		if (sqlTitle.indexOf(",") > -1) {
			String message = "comma's and new line characters are not " +
							 "allowed in a document title: " + sqlTitle;
			throw new IllegalArgumentException(message);
		}
		
		IPdfExtractorService pdfExtractorService = 
			ServicesPlugin.getDefault().getService(IPdfExtractorService.class);
		Assert.isTrue(pdfExtractorService != null, 
					  "logic error: PDF Extractor Service == null");
	
		try {
			
			// we need a temporary files folder; we will not cleanup the 
			// contents of this folder when we're done so that we can do some 
			// troubleshooting if needed
			File tmpFolder = Plugin.getDefault().createTmpFolder();
			
			// get the chapter we need from the Dictionary Structure Reference 
			// Guide; for troubleshooting purposes we will write the contents of
			// that chapter to our temporary files folder
			ByteArrayOutputStream out1 = new ByteArrayOutputStream();				
			PdfContentConsumer contentConsumer = new PdfContentConsumer(out1);
			pdfExtractorService.extractContent(new FileInputStream(dictionaryStructureFile), 
											   contentConsumer);
			contentConsumer.finish();
			String pdf1Extract = out1.toString();
			out1.close();
			dumpPdfExtract(pdf1Extract, new File(tmpFolder, "pdf1Extract.txt"));
			
			// parse the SQL Reference Guide; for troubleshooting purposes we 
			// will write the contents of that chapter to our temporary files 
			// folder
			ByteArrayOutputStream out2 = new ByteArrayOutputStream();
			contentConsumer = new PdfContentConsumer(out2);
			pdfExtractorService.extractContent(new FileInputStream(sqlFile), contentConsumer);
			contentConsumer.finish();
			String pdf2Extract = out2.toString();
			out2.close();
			dumpPdfExtract(pdf2Extract, new File(tmpFolder, "pdf2Extract.txt"));
			
			// create 1 file in a separate subfolder in the temporary files 
			// folder, for each record description found in the Dictionary 
			// Structure Reference Guide; each file will hold the raw PDF 
			// extraction data
			File tmpFolder1a = new File(tmpFolder, "phase1");
			tmpFolder1a.mkdir();			
			StringReader in1 = new StringReader(pdf1Extract);
			Phase1Extractor.performTask(in1, tmpFolder1a);
			
			// create 1 file in a separate subfolder in the temporary files 
			// folder, for each relevant table description found in the SQL 
			// Reference Guide; each file will hold the raw PDF extraction data			
			File tmpFolder1b = new File(tmpFolder, "phase1catalog");
			tmpFolder1b.mkdir();
			Phase1ExtractorCatalog.performTask(new File(tmpFolder, "pdf2Extract.txt"), 
											   tmpFolder1b);
			
			// create 1 file in yet another separate subfolder in the temporary 
			// files folder, again for each record description found in the 
			// Dictionary Structure Reference Guide; each file will hold the 
			// structured extraction data
			File tmpFolder2a = new File(tmpFolder, "phase2");
			tmpFolder2a.mkdir();			
			Phase2Extractor.performTask(tmpFolder1a, tmpFolder2a, 
									    dictionaryStructureTitle);
			
			// create 1 file in yet another separate subfolder in the temporary 
			// files folder, again for each relevant table description found in 
			// the SQL Reference Guide; each file will hold the structured 
			// extraction data
			File tmpFolder2b = new File(tmpFolder, "phase2catalog");
			tmpFolder2b.mkdir();					
			Phase2ExtractorCatalog.performTask(tmpFolder1b, tmpFolder2b, 
											   sqlTitle);
			
			// perform some checks on the structured extraction data for the 
			// Dictionary Structure Reference Guide; no exceptions will be 
			// thrown, just some logging will be done if needed
			Phase3Checker.performTask(tmpFolder2a);
			
			// perform some final optimizations on the structured extraction 
			// data for both the Dictionary Structure Reference Guide and the
			// SQL Reference Guide
			File tmpFolder4 = new File(tmpFolder, "phase4");
			tmpFolder4.mkdir();
			Phase4Formatter.performTask(tmpFolder2a, tmpFolder4);
			Phase4Formatter.performTask(tmpFolder2b, tmpFolder4);			
			
			// create the .zip file that will be used for displaying information
			// in the "Info" tab (Properties view); this is our final and only
			// relevant artifact as all temporary files created so far will be
			// removed when the workbench exits
			File zipFile = new File(folder, id + ".zip");
			if (zipFile.exists()) {
				if (!zipFile.delete()) {
					throw new RuntimeException(".zip file could not be deleted: " + 
											   zipFile.getAbsolutePath());
				}
			}
			ZipOutputStream zipOut = 
				new ZipOutputStream(new FileOutputStream(zipFile));
			PrintWriter out = new PrintWriter(zipOut);
			for (File file : tmpFolder4.listFiles()) {
				ZipEntry entry = new ZipEntry(file.getName());
				zipOut.putNextEntry(entry);
				BufferedReader in = new BufferedReader(new FileReader(file));
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					out.println(line);
				}
				out.flush();
				in.close();
			}
			out.close();
			zipOut.close();
			
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
		
		entries.add(id + "," + dictionaryStructureTitle + "," + sqlTitle);
		if (defaultForInfoTab) {
			activeId = id;
		}
		
		persist();
		
	}
	
	public void deleteEntry(String id) {
		String[] e = entries.toArray(new String[] {});
		for (int i = 0; i < e.length; i++) {
			if (e[i].startsWith(id + ",")) {
				entries.remove(e[i]);
				break;
			}
		}
		if (id.equals(activeId)) {
			activeId = null;
		}
		File file = new File(folder, id + ".zip");
		file.delete();
		persist();
	}

	public boolean entryExists(String id) {
		for (String entry : entries) {
			if (entry.startsWith(id + ",")) {
				return true;
			}
		}
		return false;
	}

	public String getActiveId() {
		return activeId;
	}

	public Iterable<String> getAllIds() {
		List<String> list = new ArrayList<>();
		for (String entry : entries) {
			list.add(entry.substring(0, entry.indexOf(",")));
		}
		return list;
	}

	public String getDictionaryStructureTitle(String id) {
		for (String entry : entries) {
			String anId = entry.substring(0, entry.indexOf(","));
			if (anId.equals(id)) {
				return entry.substring(entry.indexOf(",") + 1, 
									   entry.lastIndexOf(","));
			}
		}
		return "";
	}

	public String getDocumentTitle(File pdfFile) {
		IPdfExtractorService pdfExtractorService = 
			ServicesPlugin.getDefault().getService(IPdfExtractorService.class);
		Assert.isTrue(pdfExtractorService != null, "logic error: PDF Extractor Service == null");
		DocumentTitleExtractor documentTitleExtractor = 
			new DocumentTitleExtractor(pdfExtractorService, pdfFile);
		return documentTitleExtractor.extractTitle();
	}
	
	/**
	 * Creates a RecordInfoValueObject for the given record, provided that the
	 * user has selected a reference guide combination and an entry for the 
	 * record exists in the .zip file.  The RecordInfoValueObject will
	 * contain a list with one ElementInfoValueObject instance per element in 
	 * the record. 
	 * @param recordName the name of the record 
	 * @return a RecordInfoValueObject or null if either no reference guide
	 *         combination is selected or no information exists in the .zip file
	 */
	public RecordInfoValueObject getRecordInfoValueObject(String recordName) 
		throws IOException {		
		
		// if no reference guide combination is selected, return null
		if (activeId == null) {
			return null;
		}
		
		// adjust the record name in the case of DDLCATLOD records...
		String adjustedRecordName;
		if (recordName.endsWith("_")) {
			StringBuilder p = new StringBuilder(recordName);
			p.setLength(p.length() - 1);
			adjustedRecordName = p.toString();
		} else {
			adjustedRecordName = recordName;
		}
		
		// open the .zip file; return null if the file does not exist
		File file = new File(folder, activeId + ".zip");
		if (!file.exists()) {
			System.out.println(".zip file not found for reference guide " +
							   "combination " + activeId);
			return null;
		}
		ZipFile zipFile = new ZipFile(file);
		
		// locate the .zip entry for the record; if we cannot find it, close the
		// .zip file and return null to indicate to the caller that no 
		// information can be found...
		ZipEntry entry = zipFile.getEntry(adjustedRecordName + ".txt");
		if (entry == null) {
			zipFile.close();
			return null;
		}
		
		// create the RecordInfoValueObject together with a list of element info
		// value objects...		
		BufferedReader in = createBufferedReader(zipFile, entry);
		RecordInfoValueObject recordInfoValueObject = 
			RecordInfoValueObjectFactory.INSTANCE
									    .createRecordInfoValueObject(in);
		in.close();		
		
		// close the .zip file and we're done
		zipFile.close();
		return recordInfoValueObject;
	}	
	
	public String getSqlTitle(String id) {
		for (String entry : entries) {
			String anId = entry.substring(0, entry.indexOf(","));
			if (anId.equals(id)) {
				return entry.substring(entry.lastIndexOf(",") + 1);
			}
		}
		return "";
	}

	public boolean isValid(String id) {
		for (int i = 0; i < id.length(); i++) {
			char c = id.charAt(i);
			if (ALPHABET_LC.indexOf(c) < 0 &&
				ALPHABET_UC.indexOf(c) < 0 && 
				DIGITS.indexOf(c) < 0 &&
				SPECIAL_CHARS.indexOf(c) < 0) {
					
				return false;					
			}
		}
		return true;
	}

	private void persist() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(manifestFile));
			for (String entry : entries) {
				if (entry.startsWith(activeId + ",")) {
					out.println("*" + entry);
				} else {
					out.println(entry);
				}
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}	
	
	public boolean setActiveId(String id) {
		
		if (id == null && activeId == null) {
			return false;
		}
		
		if (id == null) {
			activeId = null;
			persist();
			return true;
		}
		
		if (id.equals(activeId)) {
			return false;
		}
		
		for (String entry : entries) {
			if (entry.startsWith(id + ",")) {				
				activeId = id;
				persist();
				return true;
			}
		}
		
		return false;
	}

}