
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;


public class Tester {

	public static void main(String[] args)
	{
		
		
		Random r = new Random();
		ArrayList<Point2D.Double> p = new ArrayList<Point2D.Double>();
		Node root = new Node(0,0,4);
		
		System.out.println("All Points");
		for(int i = 0; i < 7; i++)
		{
			p.add(new Point2D.Double(r.nextDouble() * 4, r.nextDouble() * 4));
			System.out.println(p.get(i).toString());
			
		}
		System.out.println("\n");
		
		QuadTree q = new QuadTree(root, p, new BPlusTree<Integer, MortonBlock>(4));
		
		q.print(root);
		System.out.println("\n");
		
		/*
		q.decompose(q.root);
		
		System.out.println("done decomposing\n");
		
		System.out.println("Max Depth: " + q.maxDepth + "\n");
		
		ArrayList<Point2D.Double> results = q.query2D(root, new Rectangle(1,1,3,3));
		
		System.out.println("Points in Window: ");
		
		for(Point2D.Double point : results)
		{
			System.out.println(point.toString());
		}
		*/
	}
}
