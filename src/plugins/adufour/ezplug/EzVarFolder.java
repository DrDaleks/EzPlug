package plugins.adufour.ezplug;

import java.io.File;

import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeModel;
import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.adufour.vars.lang.VarFile;

public class EzVarFolder extends EzVar<File>
{
	/**
	 * Constructs a new input variable with given name and default folder dialog path
	 * 
	 * @param varName
	 *            the name of the variable (as it will appear in the interface)
	 * @param path
	 *            the default path to show in the file dialog
	 */
	public EzVarFolder(String varName, String path)
	{
		super(new VarFile(varName, path == null ? null : new File(path)), new FileTypeModel(path, FileMode.FOLDERS, null, false));
	}
	
	/**
	 * Replaces the button text by the given string
	 * 
	 * @param text
	 */
	public void setButtonText(String text)
	{
		((FileChooser) getVarEditor()).setButtonText(text);
	}
}
