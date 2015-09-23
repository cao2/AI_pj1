import java.util.*;

public class Node { 
	
    private String name; 
    private Map<Node,Integer> child=new HashMap<Node,Integer>(); 
    private int[] cord;
    
    //constructor
    public Node(String name){ 
        this.name=name;
        cord=new int[2];
    } 
    //coordinate
    public void setCord(int x, int y){
    	cord[0]=x;
    	cord[1]=y;
    }
    public int[] getCord(){
    	return cord;
    }
   //name 
   public String getName() { 
        return name; 
    } 
    public void setName(String name) { 
        this.name = name; 
    } 
    //connection
    public Map<Node, Integer> getChild() { 
        return child; 
    } 
    public void setChild(Map<Node,Integer> child) { 
        this.child = child; 
    } 
    public String toString(){
    	String conn="";
    	for(Node tmp:child.keySet())
    		conn+=" "+tmp.getName();
    	return name+" "+cord[0]+","+cord[1]+"conn:"+conn;
    }
}
