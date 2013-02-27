package model;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextEditorStyle extends SimpleAttributeSet
{
	public TextEditorStyle()
	{
	}
	public TextEditorStyle(AttributeSet toCopy)
	{
		setBold(StyleConstants.isBold(toCopy));
		setItalic(StyleConstants.isItalic(toCopy));
		setUnderline(StyleConstants.isUnderline(toCopy));
	}
        
        public boolean isColor()
        {
                if(StyleConstants.getForeground(this) != null)
                {
                    return true;
                }
                else
                {
                    return false;
                }
        }
	
	public boolean isBold()
	{
		return StyleConstants.isBold(this);
	}
	public boolean isItalic()
	{
		return StyleConstants.isItalic(this);
	}
	public boolean isUnderline()
	{
		return StyleConstants.isUnderline(this);
	}
	public boolean isNormal()
	{
		return isBold() == false &&
				isItalic() == false &&
				isUnderline() == false;
	}
	
	public void setBold(boolean bold)
	{
		StyleConstants.setBold(this, bold);
	}
	public void setItalic(boolean italic)
	{
		StyleConstants.setItalic(this, italic);
	}
	public void setUnderline(boolean underline)
	{
		StyleConstants.setUnderline(this, underline);
	}
	
        public void setColor(boolean color)
        {
                StyleConstants.setForeground(this, Color.green);
        }
        
	public boolean equals(TextEditorStyle rhs)
	{
		return isBold() == rhs.isBold() &&
				isItalic() == rhs.isItalic() &&
				isUnderline() == rhs.isUnderline();
	}
}