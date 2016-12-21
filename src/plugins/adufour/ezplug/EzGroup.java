package plugins.adufour.ezplug;

import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.border.TitledBorder;

/**
 * Class defining a group of EzComponents, which will appear in the interface within a titled box.
 * 
 * @author Alexandre Dufour
 */
public class EzGroup extends EzPanel
{
    public interface FoldListener
    {
        void foldStateChanged(boolean state);
    }
    
    private final FoldHandler             foldHandler = new FoldHandler();
    
    private final ArrayList<FoldListener> listeners   = new ArrayList<EzGroup.FoldListener>();
    
    /**
     * Creates a new EzGroup with the given box title and set of ezComponents. Each component will
     * be drawn on the interface in their order of declaration.
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
        super(groupTitle, ezComponents);
        
        container.addMouseListener(foldHandler);
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
     * @deprecated use {@link #add(EzComponent...)}
     */
    @Deprecated
    public void addEzComponent(EzComponent... ezComponents)
    {
        super.add(ezComponents);
    }
    
    @Override
    protected void buildPanel()
    {
        if (foldHandler.isFolded)
        {
            container.removeAll();
            container.setBorder(foldHandler.foldedBorder);
        }
        else
        {
            super.buildPanel();
            container.setBorder(foldHandler.unfoldedBorder);
        }
    }
    
    public void dispose()
    {
        listeners.clear();
        
        container.removeMouseListener(foldHandler);
        
        super.dispose();
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
    
    /**
     * @return <code>true</code> if this group is currently folded (no visible component),
     *         <code>false</code> otherwise
     */
    public boolean getFoldedState()
    {
        return foldHandler.isFolded;
    }
    
    /**
     * Sets the folded state of this group
     * 
     * @param folded
     */
    public void setFoldedState(final boolean folded)
    {
        if (folded == foldHandler.isFolded) return;
        
        foldHandler.isFolded = folded;
        fireFoldStateChanged(folded);
        foldHandler.adjustToolTip();
    }
    
    private final class FoldHandler implements MouseListener
    {
        private boolean            isFolded        = false;
        
        private final static char  FOLDED_SYMBOL   = '\u25B6';
        
        private final static char  UNFOLDED_SYMBOL = '\u25BC';
        
        private final TitledBorder foldedBorder    = new TitledBorder(FoldHandler.FOLDED_SYMBOL + " " + name);
        
        private final TitledBorder unfoldedBorder  = new TitledBorder(FoldHandler.UNFOLDED_SYMBOL + " " + name);
        
        @Override
        public void mouseClicked(MouseEvent e)
        {
            FontMetrics fm = container.getGraphics().getFontMetrics();
            int w = fm.charWidth(FOLDED_SYMBOL);
            int h = fm.getHeight();
            int xBorder = 10;
            int yBorder = 4;
            if (e.getX() < xBorder || e.getY() < yBorder || e.getX() > (xBorder + w) || e.getY() > (yBorder + h))
            {
                e.consume();
                return;
            }
            
            isFolded = !isFolded;
            fireFoldStateChanged(isFolded);
            adjustToolTip();
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
            adjustToolTip();
        }
        
        @Override
        public void mouseExited(MouseEvent e)
        {
        }
        
        private void adjustToolTip()
        {
            if (isFolded && !getToolTipText().isEmpty())
            {
                container.setToolTipText(getToolTipText());
            }
            else
            {
                container.setToolTipText(null);
            }
        }
    }
}
