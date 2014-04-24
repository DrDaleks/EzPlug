package plugins.adufour.vars.lang;

import icy.plugin.PluginDescriptor;
import icy.plugin.PluginLoader;
import icy.plugin.abstract_.Plugin;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;

/**
 * Variable that manipulates a plug-in definition
 * 
 * @author Alexandre Dufour
 */
public class VarPlugin<P extends Plugin> extends Var<PluginDescriptor>
{
    public final Class<P> pluginType;
    
    public VarPlugin(String name, Class<P> pluginType)
    {
        super(name, PluginDescriptor.class, null);
        this.pluginType = pluginType;
    }
    
    @Override
    public VarEditor<PluginDescriptor> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createPluginChooser(this);
    }
    
    @SuppressWarnings("unchecked")
    public P newInstance()
    {
        try
        {
            return ((Class<P>) getValue(true).getPluginClass()).newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public String getValueAsString()
    {
        return getValue().getClassName();
    }
    
    @Override
    public PluginDescriptor parse(String text)
    {
        return PluginLoader.getPlugin(text);
    }
}
