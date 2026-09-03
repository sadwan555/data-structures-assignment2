import graph.core.IEdge;
import graph.core.IGraph;
import graph.core.IIterator;
import graph.core.IVertex;
import graph.impl.AdjacencyMatrixGraph;

/**
 * This is a file that contains some code to create a graph
 * and test the methods of the adjacency matrix graph implementation.
 *
 * The graph is the graph of airports and the distances between them,
 * similar to the example used in the lectures.
 *
 * This test checks the basic graph methods, and also checks that
 * removing an edge or vertex correctly updates the matrix and edge list.
 */
public class AdjacencyMatrixTest {
   public static void main(String[] args) throws Exception {
      IGraph<String,Integer> g = new AdjacencyMatrixGraph<String,Integer>();

      // create some vertices
      IVertex<String> hnl = g.insertVertex("HNL");
      IVertex<String> lax = g.insertVertex("LAX");
      IVertex<String> sfo = g.insertVertex("SFO");
      IVertex<String> ord = g.insertVertex("ORD");
      IVertex<String> dfw = g.insertVertex("DFW");
      IVertex<String> lga = g.insertVertex("LGA");
      IVertex<String> pvd = g.insertVertex("PVD");
      IVertex<String> mia = g.insertVertex("MIA");

      // create some edges
      IEdge<Integer> hnllax = g.insertEdge(hnl, lax, 2555);
      IEdge<Integer> laxsfo = g.insertEdge(lax, sfo, 337);
      IEdge<Integer> ordsfo = g.insertEdge(ord, sfo, 1843);
      IEdge<Integer> laxord = g.insertEdge(lax, ord, 1743);
      IEdge<Integer> dfwlax = g.insertEdge(dfw, lax, 1233);
      IEdge<Integer> ordpvd = g.insertEdge(ord, pvd, 849);
      IEdge<Integer> dfwlga = g.insertEdge(dfw, lga, 1387);
      IEdge<Integer> dfwmia = g.insertEdge(dfw, mia, 1120);
      IEdge<Integer> lgamia = g.insertEdge(lga, mia, 1099);
      IEdge<Integer> lgapvd = g.insertEdge(lga, pvd, 142);

      // test number of vertices after insertion
      if (countVertices(g) == 8)
         System.out.println("Number of vertices after insertion: correct");
      else
         System.out.println("Number of vertices after insertion: incorrect");

      // test number of edges after insertion
      if (countEdges(g) == 10)
         System.out.println("Number of edges after insertion: correct");
      else
         System.out.println("Number of edges after insertion: incorrect");

      // sample test for areAdjacent
      if (g.areAdjacent(sfo, ord))
         System.out.println("SFO and ORD adjacent: correct");
      else
         System.out.println("SFO and ORD adjacent: incorrect");

      // test areAdjacent in the opposite order
      if (g.areAdjacent(ord, sfo))
         System.out.println("ORD and SFO adjacent: correct");
      else
         System.out.println("ORD and SFO adjacent: incorrect");

      // test two vertices that should not be adjacent
      if (!g.areAdjacent(hnl, mia))
         System.out.println("HNL and MIA not adjacent: correct");
      else
         System.out.println("HNL and MIA not adjacent: incorrect");

      // sample test for endVertices
      IVertex<String>[] ends = g.endVertices(laxord);
      if ((ends[0] == lax && ends[1] == ord) ||
          (ends[1] == lax && ends[0] == ord))
         System.out.println("End vertices of LAX<->ORD: correct");
      else
         System.out.println("End vertices of LAX<->ORD: incorrect");

      // sample test for opposite
      if (g.opposite(pvd, lgapvd) == lga)
         System.out.println("Opposite of PVD along LGA<->PVD: correct");
      else
         System.out.println("Opposite of PVD along LGA<->PVD: incorrect");

      // test opposite in the other direction
      if (g.opposite(lga, lgapvd) == pvd)
         System.out.println("Opposite of LGA along LGA<->PVD: correct");
      else
         System.out.println("Opposite of LGA along LGA<->PVD: incorrect");

      // test vertex element
      String miaElement = mia.element();
      if (miaElement.equals("MIA"))
         System.out.println("Element of MIA: correct");
      else
         System.out.println("Element of MIA: incorrect");

      // test edge element
      int dfwlaxElement = dfwlax.element();
      if (dfwlaxElement == 1233)
         System.out.println("Distance from DFW to LAX: correct");
      else
         System.out.println("Distance from DFW to LAX: incorrect");

      // test incident edges before removal
      if (countIncidentEdges(g, lax) == 4)
         System.out.println("Incident edges of LAX before removal: correct");
      else
         System.out.println("Incident edges of LAX before removal: incorrect");

      if (countIncidentEdges(g, hnl) == 1)
         System.out.println("Incident edges of HNL before removal: correct");
      else
         System.out.println("Incident edges of HNL before removal: incorrect");

      if (countIncidentEdges(g, lga) == 3)
         System.out.println("Incident edges of LGA before removal: correct");
      else
         System.out.println("Incident edges of LGA before removal: incorrect");

      // test replace on a vertex
      String oldVertexElement = g.replace(mia, "MIA-NEW");
      if (oldVertexElement.equals("MIA") && mia.element().equals("MIA-NEW"))
         System.out.println("Replace vertex element: correct");
      else
         System.out.println("Replace vertex element: incorrect");

      // change it back for later tests
      g.replace(mia, "MIA");

      // test replace on an edge
      int oldEdgeElement = g.replace(dfwlax, 1300);
      if (oldEdgeElement == 1233 && dfwlax.element() == 1300)
         System.out.println("Replace edge element: correct");
      else
         System.out.println("Replace edge element: incorrect");

      // change it back for later tests
      g.replace(dfwlax, 1233);

      // test removeEdge
      int removedEdgeElement = g.removeEdge(laxsfo);
      if (removedEdgeElement == 337)
         System.out.println("Remove LAX<->SFO returns correct element: correct");
      else
         System.out.println("Remove LAX<->SFO returns correct element: incorrect");

      // after removing LAX<->SFO, the number of edges should decrease
      if (countEdges(g) == 9)
         System.out.println("Number of edges after removing LAX<->SFO: correct");
      else
         System.out.println("Number of edges after removing LAX<->SFO: incorrect");

      // after removing LAX<->SFO, LAX and SFO should not be adjacent
      if (!g.areAdjacent(lax, sfo))
         System.out.println("LAX and SFO not adjacent after edge removal: correct");
      else
         System.out.println("LAX and SFO not adjacent after edge removal: incorrect");

      // the opposite matrix position should also be cleared
      if (!g.areAdjacent(sfo, lax))
         System.out.println("SFO and LAX not adjacent after edge removal: correct");
      else
         System.out.println("SFO and LAX not adjacent after edge removal: incorrect");

      // after removing LAX<->SFO, the incident edge counts should change
      if (countIncidentEdges(g, lax) == 3)
         System.out.println("Incident edges of LAX after removing LAX<->SFO: correct");
      else
         System.out.println("Incident edges of LAX after removing LAX<->SFO: incorrect");

      if (countIncidentEdges(g, sfo) == 1)
         System.out.println("Incident edges of SFO after removing LAX<->SFO: correct");
      else
         System.out.println("Incident edges of SFO after removing LAX<->SFO: incorrect");

      // test removeVertex
      String removedVertexElement = g.removeVertex(hnl);
      if (removedVertexElement.equals("HNL"))
         System.out.println("Remove HNL returns correct element: correct");
      else
         System.out.println("Remove HNL returns correct element: incorrect");

      // after removing HNL, the number of vertices should decrease
      if (countVertices(g) == 7)
         System.out.println("Number of vertices after removing HNL: correct");
      else
         System.out.println("Number of vertices after removing HNL: incorrect");

      // removing HNL should also remove the HNL<->LAX edge
      if (countEdges(g) == 8)
         System.out.println("Number of edges after removing HNL: correct");
      else
         System.out.println("Number of edges after removing HNL: incorrect");

      // this is the important adjacency-matrix test mentioned in the assignment
      if (countIncidentEdges(g, lax) == 2)
         System.out.println("Incident edges of LAX decrease after removing HNL: correct");
      else
         System.out.println("Incident edges of LAX decrease after removing HNL: incorrect");

      // check that other edges still work after removals
      if (g.areAdjacent(lax, ord))
         System.out.println("LAX and ORD still adjacent: correct");
      else
         System.out.println("LAX and ORD still adjacent: incorrect");

      if (g.areAdjacent(dfw, lax))
         System.out.println("DFW and LAX still adjacent: correct");
      else
         System.out.println("DFW and LAX still adjacent: incorrect");

      if (g.areAdjacent(lga, pvd))
         System.out.println("LGA and PVD still adjacent: correct");
      else
         System.out.println("LGA and PVD still adjacent: incorrect");

      if (!g.areAdjacent(dfw, pvd))
         System.out.println("DFW and PVD not adjacent: correct");
      else
         System.out.println("DFW and PVD not adjacent: incorrect");

      // test that the graph still works after a middle vertex is removed
      String removedMiddleVertex = g.removeVertex(ord);
      if (removedMiddleVertex.equals("ORD"))
         System.out.println("Remove ORD returns correct element: correct");
      else
         System.out.println("Remove ORD returns correct element: incorrect");

      if (countVertices(g) == 6)
         System.out.println("Number of vertices after removing ORD: correct");
      else
         System.out.println("Number of vertices after removing ORD: incorrect");

      if (countEdges(g) == 5)
         System.out.println("Number of edges after removing ORD: correct");
      else
         System.out.println("Number of edges after removing ORD: incorrect");

      if (g.areAdjacent(lga, pvd))
         System.out.println("LGA and PVD still adjacent after removing ORD: correct");
      else
         System.out.println("LGA and PVD still adjacent after removing ORD: incorrect");

      IEdge<Integer> laxmia = g.insertEdge(lax, mia, 2340);
      if (g.areAdjacent(lax, mia) && laxmia.element() == 2340)
         System.out.println("Insert edge after removing ORD: correct");
      else
         System.out.println("Insert edge after removing ORD: incorrect");

      // test expansion of the matrix capacity
      IVertex<String> v1 = g.insertVertex("V1");
      IVertex<String> v2 = g.insertVertex("V2");
      IVertex<String> v3 = g.insertVertex("V3");
      IVertex<String> v4 = g.insertVertex("V4");
      IVertex<String> v5 = g.insertVertex("V5");

      g.insertEdge(v1, v5, 500);
      if (g.areAdjacent(v1, v5))
         System.out.println("Matrix expansion after many vertices: correct");
      else
         System.out.println("Matrix expansion after many vertices: incorrect");

      // print names of all remaining vertices
      System.out.println();
      System.out.println("Remaining vertices:");
      IIterator<IVertex<String>> vit = g.vertices();
      while (vit.hasNext()) {
         IVertex<String> v = vit.next();
         System.out.println(v.element());
      }

      // print labels of all remaining edges
      System.out.println();
      System.out.println("Remaining edge distances:");
      IIterator<IEdge<Integer>> eit = g.edges();
      while (eit.hasNext()) {
         IEdge<Integer> e = eit.next();
         System.out.println(e.element());
      }
   }

   /**
    * Count the number of vertices currently stored in the graph.
    */
   private static int countVertices(IGraph<String,Integer> g) {
      int count = 0;
      IIterator<IVertex<String>> it = g.vertices();
      while (it.hasNext()) {
         it.next();
         count++;
      }
      return count;
   }

   /**
    * Count the number of edges currently stored in the graph.
    */
   private static int countEdges(IGraph<String,Integer> g) {
      int count = 0;
      IIterator<IEdge<Integer>> it = g.edges();
      while (it.hasNext()) {
         it.next();
         count++;
      }
      return count;
   }

   /**
    * Count the number of incident edges of one vertex.
    */
   private static int countIncidentEdges(IGraph<String,Integer> g, IVertex<String> v) {
      int count = 0;
      IIterator<IEdge<Integer>> it = g.incidentEdges(v);
      while (it.hasNext()) {
         it.next();
         count++;
      }
      return count;
   }
}
