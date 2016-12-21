package plugins.adufour.ezplug;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;

import icy.system.thread.ThreadUtil;

/**
 * Class defining a text label for use with EzPlugs.
 * 
 * @author Alexandre Dufour
 */
public class EzLabel extends EzComponent
{
    private JEditorPane label;
    
    private String      text;
    
    private Color       textColor;
    
    private int         labelWidth = 200;
    
    /**
     * Creates a new label with given text
     * 
     * @param text
     *            the text to display. <b>Important note for HTML-formatted text:
     *            <ul>
     *            <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for
     *            other formatting properties of this class (e.g. text color or label width)</li>
     *            <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is
     *            performed, leaving full freedom to the developer.</li>
     *            </ul>
     *            </b>
     */
    public EzLabel(String text)
    {
        this(text, null);
    }
    
    /**
     * Creates a new label with given text and color
     * 
     * @param text
     *            the text to display.<b>Important note for HTML-formatted text:
     *            <ul>
     *            <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for
     *            other formatting properties of this class (e.g. text color or label width)</li>
     *            <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is
     *            performed, leaving full freedom to the developer.</li>
     *            </ul>
     *            </b>
     * @param textColor
     *            the default text color. NB: this parameter is not used if the specified text is in
     *            HTML format
     */
    public EzLabel(final String text, final Color textColor)
    {
        super("label");
        
        this.text = text;
        this.textColor = textColor;
        
        ThreadUtil.invoke(new Runnable()
        {
            @Override
            public void run()
            {
                label = new JEditorPane("text/html", "");
                label.setEditable(false);
                label.setOpaque(false);
                label.setMargin(new Insets(0, 2, 0, 2));
                updateLabel();
            }
        }, !SwingUtilities.isEventDispatchThread());
    }
    
    /**
     * Sets the text of this label. This text can be given unformatted or in HTML format.<br/>
     * <b>Important note for HTML-formatted text:
     * <ul>
     * <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for other
     * formatting properties of this class (e.g. text color or label width)</li>
     * <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is performed,
     * leaving full freedom to the developer.</li>
     * </ul>
     * </b>
     * 
     * @param text
     *            the text to display.<br/>
     *            <b>Important note for HTML-formatted text:
     *            <ul>
     *            <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for
     *            other formatting properties of this class (e.g. text color or label width)</li>
     *            <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is
     *            performed, leaving full freedom to the developer.</li>
     *            </ul>
     *            </b>
     */
    public void setText(String text)
    {
        this.text = text;
        updateLabel();
    }
    
    /**
     * Overrides the default label width (200px) with the specified value
     * 
     * @param width
     *            the new label width in pixels
     */
    public void setLabelWidth(int width)
    {
        this.labelWidth = width;
        updateLabel();
    }
    
    /**
     * Sets the color of the text in this label. Setting the color overrides the default color
     * computed for the current look'n'feel and skin. To revert to this default color, use
     * <code>setColor(null)</code>
     * 
     * @param textColor
     *            the new text color
     */
    public void setColor(Color textColor)
    {
        this.textColor = textColor;
        updateLabel();
    }
    
    @Override
    protected void addTo(Container container)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 2, 10);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        container.add(label, gbc);
    }
    
    @Override
    public void setToolTipText(String text)
    {
        label.setToolTipText(text);
    }
    
    /**
     * Sets the number of columns in this label (this will affect its apparent width).<br/>
     * NOTE: By default, the number of columns is translated into a width measure by taking the
     * character "<code>m</code>" as reference (assumed to be the largest possible character). It
     * does *not* indicate a limit in the number of characters per line, since the width of each
     * character is potentially different in many fonts.
     * 
     * @deprecated this method is no longer used. To specify the width of the label, use
     *             {@link #setLabelWidth(int)} instead.
     * @param nbCols
     *            the number of columns of this label
     */
    @Deprecated
    public void setNumberOfColumns(final int nbCols)
    {
    }
    
    /**
     * Sets the number of rows in this label (this will affect its apparent height)
     * 
     * @deprecated This method is no longer used, as the component's height is automatically
     *             calculated based on its contents
     * @param nbRows
     */
    @Deprecated
    public void setNumberOfRows(final int nbRows)
    {
    }
    
    private static String convertToHTML(String text, Color textColor, int labelWidth)
    {
        if (text.trim().toLowerCase().startsWith("<html>") && text.toLowerCase().contains("<body"))
        {
            // A "body" tag already exists => let's assume the developer knows what he's doing
            return text;
        }
        
        // Convert the string to HTML
        String html = text.trim();
        if (!html.toLowerCase().startsWith("<html>"))
        {
            // The text seems unformatted
            // Convert it to HTML (managing potential "new line" characters)
            html = "<html>" + html.replace("\n", "<br>") + "</html>";
        }
        
        // Now that the text is HTML, adjust its style (via CSS)
        // Warning: this is ugly (should use Document.getStyleSheet() instead, etc.)
        
        int r, g, b;
        r = textColor.getRed();
        g = textColor.getGreen();
        b = textColor.getBlue();
        
        String style = "max-width: " + labelWidth + "px; ";
        style += "font: helvetica; ";
        style += "color: rgb(" + r + "," + g + "," + b + "); ";
        
        html = html.replace("<html>", "<html><body style='" + style + "'>");
        html = html.replace("</html>", "</body></html>");
        
        return html;
    }
    
    private void updateLabel()
    {
        // Check which color to use
        Color color = textColor;
        
        if (color == null)
        {
            // Use the default from the current look'n'feel
            color = label.getForeground();
        }
        
        String html = convertToHTML(text, color, labelWidth);
        label.setText(html);
    }
}
