package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * Class defining a group of EzComponents, which will appear in the interface within a titled box.
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzGroup extends EzComponent implements Iterable<EzComponent>
{
    public interface FoldListener
    {
        void foldStateChanged(boolean state);
    }

    private final ArrayList<EzComponent>  components;

    private JPanel                        jPanelGroup;

    private final CollapseHandler         collapseHandler = new CollapseHandler();

    private final ArrayList<FoldListener> listeners       = new ArrayList<EzGroup.FoldListener>();

    /**
     * Creates a new EzGroup with the given box title and set of ezComponents. Each component will be drawn on the
     * interface in their order of declaration.
     * 
     * @param groupTitle
     *            The group title (will appear on the enclosing box in the user interface)
     * @param ezComponents
     *            the components to add (must be non-empty)
     * @throws IllegalArgumentException
     *             if ezComponents is empty
     */
    public EzGroup(String groupTitle, EzComponent... ezComponents)
    {
        super(groupTitle);

        ThreadUtil.invoke(new Runnable()
        {
            @Override
            public void run()
            {
                jPanelGroup = new JPanel(new GridBagLayout());

                jPanelGroup.addMouseListener(collapseHandler);
            }
        }, !SwingUtilities.isEventDispatchThread());

        this.components = new ArrayList<EzComponent>(ezComponents.length);

        if (ezComponents != null && ezComponents.length > 0) addEzComponent(ezComponents);
    }

    public void addFoldListener(FoldListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Adds the specified EzComponents to this group
     * 
     * @param ezComponents
     *            the components to add
     */
    public void addEzComponent(EzComponent... ezComponents)
    {
        if (ezComponents == null || ezComponents.length == 0) return;

        for (EzComponent ezComponent : ezComponents)
        {
            if (ezComponent.getGroup() != null)
            {
                throw new EzException("Cannot initialize plugin: an EzComponent may not be assigned to more than one EzGroup", false);
            }

            ezComponent.setGroup(this);
            this.components.add(ezComponent);
        }
    }

    @Override
    public void addTo(Container container)
    {
        jPanelGroup.removeAll();

        if (collapseHandler.isCollapsed)
        {
            jPanelGroup.setBorder(collapseHandler.collapsedBorder);
        }
        else
        {
            jPanelGroup.setBorder(collapseHandler.uncollapsedBorder);

            for (EzComponent ezComponent : components)
                if (ezComponent.isVisible()) ezComponent.addTo(jPanelGroup);
        }

        GridBagLayout gridbag = (GridBagLayout) container.getLayout();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(jPanelGroup, gbc);

        container.add(jPanelGroup);

    }

    public void dispose()
    {
        listeners.clear();

        jPanelGroup.removeMouseListener(collapseHandler);

        for (EzComponent ezComponent : components)
            ezComponent.dispose();

        components.clear();

        super.dispose();
    }
    
    @Override
    public Iterator<EzComponent> iterator()
    {
        return components.iterator();
    }

    private void fireFoldStateChanged(boolean state)
    {
        final ArrayList<FoldListener> listenersCopy;

        synchronized (listeners)
        {
            listenersCopy = new ArrayList<FoldListener>(listeners);
        }

        for (FoldListener listener : listenersCopy)
            listener.foldStateChanged(state);
    }

    private final class CollapseHandler implements MouseListener
    {
        private boolean            isCollapsed        = false;

        private final static char  COLLAPSED_SYMBOL   = '\u25B6';

        private final static char  UNCOLLAPSED_SYMBOL = '\u25BC';

        private final TitledBorder collapsedBorder    = new TitledBorder(CollapseHandler.COLLAPSED_SYMBOL + " " + name);

        private final TitledBorder uncollapsedBorder  = new TitledBorder(CollapseHandler.UNCOLLAPSED_SYMBOL + " " + name);

        @Override
        public void mouseClicked(MouseEvent e)
        {
            FontMetrics fm = jPanelGroup.getGraphics().getFontMetrics();
            int w = fm.charWidth(COLLAPSED_SYMBOL);
            int h = fm.getHeight();
            int xBorder = 10;
            int yBorder = 4;
            if (e.getX() < xBorder || e.getY() < yBorder || e.getX() > (xBorder + w) || e.getY() > (yBorder + h))
            {
                e.consume();
                return;
            }

            isCollapsed = !isCollapsed;
            fireFoldStateChanged(isCollapsed);
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
        }
    }
}
