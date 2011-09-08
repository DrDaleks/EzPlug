package plugins.adufour.ezplug;

import icy.gui.component.IcyLogo;
import icy.gui.frame.IcyExternalFrame;
import icy.gui.frame.IcyFrame;
import icy.gui.frame.IcyInternalFrame;
import icy.gui.util.GuiUtil;
import icy.system.thread.ThreadUtil;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileSystemView;

import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class EzGUI extends IcyFrame implements EzGUIManager, ActionListener
{
    public static final int       LOGO_HEIGHT               = 32;

    public static final int       FONT_SIZE                 = 16;

    private static final boolean  USE_SKIN_COLOR_SCHEME     = true;

    private Color                 logoTitleColor;

    private EzPlug                ezPlug;

    private Thread                executionThread;

    private JPanel                jPanelParameters;

    private final List<Component> components                = new ArrayList<Component>();

    private JPanel                jPanelBottom;

    private JPanel                jPanelButtons;

    private JButton               jButtonRun;

    private JButton               jButtonStop;

    private JButton               jButtonSaveParameters;

    private JButton               jButtonLoadParameters;

    private boolean               jButtonsParametersVisible = true;

    private JProgressBar          jProgressBar;

    public EzGUI(final EzPlug ezPlug)
    {
        super(ezPlug.getName(), true, true, false, true);

        this.ezPlug = ezPlug;
        this.executionThread = new Thread(ezPlug);

        jPanelParameters = new JPanel();

        jPanelParameters.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        jPanelParameters.setLayout(new GridBagLayout());

        jPanelParameters = new JPanel();

        jPanelParameters.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        jPanelParameters.setLayout(new GridBagLayout());

        jPanelBottom = new JPanel(new GridLayout(2, 1));

        jPanelButtons = new JPanel(new GridLayout(1, 4));
        jPanelBottom.add(jPanelButtons);

        jButtonRun = new JButton("Run");
        jButtonRun.addActionListener(this);
        jPanelButtons.add(jButtonRun);

        if (ezPlug instanceof EzStoppable)
        {
            jButtonStop = new JButton("Stop");
            jButtonStop.setEnabled(false);
            jButtonStop.addActionListener(this);
            jPanelButtons.add(jButtonStop);
        }

        jButtonSaveParameters = new JButton("Save");
        jButtonSaveParameters.addActionListener(this);
        jPanelButtons.add(jButtonSaveParameters);

        jButtonLoadParameters = new JButton("Load");
        jButtonLoadParameters.addActionListener(this);
        jPanelButtons.add(jButtonLoadParameters);

        jProgressBar = new JProgressBar();
        jProgressBar.setString("Running...");
        jPanelBottom.add(jProgressBar);

        getContentPane().add(jPanelParameters, BorderLayout.CENTER);
        getContentPane().add(jPanelBottom, BorderLayout.SOUTH);

        pack();

        // TODO plug-in home-made internal and external frames

    }

    @Override
    protected IcyInternalFrame creatInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
    {
        return new EzInternalFrame(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    protected IcyExternalFrame createExternalFrame(String title)
    {
        return new EzExternalFrame(title);
    }

    public void addComponent(Component component)
    {
        components.add(component);
    }

    public void addEzComponent(EzComponent component, boolean isSingle)
    {
        // Special case #1: if the component is a variable, register it
        if (component instanceof EzVar<?>)
        {
            ezPlug.registerVariable((EzVar<?>) component);
        }
        // Special case #2: if the component is a group, add its internal components recursively
        else if (component instanceof EzGroup)
        {
            for (EzComponent groupComponent : (EzGroup) component)
                addEzComponent(groupComponent, false);
        }

        // set the parent UI of this component
        if (component instanceof EzComponent) component.setUI(this);
        if (isSingle) components.add(component);
    }

    /**
     * Re-packs the user interface. This method should be called if one of the components was changed either in size or
     * visibility state
     * 
     * @param updateParametersPanel
     *            Set to true if the visibility of some parameters has been changed and the panel should be redrawn
     *            before re-packing the frame
     */
    public void repack(boolean updateParametersPanel)
    {
        setVisible(false);

        if (updateParametersPanel)
        {
            jPanelParameters.removeAll();

            for (Component component : components)
                if (component instanceof EzComponent)
                {
                    ((EzComponent) component).addToContainer(jPanelParameters);
                }
                else
                {
                    GridBagLayout gridbag = (GridBagLayout) jPanelParameters.getLayout();

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(2, 5, 2, 5);
                    gbc.fill = GridBagConstraints.BOTH;
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    // resize behavior
                    gbc.weightx = 1;
                    gbc.weighty = 1;

                    component.setFocusable(false);
                    gridbag.setConstraints(component, gbc);

                    jPanelParameters.add(component);
                }
        }

        pack();

        setVisible(true);
    }

    /**
     * Highlights the plug's title bar
     * 
     * @param state
     */
    public void setHighlightedState(boolean state)
    {
        JPanel jPanel = (JPanel) getContentPane().getComponent(0);

        for (Component component : jPanel.getComponents())
        {
            if (!(component instanceof IcyLogo)) continue;

            IcyLogo logo = (IcyLogo) component;
            Component logoTitle = logo.getComponent(1);

            if (state)
            {
                logoTitleColor = logoTitle.getForeground();
                logoTitle.setForeground(Color.cyan);
            }
            else
            {
                logoTitle.setForeground(logoTitleColor);

            }
            logoTitle.repaint();
        }
    }

    /**
     * Sets the state of the "Run" button on the interface
     */
    public void setRunButtonEnabled(final boolean runnable)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jButtonRun.setEnabled(runnable);
            }
        });
    }

    /**
     * Sets the text of the run button
     */
    public void setRunButtonText(final String text)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jButtonRun.setText(text);
            }
        });
    }

    void setRunningState(final boolean running)
    {
        ThreadUtil.invokeNow(new Runnable()
        {
            public void run()
            {
                jButtonRun.setEnabled(!running);
                if (ezPlug instanceof EzStoppable) jButtonStop.setEnabled(running);

                // Note: Printing a string on a progress bar is not supported on Mac OS look'n'feel.
                // jButtonRun.setText(running ? "Running..." : "Run");
                jProgressBar.setString(running ? "Running..." : "");
                jProgressBar.setStringPainted(running);

                jProgressBar.setValue(0);
                jProgressBar.setIndeterminate(running);

                // Repack the frame to ensure good behavior of some components
                repack(false);
            }
        });
    }

    /**
     * Sets whether the action panel (buttons and progress bar) are visible or not
     * 
     * @param visible
     *            the new visibility state of the action panel
     */
    public void setActionPanelVisible(final boolean visible)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jPanelBottom.setVisible(visible);
            }
        });
    }

    public void setProgressBarVisible(final boolean visible)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jProgressBar.setVisible(visible);
            }
        });
    }

    public void setParametersIOVisible(final boolean visible)
    {
        if (visible == jButtonsParametersVisible) return;

        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                if (visible)
                {
                    jPanelButtons.add(jButtonLoadParameters);
                    jPanelButtons.add(jButtonSaveParameters);
                }
                else
                {
                    jPanelButtons.remove(jButtonLoadParameters);
                    jPanelButtons.remove(jButtonSaveParameters);
                }
            }
        });

        jButtonsParametersVisible = visible;
    }

    // ActionListener //

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(jButtonRun))
        {
            ThreadUtil.bgRun(executionThread);
        }
        else if (e.getSource().equals(jButtonStop))
        {
            if (ezPlug instanceof EzStoppable) ((EzStoppable) ezPlug).stopExecution();
        }
        else if (e.getSource().equals(jButtonLoadParameters))
        {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView());

            if (jfc.showOpenDialog(getContentPane()) != JFileChooser.APPROVE_OPTION) return;

            ezPlug.loadParameters(jfc.getSelectedFile());

        }
        else if (e.getSource().equals(jButtonSaveParameters))
        {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView());

            if (jfc.showSaveDialog(getContentPane()) != JFileChooser.APPROVE_OPTION) return;

            ezPlug.saveParameters(jfc.getSelectedFile());
        }
        else
        {
            throw new UnsupportedOperationException("Action event not recognized for source " + e.getSource());
        }
    }

    public void onClosed()
    {
        setVisible(false);
        
        getInternalFrame().dispose();
        
        if (ezPlug == null) return;

        ezPlug.cleanFromUI();

        if (executionThread != null && executionThread.isAlive())
        {
            // stop the execution if it was still running
            if (ezPlug instanceof EzStoppable)
                ((EzStoppable) ezPlug).stopExecution();
            else
            {
                executionThread.interrupt();
                System.err.println("Plug " + ezPlug.getName() + " was still running and has been interrupted");
            }
        }

        // dispose all components

        for (Component component : components)
            if (component instanceof EzComponent) ((EzComponent) component).dispose(); // FIXME

        components.clear();

        jPanelParameters.removeAll();
        repack(false);

        // remove all listeners

        jButtonRun.removeActionListener(this);
        if (jButtonStop != null) jButtonStop.removeActionListener(this);
        jButtonLoadParameters.removeActionListener(this);
        jButtonSaveParameters.removeActionListener(this);

        ezPlug = null;
        super.onClosed();
    }

    // Title pane creation

    public static void paintTitlePane(Graphics2D graphics, int width, int height, String title, boolean internal)
    {
        paintBackground(width, height, internal, graphics);

        if (USE_SKIN_COLOR_SCHEME)
        {
            SubstanceColorScheme colors = SubstanceLookAndFeel.getCurrentSkin().getColorScheme(new JButton(), ComponentState.PRESSED_SELECTED);
            graphics.setColor(colors.isDark() ? Color.white : colors.getUltraDarkColor().darker().darker());
        }
        else
        {
            graphics.setColor(Color.white);
        }

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics m = graphics.getFontMetrics();
        int titleWidth = m.stringWidth(title);
        int titleHeight = m.getHeight() - 6;

        graphics.drawString(title, (width - titleWidth) / 2, (height + titleHeight) / 2);
        // graphics.dispose();
    }

    /**
     * Modified version of the {@link GuiUtil#paintBackGround(int, int, Graphics)} method for the purpose of EzGUI.
     * Changes include:<br>
     * <ul>
     * <li>Shade and lighting effects adjusted</li>
     * <li>Border outline removed</li>
     * <li>Bottom corners of the rounded rectangle masked to better stick to the rest of the frame</li>
     * </ul>
     * 
     * @param width
     * @param height
     * @param g
     */
    private static void paintBackground(int width, int height, boolean internal, Graphics2D graphics)
    {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color brightColor, darkColor;

        if (USE_SKIN_COLOR_SCHEME)
        {
            SubstanceColorScheme colors = SubstanceLookAndFeel.getCurrentSkin().getColorScheme(new JButton(), ComponentState.PRESSED_SELECTED);
            brightColor = colors.getUltraLightColor().brighter();
            darkColor = colors.getDarkColor();
        }
        else
        {
            brightColor = new Color(72, 72, 72);
            darkColor = new Color(4, 4, 4);
        }
        graphics.setPaint(new GradientPaint(0, 0, brightColor, 0, height, darkColor));

        if (internal)
        {
            // Fill a rounded rectangle with gradient paint
            float ray = Math.max(width, height) * 0.05f;
            float finalRay = Math.min(ray * 2, 20);
            graphics.fill(new RoundRectangle2D.Double(0, 0, width, height, finalRay, finalRay));
            // mask the bottom corners of the rounded rectangle
            graphics.fillRect(0, height / 2, width, height / 2);
        }
        else
        {
            graphics.fill(new Rectangle2D.Double(0, 0, width, height));
        }

        // add a bright oval portion to simulate a glass reflection
        graphics.setPaint(new GradientPaint(0, 0, brightColor, 0, height * 2, darkColor));
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        graphics.fillOval(-width + (width / 2), height / 3, width * 2, height * 3);

        // graphics.dispose();
    }

}
