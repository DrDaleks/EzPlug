package plugins.adufour.ezplug;

import icy.gui.frame.IcyExternalFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JRootPane;

import org.pushingpixels.substance.api.skin.SkinChangeListener;
import org.pushingpixels.substance.internal.ui.SubstanceRootPaneUI;
import org.pushingpixels.substance.internal.utils.SubstanceTitlePane;

public class EzExternalFrame extends IcyExternalFrame implements SkinChangeListener
{
	private static final long		serialVersionUID			= 1L;
	
	EzExternalFrame(String title)
	{
		super(title);
		getRootPane().setUI(new EzRootPaneUI());
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		// look and feel change listener
		icy.gui.util.LookAndFeelUtil.addSkinChangeListener(EzExternalFrame.this);
		
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
		
	public class EzRootPaneUI extends SubstanceRootPaneUI
	{
		@Override
		protected JComponent createTitlePane(JRootPane root)
		{
			return new EzTitlePane(root, this);
		}
	}
	
	private class EzTitlePane extends SubstanceTitlePane
	{
		private static final long	serialVersionUID	= 1L;
		
		public EzTitlePane(JRootPane root, SubstanceRootPaneUI ui)
		{
			super(root, ui);
			
			setFont(getFont().deriveFont(Font.BOLD + Font.ITALIC, EzGUI.FONT_SIZE));
			
			FontMetrics m = getFontMetrics(getFont());
			
			int titleWidth = m.stringWidth(getTitle());
			
			setPreferredSize(new Dimension(titleWidth + 100, EzGUI.LOGO_HEIGHT));
		}
		
		@Override
		public void paint(Graphics g)
		{
			EzGUI.paintTitlePane((Graphics2D) g, getWidth(), getHeight(), EzExternalFrame.this.getTitle());
		}
	}
	
	@Override
	public void skinChanged()
	{
		getRootPane().setUI(new EzRootPaneUI());
	}
}
