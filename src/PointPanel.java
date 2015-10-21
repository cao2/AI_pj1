import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.*;



public class PointPanel extends JComponent{
	ArrayList<Node> allNode=new ArrayList<Node> ();
	ArrayList<String> allName=new ArrayList<String> ();
	Node start=new Node("nonstart");
	Node target=new Node("nontarget");
	JFrame yuFrame = new JFrame();

	
	public PointPanel(){
	    yuFrame.setSize(200, 300);
	    
	    loadfile();
	    
	}

	public void loadfile(){
	    JPanel loadPanel = new JPanel();
	    loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.PAGE_AXIS));
	    //location section
	    JPanel loc=new JPanel();
	    JTextArea location=new JTextArea("coordinate: ");
	    JTextField locationf=new JTextField(20);
	    locationf.setText("/Users/cao2/Documents/workspace/AI_pj1/locsamp.txt");
	    JButton locationb=new JButton("..");
	    loc.add(location);
	    loc.add(locationf);
	    loc.add(locationb);
	    //connection section
	    JPanel cor=new JPanel();
	    JTextArea cord=new JTextArea("connection: ");
	    JTextField corf=new JTextField(20);
	    corf.setText("/Users/cao2/Documents/workspace/AI_pj1/connsamp.txt");
	    JButton corb=new JButton("..");
	    cor.add(cord);
	    cor.add(corf);
	    cor.add(corb);
	    
	    JButton loadin = new JButton("Load Coordinate");
	    
	    loadPanel.add(loc);
	    loadPanel.add(Box.createRigidArea(new Dimension(0,5)));
	    Box.createVerticalGlue();
	    loadPanel.add(cor);
	    loadPanel.add(loadin);
	    
	    //select location file prompt
	    locationb.addActionListener(new ActionListener(){
	    	@Override
	    	public void actionPerformed(ActionEvent e){
	    		JFileChooser fileChooser = new JFileChooser("choose point location file");
	            int returnValue = fileChooser.showOpenDialog(null);
	            if (returnValue == JFileChooser.APPROVE_OPTION) {
	              File selectedFile = fileChooser.getSelectedFile();
	              locationf.setText(selectedFile.getPath());
	            }
	    	}
	    });
	    
	    //select connection file prompt
	    corb.addActionListener(new ActionListener(){
	    	@Override
	    	public void actionPerformed(ActionEvent e){
	    		JFileChooser fileChooser = new JFileChooser("choose connection file");
	            int returnValue = fileChooser.showOpenDialog(null);
	            if (returnValue == JFileChooser.APPROVE_OPTION) {
	              File selectedFile = fileChooser.getSelectedFile();
	              corf.setText(selectedFile.getPath());
	            }
	    	}
	    });
	    
	    //load in file content
	    loadin.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	//read file: locsamp
	    		try {
	    			//read location file
		    		BufferedReader br = new BufferedReader(new FileReader(locationf.getText()));
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
	    		    br.close();
	    		    //read connection file
	    		    br = new BufferedReader(new FileReader(corf.getText()));
	    		    String line1 = br.readLine();
	    		    while (line1 != null) {
	    		        //check if end of the file
	    		        if (line1.equals("END"))
	    		        	break;
	    		        String[] tmp= line1.split("\\s");
	    		        ArrayList<Node> child=new ArrayList<Node>(); 
	    		        //add child node in its child list
	    		         for(int i=0;i<Integer.parseInt(tmp[1]);i++){
	    		        	 int ind=allName.indexOf(tmp[2+i]);
	    		        	 child.add(allNode.get(ind));
	    		         }
	    		        String name=tmp[0];
	    		        int ind=allName.indexOf(name);
	    		        allNode.get(ind).setChild(child);
	    		        line1 = br.readLine();
	    		    }
	    		    br.close();
	    		    System.out.println("finished reading each file");
	    		    yuFrame.setLayout(new BoxLayout(loadPanel, BoxLayout.LINE_AXIS));
	    		    yuFrame.remove(loadPanel);
	    		    yuFrame.setSize(1500, 1000);
	    		    yuFrame.setContentPane(drawpoint());
	    		    yuFrame.validate();
	    		    yuFrame.repaint();
	    		    
	    		} catch(IOException ex) {
	    		   System.out.println(ex.getMessage());
	    		}
	            
	        }
	    });
	    yuFrame.setContentPane(loadPanel);
	    yuFrame.pack();
	    yuFrame.setVisible(true);
	    yuFrame.setLocationRelativeTo(null);
	    yuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JPanel drawpoint(){
		JPanel rst=new JPanel();
		rst.setLayout(new BoxLayout(rst,BoxLayout.LINE_AXIS));
		JPanel left=new JPanel();
	    left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
	    //left.setPreferredSize(new Dimension());
	    //initial input
		JPanel init=new JPanel();
	    JLabel ini=new JLabel("start point: ");
	    String[] allNamelist=new String[allName.size()];
	    allNamelist=allName.toArray(allNamelist);
	    JComboBox<String> initc=new JComboBox<String>(allNamelist);
	    init.add(ini);
	    init.add(initc);
	    //target input
	    JPanel tgt=new JPanel();
	    JLabel tgtt=new JLabel("target point: ");
	    JComboBox<String> tgtc=new JComboBox<String>(allNamelist);
	    tgt.add(tgtt);
	    tgt.add(tgtc);
		
	    JLabel disable=new JLabel("mark points you want to disable: ");
	    JPanel dis=new JPanel();
	    GridLayout grid = new GridLayout(0,3);
	    dis.setLayout(grid);
	    JCheckBox[] checks=new JCheckBox[allNamelist.length];
	    for(int i=0;i<allNamelist.length;i++){
	    	checks[i]=new JCheckBox(allNamelist[i]);
	    	dis.add(checks[i]);
	    }
	    JButton startf=new JButton("start path finding");
	    
	   	left.add(init);
		left.add(tgt);
		left.add(disable);
		left.add(dis);
		left.add(startf);
		left.add(Box.createRigidArea(new Dimension(0,500)));

		
		left.setPreferredSize(new Dimension(250,700));
		rst.add(left);
		points right=new points();
	    rst.add(right);
	    startf.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	right.repEverythin();
	        	String start1 = (String)initc.getSelectedItem();
	        	String target1=(String) tgtc.getSelectedItem();
	        	start=allNode.get(allName.indexOf(start1));
	        	target=allNode.get(allName.indexOf(target1));
	        	right.drawtarget(start, target);
	        	ArrayList<String> disabledNode=new ArrayList<String>();
	            for(JCheckBox x:checks){
	            	if(x.isSelected()==true){
	            		int indx=allName.indexOf(x.getText());
	            		right.addDisabled(allNode.get(indx).getCord());
	            		disabledNode.add(x.getText());
	            	}
	            }
	            ArrayList<Node> path=a_star(disabledNode);
	            int[][] pathC=new int[path.size()][2];
	            int i=0;
	            for(Node x:path){
	            	pathC[i]=x.getCord();
	            	i++;
	            }
	            for(i=0;i<path.size()-1;i++){
	            	right.addpath(pathC[i][0], pathC[i][1], pathC[i+1][0], pathC[i+1][1]);
	            }
	        }
	    });
		return rst;
	}
	public class points extends JPanel {

		private final LinkedList<Line2D> path = new LinkedList<Line2D>();
		private final LinkedList<Node> targ=new LinkedList<Node>();
		private final LinkedList<int[]> disabled=new LinkedList<int[]>();

        private static final long serialVersionUID = 1L;
        points() {
            setLayout(new BorderLayout());   
        }
        
        public void addpath(int x1, int y1, int x2, int y2){
        	 Line2D tmp=new Line2D.Double(x1,-y1,x2,-y2);
        	 System.out.println("new path: ("+x1+","+y1+") to ("+x2+","+y2+");" );
        	 path.add(tmp);
        	 repaint();
        }
        
        public void addDisabled(int[] x){
        	disabled.add(x);
        	repaint();
        }
        public void repEverythin(){
        	disabled.clear();
        	path.clear();
        	targ.clear();
        	repaint();
        }
        public void drawtarget(Node x, Node y){
        	targ.add(x);
        	targ.add(y);
        	repaint();
        }
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform forma = AffineTransform.getTranslateInstance(300,700);
            forma.scale(1.1, 1.1);
            g2d.setTransform(forma);
            g2d.setPaint(Color.green);
        	int r=21;
            for(Node x:allNode){
            	int x1=x.getCord()[0];
            	int y1=x.getCord()[1];
            	int m=x1-r/2;
            	int n=-y1-r/2;
            	g2d.setColor(Color.LIGHT_GRAY);
            	g2d.fillOval(m,n,r,r);
            	for(Node y:x.getChild()){
            		int x2[]=y.getCord();
            		g2d.drawLine(x1, -y1, x2[0], -x2[1]);
            	}
            	g2d.setColor(Color.black);
            	g2d.drawString(x.getName(), x1-10, -y1+3);

            }
        	//set initial color and target color
            int flag=0;
            for(Node x:targ){
            	if (flag==0){
            		flag=1;
            		int[] cord1=x.getCord();
            		g2d.setColor(Color.green);
                    g2d.fillOval(cord1[0]-r/2, -cord1[1]-r/2, r, r);
                    g2d.setColor(Color.black);
                	g2d.drawString(x.getName(), cord1[0]-10, -cord1[1]+3);
            	}
            	else{
            		int[] cord2=x.getCord();
            		g2d.setColor(Color.red);
            		g2d.fillOval(cord2[0]-r/2, -cord2[1]-r/2, r, r);
            		g2d.setColor(Color.black);
                	g2d.drawString(x.getName(), cord2[0]-10, -cord2[1]+3);
                	flag=0;
            	}
            }
            //set color of disabled
            g2d.setColor(Color.GRAY);
            for(int[] x:disabled){
                g2d.fillOval(x[0]-r/2, -x[1]-r/2, r, r);

            }
            g2d.setColor(Color.RED);
            for(Line2D x: path){
            	g2d.draw(x);
            }
            super.paintComponent(g);
            
            
 
        }
    }

	public double dis(int[] x, int[] y){
		double rst;
		rst=Math.sqrt((x[0]-y[0])*(x[0]-y[0])+(x[1]-y[1])*(x[1]-y[1]));
		return rst;
	}

	public ArrayList<Node> a_star(ArrayList<String> disabledNode){
		ArrayList<Node> rst=new ArrayList<Node> ();
		while(true){
		//start of A* algorithm
		//exist stores all seen node
		ArrayList<String> exist=new ArrayList<String> ();
		//open stores all the unexplored node
		//open has distance gone so far as key, and the point as key's content
		HashMap<Double, Node> open=new HashMap<Double, Node> ();
		open.put(0.0, start);
		exist.add(start.getName());
		for(String mii:disabledNode)
			exist.add(mii);
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
			
			
			//if it's the target
			if(tmp.getName().equals(target.getName())){
				flag_find=1;
				System.out.print("reached the target by path: ");
				int ind;
				for(String meow:tmp.getPath())
					{
					ind=allName.indexOf(meow);
					rst.add(allNode.get(ind));
					System.out.println("rst+ "+allNode.get(ind).getName());
					}
				return rst;
				
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
						Node tmpnewx=new Node(x);
						//calculate its path for tracking
						ArrayList<String> tmpath=new ArrayList<String>();
						//copy its parent's path
						for(String meow :tmp.getPath())
							tmpath.add(meow);
						//then add node itself
						tmpath.add(x.getName());
						tmpnewx.setPath(tmpath);
						//add to open with new distance
						open.put((minkey+dis1), tmpnewx);
						
						//System.out.println("add to exist and open"+x.getName());
						}
				}//end of for
				//remove the original one
				open.remove(minkey);
				//testing the open list
				//for(double made : open.keySet()){
				//	System.out.print(made);
				//	System.out.println(" "+ open.get(made).getName());
				//}
			}
		}//end of check open
		//System.out.println("finished");
		if(flag_find==0)
			System.out.println("didn't find a path to the target.");
			return rst;
		}
	}
	

}
