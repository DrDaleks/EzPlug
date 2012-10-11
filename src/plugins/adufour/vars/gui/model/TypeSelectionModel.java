package plugins.adufour.vars.gui.model;

import icy.sequence.Sequence;

import java.io.File;

public class TypeSelectionModel extends ValueSelectionModel<Class<?>>
{
	/**
	 * Create a new type selection model with the most popular types
	 */
	public TypeSelectionModel()
	{
		super(new Class<?>[] { null, Sequence.class, Integer.class, Double.class, int[].class, double[].class, String.class, File.class, File[].class });
	}

	/**
	 * Creates a new custom type selection model with the specified types
	 * 
	 * @param classes
	 */
	public TypeSelectionModel(Class<?>... classes)
	{
		super(classes);
	}
}
