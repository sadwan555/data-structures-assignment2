package graph.impl;

import graph.core.IEdge;
import graph.core.IGraph;
import graph.core.IIterator;
import graph.core.IList;
import graph.core.IPosition;
import graph.core.IVertex;
import graph.util.DLinkedList;

public class AdjacencyListGraph<V,E> implements IGraph<V,E> {
    /**
     * Inner class used to store a vertex in an adjacency list graph.
     * Each vertex stores its element and a list of all edges incident on it.
     */
    private class AdjacencyListVertex implements IVertex<V> {
        // position of this vertex in the global vertex list
        IPosition<IVertex<V>> node;

        // element stored in this vertex
        V element;

        // adjacency list: all edges connected to this vertex
        IList<IEdge<E>> incidentEdges;

        public AdjacencyListVertex(V element) {
            this.element = element;
            this.incidentEdges = new DLinkedList<IEdge<E>>();
        }

        @Override
        public V element() {
            return element;
        }

        // Print the element stored in this vertex.
        public String toString() {
            return element.toString();
        }
    }

    /**
     * Inner class used to store an edge in an adjacency list graph.
     * Each edge stores its endpoints and its positions in the global edge list
     * and in the two incident edge lists.
     */
    private class AdjacencyListEdge implements IEdge<E> {
        // position of this edge in the global edge list
        IPosition<IEdge<E>> node;

        // position of this edge in the start vertex's adjacency list
        IPosition<IEdge<E>> startNode;

        // position of this edge in the end vertex's adjacency list
        IPosition<IEdge<E>> endNode;

        // element stored in this edge
        E element;

        // the two endpoint vertices of this edge
        AdjacencyListVertex start, end;

        public AdjacencyListEdge(AdjacencyListVertex start, AdjacencyListVertex end, E element) {
            this.start = start;
            this.end = end;
            this.element = element;
        }

        @Override
        public E element() {
            return element;
        }

        // Print the element stored in this edge.
        public String toString() {
            return element.toString();
        }
    }

    // global list of all vertices in the graph
    private IList<IVertex<V>> vertices;

    // global list of all edges in the graph
    private IList<IEdge<E>> edges;

    /**
     * Create an empty adjacency list graph.
     */
    public AdjacencyListGraph() {
        // initialise the global vertex and edge lists
        vertices = new DLinkedList<IVertex<V>>();
        edges = new DLinkedList<IEdge<E>>();
    }

    @Override
    public IVertex<V>[] endVertices(IEdge<E> e) {
        // cast to the inner edge type so its endpoint fields can be accessed
        AdjacencyListEdge edge = (AdjacencyListEdge) e;

        // create an array to store the two endpoints of the edge
        @SuppressWarnings("unchecked")
        IVertex<V>[] endpoints = new IVertex[2];

        // store the start and end vertices in the result array
        endpoints[0] = edge.start;
        endpoints[1] = edge.end;

        return endpoints;
    }
    @Override
    public IVertex<V> opposite(IVertex<V> v, IEdge<E> e) {
        // get the two endpoints of this edge
        IVertex<V>[] endpoints = endVertices(e);

        // return the endpoint on the other side of v
        if (endpoints[0].equals(v)) {
            return endpoints[1];
        } else if (endpoints[1].equals(v)) {
            return endpoints[0];
        }

        // the given edge is not incident on the given vertex
        throw new RuntimeException("Error: cannot find opposite vertex.");
    }

    @Override
    public boolean areAdjacent(IVertex<V> v, IVertex<V> w) {
        AdjacencyListVertex vertexV = (AdjacencyListVertex) v;
        AdjacencyListVertex vertexW = (AdjacencyListVertex) w;

        IIterator<IEdge<E>> it;
        // only scan the shorter adjacency list for better efficiency
        if (vertexV.incidentEdges.size() <= vertexW.incidentEdges.size()) {
            it = vertexV.incidentEdges.iterator();
        } else {
            it = vertexW.incidentEdges.iterator();
        }
        // check whether any incident edge connects the two vertices
        while (it.hasNext()) {
            AdjacencyListEdge edge = (AdjacencyListEdge) it.next();
            if ((edge.start.equals(v) && edge.end.equals(w)) ||
                    (edge.start.equals(w) && edge.end.equals(v))) {
                return true;
            }
        }
        return false;
    }
    @Override
    public V replace(IVertex<V> v, V x) {
        AdjacencyListVertex vertex = (AdjacencyListVertex) v;
        // keep the old vertex element so it can be returned
        V temp = vertex.element;
        // replace the element stored in the vertex
        vertex.element = x;
        // return the element that was replaced
        return temp;
    }

    @Override
    public E replace(IEdge<E> e, E x) {
        AdjacencyListEdge edge = (AdjacencyListEdge) e;
        // keep the old edge element, then replace it with the new one
        E temp = edge.element;
        edge.element = x;
        return temp;
    }

    @Override
    public IVertex<V> insertVertex(V v) {
        // create a new vertex object for the given element
        AdjacencyListVertex vertex = new AdjacencyListVertex(v);
        // add the vertex to the global vertex list
        IPosition<IVertex<V>> node = vertices.insertLast(vertex);
        // store the position so the vertex can be removed in O(1) time later
        vertex.node = node;
        // return the new vertex to the user of the graph
        return vertex;
    }

    @Override
    public IEdge<E> insertEdge(IVertex<V> v, IVertex<V> w, E o) {
        // cast the endpoints to the inner vertex type used by this graph
        AdjacencyListVertex start = (AdjacencyListVertex) v;
        AdjacencyListVertex end = (AdjacencyListVertex) w;
        // create the new edge object
        AdjacencyListEdge edge = new AdjacencyListEdge(start, end, o);
        // store the edge in the global edge list and in both endpoint adjacency lists
        edge.node = edges.insertLast(edge);
        edge.startNode = start.incidentEdges.insertLast(edge);
        edge.endNode = end.incidentEdges.insertLast(edge);
        return edge;
    }
    @Override
    public V removeVertex(IVertex<V> v) {
        // copy all incident edges first, because removing them changes the adjacency list
        IList<IEdge<E>> incidentEdges = new DLinkedList<IEdge<E>>();
        IIterator<IEdge<E>> it = incidentEdges(v);
        while( it.hasNext() )
            incidentEdges.insertLast(it.next());

        while (!incidentEdges.isEmpty())
            removeEdge(incidentEdges.remove(incidentEdges.first()));

        // after all connected edges are removed, remove the vertex itself
        AdjacencyListVertex vertex = (AdjacencyListVertex) v;
        vertices.remove(vertex.node);

        // return the element that was stored in the removed vertex
        return vertex.element;
    }

    @Override
    public E removeEdge(IEdge<E> e) {
        // remove the edge from every list that stores it
        AdjacencyListEdge edge = (AdjacencyListEdge) e;

        edges.remove(edge.node);
        edge.start.incidentEdges.remove(edge.startNode);
        edge.end.incidentEdges.remove(edge.endNode);

        return edge.element;
    }

    @Override
    public IIterator<IEdge<E>> incidentEdges(IVertex<V> v) {
        // in an adjacency list graph, the vertex already stores its incident edges
        return ((AdjacencyListVertex) v).incidentEdges.iterator();
    }

    @Override
    public IIterator<IVertex<V>> vertices() {
        // return an iterator over all vertices in the graph
        return vertices.iterator();
    }

    @Override
    public IIterator<IEdge<E>> edges() {
        // return an iterator over all edges in the graph
        return edges.iterator();
    }
}
