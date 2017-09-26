package plugins.adufour.vars.gui.swing;

import icy.system.thread.ThreadUtil;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import plugins.adufour.vars.lang.VarMutable;
import plugins.adufour.vars.util.TypeChangeListener;
import plugins.adufour.vars.util.TypeUtil;

@SuppressWarnings("rawtypes")
public class TypeChooser extends ComboBox implements TypeChangeListener
{
    @SuppressWarnings("unchecked")
    public TypeChooser(VarMutable variable)
    {
        super(variable);
    }
    
    @Override
    protected void activateListeners()
    {
        super.activateListeners();
        ((VarMutable)variable).addTypeChangeListener(this);
    }
    
    @Override
    protected void deactivateListeners()
    {
        super.deactivateListeners();
        ((VarMutable)variable).removeTypeChangeListener(this);
    }
    
    @Override
    protected ListCellRenderer createRenderer()
    {
        return new ListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                if (value == null) return new JLabel("Anything");
                
                return new JLabel(TypeUtil.toFriendlyString((Class<?>) value));
            }
        };
    }
    
    @Override
    protected void updateVariableValue()
    {
        Class<?> item = (Class<?>) getEditorComponent().getSelectedItem();
        ((VarMutable) variable).setType(item);
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        getEditorComponent().setSelectedItem(variable.getType());
    }

    @Override
    public void typeChanged(Object source, Class<?> oldType, Class<?> newType)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                updateInterfaceValue();
            }
        });
    }
}
