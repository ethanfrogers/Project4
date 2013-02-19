package controller;

import model.StyleList;

public interface TextEditorDocument
{
	public boolean isBold(int textIndex);
	public boolean isItalic(int textIndex);
	public boolean isUnderline(int textIndex);
	
	public void setBold(int start, int length, boolean boldOn);
	public void setItalic(int start, int length, boolean italicOn);
	public void setUnderline(int start, int length, boolean underlineOn);
	
	public void insertTextAt(int start, String toInsert, StyleList styles);
	public void removeTextAt(int start, int length);
	
	public String getText();
	public StyleList getStyles();
	public StyleList getStylesInRange(int start, int length);
}
