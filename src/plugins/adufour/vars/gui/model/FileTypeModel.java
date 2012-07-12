package plugins.adufour.vars.gui.model;

import java.io.File;
import java.io.FileFilter;

import plugins.adufour.vars.gui.FileMode;

public class FileTypeModel implements VarEditorModel<File>
{
	private final FileMode		mode;
	
	private final FileFilter	filter;
	
	private final String		path;
	
	private final boolean		allowHidden;
	
	public FileTypeModel(String initialPath, FileMode mode, FileFilter filter, boolean allowHidden)
	{
		this.path = initialPath;
		this.filter = filter;
		this.mode = mode;
		this.allowHidden = allowHidden;
	}
	
	@Override
	public boolean isValid(File value)
	{
		if (filter != null && !filter.accept(value)) return false;
		
		if (value.isFile() && mode != FileMode.FILES) return false;
		
		if (value.isDirectory() && mode != FileMode.FOLDERS) return false;
		
		if (value.isHidden() && !allowHidden) return false;
		
		return true;
	}
	
	@Override
	public File getDefaultValue()
	{
		return null;
	}
	
	public FileFilter getFilter()
	{
		return filter;
	}
	
	public FileMode getMode()
	{
		return mode;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public boolean allowHidden()
	{
		return allowHidden;
	}
}
