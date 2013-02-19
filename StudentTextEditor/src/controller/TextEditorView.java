package controller;

public interface TextEditorView extends TextEditorDocument
{
	public void setUndoEnabled(boolean enabled);
	public void setRedoEnabled(boolean enabled);
	
	public String getSaveFileName();
	public String getOpenFileName();
}
