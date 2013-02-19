package model;

public class LengthStyle
{
	private int length;
	private TextEditorStyle style;
	
	public LengthStyle(int lengthValue, TextEditorStyle styleValue)
	{
		length = lengthValue;
		style = styleValue;
	}
	
	public int getLength()
	{
		return length;
	}
	public void addToLength(int value)
	{
		length += value;
	}
	
	public TextEditorStyle getStyle()
	{
		return style;
	}
}
