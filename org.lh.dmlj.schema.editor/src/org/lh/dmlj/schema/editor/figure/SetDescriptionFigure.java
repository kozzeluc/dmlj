package org.lh.dmlj.schema.editor.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.lh.dmlj.schema.editor.Plugin;

public class SetDescriptionFigure extends Figure {	
	
	// We append each line in the set description with 2 spaces; if we don't 
	// this, a weird kind of truncation takes place on the longest line.  Since 
	// we currently don't offer the ability to edit a set description, the user 
	// will not notice this.
	private static final String FILLER = "  ";
	
	private TextFlow textFlow;
	
	private String   name;
	private String 	 pointers;
	private String 	 membershipOption;
	private String   order;
	private String	 sortKeys;
	private String	 systemOwnerArea;
	
	public SetDescriptionFigure() {
		super();		
		
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
		setForegroundColor(ColorConstants.black);
		
		FlowPage flowPage = new FlowPage();
		
		textFlow = new TextFlow();
		textFlow.setFont(Plugin.getDefault().getFigureFont());
		textFlow.setLayoutManager(new ParagraphTextLayout(textFlow,
													      ParagraphTextLayout.WORD_WRAP_SOFT));		
		
		flowPage.add(textFlow);
		
		setLayoutManager(new StackLayout());
		add(flowPage);		
	}	

	public void setMembershipOption(String membershipOption) {
		this.membershipOption = membershipOption;
		setTextFlowText();
	}


	public void setName(String name) {
		this.name = name;
		setTextFlowText();
	}

	public void setOrder(String order) {
		this.order = order;
		setTextFlowText();
	}


	public void setPointers(String pointers) {
		this.pointers = pointers;
		setTextFlowText();
	}

	public void setSortKeys(String sortKeys) {
		this.sortKeys = sortKeys;
		setTextFlowText();
	}

	public void setSystemOwnerArea(String systemOwnerArea) {
		this.systemOwnerArea = systemOwnerArea;
		setTextFlowText();
	}

	private void setTextFlowText() {
		StringBuilder p = new StringBuilder();
		
		p.append(name);
		p.append(FILLER);
		p.append("\n");
		
		p.append(pointers);
		p.append(" ");
		p.append(membershipOption);
		p.append(" ");
		p.append(order);
		p.append(FILLER);
		
		if (sortKeys != null) {
			p.append("\n");
			p.append(sortKeys);	
			p.append("  ");
		}
		
		if (systemOwnerArea != null) {
			p.append("\n(");
			p.append(systemOwnerArea);
			p.append(")");
			p.append(FILLER);
		}
		
		textFlow.setText(p.toString());		
	}	
	
}