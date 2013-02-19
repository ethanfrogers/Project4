package model;

import java.util.LinkedList;

public class StyleList extends LinkedList<LengthStyle>
{
	public StyleList()
	{
	}
	
	public void add(int length, TextEditorStyle style)
	{
		add(new LengthStyle(length, style));
	}
	
	public void add(int index, int length, TextEditorStyle style)
	{
		add(index, new LengthStyle(length, style));
	}
	
	public void addAll(StyleList styles)
	{
		addAll(this.size(), styles);
	}
	
	public void addAll(int index, StyleList styles)
	{
		for(int i = 0; i < styles.size(); i++)
		{
			add(index + i, styles.get(i).getLength(), new TextEditorStyle(styles.get(i).getStyle()));
		}
	}
	
	public int totalStyleLength()
	{
		int result = 0;
		for(int i = 0; i < this.size(); i++)
		{
			result += this.get(i).getLength();
		}
		return result;
	}
	
	
	public void setBold(int start, int length, boolean boldOn)
	{
		addStyleToRange(start, length, StyleCreator.getBoldStyle(boldOn));
	}
	public boolean isBold(int index)
	{
		return doesIndexContainStyle(index, StyleCreator.getBoldStyle());
	}
	public boolean isRangeBold(int start, int length)
	{
		return doesRangeContainStyle(start, length, StyleCreator.getBoldStyle());
	}
	
	public void setItalic(int start, int length, boolean italicOn)
	{
		addStyleToRange(start, length, StyleCreator.getItalicStyle(italicOn));
	}
	public boolean isItalic(int index)
	{
		return doesIndexContainStyle(index, StyleCreator.getItalicStyle());
	}
	public boolean isRangeItalic(int start, int length)
	{
		return doesRangeContainStyle(start, length, StyleCreator.getItalicStyle());
	}
	
	public void setUnderline(int start, int length, boolean underlineOn)
	{
		addStyleToRange(start, length, StyleCreator.getUnderlineStyle(underlineOn));
	}
	public boolean isUnderline(int index)
	{
		return doesIndexContainStyle(index, StyleCreator.getUnderlineStyle());
	}
	public boolean isRangeUnderline(int start, int length)
	{
		return doesRangeContainStyle(start, length, StyleCreator.getUnderlineStyle());
	}
	
