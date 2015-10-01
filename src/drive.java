import java.io.*;
import java.util.*;

public class drive {
//main
	public static void main(String[] args) throws IOException{

		ArrayList<Node> allNode=new ArrayList<Node> ();
		ArrayList<String> allName=new ArrayList<String> ();
		Node start=new Node("nonstart");
		Node target=new Node("nontarget");
		String locsamp="";
		String connsamp="";
		
		
		
				Scanner inp=new Scanner(System.in);
			
				System.out.print("sample location file paht:");
				locsamp=inp.nextLine();
				System.out.print("sample connection file paht:");
				connsamp=inp.nextLine();
				
		
		//read file: locsamp
		BufferedReader br = new BufferedReader(new FileReader(locsamp));
		try {
		    String line = br.readLine();
		    while (line != null) {
		        //check if end of the file
		        if (line.equals("END"))
		        	break;
		        
		        String[] tmp= line.split("\\s");
		        Node pt=new Node(tmp[0]);
		        pt.setCord(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
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
		         //System.out.println(tmp[0]+" **********************");
		         for(int i=0;i<Integer.parseInt(tmp[1]);i++){
		        	//System.out.println(tmp[2+i]);
		        	 int ind=allName.indexOf(tmp[2+i]);
		        	 //System.out.println(ind);
		        	 child.add(allNode.get(ind));
		         }
		        String name=tmp[0];
		        int ind=allName.indexOf(name);
		        double dist=dis(allNode.get(ind).getCord(),target.getCord());
		        allNode.get(ind).setChild(child);
		        allNode.get(ind).setDist(dist);
		        line = br.readLine();

		    }
		} finally {
		    br.close();
		}
		//System.out.println("finished processing file, result:");
		//for(Node x:allNode)
		//	System.out.println(x.toString());
		
		
		//start of A* algorithm
		//System.out.println("start point: "+start.getName()); 
		ArrayList<String> exist=new ArrayList<String> ();
		HashMap<Double, Node> open=new HashMap<Double, Node> ();
		open.put(0.0, start);
		exist.add(start.getName());
		ArrayList<String> inpath=new ArrayList<String> ();
		inpath.add(start.getName());
		start.setPath(inpath);
		while(open.size()!=0){
			//find the best in open
			//heuristic is stored in node, and the so far distance is in open 
			double min=999999999;//bigest number possbile for double
			double minkey=0;
			for(double x:open.keySet())
				if(x+open.get(x).getDist()<(min)){
					min=x+open.get(x).getDist();
					minkey=x;
				}
			
			Node tmp=open.get(minkey);
			System.out.println("choose: "+tmp.getName());
			if(tmp.getName().equals(target.getName())){
				System.out.println("reached the target!");
				for(String meow:tmp.getPath())
					System.out.println(meow);
				break;
			}
			else{
				for(Node x:tmp.getChild()){
					if(!exist.contains(x.getName()))
						{
						exist.add(x.getName());
						//calculate distance
						double dis1=dis(x.getCord(),tmp.getCord());
						open.put((minkey+dis1), x);
						ArrayList<String> tmpath=new ArrayList<String>();
						for(String meow :tmp.getPath())
							tmpath.add(meow);
						tmpath.add(x.getName());
						x.setPath(tmpath);
						System.out.println("add to exist and open"+x.getName());
						}
				}//end of for
				//remove the original one
				open.remove(minkey);
			}
		}//end of check open
		//System.out.println("finished");
	}
	//end of main
	public static  double dis(int[] x, int[] y){
		double rst;
		rst=Math.sqrt((x[0]-y[0])*(x[0]-y[0])+(x[1]-y[1])*(x[1]-y[1]));
		return rst;
	}
}
