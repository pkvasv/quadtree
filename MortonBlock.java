
public class MortonBlock {
	
	int databaseIndexOfMyNode;
	String locationalCode;
	
	public MortonBlock(){}
	
	public MortonBlock(int databaseIndexOfMyNode, int depth, String locationalCode)
	{
		this.databaseIndexOfMyNode = databaseIndexOfMyNode;
		this.locationalCode = locationalCode + Integer.toBinaryString(depth);
		
	}
	
	
	
}
