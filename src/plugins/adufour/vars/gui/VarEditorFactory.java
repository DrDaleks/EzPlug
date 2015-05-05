package plugins.adufour.vars.gui;

import icy.plugin.PluginDescriptor;
import icy.sequence.Sequence;
import icy.swimmingPool.SwimmingObject;

import java.awt.Color;

import plugins.adufour.vars.gui.swing.SwingFactory;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarChannel;
import plugins.adufour.vars.lang.VarGenericArray;
import plugins.adufour.vars.lang.VarMutable;
import plugins.adufour.vars.lang.VarTrigger;

public abstract class VarEditorFactory
{
    static
    {
        // FIXME put this in a place where this can be changed
        setDefaultFactory(VarEditorFactoryType.SWING);
    }
    
    public enum VarEditorFactoryType
    {
        /**
         * Graphical components are built using the AWT/SWING toolkit
         */
        SWING
    }
    
    private static VarEditorFactory defaultFactory;
    
    /**
     * @return The default factory used to create graphical components. This method may return
     *         <code>null</code> if no factory has been selected (see the
     *         {@link #setDefaultFactory(VarEditorFactoryType)}) method to select one
     */
    public static VarEditorFactory getDefaultFactory()
    {
        return defaultFactory;
    }
    
    public static VarEditorFactory setDefaultFactory(VarEditorFactoryType factoryType)
    {
        switch (factoryType)
        {
        case SWING:
            return defaultFactory = new SwingFactory();
            
        default:
            throw new IllegalArgumentException("Cannot load the \"" + factoryType + "\" factory");
        }
    }
    
    public abstract VarEditor<Integer> createButton(VarTrigger variable);
    
    public abstract VarEditor<Integer> createChannelSelector(VarChannel varChannel, Var<Sequence> sequence, boolean allowAllChannels);
    
    public abstract VarEditor<Boolean> createCheckBox(Var<Boolean> variable);
    
    public abstract VarEditor<Color> createColorChooser(Var<Color> variable);
    
    public abstract <V> VarEditor<V> createComboBox(Var<V> variable);
    
    public abstract <V> VarEditor<V> createLabel(Var<V> variable);
    
    @SuppressWarnings("rawtypes")
    public abstract VarEditor createMutableVarEditor(VarMutable varMutable);
    
    public abstract VarEditor<PluginDescriptor> createPluginChooser(Var<PluginDescriptor> variable);
    
    public abstract VarEditor<Sequence> createSequenceChooser(Var<Sequence> variable);
    
    public abstract VarEditor<Sequence[]> createSequenceList(VarGenericArray<Sequence[]> varSequenceArray);
    
    public abstract VarEditor<Sequence> createSequenceViewer(Var<Sequence> variable);
    
    public abstract <N extends Number> VarEditor<N> createSlider(Var<N> variable);
    
    public abstract VarEditor<SwimmingObject> createSwimmingObjectChooser(Var<SwimmingObject> variable);
    
    public abstract <N extends Number> VarEditor<N> createSpinner(Var<N> variable);
    
    public abstract <V> VarEditor<V> createTextArea(Var<V> variable, int rows);
    
    public abstract <V> VarEditor<V> createTextField(Var<V> variable);
    
    public abstract VarEditor<String> createPasswordField(Var<String> variable);
    
    public abstract VarEditor<VarMutable> createTypeChooser(VarMutable variable);
}
