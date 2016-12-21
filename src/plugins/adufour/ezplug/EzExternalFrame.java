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
    private static final long serialVersionUID = 1L;

    EzExternalFrame(String title)
    {
        super(title);
        
        // simulate a skin changed event to setup the title pane correctly
        skinChanged();

        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getContentPane().setLayout(new BorderLayout(0, 0));
    }
    
    @Override
    public void close()
    {
        super.close();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }

    private final class EzRootPaneUI extends SubstanceRootPaneUI
    {
        @Override
        protected JComponent createTitlePane(JRootPane newRoot)
        {
            return new EzTitlePane(newRoot, this);
        }
    }

    private final class EzTitlePane extends SubstanceTitlePane
    {
        private static final long serialVersionUID = 1L;

        public EzTitlePane(JRootPane root, final SubstanceRootPaneUI ui)
        {
            super(root, ui);

            setFont(getFont().deriveFont(Font.BOLD + Font.ITALIC, EzGUI.FONT_SIZE));

            FontMetrics m = getFontMetrics(getFont());

            int titleWidth = m.stringWidth(getTitle());

            setPreferredSize(new Dimension(titleWidth + 100, EzGUI.LOGO_HEIGHT));
        }

        @Override
        public void paintComponent(Graphics g)
        {
            EzGUI.paintTitlePane((Graphics2D) g, getWidth(), getHeight(), EzExternalFrame.this.getTitle(), false);
        }
    }

    @Override
    public void skinChanged()
    {
        EzRootPaneUI ui = new EzRootPaneUI();
        getRootPane().setUI(ui);
        updateTitlePane((EzTitlePane) ui.getTitlePane());
    }
}
