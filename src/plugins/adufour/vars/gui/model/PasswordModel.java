package plugins.adufour.vars.gui.model;

public class PasswordModel implements VarEditorModel<String>
{
    @Override
    public boolean isValid(String value)
    {
        return true;
    }
    
    @Override
    public String getDefaultValue()
    {
        return "";
    }
}