	/**
	 * Set the text that starts at the given index and goes for the given length to contain the given
	 * style.
	 *
	 * @pre 0 <= start && start < length()
	 * @pre length > 0
	 * @pre start + length <= length()
	 *
	 * @post doesRangeContainStyle(start, length, style) == true
	 */
	private void addStyleToRange(int start, int length, TextEditorStyle style)
	{
		if(length > 0)
		{
			splitStyleAtTextIndex(start);
			
			int styleIndex = getStyleIndexFromTextIndex(start);
			if(isLastIndexInAStyle(start + length - 1) == false)
			{
				splitStyleAtTextIndex(start + length);
			}
			
			int cur = start;
			while(cur < start + length)
			{
				styleIndex = getStyleIndexFromTextIndex(cur);
				this.get(styleIndex).getStyle().addAttributes(style);
				cur += this.get(styleIndex).getLength();
				
				if(canMergeFromLeft(styleIndex))
				{
					mergeFromLeft(styleIndex);
					styleIndex--;
				}
			}
			
			if(canMergeFromRight(styleIndex))
			{
				mergeFromRight(styleIndex);
			}
		}
	}
	private boolean doesIndexContainStyle(int index, TextEditorStyle style)
	{
		int styleIndex = getStyleIndexFromTextIndex(index);
		return this.get(styleIndex).getStyle().containsAttributes(style);
	}
	private boolean doesRangeContainStyle(int start, int length, TextEditorStyle style)
	{
		boolean result = true;
		for(int i = start; i < start + length; i++)
		{
			if(doesIndexContainStyle(i, style) == false)
			{
				result = false;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Insert the given styles into this model's style list. The style list given must
	 * be non-mergable within itself.
	 */
	public void insertStylesToText(int textIndex, StyleList textStyles)
	{
		if(this.size() == 0)
		{
			this.addAll(textStyles);
		}
		else
		{
			int styleIndex = this.size();
			if(textIndex < totalStyleLength())
			{
				splitStyleAtTextIndex(textIndex);
				styleIndex = getStyleIndexFromTextIndex(textIndex);
			}
			
			int lastNewStyle = styleIndex + textStyles.size() - 1;
			
			this.addAll(styleIndex, textStyles);
			
			if(canMergeFromLeft(styleIndex))
			{
				mergeFromLeft(styleIndex);
				lastNewStyle--;
			}
			if(canMergeFromRight(lastNewStyle))
			{
				mergeFromRight(lastNewStyle);
			}
		}
	}
	
	private boolean canMergeFromLeft(int mergeIntoIndex)
	{
		return mergeIntoIndex > 0 &&
				canMergeStyles(mergeIntoIndex, -1);
	}
	private boolean canMergeFromRight(int mergeIntoIndex)
	{
		return mergeIntoIndex < this.size() - 1 &&
				canMergeStyles(mergeIntoIndex, 1);
	}
	private boolean canMergeStyles(int mergeIntoIndex, int distance)
	{
		return this.get(mergeIntoIndex).getStyle().equals(this.get(mergeIntoIndex + distance).getStyle());
	}
	
	private void mergeFromLeft(int mergeIntoIndex)
	{
		mergeStyles(mergeIntoIndex, -1);
	}
	private void mergeFromRight(int mergeIntoIndex)
	{
		mergeStyles(mergeIntoIndex, 1);
	}
	private void mergeStyles(int mergeIntoIndex, int distance)
	{
		this.get(mergeIntoIndex).addToLength(this.get(mergeIntoIndex + distance).getLength());
		this.remove(mergeIntoIndex + distance);
	}
	
	/**
	 * Split the style that contains the given index such that the index given will be the start of
	 * its own style.
	 *
	 * @pre 0 <= index && index < docString.length
	 *
	 * @post none
	 */
	public void splitStyleAtTextIndex(int index)
	{
		int styleIndex = getStyleIndexFromTextIndex(index);
		int styleTextIndex = getTextIndexFromStyleIndex(styleIndex);
		
		if(styleTextIndex != index)
		{
			int lengthBeforeIndex = index - styleTextIndex;
			
			TextEditorStyle newStyle = new TextEditorStyle(this.get(styleIndex).getStyle());
			int newLength = this.get(styleIndex).getLength() - lengthBeforeIndex;
			
			this.add(styleIndex + 1, newLength, newStyle);
			this.get(styleIndex).addToLength(-1 * newLength);
		}
	}
	
	public void removeStylesFromText(int index, int length)
	{
		for(int i = index + length - 1; i >= index; i--)
		{
			int styleIndex = getStyleIndexFromTextIndex(i);
			if(this.get(styleIndex).getLength() <= 1)
			{
				this.remove(styleIndex);
			}
			else
			{
				this.get(styleIndex).addToLength(-1);
			}
		}
		
		if(0 < index && index < totalStyleLength())
		{
			int previousStyle = getStyleIndexFromTextIndex(index - 1);
			int currentStyle = getStyleIndexFromTextIndex(index);
			if(previousStyle != currentStyle && canMergeStyles(previousStyle, currentStyle))
			{
				mergeStyles(previousStyle, currentStyle);
			}
		}
	}
	
	/**
	 * Get the style that is used at the given index.
	 *
	 * @pre 0 <= index && index < length()
	 *
	 * @post retval != null
	 */
	public TextEditorStyle getStyleAt(int index)
	{
		return new TextEditorStyle(this.get(getStyleIndexFromTextIndex(index)).getStyle());
	}
	
	public StyleList getStylesInRange(int startIndex, int length)
	{
		StyleList result = new StyleList();
		
		int styleIndex = getStyleIndexFromTextIndex(startIndex);
		int styleTextStart = getTextIndexFromStyleIndex(styleIndex);
		
		int styleLength = Math.min(this.get(styleIndex).getLength() - (startIndex - styleTextStart), length);
		
		result.add(styleLength, new TextEditorStyle(this.get(styleIndex).getStyle()));
		length -= styleLength;
		styleIndex++;
		
		while(length > 0 && styleIndex < this.size())
		{
			styleLength = Math.min(this.get(styleIndex).getLength(), length);
			result.add(styleLength, new TextEditorStyle(this.get(styleIndex).getStyle()));
			length -= styleLength;
			styleIndex++;
		}
		
		return result;
	}
	
	/**
	 * Get the style index that represents the formatting for the text at the given index.
	 *
	 * @pre 0 <= textIndex && textIndex < length()
	 *
	 * @post none
	 */
	public int getStyleIndexFromTextIndex(int textIndex)
	{
		int result = 0;
		
		for(int i = 0; i < this.size(); i++)
		{
			if(textIndex < this.get(i).getLength())
			{
				result = i;
				break;
			}
			else
			{
				textIndex -= this.get(i).getLength();
			}
		}
		
		return result;
	}
	
	public int getTextIndexFromStyleIndex(int styleIndex)
	{
		int result = 0;
		for(int i = 0; i < styleIndex; i++)
		{
			result += this.get(i).getLength();
		}
		return result;
	}
	
	public boolean isLastIndexInAStyle(int textIndex)
	{
		boolean result = false;
		
		if(totalStyleLength() - 1 == textIndex ||
			getStyleIndexFromTextIndex(textIndex) + 1 == getStyleIndexFromTextIndex(textIndex + 1))
		{
			result = true;
		}
		
		return result;
	}
}