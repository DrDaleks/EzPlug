package plugins.adufour.vars.gui.swing;

import icy.plugin.PluginDescriptor;
import icy.plugin.PluginLoader;
import icy.plugin.abstract_.Plugin;

import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.JXComboBox;

import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarPlugin;

public class PluginChooser<P extends Plugin> extends SwingVarEditor<PluginDescriptor>
{
    public PluginChooser(Var<PluginDescriptor> variable)
    {
        super(variable);
    }
    
    @Override
    protected JComponent createEditorComponent()
    {
        JXComboBox combo = new JXComboBox();
        
        combo.setModel(new ComboBoxModel()
        {
            @Override
            public void removeListDataListener(ListDataListener l)
            {
            }
            
            @Override
            public int getSize()
            {
                @SuppressWarnings("unchecked")
                Class<P> pluginType = ((VarPlugin<P>) variable).pluginType;
                return PluginLoader.getPlugins(pluginType).size();
            }
            
            @Override
            public Object getElementAt(int index)
            {
                @SuppressWarnings("unchecked")
                Class<P> pluginType = ((VarPlugin<P>) variable).pluginType;
                return PluginLoader.getPlugins(pluginType).get(index);
            }
            
            @Override
            public void addListDataListener(ListDataListener l)
            {
            }
            
            @Override
            public void setSelectedItem(Object anItem)
            {
                variable.setValue((PluginDescriptor) anItem);
            }
            
            @Override
            public Object getSelectedItem()
            {
                return variable.getValue();
            }
        });
        
        return combo;
    }
    
    @Override
    protected void activateListeners()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void deactivateListeners()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        // TODO Auto-generated method stub
        
    }
    
}
