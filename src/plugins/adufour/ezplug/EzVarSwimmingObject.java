package plugins.adufour.ezplug;

import icy.main.Icy;
import icy.swimmingPool.SwimmingObject;
import icy.swimmingPool.SwimmingPoolEvent;
import icy.swimmingPool.SwimmingPoolListener;
import icy.system.thread.ThreadUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;

public class EzVarSwimmingObject<T> extends EzVar<SwimmingObject> implements SwimmingPoolListener
{
	private static final long				serialVersionUID	= 1L;
	
	private JComboBox						jComboSequences;
	
	private JComboSequenceBoxListener		jComboSequenceBoxListener;
	
	private JComboBoxModel					jComboSequenceBoxModel;
	
	private JComboSequenceBoxRenderer		jComboSequenceBoxRenderer;
	
	private final ArrayList<SwimmingObject>	objects;
	
	private final String					className;
	
	public EzVarSwimmingObject(String varName, Class<T> objectType)
	{
		super(varName);
		
		className = objectType.getName();
		
		objects = new ArrayList<SwimmingObject>();
		
		ThreadUtil.invoke(new Runnable()
		{
			public void run()
			{
				jComboSequenceBoxListener = new JComboSequenceBoxListener();
				jComboSequenceBoxModel = new JComboBoxModel();
				jComboSequenceBoxRenderer = new JComboSequenceBoxRenderer();
				
				jComboSequences = new JComboBox(jComboSequenceBoxModel);
				
				if (Icy.getMainInterface().getSequences().size() > 0)
					jComboSequences.setSelectedIndex(1);
				
				jComboSequences.setRenderer(jComboSequenceBoxRenderer);
				
				jComboSequences.addActionListener(jComboSequenceBoxListener);
				
				Icy.getMainInterface().getSwimmingPool().addListener(EzVarSwimmingObject.this);
				
				setComponent(jComboSequences);
			}
		}, !SwingUtilities.isEventDispatchThread());
		
	}
	
	/**
	 * The combo box model is slightly different from that of SequenceSelector with the following
	 * modifications: <br>
	 * - the list of pointers to this variable is added to the combo box<br>
	 * - no local reference to the currently selected sequence
	 */
	private final class JComboBoxModel implements ComboBoxModel
	{
		private Object	item;
		
		@Override
		public Object getSelectedItem()
		{
			return item;
		}
		
		@Override
		public void setSelectedItem(Object anItem)
		{
			this.item = anItem;
		}
		
		@Override
		public void addListDataListener(ListDataListener l)
		{
		}
		
		@Override
		public Object getElementAt(int index)
		{
			// first is the "no" selection
			if (index == 0)
				return null;
			
			return objects.get(index - 1);
		}
		
		@Override
		public int getSize()
		{
			// slot 0 is dedicated to "no selection", everything else is shifted up by one increment
			return 1 + objects.size();
		}
		
		@Override
		public void removeListDataListener(ListDataListener l)
		{
		}
		
	}
	
	private final class JComboSequenceBoxRenderer implements ListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if (value == null)
				return new JLabel(" ");
			
			if (value instanceof SwimmingObject)
				return new JLabel(((SwimmingObject) value).getName());
			
			return new JLabel("error"); // should never be displayed
		}
	}
	
	private final class JComboSequenceBoxListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			SwimmingObject newValue = (SwimmingObject) jComboSequenceBoxModel.getSelectedItem();
			
			if (newValue != null)
				jComboSequences.setToolTipText(newValue.getName());
			
			fireVariableChanged(newValue);
		}
	}
	
	@Override
	public SwimmingObject getValue()
	{
		return getValue(true);
	}
	
	/**
	 * Returns the currently selected sequence
	 * 
	 * @param throwExceptionIfNull
	 *            throws a EzException if the currently selected sequence is null
	 * @throws EzException
	 *             if the currently selection is null and throwExceptionIfNull is true.
	 */
	public SwimmingObject getValue(boolean throwExceptionIfNull) throws EzException
	{
		if (jComboSequenceBoxModel.getSelectedItem() == null)
		{
			if (throwExceptionIfNull)
				throw new EzException("Variable \"" + name + "\": No selection", true);
			return null;
		}
		
		return (SwimmingObject) jComboSequenceBoxModel.getSelectedItem();
	}
	
	@Override
	public void setValue(SwimmingObject object)
	{
		jComboSequenceBoxModel.setSelectedItem(object);
	}
	
	@Override
	public void swimmingPoolChangeEvent(SwimmingPoolEvent event)
	{
		SwimmingObject object = event.getResult();
		
		if (object.getObjectClassName() != className)
			return;
		
		switch (event.getType())
		{
			case ELEMENT_ADDED:
				objects.add(object);
			break;
			
			case ELEMENT_REMOVED:
				objects.remove(object);
			break;
		}
	}
	
	// Dispose //
	
	@Override
	public void dispose()
	{
		Icy.getMainInterface().getSwimmingPool().removeListener(this);
		
		jComboSequences.removeActionListener(jComboSequenceBoxListener);
		jComboSequences.setRenderer(null);
		jComboSequenceBoxListener = null;
		jComboSequenceBoxRenderer = null;
		
		objects.clear();
		
		super.dispose();
	}
	

}
