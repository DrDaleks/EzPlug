package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * Class defining a button for use with EzPlugs. An EzButton adopts the same layout as other
 * EzComponents, and fires an action listener whenever it is clicked from the interface.
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzButton extends EzComponent
{
	private JButton	jButton;
	
	/**
	 * Creates a new EzButton with given title and action listener (i.e. the method which will be
	 * called when the button is clicked)
	 * 
	 * @param title
	 *            the button title
	 * @param listener
	 *            the listener which will be called when the button is clicked
	 */
	public EzButton(final String title, final ActionListener listener)
	{
		super(title);
		
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				jButton = new JButton(title);
				jButton.addActionListener(listener);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	@Override
	public void addTo(Container container)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 5, 2, 5);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		
		container.add(jButton, gbc);
	}
	
	@Override
	public void setToolTipText(final String text)
	{
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				jButton.setToolTipText(text);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	public void setText(final String text)
	{
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				jButton.setText(text);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	@Override
	public void dispose()
	{
		jButton = null;
		super.dispose();
	}

    public void setEnabled(final boolean enabled)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jButton.setEnabled(enabled);
            }
        });
    }
}
