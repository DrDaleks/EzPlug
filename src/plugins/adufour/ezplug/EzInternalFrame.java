package plugins.adufour.ezplug;

import icy.gui.frame.IcyInternalFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.pushingpixels.substance.api.skin.SkinChangeListener;
import org.pushingpixels.substance.internal.ui.SubstanceDesktopIconUI;
import org.pushingpixels.substance.internal.ui.SubstanceInternalFrameUI;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;

public class EzInternalFrame extends IcyInternalFrame implements SkinChangeListener
{
    private static final long serialVersionUID = 1L;

    EzInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
    {
        super(title, resizable, closable, maximizable, iconifiable);
        SubstanceInternalFrameUI ui = new EzInternalFrameUI();
        setUI(ui);
        updateTitlePane(ui.getTitlePane());

        setOpaque(false);
        setBorder(new Border()
        {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
            {
                // leave the border transparent
                // we're just creating a "hot-zone" to capture mouse resize events
            }

            @Override
            public Insets getBorderInsets(Component c)
            {
                return new Insets(2, 2, 2, 2);
            }

            @Override
            public boolean isBorderOpaque()
            {
                return false;
            }
        });

        // look and feel change listener
        icy.gui.util.LookAndFeelUtil.addSkinChangeListener(EzInternalFrame.this);
    }

    @Override
    public void dispose()
    {
        super.dispose();

        icy.gui.util.LookAndFeelUtil.removeSkinChangeListener(EzInternalFrame.this);
        setUI(null);
    }

    @Override
    public boolean isOnSystemIcon(Point p)
    {
        return ((EzInternalFrameTitlePane) titlePane).isOnSystemIcon(p);
    }

    public class EzInternalFrameUI extends SubstanceInternalFrameUI
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
    public class EzInternalFrameTitlePane extends SubstanceInternalFrameTitlePane
    {
        private static final long serialVersionUID = 1L;

        final Icon                icon             = frame.getFrameIcon();
        final Point               iconLocation     = new Point(5, (EzGUI.LOGO_HEIGHT / 2) - (icon.getIconHeight() / 2));
        final Rectangle           iconRectangle    = new Rectangle(iconLocation, new Dimension(icon.getIconWidth(), icon.getIconHeight()));

        public EzInternalFrameTitlePane()
        {
            super(EzInternalFrame.this);

            setFont(getFont().deriveFont(Font.BOLD + Font.ITALIC, EzGUI.FONT_SIZE));

            FontMetrics m = getFontMetrics(getFont());

            int titleWidth = m.stringWidth(EzInternalFrame.this.getTitle());

            setPreferredSize(new Dimension(titleWidth + 100, EzGUI.LOGO_HEIGHT));

            // putClientProperty(this, DecorationAreaType.SECONDARY_TITLE_PANE);
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

        public boolean isOnSystemIcon(Point p)
        {
            return iconRectangle.contains(p);
        }

        /**
         * Layout manager for this title pane. Patched version of SubstanceTitlePaneLayout to adjust the buttons
         * position (they stick tighter in the upper right-hand corner)
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
                    if (!leftToRight) x += buttonWidth;
                }

                if (isMaximizable())
                {
                    spacing = isClosable() ? 2 : 4;
                    x += leftToRight ? -spacing - buttonWidth : spacing;
                    maxButton.setBounds(x, y, buttonWidth, buttonHeight);
                    if (!leftToRight) x += buttonWidth;
                }

                if (isIconifiable())
                {
                    spacing = isMaximizable() ? 2 : (isClosable() ? 2 : 4);
                    x += leftToRight ? -spacing - buttonWidth : spacing;
                    iconButton.setBounds(x, y, buttonWidth, buttonHeight);
                    if (!leftToRight) x += buttonWidth;
                }
            }
        }

    }

    /**
     * This class is a fork of SubstanceInternalFrameUI because the "titlePane" field of the original class has no set
     * method and is declared private, yielding a NullPointerException when closing the window (due to a confusion
     * between SubstanceInternalFrameUI.titlePane and BasicInternalFrameUI.northPane)
     * 
     * @author Alexandre Dufour
     * @deprecated replaced with new implementation which uses reflection to access the private field
     */
    public class EzInternalFrameUI_OLD extends BasicInternalFrameUI
    {
        EzInternalFrameTitlePane             titlePane;

        private final PropertyChangeListener substancePropertyListener;

        public EzInternalFrameUI_OLD()
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
            this.titlePane = new EzInternalFrameTitlePane();
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
        SubstanceInternalFrameUI ui = new EzInternalFrameUI();
        setUI(ui);
        updateTitlePane(ui.getTitlePane());
    }
}
