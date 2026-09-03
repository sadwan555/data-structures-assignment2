import graph.core.IEdge;
import graph.core.IGraph;
import graph.core.IIterator;
import graph.core.IVertex;
import graph.impl.EdgeListGraph;
/**
 * This is a file that contains some code to create a 
 * graph and test some of its methods.
 *
 * The graph is the graph of airports and the distances
 * between them that we have seen in the lectures.
 * 
 * This is an example of testing some of the methods
 * of the edge list graph implementation. 
 * 
 * You should add some more tests to test the other
 * methods that a graph implementation contains.
 */
public class EdgeListTest {
   public static void main( String[] args ) throws Exception {
      IGraph<String,Integer> g = new EdgeListGraph<String,Integer>();
      // create some vertices
      IVertex<String> hnl = g.insertVertex( "HNL" );
      IVertex<String> lax = g.insertVertex( "LAX" );
      IVertex<String> sfo = g.insertVertex( "SFO" );
      IVertex<String> ord = g.insertVertex( "ORD" );
      IVertex<String> dfw = g.insertVertex( "DFW" );
      IVertex<String> lga = g.insertVertex( "LGA" );
      IVertex<String> pvd = g.insertVertex( "PVD" );
      IVertex<String> mia = g.insertVertex( "MIA" );

      // create some edges
      IEdge<Integer> hnllax = g.insertEdge( hnl, lax, 2555 );
      IEdge<Integer> laxsfo = g.insertEdge( lax, sfo, 337 );
      IEdge<Integer> ordsfo = g.insertEdge( ord, sfo, 1843 );
      IEdge<Integer> laxord = g.insertEdge( lax, ord, 1743 );
      IEdge<Integer> dfwlax = g.insertEdge( dfw, lax, 1233 );
      IEdge<Integer> ordpvd = g.insertEdge( ord, pvd, 849 );
      IEdge<Integer> dfwlga = g.insertEdge( dfw, lga, 1387 );
      IEdge<Integer> dfwmia = g.insertEdge( dfw, mia, 1120 );
      IEdge<Integer> lgamia = g.insertEdge( lga, mia, 1099 );
      IEdge<Integer> lgapvd = g.insertEdge( lga, pvd, 142 );
      
      // sample test for areAdjacent
      if ( g.areAdjacent( sfo,  ord ) )
         System.out.println( "SFO and ORD adjacent: correct" );
      else
         System.out.println( "SFO and ORD adjacent: incorrect" );
      
      // sample test for endVertices
      IVertex<String>[] ends = g.endVertices( laxord );
      if ( ( ends[0] == lax && ends[1] == ord ) ||
           ( ends[1] == lax && ends[0] == ord ) )
         System.out.println( "End vertices of LAX<->ORD: correct" );
      else
         System.out.println( "End vertices of LAX<->ORD: incorrect" );
      
      // sample test for opposite
      if ( g.opposite( pvd, lgapvd ) == lga )
         System.out.println( "Opposite of PVD along LGA<->PVD: correct" );
      else
         System.out.println( "Opposite of PVD along LGA<->PVD: incorrect" );
      
      // example of getting an object from the graph 
      String miaElement = mia.element();
      System.out.println( "Element of MIA is: " + miaElement );
      
      // the edge labels were set as type int.
      int dfwlaxElement = dfwlax.element();
      System.out.println( "Distance from DFW to LAX is: " + dfwlaxElement );
      
      // print names of all vertices
      IIterator<IVertex<String>> it = g.vertices();
      while( it.hasNext() ) {
         // here I must cast also, since it.next() returns an Object
         IVertex<String> v = it.next();
         System.out.println( v.element() );
      }
   }
}
