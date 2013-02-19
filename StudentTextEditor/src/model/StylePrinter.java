package model;

public class StylePrinter
{
	public static void printStyles(StyleList styles)
	{
		System.out.println("\t" + stylesToString(styles));
	}
	public static void printStyle(LengthStyle ls)
	{
		System.out.println("\t" + styleToString(ls));
	}
	public static void printStyle(TextEditorStyle tes)
	{
		System.out.println("\t" + styleToString(tes));
	}
	
	public static String stylesToString(StyleList styles)
	{
		String result = "";
		if(styles.size() > 0)
		{
			for(int i = 0; i < styles.size(); i++)
			{
				result += styleToString(styles.get(i)) + "\n";
			}
		}
		else
		{
			result = "(No Styles)";
		}
		return result;
	}
	public static String styleToString(LengthStyle ls)
	{
		String result = "Length: " + ls.getLength() + ", ";
		result += styleToString(ls.getStyle());
		return result;
	}
	public static String styleToString(TextEditorStyle tes)
	{
		String result = "Styles: ";
		if(tes.isBold())
		{
			result += "Bold, ";
		}
		if(tes.isItalic())
		{
			result += "Italic, ";
		}
		if(tes.isUnderline())
		{
			result += "Underline";
		}
		if(tes.isNormal())
		{
			result += "None";
		}
		return result;
	}
}