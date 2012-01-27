package plugins.adufour.ezplug;

public interface EzGUIManager
{
	/**
	 * Re-packs the user interface. This method should be called if one of the components was
	 * changed either in size or visibility state
	 * 
	 * @param updateParametersPanel
	 *            Set to true if the visibility of some parameters has been changed and the panel
	 *            should be redrawn before re-packing the frame
	 */
	void repack(boolean updateParametersPanel);
}
