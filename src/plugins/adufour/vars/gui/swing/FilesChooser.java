package plugins.adufour.vars.gui.swing;

import icy.system.thread.ThreadUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeListModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

/**
 * File chooser component for the Vars library. This component is tailored to select a list of files
 * via a {@link JFileChooser} component
 * 
 * @author Alexandre Dufour
 * 
 */
public class FilesChooser extends SwingVarEditor<File[]>
{
	private ActionListener	actionListener;
	
	public FilesChooser(Var<File[]> variable)
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
		
		VarEditorModel<File[]> model = (VarEditorModel<File[]>) variable.getDefaultEditorModel();
		
		if (model instanceof FileTypeListModel)
		{
			path = ((FileTypeListModel) model).getPath();
			fileMode = ((FileTypeListModel) model).getMode();
			allowHidden = ((FileTypeListModel) model).allowHidden();
		}
		
		final JFileChooser jFileChooser = new JFileChooser(path);
		switch (fileMode)
		{
			case FILES:
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jButton.setText("Select files...");
			break;
			
			case FOLDERS:
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jButton.setText("Select folders...");
			break;
			
			default:
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jButton.setText("Select files or folders...");
		}
		
		jFileChooser.setMultiSelectionEnabled(true);
		jFileChooser.setFileHidingEnabled(allowHidden);
		
		actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
				
				variable.setValue(jFileChooser.getSelectedFiles());
				File[] files = jFileChooser.getSelectedFiles();
				setButtonText(files.length + " file" + (files.length > 1 ? "s" : "") + " (click to change)");
			}
		};
		
		return jButton;
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
	    File[] newValue = variable.getValue();
	    
		String list = "<html><pre><font size=3>";
		
		if (newValue != null)
		{
			for (File f : newValue)
			{
				list += f.getAbsolutePath();
				if (f.isDirectory()) list += "/";
				list += "<br>";
			}
		}
		
		list += "</font></pre></html>";
		
		getEditorComponent().setToolTipText(list);
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
