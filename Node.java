
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


class Node {
	
	/*
	 * child Nodes of this Node
	 */
	Node SW, SE, NW, NE, parent;
	
	/*
	 * Points possessed by this Node
	 */
	List<Point2D.Double> points;
	
	/*
	 * Records the state of this Node
	 */
	boolean isLeaf;
	
	/*
	 * Locational code of this Node
	 * Based on the position of this Node
	 * in relation to the Nodes around it
	 */
	String code;

	/*
	 * Depth of this node
	 */
	int depth;
	
	/*
	 * Square region that is
	 * represented by this Node
	 */
	Rectangle2D.Double bounds;
	
	/*
	 * Constructor
	 */
	public Node(double xTopLeft, double yTopLeft, double length)
	{
		/*
		 * Initialize the region represented
		 * by this Node. (Based on Java coordinate system)
		 */
		bounds = new Rectangle2D.Double(xTopLeft, yTopLeft, length, length);
		
		/*
		 * Initialize the point List
		 */
		points = new ArrayList<Point2D.Double>();
		
		/*
		 * This Node is initially a leaf,
		 * this will be updated when it is split
		 */
		isLeaf = true;
		
		/*
		 * This will be updated
		 * as soon as its parent is assigned
		 */
		depth = 0;	
		
		/*
		 * Will also be update
		 * upon assignment of this
		 * Node's parent
		 */
		code = "";
	}
	
	/*
	 * Determines if a point falls
	 * in the region represented by the Node
	 */
	public boolean contains(Point2D.Double p)
	{
		return bounds.contains(p);
	}
	
	/*
	 * Splits the Node into 4 child Nodes.
	 * The points of the Node are then
	 * redistributed among the children
	 */
	public void split()
	{
		/*
		 * Initialize the new child Nodes
		 */
		this.SW = new Node(this.bounds.x, this.bounds.y + this.bounds.height/2, this.bounds.height/2);
		this.SE = new Node(this.bounds.x + this.bounds.height/2, this.bounds.y + this.bounds.height/2, this.bounds.height/2);
		this.NW = new Node(this.bounds.x, this.bounds.y, this.bounds.height/2);
		this.NE = new Node(this.bounds.x + this.bounds.height/2, this.bounds.y, this.bounds.height/2);
		
		/*
		 * Set the new child Node parent
		 * to this Node
		 */
		this.SW.parent = this;
		this.SE.parent = this;
		this.NW.parent = this;
		this.NE.parent = this;
		
		/*
		 * The child Nodes are
		 * 1 level deeper than the parent
		 */
		this.SW.depth = this.depth + 1;
		this.SE.depth = this.depth + 1;
		this.NW.depth = this.depth + 1;
		this.NE.depth = this.depth + 1;
		
		/*
		 * Code is based on location.
		 * 
		 *     |
		 * 00  |  01
		 *-----+-----
		 * 10  |  11
		 *     |
		 *     
		 * Node's are recursively defined, so their
		 * code can be defined as the code of their parent
		 * concatenated with the their code in relation to
		 * their parent.    
		 */
		this.SW.code = this.code + "10";
		this.SE.code = this.code + "11";
		this.NW.code = this.code + "00";
		this.NE.code = this.code + "01";
		
		/*
		 * This Node has children
		 * It is no longer a leaf.
		 */
		this.isLeaf = false;	
	}
	
	/*
	 * Given a rectangle that may intersect
	 * or contain the region of this Node, return
	 * all points that are in this Node that fall
	 * in the given rectangle.
	 */
	public ArrayList<Point2D.Double> findAllPointsIn(Rectangle r)
	{
		//Points that fall in the given rectangle
		ArrayList<Point2D.Double> result = new ArrayList<Point2D.Double>();
		
		//Check each of the points in this Node
		for(Point2D.Double p : this.points)
		{
			//The point is in this Node
			//and it falls inside the given
			//rectangle
			if(r.contains(p))
			{
				result.add(p);
			}
		}
		
		return result;
	}
}
