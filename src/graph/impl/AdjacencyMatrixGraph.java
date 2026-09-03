package graph.impl;

import graph.core.IEdge;
import graph.core.IGraph;
import graph.core.IIterator;
import graph.core.IList;
import graph.core.IPosition;
import graph.core.IVertex;
import graph.util.DLinkedList;

public class AdjacencyMatrixGraph<V,E> implements IGraph<V,E> {
    /**
     * Internal vertex representation used by the adjacency matrix graph.
     * Each vertex stores its element, its position in the global vertex list,
     * and its row/column index in the matrix.
     */
    private class AdjacencyMatrixVertex implements IVertex<V> {
        // Position of this vertex in the global vertex list, used for O(1) removal from that list.
        IPosition<IVertex<V>> node;

        // Row and column index used to access this vertex in the adjacency matrix.
        int index;

        // User data stored at this vertex.
        V element;

        public AdjacencyMatrixVertex(V element) {
            this.element = element;
        }

        @Override
        public V element() {
            return element;
        }

        public String toString() {
            return element.toString();
        }
    }

    /**
     * Internal edge representation used by the adjacency matrix graph.
     * Each edge stores its element, its position in the global edge list,
     * and direct references to its two endpoint vertices.
     */
    private class AdjacencyMatrixEdge implements IEdge<E> {
        // Position of this edge in the global edge list, used for O(1) removal from that list.
        IPosition<IEdge<E>> node;

        // User data stored at this edge, such as a distance or weight.
        E element;

        // Endpoint vertices connected by this edge. For this undirected graph, the order is not significant.
        AdjacencyMatrixVertex start, end;

