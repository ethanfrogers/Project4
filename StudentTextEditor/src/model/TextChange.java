package model;

public class TextChange
{
	private TextChangeType type;
	private int startIndex;
	private int length;
	
	public TextChange(TextChangeType changeType, int changeStartIndex, int changeLength)
	{
		type = changeType;
		startIndex = changeStartIndex;
		length = changeLength;
	}
	
	public TextChangeType getChangeType()
	{
		return type;
	}
	public int getStartIndex()
	{
		return startIndex;
	}
	public int getLength()
	{
		return length;
	}
}