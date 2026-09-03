package graph.impl;

import graph.core.IEdge;
import graph.core.IGraph;
import graph.core.IIterator;
import graph.core.IList;
import graph.core.IPosition;
import graph.core.IVertex;
import graph.util.DLinkedList;

public class EdgeListGraph<V,E> implements IGraph<V,E> {
	/**
	 * Inner class to represent a vertex in an edge list graph implementation
	 */
	private class EdgeListVertex implements IVertex<V> {
		// reference to a node in the vertex list
		IPosition<IVertex<V>> node;

		// element stored in this vertex
		V element;

		public EdgeListVertex(V element) {
			this.element = element;
		}

		@Override
		public V element() {
			return element;
		}

		// It's useful to have a toString() method that can
		// return details about this object, so you can
		// print the object later and get useful information.
		// This one prints the element
		public String toString() {
			return element.toString();
		}
	}

	/**
	 * Inner class to represent an edge in an edge list graph implementation.
	 *
	 */
	private class EdgeListEdge implements IEdge<E> {
		// reference to a node in the edge list
		IPosition<IEdge<E>> node;

		// element stored in this edge
		E element;

		// the start and end vertices that this edge connects
		EdgeListVertex start, end;

		// constructor to set the three fields
		public EdgeListEdge(EdgeListVertex start, EdgeListVertex end, E element) {
			this.start = start;
			this.end = end;
			this.element = element;
		}

		@Override
		public E element() {
			return element;
		}

		public String toString() {
			return element.toString();
		}
	}

	// vertex list
	private IList<IVertex<V>> vertices;

	// edge list
	private IList<IEdge<E>> edges;

	/**
	 * Constructor
	 */
	public EdgeListGraph() {
		// create new (empty) lists of edges and vertices
		vertices = new DLinkedList<IVertex<V>>();
		edges = new DLinkedList<IEdge<E>>();
	}

	@Override
	public IVertex<V>[] endVertices(IEdge<E> e) {
		// need to cast Edge type to EdgeListEdge
		EdgeListEdge edge = (EdgeListEdge) e;

		// create new array of length 2 that will contain
		// the edge's end vertices
		@SuppressWarnings("unchecked")
		IVertex<V>[] endpoints = new IVertex[2];

		// fill array
		endpoints[0] = edge.start;
		endpoints[1] = edge.end;

		return endpoints;
	}

	@Override
	public IVertex<V> opposite(IVertex<V> v, IEdge<E> e) {
		// find end points of Edge e
		IVertex<V>[] endpoints = endVertices(e);

		// return the end point that is not v
		if (endpoints[0].equals(v)) {
			return endpoints[1];
		} else if (endpoints[1].equals(v)) {
			return endpoints[0];
		}

		// Problem! e is not connected to v.
		throw new RuntimeException("Error: cannot find opposite vertex.");
	}

	@Override
	public boolean areAdjacent(IVertex<V> v, IVertex<V> w) {
		// iterate through all the edges in the graph
		IIterator<IEdge<E>> it = edges.iterator();

		while (it.hasNext()) {
			// must cast Object type to EdgeListEdge type
			EdgeListEdge edge = (EdgeListEdge) it.next();

			// edge connects v -> w (so they are adjacent)
			if ((edge.start.equals(v)) && (edge.end.equals(w)))
				return true;

			// edge connects w -> v (so they are adjacent)
			if ((edge.end.equals(v)) && (edge.start.equals(w)))
				return true;
		}
		// no edge was found that connects v to w.
		return false;
	}

	@Override
	public V replace(IVertex<V> v, V x) {
		EdgeListVertex vertex = (EdgeListVertex) v;
		// store old element that we should return
		V temp = vertex.element;

		// do the replacement
		vertex.element = x;

		// return the old value
		return temp;
	}

	@Override
	public E replace(IEdge<E> e, E x) {
		EdgeListEdge edge = (EdgeListEdge) e;
		E temp = edge.element;
		edge.element = x;
		return temp;
	}

	@Override
	public IVertex<V> insertVertex(V v) {
		// create new vertex
		EdgeListVertex vertex = new EdgeListVertex(v);

		// insert the vertex into the vertex list
		// (returns a reference to the new Node that was created)
		IPosition<IVertex<V>> node = vertices.insertLast(vertex);

		// this reference must be stored in the vertex,
		// to make it easier to remove the vertex later.
		vertex.node = node;

		// return the new vertex that was created
		return vertex;
	}

	@Override
	public IEdge<E> insertEdge(IVertex<V> v, IVertex<V> w, E o) {
		// create new edge object
		EdgeListEdge edge = new EdgeListEdge((EdgeListVertex) v, (EdgeListVertex) w, o);

		// insert into the edge list and store the reference to the node
		// in the edge object
		IPosition<IEdge<E>> n = edges.insertLast(edge);
		edge.node = n;
		return edge;
	}

	@Override
	public V removeVertex(IVertex<V> v) {
		// first find all incident edges and remove those
		IList<IEdge<E>> incidentEdges = new DLinkedList<IEdge<E>>();
		IIterator<IEdge<E>> it = incidentEdges(v);
		while( it.hasNext() )
			incidentEdges.insertLast(it.next());
		
		while (!incidentEdges.isEmpty())
			removeEdge(incidentEdges.remove(incidentEdges.first()));

		// now we can remove the vertex from the vertex list
		EdgeListVertex vertex = (EdgeListVertex) v;
		vertices.remove(vertex.node);

		// return the element of the vertex that was removed
		return vertex.element;
	}

	@Override
	public E removeEdge(IEdge<E> e) {
		// remove edge from edge list and return its element
		EdgeListEdge edge = (EdgeListEdge) e;
		edges.remove(edge.node);
		return edge.element;
	}

	@Override
	public IIterator<IEdge<E>> incidentEdges(IVertex<V> v) {
		// strategy:
		// find all edges that are connected to v and
		// add them to "list".
		// Later, use the iterator() method in List to
		// get an iterator over these edges.
		IList<IEdge<E>> list = new DLinkedList<IEdge<E>>();

		IIterator<IEdge<E>> it = edges.iterator();
		while (it.hasNext()) {
			EdgeListEdge edge = (EdgeListEdge) it.next();
			if (edge.start.equals(v))
				list.insertLast(edge);
			if (edge.end.equals(v))
				list.insertLast(edge);
		}
		return list.iterator();
	}

	@Override
	public IIterator<IVertex<V>> vertices() {
		return vertices.iterator();
	}

	@Override
	public IIterator<IEdge<E>> edges() {
		return edges.iterator();
	}
}