        public AdjacencyMatrixEdge(AdjacencyMatrixVertex start, AdjacencyMatrixVertex end, E element) {
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

    // Global list containing all vertices currently in the graph.
    private IList<IVertex<V>> vertices;

    // Global list containing all edges currently in the graph.
    private IList<IEdge<E>> edges;

    // Adjacency matrix. matrix[i][j] stores the edge between vertices with indices i and j,
    // or null if those vertices are not adjacent. Since the graph is undirected, the matrix is symmetric.
    private IEdge<E>[][] matrix;

    // Current allocated width and height of the matrix. The matrix grows when vertexCount reaches this value.
    private int capacity;

    // Number of active vertices in the graph. Valid matrix indices are from 0 to vertexCount - 1.
    private int vertexCount;

    /**
     * Constructs an empty adjacency matrix graph with an initial matrix capacity.
     */
    public AdjacencyMatrixGraph() {
        vertices = new DLinkedList<IVertex<V>>();
        edges = new DLinkedList<IEdge<E>>();

        capacity = 10;
        vertexCount = 0;

        @SuppressWarnings("unchecked")
        IEdge<E>[][] temp = new IEdge[capacity][capacity];
        matrix = temp;
    }

    /**
     * Returns the two endpoint vertices of the given edge.
     */
    @Override
    public IVertex<V>[] endVertices(IEdge<E> e) {
        AdjacencyMatrixEdge edge = (AdjacencyMatrixEdge) e;

        @SuppressWarnings("unchecked")
        IVertex<V>[] endpoints = new IVertex[2];

        endpoints[0] = edge.start;
        endpoints[1] = edge.end;

        return endpoints;
    }

    /**
     * Returns the endpoint of edge e that is opposite to vertex v.
     * Throws an exception if v is not one of the endpoints of e.
     */
    @Override
    public IVertex<V> opposite(IVertex<V> v, IEdge<E> e) {
        IVertex<V>[] endpoints = endVertices(e);

        if (endpoints[0].equals(v)) {
            return endpoints[1];
        } else if (endpoints[1].equals(v)) {
            return endpoints[0];
        }

        throw new RuntimeException("Error: cannot find opposite vertex.");
    }

    /**
     * Checks whether two vertices are adjacent by looking up one matrix cell.
     */
    @Override
    public boolean areAdjacent(IVertex<V> v, IVertex<V> w) {
        AdjacencyMatrixVertex vertexV = (AdjacencyMatrixVertex) v;
        AdjacencyMatrixVertex vertexW = (AdjacencyMatrixVertex) w;

        return matrix[vertexV.index][vertexW.index] != null;
    }

    /**
     * Replaces the element stored at a vertex and returns the old element.
     */
    @Override
    public V replace(IVertex<V> v, V x) {
        AdjacencyMatrixVertex vertex = (AdjacencyMatrixVertex) v;
        V temp = vertex.element;
        vertex.element = x;
        return temp;
    }

    /**
     * Replaces the element stored at an edge and returns the old element.
     */
    @Override
    public E replace(IEdge<E> e, E x) {
        AdjacencyMatrixEdge edge = (AdjacencyMatrixEdge) e;
        E temp = edge.element;
        edge.element = x;
        return temp;
    }

    /**
     * Inserts a new vertex, assigns it the next available matrix index,
     * and expands the matrix first if the current capacity is full.
     */
    @Override
    public IVertex<V> insertVertex(V v) {
        if (vertexCount == capacity) {
            expandMatrix();
        }

        AdjacencyMatrixVertex vertex = new AdjacencyMatrixVertex(v);

        vertex.index = vertexCount;
        vertexCount++;

        vertex.node = vertices.insertLast(vertex);

        return vertex;
    }

    /**
     * Inserts an undirected edge between two vertices.
     * The edge is stored in the global edge list and in both symmetric matrix cells.
     */
    @Override
    public IEdge<E> insertEdge(IVertex<V> v, IVertex<V> w, E o) {
        AdjacencyMatrixVertex vertexV = (AdjacencyMatrixVertex) v;
        AdjacencyMatrixVertex vertexW = (AdjacencyMatrixVertex) w;

        AdjacencyMatrixEdge edge = new AdjacencyMatrixEdge(vertexV, vertexW, o);
        edge.node = edges.insertLast(edge);

        matrix[vertexV.index][vertexW.index] = edge;
        matrix[vertexW.index][vertexV.index] = edge;

        return edge;
    }

    /**
     * Removes a vertex and all of its incident edges.
     * To avoid leaving an empty index, the last indexed vertex is moved into the removed vertex's index.
     */
    @Override
    public V removeVertex(IVertex<V> v) {
        // Save the index to be removed and the current last active matrix index.
        AdjacencyMatrixVertex vertex = (AdjacencyMatrixVertex) v;

        int removedIndex = vertex.index;
        int lastIndex = vertexCount - 1;

        // Copy incident edges into a temporary list before removing them.
        // This avoids modifying the matrix while iterating over incident edges.
        IList<IEdge<E>> edgesToRemove = new DLinkedList<IEdge<E>>();
        IIterator<IEdge<E>> it = incidentEdges(vertex);
        while (it.hasNext()) {
            edgesToRemove.insertLast(it.next());
        }

        // Remove each incident edge using removeEdge so both the edge list and matrix are updated.
        while (!edgesToRemove.isEmpty()) {
            removeEdge(edgesToRemove.remove(edgesToRemove.first()));
        }

        // Remove the vertex from the global vertex list.
        vertices.remove(vertex.node);

        // If the removed vertex was not the last active index, move the last indexed vertex into the gap.
        if (removedIndex != lastIndex) {
            AdjacencyMatrixVertex movedVertex = null;
            IIterator<IVertex<V>> vertexIterator = vertices.iterator();
            while (vertexIterator.hasNext()) {
                AdjacencyMatrixVertex current = (AdjacencyMatrixVertex) vertexIterator.next();
                if (current.index == lastIndex) {
                    movedVertex = current;
                }
            }

            // Move the last row and last column into the removed row and column.
            for (int i = 0; i < vertexCount; i++) {
                matrix[removedIndex][i] = matrix[lastIndex][i];
                matrix[i][removedIndex] = matrix[i][lastIndex];
            }

            // Update the moved vertex so future matrix lookups use its new index.
            if (movedVertex != null) {
                movedVertex.index = removedIndex;
            }
        }

        // Clear the old last row and column because that index will no longer be active.
        for (int i = 0; i < vertexCount; i++) {
            matrix[lastIndex][i] = null;
            matrix[i][lastIndex] = null;
        }

        vertexCount--;

        return vertex.element;
    }

    /**
     * Removes an edge from the matrix and from the global edge list.
     */
    @Override
    public E removeEdge(IEdge<E> e) {
        AdjacencyMatrixEdge edge = (AdjacencyMatrixEdge) e;
        AdjacencyMatrixVertex start = edge.start;
        AdjacencyMatrixVertex end = edge.end;

        matrix[start.index][end.index] = null;
        matrix[end.index][start.index] = null;

        edges.remove(edge.node);
        return edge.element;
    }

    /**
     * Returns all edges incident on a vertex by scanning the vertex's matrix row.
     */
    @Override
    public IIterator<IEdge<E>> incidentEdges(IVertex<V> v) {
        IList<IEdge<E>> list = new DLinkedList<IEdge<E>>();
        AdjacencyMatrixVertex vertexV = (AdjacencyMatrixVertex) v;

        for (int i = 0; i < vertexCount; i++) {
            if (matrix[vertexV.index][i] != null) {
                list.insertLast(matrix[vertexV.index][i]);
            }
        }

        return list.iterator();
    }

    /**
     * Returns an iterator over all vertices currently in the graph.
     */
    @Override
    public IIterator<IVertex<V>> vertices() {
        return vertices.iterator();
    }

    /**
     * Returns an iterator over all edges currently in the graph.
     */
    @Override
    public IIterator<IEdge<E>> edges() {
        return edges.iterator();
    }

    /**
     * Doubles the matrix capacity and copies all existing edge references
     * into the same index positions in the new matrix.
     */
    @SuppressWarnings("unchecked")
    private void expandMatrix() {
        int newCapacity = capacity * 2;
        IEdge<E>[][] newMatrix = new IEdge[newCapacity][newCapacity];

        for (int i = 0; i < capacity; i++) {
            for (int j = 0; j < capacity; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }

        matrix = newMatrix;
        capacity = newCapacity;
    }
}
