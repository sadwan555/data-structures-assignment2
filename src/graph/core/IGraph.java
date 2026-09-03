package graph.core;

public interface IGraph<V,E> {
   /**
    * Find the vertices that are connected by a given Edge.
    * @param e An edge.
    * @return An array (of length 2) containing the two end vertices of {@code e}.
    */
    public IVertex<V>[] endVertices(IEdge<E> e);
    
    /**
     * Find the Vertex that is opposite {@code v} if traveling along edge {@code e}.
     * In other words, {@code e} connects {@code v} to which other vertex?
     * @param v The vertex to begin at.
     * @param e The edge to travel along.
     * @return The vertex opposite {@code v} along edge {@code e}.
     */
    public IVertex<V> opposite(IVertex<V> v, IEdge<E> e);
    
    /**
     * Find whether two vertices are adjacent. Two vertices are adjacent if
     * there is an edge that connects them directly.
     * @param v The first vertex to check.
     * @param w The second vertex to check.
     * @return {@code true} if {@code v} and {@code w} are connected by an edge,
     *    {@code false} otherwise.
     */
    public boolean areAdjacent(IVertex<V> v, IVertex<V> w);
    
    /**
     * Replace the element of vertex {@code v} with a new element ({@code o}). 
     * @param v The vertex whose element should be changed.
     * @param o The new element to store at this vertex.
     * @return The old element that was stored in {@code v} before this method was called.
     */
    public V replace(IVertex<V> v, V o);
    
    /**
     * Replace the element of edge {@code e} with a new element ({@code o}). 
     * @param e The edge whose element should be changed.
     * @param o The new element to store at this edge.
     * @return The old element that was stored in {@code e} before this method was called.
     */
    public E replace(IEdge<E> e, E o);
    
    /**
     * Insert a new vertex into the graph. The element in the new vertex is given as parameter {@code o}.
     * @param o The element to be stored in the new vertex.
     * @return The vertex that was created.
     */
    public IVertex<V> insertVertex(V o);
    
    /**
     * Insert a new edge into the graph. The edge should connect the vertices {@code v} and {@code w}, and have element {@code o}.
     * @param v The first vertex to connect.
     * @param w The second vertex to connect.
     * @param o The element to store in this edge.
     * @return The new edge that is created.
     */
    public IEdge<E> insertEdge(IVertex<V> v, IVertex<V> w, E o);
    
    /**
     * Remove a vertex from the graph.
     * @param v The vertex to be removed.
     * @return The element that was stored at that vertex before it was removed.
     */
    public V removeVertex(IVertex<V> v);
    
    /**
     * Remove an edge from the graph.
     * @param e The edge to be removed.
     * @return The element that was stored at that edge before it was removed.
     */
    public E removeEdge(IEdge<E> e);
    
    /**
     * Find the edges that are incident on {@code v}. This is should be an iterator that can iterate
     * over all the edges that are connected to vertex {@code v}.
     * @param v The vertex that the edges should be connected to.
     * @return An iterator that can iterate over all the edges connected to {@code v}.
     */
    public IIterator<IEdge<E>> incidentEdges(IVertex<V> v);
    
    /**
     * Find all the vertices in the graph.
     * @return An iterator that can iterate over all the vertices in the graph.
     */
    public IIterator<IVertex<V>> vertices();
    
    /**
     * Find all the edges in the graph.
     * @return An iterator that can iterate over all the edges in the graph.
     */
    public IIterator<IEdge<E>> edges();
}