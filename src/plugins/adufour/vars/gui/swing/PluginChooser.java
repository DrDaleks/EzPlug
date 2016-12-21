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
        JComboBox<PluginDescriptor> combo = new JComboBox<PluginDescriptor>();

        combo.setModel(new ComboBoxModel<PluginDescriptor>()
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
            public PluginDescriptor getElementAt(int index)
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
            public PluginDescriptor getSelectedItem()
            {
                return variable.getValue();
            }
        });

        if (combo.getModel().getSize() > 1) combo.setSelectedIndex(0);

        combo.setRenderer(new ListCellRenderer<PluginDescriptor>()
        {
            @Override
            public Component getListCellRendererComponent(JList<? extends PluginDescriptor> list, PluginDescriptor descriptor, int index, boolean isSelected, boolean cellHasFocus)
            {
                if (descriptor == null) return new JLabel("no selection");
                JLabel header = new JLabel(descriptor.getName());
                header.setToolTipText(descriptor.getDescription());
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
