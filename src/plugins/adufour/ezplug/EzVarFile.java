package plugins.adufour.ezplug;

import java.io.File;

import plugins.adufour.vars.gui.FileChooser;
import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeModel;
import plugins.adufour.vars.lang.VarFile;

/**
 * Class defining a variable of type String, embarking a button triggering a file dialog as graphical component
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarFile extends EzVar<File>
{
    /**
     * Constructs a new input variable with given name and default file dialog path
     * 
     * @param varName
     *            the name of the variable (as it will appear in the interface)
     * @param path
     *            the default path to show in the file dialog
     */
    public EzVarFile(String varName, String path)
    {
        super(new VarFile(varName, null), new FileTypeModel(path, FileMode.FILES, null, false));
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
