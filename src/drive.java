import java.io.*;
import java.util.*;

public class drive {
//main
	public static void main(String[] args) throws IOException{

		ArrayList<Node> allNode=new ArrayList<Node> ();
		ArrayList<String> allName=new ArrayList<String> ();
		Node start=new Node("nonstart");
		Node target=new Node("nontarget");
		String locsamp="locsamp.txt";
		String connsamp="connsamp.txt";
		
		
		//get the input from users
		Scanner inp=new Scanner(System.in);
		System.out.print("sample location file paht:(for example '/local/locsamp.txt') ");
		locsamp=inp.nextLine();
		System.out.print("sample connection file paht:(for example '/local/connsamp.txt') ");
		connsamp=inp.nextLine();
			
		
		//read file: locsamp
		BufferedReader br = new BufferedReader(new FileReader(locsamp));
		try {
		    String line = br.readLine();
		    while (line != null) {
		        //check if end of the file
		        if (line.equals("END"))
		        	break;
		        //get the content of the file and create corresponding node with it's coordinates
		        String[] tmp= line.split("\\s");
		        Node pt=new Node(tmp[0]);
		        pt.setCord(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
		        //allNode store all the Node 
		        //allName stores only name for easy and faster searching
		        allNode.add(pt);
		        allName.add(tmp[0]);
		        line = br.readLine();

		    }
		} finally {
		    br.close();
		}
		/**
		for(String tmp:allName)
			System.out.println(tmp);
		System.out.println("***************");
		for(Node tmp:allNode)
			System.out.println(tmp.getName());
			**/
		
		//ask for initial point and target point	
		while(true){
			System.out.print("initial point:");
			String stat=inp.nextLine();
			if(allName.contains(stat)){
				start = allNode.get(allName.indexOf(stat));
				break;
				}
			System.out.println("point not exist");
		}
		while(true){
			System.out.print("target point:");
			String tgt=inp.nextLine();
			if(allName.contains(tgt)){
				target = allNode.get(allName.indexOf(tgt));
				break;}
			System.out.println("point not exist");
		}//end asking 
		
		
		//read file: connsamp
		 br = new BufferedReader(new FileReader(connsamp));
		try {
		    String line = br.readLine();
		    while (line != null) {
		        //check if end of the file
		        if (line.equals("END"))
		        	break;
		        String[] tmp= line.split("\\s");
		        ArrayList<Node> child=new ArrayList<Node>(); 
		        //add child node in its child list
		         for(int i=0;i<Integer.parseInt(tmp[1]);i++){
		        	 int ind=allName.indexOf(tmp[2+i]);
		        	 child.add(allNode.get(ind));
		         }
		        String name=tmp[0];
		        int ind=allName.indexOf(name);
		        //add its heuristic to Dist using setDist
		        double dist=dis(allNode.get(ind).getCord(),target.getCord());
		        allNode.get(ind).setChild(child);
		        allNode.get(ind).setDist(dist);
		        line = br.readLine();

		    }
		} finally {
		    br.close();
		}
		
		while(true){
		
		//start of A* algorithm
		//exist stores all seen node
		ArrayList<String> exist=new ArrayList<String> ();
		//open stores all the unexplored node
		//open has distance gone so far as key, and the point as key's content
		HashMap<Double, Node> open=new HashMap<Double, Node> ();
		open.put(0.0, start);
		exist.add(start.getName());
		ArrayList<String> inpath=new ArrayList<String> ();
		//insert the path for start point
		inpath.add(start.getName());
		start.setPath(inpath);
		//flag_find is to determine if a soultion is found
		int flag_find=0;
		while(open.size()!=0){
			//find the best in open
			//heuristic is stored in node 
			
			//min is the smallest estimation
			//minkey is the distance done so far
			double min=999999999;//bigest number possbile for double
			double minkey=0;
			for(double x:open.keySet())
				if(x+dis(open.get(x).getCord(),target.getCord())<(min)){
					min=x+dis(open.get(x).getCord(),target.getCord());
					minkey=x;
				}
			
			Node tmp=open.get(minkey);
			System.out.println("**************************choose "+tmp.getName()+" : "+minkey);
			
			
			//if it's the target
			if(tmp.getName().equals(target.getName())){
				flag_find=1;
				System.out.print("reached the target by path: ");
				int just=0;
				for(String meow:tmp.getPath())
					{
					if (just==0)
						System.out.print(meow);
					else if(just==tmp.getPath().size()-1){
						System.out.println(" to "+meow);
					}
					else{
						System.out.print(" to "+meow+", "+meow);
					}
					just++;
					}
				break;
			}
			else{
				exist.add(tmp.getName());
				for(Node x:tmp.getChild()){
					if(!exist.contains(x.getName()))
						{
						//add to exist list
						//exist.add(x.getName());
						//calculate distance
						double dis1=dis(x.getCord(),tmp.getCord());
						//add to open with new distance
						open.put((minkey+dis1), x);
						//calculate its path for tracking
						ArrayList<String> tmpath=new ArrayList<String>();
						//copy its parent's path
						for(String meow :tmp.getPath())
							tmpath.add(meow);
						//then add node itself
						tmpath.add(x.getName());
						x.setPath(tmpath);
						//System.out.println("add to exist and open"+x.getName());
						}
				}//end of for
				//remove the original one
				open.remove(minkey);
				for(double made : open.keySet()){
					System.out.print(made);
					System.out.println(" "+ open.get(made).getName());
				}
			}
		}//end of check open
		//System.out.println("finished");
		if(flag_find==0)
			System.out.println("didn't find a path to the target.");
		System.out.println("end? (y or n)");
		String ed=inp.nextLine();
		if(ed.equals("y"))
			break;
		while(true){
			System.out.print("initial point:");
			String stat=inp.nextLine();
			if(allName.contains(stat)){
				start = allNode.get(allName.indexOf(stat));
				break;
				}
			System.out.println("point not exist");
		}
		while(true){
			System.out.print("target point:");
			String tgt=inp.nextLine();
			if(allName.contains(tgt)){
				target = allNode.get(allName.indexOf(tgt));
				break;}
			System.out.println("point not exist");
		}//end asking
		}
	}
	//end of main
	//function for calculating distance between two coordinates
	public static  double dis(int[] x, int[] y){
		double rst;
		rst=Math.sqrt((x[0]-y[0])*(x[0]-y[0])+(x[1]-y[1])*(x[1]-y[1]));
		return rst;
	}
}
