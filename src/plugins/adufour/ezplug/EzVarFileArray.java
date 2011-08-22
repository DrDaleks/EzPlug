package plugins.adufour.ezplug;

import icy.file.FileUtil;
import icy.system.thread.ThreadUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 * Class defining a variable of type String, embarking a button triggering a file dialog as
 * graphical component
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarFileArray extends EzVar<File[]>
{
	private static final long	serialVersionUID	= 1L;

	private JButton			jButtonFile;
	
	private JFileChooser	jFileChooser;
	
	private File[]			selectedFiles;
	
	/**
	 * Constructs a new input variable with given name and default file dialog path
	 * 
	 * @param varName
	 *            the name of the variable (as it will appear in the interface)
	 * @param path
	 *            the default path to show in the file dialog
	 */
	public EzVarFileArray(String varName, final String path)
	{
		super(varName);
		
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				
				jButtonFile = new JButton("Select files...");
				
				jFileChooser = new JFileChooser(path);
				jFileChooser.setMultiSelectionEnabled(true);
				
				jButtonFile.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
							return;
						
						selectedFiles = jFileChooser.getSelectedFiles();
						
						jButtonFile.setText(selectedFiles.length + " files");
						
						if (selectedFiles.length == 0)
							return;
						
						String files = FileUtil.getDirectory(selectedFiles[0].getAbsolutePath());
						files += ": " + selectedFiles[0].getName();
						
						for (int i = 1; i < selectedFiles.length; i++)
						{
							files += ", " + selectedFiles[i].getName();
						}
						
						jButtonFile.setToolTipText(files);
						
						fireVariableChanged(selectedFiles);
					}
				});
				
				setComponent(jButtonFile);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	public File[] getValue()
	{
		return selectedFiles;
	}
}
