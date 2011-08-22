package plugins.adufour.ezplug;

import javax.swing.JFileChooser;

public class EzVarFolder extends EzVarFile
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Creates a new variable that receives a disk folder
	 * 
	 * @param varName
	 *            the variable name
	 * @param path
	 *            the initial path (or null for a default path)
	 */
	public EzVarFolder(String varName, String path)
	{
		super(varName, path);
		
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setAcceptAllFileFilterUsed(false);
		jFileChooser.setFileHidingEnabled(true);
		super.setButtonText("Select folder...");
	}
	
}
