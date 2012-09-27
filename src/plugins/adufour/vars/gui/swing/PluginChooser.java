package plugins.adufour.vars.gui.swing;

import icy.plugin.PluginDescriptor;
import icy.plugin.PluginLoader;
import icy.plugin.abstract_.Plugin;

import java.awt.Component;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.JXHeader;

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
        JComboBox combo = new JComboBox();

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

        if (combo.getModel().getSize() > 1) combo.setSelectedIndex(0);

        combo.setRenderer(new ListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                if (value == null) return new JLabel("no selection");
                PluginDescriptor desc = (PluginDescriptor) value;
                JXHeader header = new JXHeader(desc.getName(), desc.getDescription());
                return header;
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
