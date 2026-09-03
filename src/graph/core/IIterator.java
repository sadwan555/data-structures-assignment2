package graph.core;

/**
 * Iterator ADT.
 */
public interface IIterator<T> {
   
   /**
    * Check whether the iterator has more elements.
    * @return {@code true} if there are more elements to iterate through, {@code false} otherwise.
    */
	public boolean hasNext();
	
	/**
	 * Get the next element from the iterator.
	 * @return The next element, or {@code null} if there are no more elements.
	 */
	public T next();
}
