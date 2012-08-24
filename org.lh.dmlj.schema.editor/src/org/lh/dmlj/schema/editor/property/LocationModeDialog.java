package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class LocationModeDialog extends Dialog {

	private java.util.List<Element> availableElements = new ArrayList<>();
	private Button 		 		    btnAddCalcElements;
	private Button 		 		    btnCalc;
	private Button 		 			btnDirect;
	private Button 		 			btnDisplacementPages;
	private Button 		 			btnMoveCalcElementDown;
	private Button 		 			btnMoveCalcElementUp;
	private Button 		 			btnNoDisplacement;
	private Button 		 			btnRemoveCalcElements;
	private Button 		 			btnSymbolicDisplacement;
	private Button 		 			btnVia;
	private java.util.List<Element> calcElements = new ArrayList<>();
	private Combo 		 			comboCalcDuplicatesOption;
	private Combo 		 			comboViaSet;
	private Short					displacementPageCount;
	private DuplicatesOption		duplicatesOption;
	private List 		 			listAvailableElements;
	private List 		 			listCalcElements;
	private LocationMode			locationMode;
	private SchemaRecord 			record;
	private String 					symbolicDisplacementName;
	private Text   		 			textDisplacementPages;
	private Text 		 			textSymbolicDisplacement;
	private String				    viaSetName;
	
	private static boolean isOccursInvolved(Element element) {		
		for (Element current = element; current != null;
			 current = current.getParent()) {
			
			if (current.getOccursSpecification() != null) {
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LocationModeDialog(Shell parentShell, SchemaRecord record) {
		super(parentShell);
		this.record = record;
	}	
	
	protected void addCalcElements() {
		
		int[] i = listAvailableElements.getSelectionIndices();
		Arrays.sort(i);
		
		java.util.List<Element> tmpList = new ArrayList<>();
		int k = listCalcElements.getItemCount();
		for (int j = 0; j < i.length; j++) {
			Element element = availableElements.get(i[j]);
			calcElements.add(element);
			listCalcElements.add(element.getName());
			tmpList.add(element);
		}	
		int[] m = new int[i.length];
		for (int n = 0; n < m.length; n++) {
			m[n] = k + n;
		}
		listCalcElements.deselectAll();
		listCalcElements.select(m);
		
		availableElements.removeAll(tmpList);
		listAvailableElements.remove(i);
		
		enableAndDisable();
		
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
	    shell.setText("Set location mode for " + record.getName());	    
	}	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 5;
		
		btnCalc = new Button(container, SWT.RADIO);
		btnCalc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnCalc.setText("CALC using :");
		new Label(container, SWT.NONE);
		
		listAvailableElements = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listAvailableElements.setToolTipText("Available elements");
		GridData gd_listAvailableElements = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 2);
		gd_listAvailableElements.heightHint = 75;
		gd_listAvailableElements.widthHint = 150;
		gd_listAvailableElements.minimumWidth = -1;
		gd_listAvailableElements.horizontalIndent = 15;
		listAvailableElements.setLayoutData(gd_listAvailableElements);
		
		btnAddCalcElements = new Button(container, SWT.NONE);
		btnAddCalcElements.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		btnAddCalcElements.setText(">");
		
		listCalcElements = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listCalcElements.setToolTipText("CALC key elements");
		GridData gd_listCalcElements = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 2);
		gd_listCalcElements.heightHint = 75;
		gd_listCalcElements.widthHint = 150;
		gd_listCalcElements.minimumWidth = 100;
		listCalcElements.setLayoutData(gd_listCalcElements);
		
		btnMoveCalcElementUp = new Button(container, SWT.NONE);
		GridData gd_btnMoveCalcElementUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_btnMoveCalcElementUp.widthHint = 40;
		btnMoveCalcElementUp.setLayoutData(gd_btnMoveCalcElementUp);
		btnMoveCalcElementUp.setText("Up");
		
		btnRemoveCalcElements = new Button(container, SWT.NONE);
		btnRemoveCalcElements.setText("<");
		
		btnMoveCalcElementDown = new Button(container, SWT.NONE);
		GridData gd_btnMoveCalcElementDown = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnMoveCalcElementDown.widthHint = 40;
		btnMoveCalcElementDown.setLayoutData(gd_btnMoveCalcElementDown);
		btnMoveCalcElementDown.setText("Down");
		
		Label lblDuplicates = new Label(container, SWT.NONE);
		GridData gd_lblDuplicates = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDuplicates.verticalIndent = 5;
		gd_lblDuplicates.horizontalIndent = 15;
		lblDuplicates.setLayoutData(gd_lblDuplicates);
		lblDuplicates.setText("Duplicates :");
		
		comboCalcDuplicatesOption = new Combo(container, SWT.READ_ONLY);
		GridData gd_comboCalcDuplicatesOption = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_comboCalcDuplicatesOption.verticalIndent = 5;
		gd_comboCalcDuplicatesOption.widthHint = 100;
		comboCalcDuplicatesOption.setLayoutData(gd_comboCalcDuplicatesOption);
		
		Label label_1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd_label_1.verticalIndent = 10;
		gd_label_1.horizontalIndent = 15;
		label_1.setLayoutData(gd_label_1);
		
		btnDirect = new Button(container, SWT.RADIO);
		btnDirect.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnDirect.setText("DIRECT");
		new Label(container, SWT.NONE);
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd_label.verticalIndent = 10;
		gd_label.horizontalIndent = 15;
		label.setLayoutData(gd_label);
		
		btnVia = new Button(container, SWT.RADIO);
		btnVia.setText("VIA set :");
		
		comboViaSet = new Combo(container, SWT.READ_ONLY);
		GridData gd_comboViaSet = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_comboViaSet.widthHint = 100;
		comboViaSet.setLayoutData(gd_comboViaSet);
		
		Label lblDisplacement = new Label(container, SWT.NONE);
		lblDisplacement.setAlignment(SWT.RIGHT);
		GridData gd_lblDisplacement = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblDisplacement.verticalIndent = 5;
		gd_lblDisplacement.horizontalIndent = 15;
		lblDisplacement.setLayoutData(gd_lblDisplacement);
		lblDisplacement.setText("Displacement :");
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		
		btnNoDisplacement = new Button(composite, SWT.RADIO);
		btnNoDisplacement.setText("None");
		new Label(composite, SWT.NONE);
		
		btnSymbolicDisplacement = new Button(composite, SWT.RADIO);
		btnSymbolicDisplacement.setText("USING symbolic displacement :");
		
		textSymbolicDisplacement = new Text(composite, SWT.BORDER);
		GridData gd_textSymbolicDisplacement = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textSymbolicDisplacement.widthHint = 150;
		textSymbolicDisplacement.setLayoutData(gd_textSymbolicDisplacement);
		
		btnDisplacementPages = new Button(composite, SWT.RADIO);
		btnDisplacementPages.setText("Pages :");
		
		textDisplacementPages = new Text(composite, SWT.BORDER | SWT.RIGHT);
		GridData gd_textDisplacementPages = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textDisplacementPages.widthHint = 50;
		textDisplacementPages.setLayoutData(gd_textDisplacementPages);

		btnCalc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		btnDirect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		btnVia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		listAvailableElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		listCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		btnAddCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addCalcElements();				
			}						
		});
		
		btnRemoveCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeCalcElements();				
			}						
		});
		
		btnMoveCalcElementUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveCalcElementUp();				
			}						
		});
		
		btnMoveCalcElementDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveCalcElementDown();				
			}						
		});
		
		comboCalcDuplicatesOption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		comboViaSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
			
		btnNoDisplacement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		btnSymbolicDisplacement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		btnDisplacementPages.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		textSymbolicDisplacement.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		
		textSymbolicDisplacement.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
					String p = textSymbolicDisplacement.getText().toUpperCase();
					textSymbolicDisplacement.setText(p);
					textSymbolicDisplacement.setSelection(p.length());
					enableAndDisable();					
				}
			}
		});
		
		textDisplacementPages.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		
		textDisplacementPages.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
					enableAndDisable();					
				}
			}
		});
		
		initialize();
		
		return container;
	}

	/**
	 * Checks whether the user has changed anything to the record's location 
	 * mode data.
	 * @return true if anything has changed to the record's location mode, false
	 *         if not
	 */
	private boolean anythingChanged() {
		
		// did the location mode itself change ?
		if (btnCalc.getSelection() &&
			record.getLocationMode() != LocationMode.CALC ||
			btnDirect.getSelection() &&
			record.getLocationMode() != LocationMode.DIRECT ||
			btnVia.getSelection() &&
			record.getLocationMode() != LocationMode.VIA) {
			
			return true;
		}
		
		// if the location mode is DIRECT, there's nothing else that can be
		// changed
		if (record.getLocationMode() == LocationMode.DIRECT) {
			return false;
		}
		
		// separate checks for CALC and VIA
		if (record.getLocationMode() == LocationMode.CALC) {
			// CALC record, check the CALC key elements			
			if (calcElements.size() != record.getCalcKey().getElements().size()) {
				// the number of elements in the CALC key has changed
				return true;
			}
			for (int i = 0; i < calcElements.size(); i++) {
				Element modelElement = 
					record.getCalcKey().getElements().get(i).getElement();
				Element dialogElement = calcElements.get(i);
				if (dialogElement != modelElement) {
					// element mismatch
					return true;
				}
			}
			// check the duplicates option
			String modelDuplicatesOption = record.getCalcKey()
					  							 .getDuplicatesOption()
					  							 .toString()
					  							 .replaceAll("_", " ");
			if (!comboCalcDuplicatesOption.getText().equals(modelDuplicatesOption)) {
				// duplicates option changed
				return true;
			}
		} else {
			// VIA record, check the VIA set first
			String modelSetName = 
				record.getViaSpecification().getSet().getName();
			String dialogSetName = comboViaSet.getText();
			if (!dialogSetName.equals(modelSetName)) {
				// VIA set changed
				return true;
			}
			// check the displacement specification
			ViaSpecification via = record.getViaSpecification();
			if (btnNoDisplacement.getSelection() &&
				(via.getSymbolicDisplacementName() != null || 
				 via.getDisplacementPageCount() != null)) {
				 
				// user has removed symbolic displacement name or displacement
				// pages
				return true;
			} else if (btnSymbolicDisplacement.getSelection() &&
					   (via.getSymbolicDisplacementName() == null ||
					    via.getDisplacementPageCount() != null)) {
				
				// user has specified that a symbolic displacement name has to
				// be used
				return true;
			} else if (btnSymbolicDisplacement.getSelection() &&
					   !textSymbolicDisplacement.getText()
					   							.trim()
					   							.equals(via.getSymbolicDisplacementName())) {
			
				// user has modified the symbolic displacement name
				return true;
			} else if (btnDisplacementPages.getSelection() &&
					   (via.getSymbolicDisplacementName() != null ||
					    via.getDisplacementPageCount() == null)) {
				
				// user has specified a displacement page count
				return true;
			} else if (btnDisplacementPages.getSelection() &&
					   Short.valueOf(textDisplacementPages.getText().trim())
							.shortValue() != via.getDisplacementPageCount()
												.shortValue()) {
				
				// user has changed the displacement page count
				return true;
			}
		}
		// if we get here, no changes were entered
		return false;
	}

	private void enableAndDisable() {
		
		// CALC stuff
		listAvailableElements.setEnabled(btnCalc.getSelection());
		listCalcElements.setEnabled(btnCalc.getSelection());
		btnAddCalcElements.setEnabled(btnCalc.getSelection() &&
									  listAvailableElements.getSelectionCount() > 0 &&
									  getComputedCalcKeyLength() <= 256);
		btnRemoveCalcElements.setEnabled(btnCalc.getSelection() &&
				 					 	 listCalcElements.getSelectionCount() > 0);
		btnMoveCalcElementUp.setEnabled(btnCalc.getSelection() &&
				 						listCalcElements.getSelectionCount() == 1 &&
				 						listCalcElements.getSelectionIndex() > 0);
		btnMoveCalcElementDown.setEnabled(btnCalc.getSelection() &&
										  listCalcElements.getSelectionCount() == 1 &&
					 					  listCalcElements.getSelectionIndex() < listCalcElements.getItemCount() - 1);
		comboCalcDuplicatesOption.setEnabled(btnCalc.getSelection());
		
		// VIA stuff
		comboViaSet.setEnabled(btnVia.getSelection());
		btnNoDisplacement.setEnabled(btnVia.getSelection());
		btnSymbolicDisplacement.setEnabled(btnVia.getSelection());
		btnDisplacementPages.setEnabled(btnVia.getSelection());
		textSymbolicDisplacement.setEnabled(btnVia.getSelection() &&
											btnSymbolicDisplacement.getSelection());
		textDisplacementPages.setEnabled(btnVia.getSelection() &&
										 btnDisplacementPages.getSelection());
		
		// OK button 
		Button okButton = getButton(IDialogConstants.OK_ID);		
		boolean enabled = false;
		if (btnCalc.getSelection()) {
			// CALC radio button selected; check if we have at least 1 CALC
			// key element and a value for the duplicates option
			enabled = !calcElements.isEmpty() && 
					  comboCalcDuplicatesOption.getSelectionIndex() > -1;
		} else if (btnDirect.getSelection()) {
			// DIRECT radio button selected; nothing needed
			enabled = true;
		} else {
			// VIA radio button selected; check if we have a set and, if
			// specified, if the displacement specification is valid
			if (comboViaSet.getSelectionIndex() > -1) {
				if (btnSymbolicDisplacement.getSelection()) {
					ValidationResult validationResult = 
						NamingConventions.validate(textSymbolicDisplacement.getText(), 
												   NamingConventions.Type
												   .SYMBOLIC_DISPLACEMENT);
					if (validationResult.getStatus() == ValidationResult.Status.OK) {
						enabled = true;
					}
				} else if (btnDisplacementPages.getSelection()) {						
					try {
						// get the new displacement page count
						Short i = Short.valueOf(textDisplacementPages.getText())
								 	   .shortValue();
						// must be an unsigned integer in the range 0 through 
						// 32,767
						if (i > 0) {
							enabled = true;
						}
					} catch (NumberFormatException e) {							
					}
				} else {
					enabled = true;
				}
			}
		}
		if (enabled) {
			enabled = anythingChanged();
		}
		okButton.setEnabled(enabled);
		
		if (!enabled) {
			return;
		}
		
		// make sure we've got all information available should the user press
		// the OK button
		duplicatesOption = null;
		viaSetName = null;
		symbolicDisplacementName = null;
		displacementPageCount = null;
		if (btnCalc.getSelection()) {
			locationMode = LocationMode.CALC;
			if (comboCalcDuplicatesOption.getSelectionIndex() > -1) {
				for (DuplicatesOption aDuplicatesOption : DuplicatesOption.VALUES) {
					if (aDuplicatesOption.toString()
										 .replaceAll("_", " ")
										 .equals(comboCalcDuplicatesOption.getText())) {
						
						duplicatesOption = aDuplicatesOption;
						break;
					}
				}
			}
		} else if (btnDirect.getSelection()) {
			locationMode = LocationMode.DIRECT;
		} else {
			locationMode = LocationMode.VIA;
			viaSetName = comboViaSet.getText();
			if (btnSymbolicDisplacement.getSelection()) {
				symbolicDisplacementName = 
					textSymbolicDisplacement.getText().trim();
			} else if (btnDisplacementPages.getSelection()) {
				displacementPageCount = 
					Short.valueOf(textDisplacementPages.getText().trim());
			}
		}		
		
	}

	java.util.List<Element> getCalcKeyElements() {
		return new ArrayList<>(calcElements);
	}
	
	private int getComputedCalcKeyLength() {
		// compute the length of the CALC key given the CALC key elements in the
		// right list and all selected elements in the left list
		int i = 0;
		for (Element element : calcElements) {
			i += element.getLength();
		}
		int[] j = listAvailableElements.getSelectionIndices();
		for (int k : j) {
			Element element = availableElements.get(k);
			i += element.getLength();
		}
		return i;
	}
	
	Short getDisplacementPageCount() {
		return displacementPageCount;
	}

	DuplicatesOption getDuplicatesOption() {
		return duplicatesOption;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(475, 425);
	}
	
	LocationMode getLocationMode() {
		return locationMode;
	}

	String getSymbolicDisplacementName() {
		return symbolicDisplacementName;
	}

	String getViaSetName() {
		return viaSetName;
	}

	private void initialize() {
		
		// fill the list with the available (potential) CALC key elements; the
		// list with the actual CALC key elements, if relevant, will be filled 
		// later - take care of the backing list as well (in order to have the
		// underlying Element instances available)
		for (Element element : record.getElements()) {
			if (!element.getName().equals("FILLER") &&
				!isOccursInvolved(element)) {
				
				// restrictions :
				// - no element named FILLER can be used in the CALC key
				// - no repeating element (that is, one defined with an OCCURS 
				//   clause) and no element subordinate to a repeating element 
				//   can be used in the CALC key
				boolean calcKeyElement = false;
				if (record.getLocationMode() == LocationMode.CALC) {
					for (KeyElement keyElement : element.getKeyElements()) {
						if (keyElement.getKey() == record.getCalcKey()) {
							calcKeyElement = true;
							break;
						}
					}
				}
				if (!calcKeyElement) {
					availableElements.add(element);
					listAvailableElements.add(element.getName());
				}				
			}
		}
		
		// disable the CALC radio button if the record is not CALC and the list
		// of available elements is empty
		if (record.getLocationMode() != LocationMode.CALC &&
			availableElements.isEmpty()) {
			
			btnCalc.setEnabled(false);
		}
		
		// fill the combo containing the CALC key duplicates option values
		comboCalcDuplicatesOption.add(DuplicatesOption.FIRST
													  .toString()
													  .replaceAll("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.LAST
													  .toString()
													  .replaceAll("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.BY_DBKEY
													  .toString()
													  .replaceAll("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.NOT_ALLOWED
													  .toString()
													  .replaceAll("_", " "));
		
		// fill the combo containing the VIA set names; add all sets in which
		// the record participates as a member
		for (MemberRole memberRole : record.getMemberRoles()) {
			comboViaSet.add(memberRole.getSet().getName());
		}
		
		// if the record does not participate as a member in any set, disallow
		// the VIA radio button; the record cannot have VIA as its location mode
		if (comboViaSet.getItemCount() == 0) {
			btnVia.setEnabled(false);			
		}
		
		// set the location mode radio button selections and initialize location 
		// mode specific controls 
		if (record.getLocationMode() == LocationMode.CALC) {
			// location mode is CALC
			btnCalc.setSelection(true);
			// fill the list with CALC key elements and its backing list
			for (KeyElement keyElement : record.getCalcKey().getElements()) {
				Element element = keyElement.getElement();
				calcElements.add(element);
				listCalcElements.add(element.getName());
			}
			// select the duplicates option in the combo
			String duplicatesOption = record.getCalcKey()
											.getDuplicatesOption()
											.toString()
											.replaceAll("_", " ");
			for (int i = 0; i < comboCalcDuplicatesOption.getItemCount(); i++) {
				if (comboCalcDuplicatesOption.getItem(i).equals(duplicatesOption)) {
					comboCalcDuplicatesOption.select(i);
					break;
				}
			}
			//
			btnNoDisplacement.setSelection(true);
		} else if (record.getLocationMode() == LocationMode.DIRECT) {
			// location mode is DIRECT
			btnDirect.setSelection(true);
			//
			btnNoDisplacement.setSelection(true);
		} else {
			// location mode is VIA
			btnVia.setSelection(true);
			// select the set in the combo
			String setName = record.getViaSpecification().getSet().getName();
			for (int i = 0; i < comboViaSet.getItemCount(); i++) {
				if (comboViaSet.getItem(i).equals(setName)) {
					comboViaSet.select(i);
					break;
				}
			}
			// set the displacement data
			if (record.getViaSpecification().getSymbolicDisplacementName() != null) {
				btnSymbolicDisplacement.setSelection(true);
				textSymbolicDisplacement.setText(record.getViaSpecification()
													   .getSymbolicDisplacementName());
			} else if (record.getViaSpecification().getDisplacementPageCount() != null) {
				btnDisplacementPages.setSelection(true);
				short pages = record.getViaSpecification()
									.getDisplacementPageCount()
									.shortValue();
				textDisplacementPages.setText(String.valueOf(pages));
			} else {
				btnNoDisplacement.setSelection(true);
			}
		}		
	}

	protected void moveCalcElementDown() {		
		
		int i = listCalcElements.getSelectionIndex();
		Element element = calcElements.get(i);
		
		calcElements.remove(i);
		calcElements.add(i + 1, element);
		
		listCalcElements.remove(i);
		listCalcElements.add(element.getName(), i + 1);
		listCalcElements.select(i + 1);
		
		enableAndDisable();
		
	}

	protected void moveCalcElementUp() {		
		
		int i = listCalcElements.getSelectionIndex();
		Element element = calcElements.get(i);
		
		calcElements.remove(i);
		calcElements.add(i - 1, element);
		
		listCalcElements.remove(i);
		listCalcElements.add(element.getName(), i - 1);
		listCalcElements.select(i - 1);
		
		enableAndDisable();
		
	}

	protected void removeCalcElements() {		
		
		int[] i = listCalcElements.getSelectionIndices();		
		
		java.util.List<Element> tmpList = new ArrayList<>();
		java.util.List<Element> allElements = record.getElements();
		for (int j = 0; j < i.length; j++) {
			Element element = calcElements.get(i[j]);
			int m = allElements.indexOf(element);
			boolean inserted = false;
			for (int k = 0; k < availableElements.size(); k++) {
				int n = allElements.indexOf(availableElements.get(k));			
				if (m < n) {			
					availableElements.add(k, element);
					listAvailableElements.add(element.getName(), k);
					tmpList.add(element);
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				availableElements.add(element);
				listAvailableElements.add(element.getName());
				tmpList.add(element);
			}
		}	
		int[] m = new int[i.length];
		for (int n = 0; n < m.length; n++) {
			m[n] = availableElements.indexOf(tmpList.get(n));
		}
		listAvailableElements.deselectAll();
		listAvailableElements.select(m);
		
		calcElements.removeAll(tmpList);
		listCalcElements.remove(i);
		
		enableAndDisable();		
		
	}

}
