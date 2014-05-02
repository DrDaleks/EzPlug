package plugins.adufour.vars.gui.swing;

import icy.plugin.PluginDescriptor;
import icy.plugin.abstract_.Plugin;
import icy.sequence.Sequence;
import icy.swimmingPool.SwimmingObject;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarChannel;
import plugins.adufour.vars.lang.VarGenericArray;
import plugins.adufour.vars.lang.VarMutable;
import plugins.adufour.vars.lang.VarTrigger;

public class SwingFactory extends VarEditorFactory
{
    @Override
    public VarEditor<Integer> createButton(VarTrigger variable)
    {
        return new Button(variable);
    }
    
    @Override
    public VarEditor<Boolean> createCheckBox(Var<Boolean> variable)
    {
        return new CheckBox(variable);
    }
    
    @Override
    public <V> VarEditor<V> createComboBox(Var<V> variable)
    {
        return new ComboBox<V>(variable);
    }
    
    @Override
    public <V> VarEditor<V> createLabel(Var<V> variable)
    {
        return new Label<V>(variable);
    }
    
    @Override
    public VarEditor<Sequence> createSequenceChooser(Var<Sequence> variable)
    {
        return new SequenceChooser(variable);
    }
    
    @Override
    public <N extends Number> VarEditor<N> createSlider(Var<N> variable)
    {
        return new Slider<N>(variable);
    }
    
    @Override
    public <N extends Number> VarEditor<N> createSpinner(Var<N> variable)
    {
        return new Spinner<N>(variable);
    }
    
    @Override
    public VarEditor<SwimmingObject> createSwimmingObjectChooser(Var<SwimmingObject> variable)
    {
        return new SwimmingObjectChooser(variable);
    }
    
    @Override
    public <V> VarEditor<V> createTextArea(Var<V> variable, int rows)
    {
        return new TextArea<V>(variable, rows);
    }
    
    @Override
    public <V> VarEditor<V> createTextField(Var<V> variable)
    {
        return new TextField<V>(variable);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public VarEditor<VarMutable> createTypeChooser(VarMutable variable)
    {
        return new TypeChooser(variable);
    }
    
    @Override
    public VarEditor<PluginDescriptor> createPluginChooser(Var<PluginDescriptor> variable)
    {
        return new PluginChooser<Plugin>(variable);
    }
    
    @Override
    public VarEditor<Integer> createChannelSelector(VarChannel varChannel, Var<Sequence> sequence, boolean allowAllChannels)
    {
        return new ChannelSelector(varChannel, sequence, allowAllChannels);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public VarEditor createMutableVarEditor(VarMutable varMutable)
    {
        return new MutableVarEditor(varMutable);
    }
    
    @Override
    public VarEditor<Sequence> createSequenceViewer(Var<Sequence> variable)
    {
        return new SequenceViewer(variable);
    }
    
    @Override
    public VarEditor<Sequence[]> createSequenceList(VarGenericArray<Sequence[]> varSequenceArray)
    {
        return new SequenceList(varSequenceArray);
    }
}