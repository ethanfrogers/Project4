package view;

import model.LengthStyle;
import model.TextEditorStyle;
import model.StyleList;
import model.StyleCreator;

import javax.swing.JTextPane;

import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;

public class JTextEditor extends JTextPane
{
	private StyledDocument doc;
	
	public JTextEditor()
	{
		// This ensures that even if the control loses focus, the highlight will still remain.
		setCaret(
			new DefaultCaret()
			{
				public void setSelectionVisible(boolean vis)
				{
					super.setSelectionVisible(true);
				}
			}
		);
		
		doc = getStyledDocument();
	}
	
	// ----------------------------------------------------------------------------
	// Text Methods
	// ----------------------------------------------------------------------------
	public String getText()
	{
		String result = "";
		
		if(doc.getLength() > 0)
		{
			try
			{
				result = doc.getText(0, doc.getLength());
			}
			catch(BadLocationException e)
			{
				System.out.println(e);
			}
		}
		
		return result;
	}
	
	public void insertTextAt(int start, String toInsert, StyleList styles)
	{
		try
		{
			int curIndex = 0;
			LengthStyle ls = null;
			for(int i = 0; i < styles.size(); i++)
			{
				ls = styles.get(i);
				
				doc.insertString(start + curIndex, toInsert.substring(curIndex, curIndex + ls.getLength()), ls.getStyle());
				
				curIndex += ls.getLength();
			}
		}
		catch(BadLocationException e)
		{
			System.out.println(e);
		}
	}
	
	public void removeTextAt(int start, int length)
	{
		try
		{
			doc.remove(start, length);
		}
		catch(BadLocationException e)
		{
			System.out.println(e);
		}
	}
	
	// ----------------------------------------------------------------------------
	// General Purpose Selection Methods
	// ----------------------------------------------------------------------------
	public int getSelectionLength()
	{
		return getSelectionEnd() - getSelectionStart();
	}
	
	// ----------------------------------------------------------------------------
	// General Purpose Text Style Methods
	// ----------------------------------------------------------------------------
	public TextEditorStyle getStyleAtIndex(int index)
	{
		TextEditorStyle result;
		if(index >= doc.getLength())
		{
			result = new TextEditorStyle(getInputAttributes().copyAttributes());
		}
		else
		{
			result = new TextEditorStyle(doc.getCharacterElement(index).getAttributes().copyAttributes());
		}
		return result;
	}
	
	private void setRangeStyle(int start, int length, TextEditorStyle style, boolean toSet)
	{
		if(length > 0)
		{
			doc.setCharacterAttributes(start, length, style, false);
		}
		else
		{
			if(toSet)
			{
				getInputAttributes().addAttributes(style);
			}
			else
			{
				getInputAttributes().removeAttributes(style);
			}
		}
	}
	
	/**
	 * Get all of the styles that are found within the given range.
	 *
	 * @pre 0 <= start && start < getText.length()
	 * @pre length >= 0
	 */
	public StyleList getStylesInRange(int start, int length)
	{
		StyleList result = new StyleList();
		
		if(length > 0)
		{
			result.add(1, getStyleAtIndex(start));
			for(int i = start + 1; i < start + length; i++)
			{
				if(result.get(result.size() - 1).getStyle().equals(getStyleAtIndex(i)))
				{
					result.get(result.size() - 1).addToLength(1);
				}
				else
				{
					result.add(1, getStyleAtIndex(i));
				}
			}
		}
		
		return result;
	}
	
	public StyleList getStyles()
	{
		return getStylesInRange(0, getText().length());
	}
        //-----------------------------------------------------------------------------
        //Color Style Methods
        //-----------------------------------------------------------------------------
        public boolean isColor(int testIndex)
        {
                return getStyleAtIndex(testIndex).isColor();
        }
        public void setColor(int start, int length, boolean color)
        {
                setRangeStyle(start, length, StyleCreator.getColorStyle(color || length == 0),color);
        }
	
	// ----------------------------------------------------------------------------
	// Bold Style Methods
	// ----------------------------------------------------------------------------
	public boolean isBold(int textIndex)
	{
		return getStyleAtIndex(textIndex).isBold();
	}
	public void setBold(int start, int length, boolean bold)
	{
		setRangeStyle(start, length, StyleCreator.getBoldStyle(bold || length == 0), bold);
	}
	
	// ----------------------------------------------------------------------------
	// Italic Style Methods
	// ----------------------------------------------------------------------------
	public boolean isItalic(int textIndex)
	{
		return getStyleAtIndex(textIndex).isItalic();
	}
	public void setItalic(int start, int length, boolean italic)
	{
		setRangeStyle(start, length, StyleCreator.getItalicStyle(italic || length == 0), italic);
	}
	
	// ----------------------------------------------------------------------------
	// Underline Style Methods
	// ----------------------------------------------------------------------------
	public boolean isUnderline(int textIndex)
	{
		return getStyleAtIndex(textIndex).isUnderline();
	}
	public void setUnderline(int start, int length, boolean underline)
	{
		setRangeStyle(start, length, StyleCreator.getUnderlineStyle(underline || length == 0), underline);
	}
}
