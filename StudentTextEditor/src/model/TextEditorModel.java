package model;

import controller.TextEditorDocument;

import java.util.Observable;

/**
 * The TextEditorModel only stores information about what is currently in the document. It does not
 * store information about what will come into the document, such as current font selection.
 */
public class TextEditorModel extends Observable implements TextEditorDocument
{
	private String docString;
	private StyleList styles;
	
	public TextEditorModel()
	{
		styles = new StyleList();
		docString = "";
	}
	
	// ----------------------------------------------------------------------------
	// Text Functions
	// ----------------------------------------------------------------------------
	public int length()
	{
		return docString.length();
	}
	
	public String getText()
	{
		return docString;
	}
	
	public String getStyleText(int styleIndex)
	{
		int textIndex = styles.getTextIndexFromStyleIndex(styleIndex);
		int textLength = styles.get(styleIndex).getLength();
		return docString.substring(textIndex, textIndex + textLength);
	}
	
	/**
	 * Get the substring of the text that fits within the bounds given.
	 *
	 * @pre 0 <= start && start < length()
	 * @pre length >= 0
	 * @pre start + length <= length()
	 *
	 * @post retval != null
	 */
	public String getTextSubstring(int start, int length)
	{
		return docString.substring(start, start + length);
	}

	// ----------------------------------------------------------------------------
	// Styles Functions
	// ----------------------------------------------------------------------------
	public void setBold(int start, int length, boolean boldOn)
	{
		styles.setBold(start, length, boldOn);
		updateObservers(new TextChange(TextChangeType.BOLD, start, length));
	}
	public boolean isBold(int index)
	{
		return styles.isBold(index);
	}
	public boolean isRangeBold(int start, int length)
	{
		return styles.isRangeBold(start, length);
	}
	
	public void setItalic(int start, int length, boolean italicOn)
	{
		styles.setItalic(start, length, italicOn);
		updateObservers(new TextChange(TextChangeType.ITALIC, start, length));
	}
	public boolean isItalic(int index)
	{
		return styles.isItalic(index);
	}
	public boolean isRangeItalic(int start, int length)
	{
		return styles.isRangeItalic(start, length);
	}
	
	public void setUnderline(int start, int length, boolean underlineOn)
	{
		styles.setUnderline(start, length, underlineOn);
		updateObservers(new TextChange(TextChangeType.UNDERLINE, start, length));
	}
	public boolean isUnderline(int index)
	{
		return styles.isUnderline(index);
	}
	public boolean isRangeUnderline(int start, int length)
	{
		return styles.isRangeUnderline(start, length);
	}	
	
	public void appendText(String text, TextEditorStyle style)
	{
		StyleList ls = new StyleList();
		ls.add(text.length(), style);
		appendText(text, ls);
	}
	public void appendText(String text, StyleList textStyles)
	{
		insertTextAt(docString.length(), text, textStyles);
	}
	/**
	 * Insert the given text String at the given index in the current document.
	 *
	 * @pre 0 <= index && index <= length()
	 *
	 * @post getTextSubstring(index, text.length).equals(text) == true
	 * @post getStylesInRange(index, text.length).equals(textStyles) == true
	 */
	public void insertTextAt(int index, String text, StyleList textStyles)
	{
		styles.insertStylesToText(index, textStyles);
		insertStringToText(index, text);
	}
	public void insertTextAt(int index, String text, TextEditorStyle style)
	{
		StyleList ls = new StyleList();
		ls.add(text.length(), style);
		insertTextAt(index, text, ls);
	}
	
	private void insertStringToText(int index, String text)
	{
		String newDoc = "";
		if(index > 0)
		{
			newDoc += docString.substring(0, index);
		}
		newDoc += text;
		if(index < length())
		{
			newDoc += docString.substring(index);
		}
		docString = newDoc;
		
		updateObservers(new TextChange(TextChangeType.INSERT, index, text.length()));
	}
	
	
	/**
	 * Remove the text and styles that are found within the given range.
	 *
	 * @pre 0 <= start && start < length()
	 * @pre length > 0
	 */
	public void removeTextAt(int index, int length)
	{
		styles.removeStylesFromText(index, length);
		removeStringFromText(index, length);
	}
	
	private void removeStringFromText(int index, int length)
	{
		String newDoc = "";
		if(index > 0)
		{
			newDoc += docString.substring(0, index);
		}
		if(index + length < docString.length())
		{
			newDoc += docString.substring(index + length);
		}
		docString = newDoc;
		
		updateObservers(new TextChange(TextChangeType.REMOVE, index, length));
	}
	
	public StyleList getStyles()
	{
		return styles;
	}
	public int getStyleCount()
	{
		return styles.size();
	}
	public TextEditorStyle getStyle(int styleIndex)
	{
		return styles.get(styleIndex).getStyle();
	}
	public int getStyleLength(int styleIndex)
	{
		return styles.get(styleIndex).getLength();
	}
	public StyleList getStylesInRange(int start, int length)
	{
		return styles.getStylesInRange(start, length);
	}
	
	protected void updateObservers(TextChange change)
	{
		setChanged();
		notifyObservers(change);
	}
}
