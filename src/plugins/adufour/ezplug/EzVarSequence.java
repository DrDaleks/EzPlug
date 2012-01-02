package plugins.adufour.ezplug;

import icy.file.FileUtil;
import icy.gui.main.MainEvent;
import icy.gui.main.MainListener;
import icy.gui.viewer.Viewer;
import icy.main.Icy;
import icy.sequence.Sequence;
import icy.system.thread.ThreadUtil;
import icy.util.StringUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;

/**
 * Class defining a sequence type variable which displays as a combo box where the user may choose
 * among all open sequences
 * 
 * @author Alexandre Dufour
 */
public class EzVarSequence extends EzVar<Sequence> implements MainListener
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * The combo box model is slightly different from that of SequenceSelector with the following
	 * modifications: <br>
	 * - the list of pointers to this variable is added to the combo box<br>
	 * - no local reference to the currently selected sequence
	 */
	private final class JComboSequenceBoxModel implements ComboBoxModel
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
			// first is the "no sequence" selection
			if (index == 0)
				return null;
			
			return Icy.getMainInterface().getSequences().get(index - 1);
		}
		
		@Override
		public int getSize()
		{
			// slot 0 is dedicated to "no sequence" all sequence are shifted up by one increment
			return 1 + Icy.getMainInterface().getSequences().size();
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
				return new JLabel("No sequence");
			
			if (value instanceof Sequence)
			{
				String s = FileUtil.getFileName(((Sequence) value).getName());
				JLabel label = new JLabel(StringUtil.limit(s, 24));
				return label;
			}
			
			return new JLabel(""); // should never be displayed
		}
	}
	
	private final class JComboSequenceBoxListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Sequence newValue = (Sequence) jComboSequenceBoxModel.getSelectedItem();
			
			if (newValue != null)
				jComboSequences.setToolTipText(newValue.getName());
			
			fireVariableChanged(newValue);
		}
	}
	
	private JComboBox					jComboSequences;
	
	private JComboSequenceBoxListener	jComboSequenceBoxListener;
	
	private JComboSequenceBoxModel		jComboSequenceBoxModel;
	
	private JComboSequenceBoxRenderer	jComboSequenceBoxRenderer;
	
	/**
	 * Constructs a new variable that handles image data. <br>
	 * 
	 * @param varName
	 *            the parameter name
	 */
	public EzVarSequence(String varName)
	{
		super(varName);
		
		ThreadUtil.invoke(new Runnable()
		{
			public void run()
			{
				jComboSequenceBoxListener = new JComboSequenceBoxListener();
				jComboSequenceBoxModel = new JComboSequenceBoxModel();
				jComboSequenceBoxRenderer = new JComboSequenceBoxRenderer();
				
				jComboSequences = new JComboBox(jComboSequenceBoxModel);
				
				if (Icy.getMainInterface().getSequences().size() > 0)
					jComboSequences.setSelectedIndex(1);
				
				jComboSequences.setRenderer(jComboSequenceBoxRenderer);
				
				jComboSequences.addActionListener(jComboSequenceBoxListener);
				
				Icy.getMainInterface().addListener(EzVarSequence.this);
				
				setComponent(jComboSequences);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	@Override
	public Sequence getValue()
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
	public Sequence getValue(boolean throwExceptionIfNull) throws EzException
	{
		if (jComboSequenceBoxModel.getSelectedItem() == null)
		{
			if (throwExceptionIfNull)
				throw new EzException("Variable \"" + name + "\": No sequence selected", true);
			return null;
		}
		
		return (Sequence) jComboSequenceBoxModel.getSelectedItem();
	}
	
	@Override
	public void setValue(Sequence sequence)
	{
		jComboSequenceBoxModel.setSelectedItem(sequence);
	}
	
	// MainListener //
	
	@Override
	public void painterAdded(MainEvent event)
	{
	}
	
	@Override
	public void painterRemoved(MainEvent event)
	{
	}
	
	@Override
	public void roiAdded(MainEvent event)
	{
	}
	
	@Override
	public void roiRemoved(MainEvent event)
	{
	}
	
	@Override
	public void sequenceClosed(MainEvent event)
	{
	}
	
	@Override
	public void sequenceFocused(MainEvent event)
	{
	}
	
	@Override
	public void sequenceOpened(MainEvent event)
	{
	}
	
	@Override
	public void viewerClosed(MainEvent event)
	{
	}
	
	@Override
	public void viewerFocused(final MainEvent event)
	{
		ThreadUtil.invokeLater(new Runnable()
		{
			public void run()
			{
				final Viewer viewer = (Viewer) event.getSource();
				if (viewer == null)
				{
					Object o = jComboSequences.getSelectedItem();
					
					// the sequence was just closed
					// if it was the selected one, select the "no sequence" option
					
					if (o instanceof Sequence && !Icy.getMainInterface().getSequences().contains(o))
					{
						jComboSequences.setSelectedIndex(0);
						jComboSequences.setToolTipText(null);
					}
				}
				else
				{
					// the focus has changed (or a new sequence was just opened)
					// if nothing was selected, pick the newly focused sequence
					
					if (jComboSequences.getSelectedIndex() <= 0)
					{
						jComboSequences.setSelectedItem(viewer.getSequence());
						jComboSequences.setToolTipText(viewer.getSequence().getName());
					}
				}
				jComboSequences.repaint();
				jComboSequences.updateUI();
				// note: in case a VarSequence was declared but not registered, its owner is null
				if (getUI() != null) getUI().repack(false);
			}
		});
	}
	
	@Override
	public void viewerOpened(MainEvent event)
	{
	}
	
	// Dispose //
	
	@Override
	public void dispose()
	{
		Icy.getMainInterface().removeListener(this);
		
		jComboSequences.removeActionListener(jComboSequenceBoxListener);
		jComboSequences.setRenderer(null);
		jComboSequenceBoxListener = null;
		jComboSequenceBoxRenderer = null;
		
		super.dispose();
	}
	
	@Override
	public void pluginClosed(MainEvent arg0)
	{
	}
	
	@Override
	public void pluginOpened(MainEvent arg0)
	{
	}
	
}
