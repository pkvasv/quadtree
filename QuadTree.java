import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


public class QuadTree {
	/*
	 * Root node of this tree
	 * encompasses the whole region
	 */
	Node root;
	
	/*
	 * List of all 
	 * points in the region
	 */
	List<Point2D.Double> allPoints;
	
	/*
	 * Maximum number of points 
	 * that may exist in a Node
	 * before it must be split
	 * into child nodes
	 */
	final int MAXPTS = 1;
	
	/*
	 * B+ Tree to maintain
	 * the leaf nodes of this
	 * tree. The Morton number
	 * of each leaf node is used
	 * as its key for indexing.
	 */
	BPlusTree<Integer, MortonBlock> b;
	
	/*
	 * ArrayList of Leaf
	 * nodes
	 */
	ArrayList<Node> nodes;
	
	/*
	 * The depth of
	 * this QuadTree. It is
	 * the depth of the deepest
	 * Node. The depth of the root 
	 * is set at 0.
	 */
	int maxDepth;
	
	/*
	 * Constructor
	 */
	public QuadTree(Node root, List<Point2D.Double> allPoints, BPlusTree<Integer, MortonBlock> b)
	{
		/*
		 * Intialized to 0,
		 * only the root exists
		 */
		maxDepth = 0;
		
		/*
		 * Initialize root and
		 * list of leaf nodes
		 * and B+ Tree
		 */
		this.root = root;
		
		nodes = new ArrayList<Node>();
		
		this.b = b;
		
		/*
		 * Intialize the set
		 * of all points
		 */
		this.allPoints = allPoints;
		
		/*
		 * Root has no parent
		 */
		root.parent = null;
		
		/*
		 * Insert the points 
		 * into the QuadTree,
		 * splitting into sub
		 * children as necessary
		 */
		for(Point2D.Double point : this.allPoints) {insert(point);}
	}
	
	/*
	 * Main insert method. Begins
	 * insertion at the root.
	 */
	public void insert(Point2D.Double point)
	{
		insertHelper(root, point);
	}
	
	/*
	 * Inserts a point, starting
	 * at a given Node. Will split the
	 * Node if it contains too many points
	 */
	public boolean insertHelper(Node n, Point2D.Double point)
	{
		//Check to make sure
		//point falls in the node
		if(!n.contains(point))
		{
			return false;
		}
		
		//Node is not full
		//It can accommodate
		//another point
		if(n.points.size() < MAXPTS)
		{
			
			//Add the point to
			//the existing list of points
			n.points.add(point);
			
			//Update depth of the
			//whole QuadTree
			if(n.depth > this.maxDepth)
			{
				maxDepth = n.depth;
			}
			
			//Insertion was successful
			return true;
		}
		
		//Check if this
		//Node has children.
		if(n.NE == null)
		{
			//This node does not have
			//children, and can not 
			//accomodate any more points
			//The node is split into
			//its children
			n.split();
			
			//The points of this Node must
			//be distributed among its children
			for(Point2D.Double p : n.points)
			{
				insertHelper(n, p);
			}
		}
		
		//Successfully inserted point
		//in the NE childNode of this node
		if(insertHelper(n.NE, point))
		{
			return true;
		}
		
		//Successfully inserted point
		//in the NW childNode of this node
		else if(insertHelper(n.NW, point))
		{
			return true;
		}
		
		//Successfully inserted point
		//in the SE childNode of this node
		else if(insertHelper(n.SE, point))
		{
			return true;
		}
		
		//Successfully inserted point
		//in the SW childNode of this node
		else if(insertHelper(n.SW, point))
		{
			return true;
		}
		
		//Insertion was
		//not successful 
		return false;			
	}
	
	/*
	 * Prints the contents of this QuadTree
	 */
	public void print(Node n)
	{
		//Only information
		//about leaves is printed
		if(n.isLeaf)
		{
			System.out.println("Depth: " + n.depth);
			
			System.out.println("Code: " + n.code);
			
			for(Point2D.Double p : n.points)
			{
				System.out.println(p.toString());
			}
			
			
		}
		
		//This Node is not
		//a leaf, try its
		//children
		else
		{
			print(n.NE);
			
			print(n.NW);
			
			print(n.SE);
			
			print(n.SW);
		}
	}
	
	/*
	 * UNFINISHED
	 * 
	 * 
	public void decompose(Node n)
	{
		if(n.isLeaf)
		{
			
			if(n.points != null && n.points.size() > 0)
			{
				nodes.add(n);
				
				MortonBlock mb = new MortonBlock(nodes.size() - 1, n.depth, n.code);
				this.b.insert(Integer.parseInt(mb.locationalCode, 2), mb);
				//System.out.println("inserted to btree");
				System.out.println(mb.locationalCode);
				
			}
			
			}
		else
		{
			
			decompose(n.NE);
			decompose(n.NW);
			decompose(n.SE);
			decompose(n.SW);
		}
	}
	
	public ArrayList<Point2D.Double> query2D(Node n, Rectangle window)
	{
		ArrayList<Point2D.Double> results = new ArrayList<Point2D.Double>();
		
		int min = interleave((int)window.x, (int)window.y, this.maxDepth);
		System.out.println("min :" + min);
		//int max = (int) (Integer.parseInt(Integer.toBinaryString(interleave((int)(window.x + window.width), (int)(window.y + window.height))) + Integer.toBinaryString(this.maxDepth), 2) + Math.pow(2, 2 *(this.maxDepth)));
		
		int max = Integer.parseInt(Integer.toBinaryString(interleave((int)(window.x + window.width), (int)(window.y + window.height), this.maxDepth * 2)) + Integer.toBinaryString(this.maxDepth), 2);
		
		
		System.out.println("max " + max);
		
		
		**
		 * 	FIX 
		 * BELOW
		 * FROM 
		 * I START INDEX
		 * TO 
		 * I 
		 * END 
		 * INDEX
		 *
		for(int i = min; i <=max ; i++)
		{
			
			MortonBlock temp = this.b.find(i);
			
		
			if(temp != null)
			{
				results.addAll(nodes.get(temp.databaseIndexOfMyNode).findAllPointsIn(window));
			}
		}
		
		return results;
	}
	
	*
	**/
	
	//Interleave the y and
	//x coordinate bits of a point.
	//Pads the returned number to the 
	//given length
	public static int interleave(int xCoord, int yCoord, int length)
	{
		//Convert x to its bits
		int x = Integer.parseInt(Integer.toBinaryString(xCoord), 2);
		
		//Convert y to its bits
        int y = Integer.parseInt(Integer.toBinaryString(yCoord), 2);
        
        //This is the result
        int z = 0;
 
        //For each bit in each coordinate
        for (int i = 0; i < Integer.SIZE; i++) 
        {
        	//bitwise operations on the x
        	int x_masked_i = (x & (1 << i));
        	
        	//bitwise operations on the y
            int y_masked_i = (y & (1 << i));
 
            //join the x and the y
            //merge them into z
            z |= (x_masked_i << i);
            z |= (y_masked_i << (i + 1));
        }
        
        //This string will be padded
        String zString = Integer.toBinaryString(z);
        
        if(zString.length() < length)
        {
        	for(int j = zString.length(); j <= length; j++)
        	{
        		zString = zString + "0";
        	}
        }
        
        //Convert the padded string
        //back to an integer value
        z = Integer.parseInt(zString, 2);
        
        //Return result in base 10
        return z;
	}
	
	
}
