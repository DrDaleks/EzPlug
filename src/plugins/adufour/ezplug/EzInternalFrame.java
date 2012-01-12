package plugins.adufour.ezplug;

import icy.gui.frame.IcyInternalFrame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.lang.reflect.Field;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import org.pushingpixels.substance.internal.ui.SubstanceInternalFrameUI;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;

public class EzInternalFrame extends IcyInternalFrame
{
	private static final long	serialVersionUID	= 1L;
	
	EzInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
	{
		super(title, resizable, closable, maximizable, iconifiable);
		
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		updateUI();
	}
	
	@Override
	public void doLayout()
	{
		if (isVisible())
			super.doLayout();
	}
	
	@Override
	public void dispose()
	{
		// FIXME Memory leak: "this" is not destroyed properly.
		// the if test is needed to avoid the following bug:
		// 1) externalize the frame
		// 2) close the external frame => this dispose is called once
		// 3) close ICY => this dispose is called again => not normal !
		// => the UI doens't know about the frame anymore
		if (isVisible())
		{
			getUI().uninstallUI(this);
		}
		super.dispose();
	}
	
	private final class EzInternalFrameUI extends SubstanceInternalFrameUI
	{
		public EzInternalFrameUI()
		{
			super(EzInternalFrame.this);
		}
		
		@Override
		protected JComponent createNorthPane(JInternalFrame w)
		{
			// Access the private field "titlePane" via reflection
			
			try
			{
				Field titlePane = null;
				titlePane = SubstanceInternalFrameUI.class.getDeclaredField("titlePane");
				titlePane.setAccessible(true);
				titlePane.set(this, new EzInternalFrameTitlePane());
				return (SubstanceInternalFrameTitlePane) titlePane.get(this);
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
	/**
	 * Custom title pane with elegant logo and title
	 * 
	 * @author Alexandre Dufour
	 * 
	 */
	private final class EzInternalFrameTitlePane extends SubstanceInternalFrameTitlePane
	{
		private static final long	serialVersionUID	= 1L;
		
		final Icon					icon				= frame.getFrameIcon();
		final Point					iconLocation		= new Point(5, (EzGUI.LOGO_HEIGHT / 2) - (icon.getIconHeight() / 2));
		
		public EzInternalFrameTitlePane()
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
		public void paintComponent(Graphics g)
		{
			Graphics2D g2d = (Graphics2D) g.create();
			EzGUI.paintTitlePane(g2d, getWidth(), getHeight(), EzInternalFrame.this.getTitle(), true);
			
			// paint the icon manually, as it is not the default for internal frames
			icon.paintIcon(frame, g, iconLocation.x, iconLocation.y);
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
	
	@Override
	public void updateUI()
	{
		SubstanceInternalFrameUI ui = new EzInternalFrameUI();
		setUI(ui);
		updateTitlePane(ui.getTitlePane());
	}
}
