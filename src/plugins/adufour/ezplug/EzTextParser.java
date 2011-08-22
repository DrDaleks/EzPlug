package plugins.adufour.ezplug;

/**
 * Interface giving text parsing abilities to implementing classes
 * 
 * @author Alexandre Dufour
 * 
 * @param <T> the output type of the parsed text
 */
interface EzTextParser<T>
{
	/**
	 * Parse the user input and convert it to the correct object type
	 * 
	 * @param s
	 *            the user input to parse
	 */
	T parseInput(String s);
}
