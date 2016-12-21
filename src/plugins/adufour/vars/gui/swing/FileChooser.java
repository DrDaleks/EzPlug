package plugins.adufour.vars.gui.swing;

import icy.system.thread.ThreadUtil;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

/**
 * File chooser component
 * 
 * @author Alexandre Dufour
 * 
 */
public class FileChooser extends SwingVarEditor<File>
{
    private ActionListener actionListener;

    /**
     * Creates a new file chooser component
     * 
     * @param variable
     *            the variable to attach to this file chooser
     */
    public FileChooser(Var<File> variable)
    {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent()
    {
        JButton jButton = new JButton();

        String path = null;
        FileMode fileMode = FileMode.ALL;
        boolean allowHidden = false;

        VarEditorModel<File> model = variable.getDefaultEditorModel();

        if (model instanceof FileTypeModel)
        {
            path = ((FileTypeModel) model).getPath();
            fileMode = ((FileTypeModel) model).getMode();
            allowHidden = ((FileTypeModel) model).allowHidden();
        }

        final JFileChooser jFileChooser = new JFileChooser(path);
        switch (fileMode)
        {
        case FILES:
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jButton.setText("Choose file name...");
        break;

        case FOLDERS:
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jButton.setText("Choose folder name...");
        break;

        default:
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jButton.setText("Choose file or folder name...");
        }

        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileHidingEnabled(!allowHidden);

        actionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;

                variable.setValue(jFileChooser.getSelectedFile());
                setButtonText(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        };

        // jButton.addActionListener(actionListener);

        return jButton;
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(100, 20);
    }

    /**
     * Replaces the button text by the given string
     * 
     * @param text
     */
    public void setButtonText(final String text)
    {

        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                getEditorComponent().setText(text);
            }
        });
    }

    @Override
    protected void updateInterfaceValue()
    {
        File newValue = variable.getValue();

        String tooltip = "<html><pre><font size=3>";

        if (newValue != null)
        {
            tooltip += newValue.getAbsolutePath();
            if (newValue.isDirectory()) tooltip += "/";
        }

        tooltip += "</font></pre></html>";

        getEditorComponent().setToolTipText(tooltip);
    }

    @Override
    public JButton getEditorComponent()
    {
        return (JButton) super.getEditorComponent();
    }

    @Override
    protected void activateListeners()
    {
        getEditorComponent().addActionListener(actionListener);
    }

    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeActionListener(actionListener);
    }
}
