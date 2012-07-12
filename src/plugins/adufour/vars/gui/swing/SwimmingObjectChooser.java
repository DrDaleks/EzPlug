package plugins.adufour.vars.gui.swing;

import icy.main.Icy;
import icy.swimmingPool.SwimmingObject;
import icy.swimmingPool.SwimmingPoolEvent;
import icy.swimmingPool.SwimmingPoolListener;
import icy.system.thread.ThreadUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

import plugins.adufour.vars.lang.Var;

public class SwimmingObjectChooser extends SwingVarEditor<SwimmingObject> implements SwimmingPoolListener
{
	private JComboBoxListener			jComboBoxListener;
	
	private JComboBoxModel				jComboBoxModel;
	
	private JComboBoxRenderer			jComboBoxRenderer;
	
	private ArrayList<SwimmingObject>	validObjects;
	
	/**
	 * The combo box model is slightly different from that of SequenceSelector with the following
	 * modifications: <br>
	 * - the list of pointers to this variable is added to the combo box<br>
	 * - no local reference to the currently selected sequence
	 */
	private final class JComboBoxModel extends DefaultComboBoxModel
	{
		private static final long	serialVersionUID	= 1L;
		
		@Override
		public void addListDataListener(ListDataListener l)
		{
		}
		
		@Override
		public Object getElementAt(int index)
		{
			// first is the "no" selection
			if (index == 0) return null;
			
			return validObjects.get(index - 1);
		}
		
		@Override
		public int getSize()
		{
			// slot 0 is dedicated to "no selection", everything else is shifted up by one increment
			return 1 + validObjects.size();
		}
		
	}
	
	private final class JComboBoxRenderer implements ListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if (value == null) return new JLabel("No selection");
			
			if (value instanceof SwimmingObject)
			{
				SwimmingObject swObj = (SwimmingObject) value;
				String name = swObj.getName();
				return new JLabel(name);
			}
			
			return new JLabel("error"); // should never be displayed
		}
	}
	
	private final class JComboBoxListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			SwimmingObject newValue = (SwimmingObject) jComboBoxModel.getSelectedItem();
			
			if (newValue != null) getEditorComponent().setToolTipText(newValue.getName());
			
			variable.setValue(newValue);
		}
	}
	
	public SwimmingObjectChooser(Var<SwimmingObject> variable)
	{
		super(variable);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		validObjects.clear();
		
		JComboBox jComboBox = getEditorComponent();
		// replace custom instances by new empty ones for garbage collection
		jComboBox.setRenderer(new DefaultListCellRenderer());
		jComboBox.setModel(new DefaultComboBoxModel());
	}
	
	@Override
	public JComponent createEditorComponent()
	{
		validObjects = new ArrayList<SwimmingObject>();
		jComboBoxListener = new JComboBoxListener();
		jComboBoxModel = new JComboBoxModel();
		jComboBoxRenderer = new JComboBoxRenderer();
		
		JComboBox jComboBox = new JComboBox(jComboBoxModel);
		
		for (SwimmingObject swObj : Icy.getMainInterface().getSwimmingPool().getObjects())
			if (variable.getDefaultEditorModel().isValid(swObj))
			{
				validObjects.add(swObj);
			}
		
		if (validObjects.size() > 0) jComboBox.setSelectedIndex(1);
		
		jComboBox.setRenderer(jComboBoxRenderer);
		
//		jComboBox.addActionListener(jComboBoxListener);
		
//		Icy.getMainInterface().getSwimmingPool().addListener(SwimmingObjectChooser.this);
		
		return jComboBox;
	}
	
	@Override
	protected void updateInterfaceValue()
	{
		jComboBoxModel.setSelectedItem(variable.getValue());
	}
	
	@Override
	public void swimmingPoolChangeEvent(SwimmingPoolEvent event)
	{
		SwimmingObject object = event.getResult();
		
		if (!variable.getDefaultEditorModel().isValid(object)) return;
		
		switch (event.getType())
		{
			case ELEMENT_ADDED:
				validObjects.add(object);
			break;
			
			case ELEMENT_REMOVED:
				validObjects.remove(object);
			break;
		}
		
		ThreadUtil.invokeLater(new Runnable()
		{
			public void run()
			{
			    getEditorComponent().repaint();
			    getEditorComponent().updateUI();
			}
		});
	}

	@Override
	public JComboBox getEditorComponent()
	{
		return (JComboBox) super.getEditorComponent();
	}

	@Override
	protected void activateListeners()
	{
	    getEditorComponent().addActionListener(jComboBoxListener);
		Icy.getMainInterface().getSwimmingPool().addListener(SwimmingObjectChooser.this);
	}

	@Override
	protected void deactivateListeners()
	{
	    getEditorComponent().removeActionListener(jComboBoxListener);
		Icy.getMainInterface().getSwimmingPool().removeListener(SwimmingObjectChooser.this);
	}
	
}
