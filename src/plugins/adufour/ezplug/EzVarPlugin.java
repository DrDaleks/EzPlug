package plugins.adufour.ezplug;

import icy.plugin.PluginDescriptor;
import icy.plugin.abstract_.Plugin;
import plugins.adufour.vars.lang.VarPlugin;

/**
 * New variable displaying a list of plug-ins of a given type
 * 
 * @author Alexandre Dufour
 * @param <P>
 */
public class EzVarPlugin<P extends Plugin> extends EzVar<PluginDescriptor>
{
    public EzVarPlugin(String name, Class<P> pluginType)
    {
        super(new VarPlugin<P>(name, pluginType), null);
    }
    
    @SuppressWarnings("unchecked")
    public P newInstance()
    {
        return ((VarPlugin<P>) variable).newInstance();
    }
}
