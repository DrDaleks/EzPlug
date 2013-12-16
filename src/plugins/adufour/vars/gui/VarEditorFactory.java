package plugins.adufour.vars.gui;

import icy.sequence.Sequence;
import icy.swimmingPool.SwimmingObject;
import plugins.adufour.vars.gui.swing.SwingFactory;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarMutable;
import plugins.adufour.vars.lang.VarTrigger;

public abstract class VarEditorFactory
{
    private static VarEditorFactory defaultFactory = new SwingFactory(); // TODO almost there...
    
    public static VarEditorFactory getDefaultFactory()
    {
        return defaultFactory;
    }
    
    public abstract <V> VarEditor<V> createLabel(Var<V> variable);
    
    public abstract VarEditor<Integer> createButton(VarTrigger variable);
    
    public abstract <V> VarEditor<V> createTextField(Var<V> variable);
    
    public abstract <V> VarEditor<V> createComboBox(Var<V> variable);
    
    public abstract <N extends Number> VarEditor<N> createSpinner(Var<N> variable);
    
    public abstract <V> VarEditor<V> createTextArea(Var<V> variable, int rows);
    
    public abstract VarEditor<Boolean> createCheckBox(Var<Boolean> variable);
    
    public abstract VarEditor<Sequence> createSequenceChooser(Var<Sequence> variable);
    
    public abstract VarEditor<SwimmingObject> createSwimmingObjectChooser(Var<SwimmingObject> variable);
    
    public abstract VarEditor<VarMutable> createTypeChooser(VarMutable variable);
    
    public abstract <N extends Number> VarEditor<N> createSlider(Var<N> variable);
}
