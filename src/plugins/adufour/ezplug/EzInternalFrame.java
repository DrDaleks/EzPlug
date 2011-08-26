package plugins.adufour.ezplug;

import icy.gui.frame.IcyInternalFrame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.pushingpixels.substance.api.skin.SkinChangeListener;
import org.pushingpixels.substance.internal.ui.SubstanceDesktopIconUI;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;

public class EzInternalFrame extends IcyInternalFrame implements SkinChangeListener
{
	private static final long	serialVersionUID	= 1L;
	
	EzInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
	{
		super(title, resizable, closable, maximizable, iconifiable);
		setUI(new EzInternalFrameUI());
		setOpaque(false);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// look and feel change listener
		icy.gui.util.LookAndFeelUtil.addSkinChangeListener(EzInternalFrame.this);
	}
	
//	public void dispose()
//	{
//		setVisible(false);
//		
//		super.dispose();
//		
//		if (ezPlug == null)
//			return;
//		
//		ezPlug.cleanFromUI();
//		
//		if (executionThread != null && executionThread.isAlive())
//		{
//			// stop the execution if it was still running
//			if (ezPlug instanceof EzStoppable)
//				((EzStoppable) ezPlug).stopExecution();
//			else
//			{
//				executionThread.interrupt();
//				System.err.println("Plug " + ezPlug.getName() + " was still running and has been interrupted");
//			}
//		}
//		
//		// dispose all components
//		
//		for (Component component : components)
//			if (component instanceof EzComponent)
//				((EzComponent) component).dispose(); // FIXME
//				
//		components.clear();
//		
//		jPanelParameters.removeAll();
//		repack(false);
//		
//		// remove all listeners
//		
//		jButtonRun.removeActionListener(this);
//		if (jButtonStop != null)
//			jButtonStop.removeActionListener(this);
//		jButtonLoadParameters.removeActionListener(this);
//		jButtonSaveParameters.removeActionListener(this);
//		
//		icy.gui.util.LookAndFeelUtil.removeSkinChangeListener(this);
//		
//		ezPlug = null;
//	}
		
	/**
	 * Custom title pane with elegant logo and title
	 * 
	 * @author Alexandre Dufour
	 * 
	 */
	public class EzTitlePane extends SubstanceInternalFrameTitlePane
	{
		private static final long	serialVersionUID	= 1L;
		
		public EzTitlePane()
		{
			super(EzInternalFrame.this);
			
			setFont(getFont().deriveFont(Font.BOLD + Font.ITALIC, EzGUI.FONT_SIZE));
			
			FontMetrics m = getFontMetrics(getFont());
			
			int titleWidth = m.stringWidth(EzInternalFrame.this.getTitle());
			
			setPreferredSize(new Dimension(titleWidth + 100, EzGUI.LOGO_HEIGHT));
		}
		
		@Override
		protected LayoutManager createLayout()
		{
			return new EzTitlePaneLayout();
		}
		
		@Override
		public void paint(Graphics g)
		{
			EzGUI.paintTitlePane((Graphics2D) g, getWidth(), getHeight(), EzInternalFrame.this.getTitle());
		}
		
		/**
		 * Layout manager for this title pane. Patched version of SubstanceTitlePaneLayout to adjust
		 * the buttons position (they stick tighter in the upper right-hand corner)
		 * 
		 * @author Kirill Grouchnikov
		 * @author Alexandre Dufour
		 */
		protected class EzTitlePaneLayout extends SubstanceInternalFrameTitlePane.SubstanceTitlePaneLayout
		{
			@Override
			public void layoutContainer(Container c)
			{
				boolean leftToRight = getComponentOrientation().isLeftToRight();
				
				int w = getWidth();
				int x = leftToRight ? w : 0;
				int y = 2;
				int spacing;
				
				// assumes all buttons have the same dimensions
				// these dimensions include the borders
				int buttonHeight = closeButton.getIcon().getIconHeight();
				int buttonWidth = closeButton.getIcon().getIconWidth();
				
				// old version (patched by Alexandre Dufour)
				// y = (getHeight() - buttonHeight) / 2;
				y = 4;
				
				if (isClosable())
				{
					spacing = 4;
					x += leftToRight ? -spacing - buttonWidth : spacing;
					closeButton.setBounds(x, y, buttonWidth, buttonHeight);
					if (!leftToRight)
						x += buttonWidth;
				}
				
				if (isMaximizable())
				{
					spacing = isClosable() ? 2 : 4;
					x += leftToRight ? -spacing - buttonWidth : spacing;
					maxButton.setBounds(x, y, buttonWidth, buttonHeight);
					if (!leftToRight)
						x += buttonWidth;
				}
				
				if (isIconifiable())
				{
					spacing = isMaximizable() ? 2 : (isClosable() ? 2 : 4);
					x += leftToRight ? -spacing - buttonWidth : spacing;
					iconButton.setBounds(x, y, buttonWidth, buttonHeight);
					if (!leftToRight)
						x += buttonWidth;
				}
			}
		}
		
	}
	
	/**
	 * This class is a fork of SubstanceInternalFrameUI because the "titlePane" field of the
	 * original class has no set method and is declared private, yielding a NullPointerException
	 * when closing the window (due to a confusion between SubstanceInternalFrameUI.titlePane and
	 * BasicInternalFrameUI.northPane)
	 * 
	 * @author Alexandre Dufour
	 * 
	 */
	public class EzInternalFrameUI extends BasicInternalFrameUI
	{
		private EzTitlePane						titlePane;
		
		private final PropertyChangeListener	substancePropertyListener;
		
		public EzInternalFrameUI()
		{
			super(EzInternalFrame.this);
			
			substancePropertyListener = new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent evt)
				{
					if (JInternalFrame.IS_CLOSED_PROPERTY.equals(evt.getPropertyName()))
					{
						titlePane.uninstall();
						if (frame != null)
						{
							JDesktopIcon jdi = frame.getDesktopIcon();
							SubstanceDesktopIconUI ui = (SubstanceDesktopIconUI) jdi.getUI();
							ui.uninstallUI(jdi);
						}
					}
					
					if ("background".equals(evt.getPropertyName()))
					{
						Color newBackgr = (Color) evt.getNewValue();
						if (!(newBackgr instanceof UIResource))
						{
							titlePane.setBackground(newBackgr);
							frame.getDesktopIcon().setBackground(newBackgr);
						}
					}
				}
			};
		}
		
		@Override
		protected JComponent createNorthPane(JInternalFrame internalFrame)
		{
			this.titlePane = new EzTitlePane();
			this.titlePane.setToolTipText(null);
			return titlePane;
		}
		
		protected void installListeners()
		{
			super.installListeners();
			this.frame.addPropertyChangeListener(substancePropertyListener);
		}
		
		protected void uninstallComponents()
		{
			this.titlePane.uninstall();
			super.uninstallComponents();
		}
		
		@Override
		protected void uninstallListeners()
		{
			super.uninstallListeners();
			this.frame.removePropertyChangeListener(substancePropertyListener);
		}
	}
	
	@Override
	public void skinChanged()
	{
		setUI(new EzInternalFrameUI());
	}
}
