package plugins.adufour.ezplug;

import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * GUI component for EzPlug that displays a number of tab pages. <br/>
 * Developer note: this component is backed by a {@link JTabbedPane}
 * 
 * @author Alexandre Dufour
 */
public class EzTabs extends EzPanel
{
    public enum TabPlacement
    {
        TOP(JTabbedPane.TOP), BOTTOM(JTabbedPane.BOTTOM), LEFT(JTabbedPane.LEFT), RIGHT(JTabbedPane.RIGHT);
        
        final int tabPlacement;
        
        private TabPlacement(int tabPlacement)
        {
            this.tabPlacement = tabPlacement;
        }
    }
    
    /**
     * Create a new component to store tab pages.
     * 
     * @param uniqueID
     *            a unique identifier. This identifier will not be shown on screen, but is necessary
     *            to store and load user parameters.
     */
    public EzTabs(String uniqueID, TabPlacement tabPlacement)
    {
        super(uniqueID);
        
        ((JTabbedPane) container).setTabPlacement(tabPlacement.tabPlacement);
    }
    
    @Override
    protected JComponent createContainer()
    {
        return new JTabbedPane();
    }
    
    @Override
    protected void buildPanel()
    {
        JTabbedPane tabs = (JTabbedPane) container;
        
        int currentTab = tabs.getSelectedIndex();
        
        container.removeAll();
        
        for (EzComponent tab : this)
            if (tab.isVisible())
            {
                JPanel tabPanel = new JPanel(new GridBagLayout());
                tab.addTo(tabPanel);
                tabs.addTab(tab.name, tabPanel);
            }
        
        if (currentTab >= 0 && currentTab < tabs.getTabCount())
        {
            tabs.setSelectedIndex(currentTab);
        }
    }
}
