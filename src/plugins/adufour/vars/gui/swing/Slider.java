package plugins.adufour.vars.gui.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

public class Slider<N extends Number> extends SwingVarEditor<N>
{
    private static final int MAX_SPINNER_WIDTH = 100;
    
    private ChangeListener   changeListener;
    
    public Slider(Var<N> variable)
    {
        super(variable);
    }
    
    @Override
    public JComponent createEditorComponent()
    {
        VarEditorModel<N> model = variable.getDefaultEditorModel();
        
        if (model == null || !(model instanceof RangeModel))
        {
            throw new UnsupportedOperationException("Incorrect model for variable " + variable.getName() + ": " + model);
        }
        
        final RangeModel<N> constraint = (RangeModel<N>) model;
        
        // the model range must be converted into integer ticks for the slider component
        
        int nbTicks = constraint.getRangeSize();
        int currentTick = constraint.indexOf(constraint.getDefaultValue());
        
        final JSlider jSlider = new JSlider(JSlider.HORIZONTAL, 0, nbTicks, currentTick);
        jSlider.setMinorTickSpacing(1);
        jSlider.setSnapToTicks(true);
        
        HashMap<N, String> labels = constraint.getLabels();
        
        Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
        Font font = new Font("Serif", Font.ITALIC, 11);
        for (N n : labels.keySet())
        {
            JLabel label = new JLabel(labels.get(n));
            label.setFont(font);
            table.put(constraint.indexOf(n), label);
        }
        jSlider.setLabelTable(table);
        jSlider.setPaintLabels(true);
        
        changeListener = new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                if (variable.getReference() == null) variable.setValue(constraint.getValueForIndex(jSlider.getValue()));
            }
        };
        
        // Assign a maximum size to the spinner to avoid huge-ass interfaces
        Dimension dim = jSlider.getPreferredSize();
        dim.setSize(Math.min(dim.width, MAX_SPINNER_WIDTH), dim.height);
        jSlider.setPreferredSize(dim);
        
        return jSlider;
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        N value = variable.getValue();
        if (value == null) value = variable.getDefaultValue();
        
        int tick = ((RangeModel<N>) variable.getDefaultEditorModel()).indexOf(value);
        getEditorComponent().setValue(tick);
    }
    
    @Override
    public JSlider getEditorComponent()
    {
        return (JSlider) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        JSlider jSlider = getEditorComponent();
        jSlider.addChangeListener(changeListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        JSlider jSlider = getEditorComponent();
        jSlider.removeChangeListener(changeListener);
    }
}