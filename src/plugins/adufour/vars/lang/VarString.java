package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.TextField;

public class VarString extends Var<String>
{
	public VarString(String name, String defaultValue)
	{
		super(name, String.class, defaultValue);
	}
	
	@Override
	public String parse(String s)
	{
		return s;
	}
	
	@Override
	public VarEditor<String> createVarEditor()
	{
		if (getDefaultEditorModel() == null) return new TextField<String>(this);
		
		return super.createVarEditor();
	}
}
