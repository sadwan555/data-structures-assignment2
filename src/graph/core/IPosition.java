package graph.core;

/**
 * Position ADT. A position is something that stores an element.
 *
 */
public interface IPosition<T> {
   /**
    * Get the element stored in this position.
    * @return
    */
   public T element();
}
