package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Class defining a text label for use with EzPlugs.
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzLabel extends EzComponent
{
	private JTextArea			jTextArea;
	
	/**
	 * Creates a new EzButton with given title and action listener (i.e. the method which will be
	 * called when the button is clicked)
	 * 
	 * @param text
	 *            the text to display
	 */
	public EzLabel(String text)
	{
		this(text, Color.black);
	}
	
	/**
	 * Creates a new EzButton with given title and action listener (i.e. the method which will be
	 * called when the button is clicked)
	 * 
	 * @param text
	 *            the text to display
	 * @param textColor
	 *            the default text color
	 */
	public EzLabel(final String text, final Color textColor)
	{
		super("label");
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				jTextArea = new JTextArea(text.isEmpty() ? " " : text);
				jTextArea.setLineWrap(true);
				jTextArea.setWrapStyleWord(true);
				jTextArea.setForeground(textColor);
				jTextArea.setMargin(new Insets(0, 2, 0, 2));
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	/**
	 * Sets the text of this label
	 * 
	 * @param text
	 *            the text to display
	 */
	public void setText(String text)
	{
		jTextArea.setText(text);
	}
	
	/**
	 * Set the color of the text in this label
	 * 
	 * @param textColor
	 *            the new text color
	 */
	public void setColor(Color textColor)
	{
		jTextArea.setForeground(textColor);
	}
	
	@Override
	protected void addTo(Container container)
	{
		GridBagLayout gridbag = (GridBagLayout) container.getLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 5, 2, 5);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		jTextArea.setFocusable(false);
		gridbag.setConstraints(jTextArea, gbc);
		container.add(jTextArea);
	}

	@Override
	public void setToolTipText(String text)
	{
		jTextArea.setToolTipText(text);
	}
	
}
