package plugins.adufour.ezplug;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import icy.system.thread.ThreadUtil;

/**
 * Class defining a generic panel containing multiple {@link EzComponent} objects. For two concrete
 * examples of this class, see {@link EzGroup} and {@link EzTabs}
 * 
 * @author Alexandre Dufour
 */
public class EzPanel extends EzComponent implements Iterable<EzComponent>
{
    @SuppressWarnings("serial")
    private class EzComponentList extends ArrayList<EzComponent>
    {
        public EzComponentList(int initialSize)
        {
            super(initialSize);
        }
        
        @Override
        public boolean add(EzComponent e)
        {
            e.setParentPanel(EzPanel.this);
            return super.add(e);
        }
        
        @Override
        public void add(int index, EzComponent element)
        {
            element.setParentPanel(EzPanel.this);
            super.add(index, element);
        }
        
        @Override
        public boolean addAll(Collection<? extends EzComponent> c)
        {
            for (EzComponent comp : c)
                comp.setParentPanel(EzPanel.this);
            return super.addAll(c);
        }
        
        @Override
        public boolean addAll(int index, Collection<? extends EzComponent> c)
        {
            for (EzComponent comp : c)
                comp.setParentPanel(EzPanel.this);
            return super.addAll(index, c);
        }
        
        @Override
        public void clear()
        {
            for (EzComponent comp : this)
                comp.setParentPanel(null);
            super.clear();
        }
        
        @Override
        public EzComponent remove(int index)
        {
            EzComponent oldElement = super.remove(index);
            if (oldElement != null) oldElement.setParentPanel(null);
            return oldElement;
        }
        
        @Override
        protected void removeRange(int fromIndex, int toIndex)
        {
            for (int i = fromIndex; i < toIndex; i++)
            {
                if (get(i) != null) get(i).setParentPanel(null);
            }
            super.removeRange(fromIndex, toIndex);
        }
        
        @Override
        public EzComponent set(int index, EzComponent element)
        {
            element.setParentPanel(EzPanel.this);
            EzComponent oldElement = super.set(index, element);
            if (oldElement != null) oldElement.setParentPanel(null);
            return oldElement;
        }
    }
    
    private final List<EzComponent> components;
    
    protected JComponent            container;
    
    protected String                toolTipText = "";
    
    public EzPanel(String name, EzComponent... ezComponents)
    {
        super(name);
        
        ThreadUtil.invoke(new Runnable()
        {
            @Override
            public void run()
            {
                container = createContainer();
            }
        }, !SwingUtilities.isEventDispatchThread());
        
        this.components = new EzComponentList(ezComponents.length);
        
        add(ezComponents);
    }
    
    @SuppressWarnings("static-method")
    protected JComponent createContainer()
    {
        return new JPanel(new GridBagLayout());
    }
    
    /**
     * Adds the specified EzComponents to this panel
     * 
     * @param ezComponents
     *            the components to add
     */
    public void add(EzComponent... ezComponents)
    {
        if (ezComponents == null || ezComponents.length == 0) return;
        
        for (EzComponent ezComponent : ezComponents)
            components.add(ezComponent);
    }
    
    @Override
    public Iterator<EzComponent> iterator()
    {
        return components.iterator();
    }
    
    protected void buildPanel()
    {
        container.removeAll();
        for (EzComponent ezComponent : components)
            if (ezComponent.isVisible()) ezComponent.addTo(container);
    }
    
    @Override
    protected void addTo(Container parentContainer)
    {
        buildPanel();
        
        GridBagLayout gridbag = (GridBagLayout) parentContainer.getLayout();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gridbag.setConstraints(this.container, gbc);
        
        parentContainer.add(this.container);
    }
    
    public String getToolTipText()
    {
        return toolTipText;
    }
    
    @Override
    public void setToolTipText(String text)
    {
        this.toolTipText = text;
    }
    
    @Override
    protected void dispose()
    {
        for (EzComponent ezComponent : components)
            ezComponent.dispose();
        components.clear();
        
        super.dispose();
    }
}
