import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class PointPanel extends JComponent{
	ArrayList<Node> allNode=new ArrayList<Node> ();
	ArrayList<String> allName=new ArrayList<String> ();
	Node start=new Node("nonstart");
	Node target=new Node("nontarget");
	JFrame yuFrame = new JFrame();
	
	public PointPanel(){
	    yuFrame.setSize(200, 300);
	    yuFrame.setTitle("A* algorithm");
	    loadfile();
	}

	public void loadfile(){
	    JPanel loadPanel = new JPanel();
	    loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.PAGE_AXIS));
	    //location section
	    JPanel loc=new JPanel();
	    JLabel location=new JLabel("coordinate: ");
	    JTextField locationf=new JTextField(20);
	    locationf.setText("/Users/locsamp.txt");
	    JButton locationb=new JButton("..");
	    loc.add(location);
	    loc.add(locationf);
	    loc.add(locationb);
	    //connection section
	    JPanel cor=new JPanel();
	    JLabel cord=new JLabel("connection: ");
	    JTextField corf=new JTextField(20);
	    corf.setText("/Users/connsamp.txt");
	    JButton corb=new JButton("..");
	    //set content of the start interface
	    cor.add(cord);
	    cor.add(corf);
	    cor.add(corb);
	    JButton loadin = new JButton("Load Files");
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
	        	//loadfiles
	        	readfile(locationf.getText(),corf.getText());
	        	//create new panel with graph displays of nodes
	        	yuFrame.remove(loadPanel);
	 		    yuFrame.setLayout(new FlowLayout());
	 		    yuFrame.setContentPane(drawpoint());
	 		    yuFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	 		    //yuFrame.setUndecorated(true);
	 		    yuFrame.setLocationRelativeTo(null);
	 		    yuFrame.setBackground(Color.black);
	 		    yuFrame.validate();
	 		    yuFrame.repaint();
	            
	        }
	    });
	    yuFrame.setContentPane(loadPanel);
	    yuFrame.pack();
	    yuFrame.setVisible(true);
	    yuFrame.setLocationRelativeTo(null);
	    yuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	public void readfile(String loc, String conn){
		try {
			//read location file
    		BufferedReader br = new BufferedReader(new FileReader(loc));
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
		    br = new BufferedReader(new FileReader(conn));
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
		    //System.out.println("finished reading each file");
		} catch(IOException ex) {
		   System.out.println(ex.getMessage());
		}
		
	}
	public static void setFontforall ( Component component, Font font )
	{
	    component.setFont ( font );
	    component.setForeground(Color.red);
	    component.setBackground(new Color(6,42,67));
	    if ( component instanceof Container )
	    {
	        for ( Component com : ( ( Container ) component ).getComponents () )
	        {
	            setFontforall ( com, font );
	        }
	    }
	}

	public JPanel drawpoint(){
		
		
		JPanel rst=new JPanel();
		
		rst.setLayout(new BoxLayout(rst,BoxLayout.LINE_AXIS));
		JPanel left=new JPanel();
		Font font=new Font("Serif", Font.BOLD, 14);
	    left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
	    left.setAlignmentX( Component.LEFT_ALIGNMENT );

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
	    tgtt.setFont(font);
	    JComboBox<String> tgtc=new JComboBox<String>(allNamelist);
	    tgt.add(tgtt);
	    tgt.add(tgtc);
		
	    //heuristic input
	    JPanel heuPane=new JPanel();
	    JLabel heu=new JLabel("Heuristic");
	    String[] heuList=new String[2];
	    heuList[0]="Straight Line Distance";
	    heuList[1]="Fewest Linkes";
	    JComboBox<String> heut=new JComboBox<String>(heuList);
	    heuPane.add(heu);
	    heuPane.add(heut);
	    
	    //disabled checkbox panel
	    JPanel disablP=new JPanel();
	    JLabel disable=new JLabel("points to disable");
	    JLabel lala=new JLabel("                ");
	    JPanel dis=new JPanel();
	    GridLayout grid = new GridLayout(0,4);
	    dis.setLayout(grid);
	    JCheckBox[] checks=new JCheckBox[allNamelist.length];
	    for(int i=0;i<allNamelist.length;i++){
	    	checks[i]=new JCheckBox(allNamelist[i]);
	    	dis.add(checks[i]);
	    }
	    disablP.add(disable);
	    disablP.add(lala);
	    disablP.add(dis);
	    
	    //buttons to start path finding
	    JButton startf=new JButton("start path finding");
	    startf.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    //buttons to control the travese process
	    JPanel process=new JPanel();
	    JButton palse=new JButton("Pause the traverse");
	    JButton resume=new JButton("resume");
	    process.add(palse);
	    process.add(resume);
	    
	    //choose if want to show traverse
	    JPanel traop=new JPanel();
	    JLabel trap=new JLabel("Show travese process");
	    String[] trapList=new String[2];
	    trapList[0]="Show travese process";
	    trapList[1]="only final path";
	    JComboBox<String> trapB=new JComboBox<String>(trapList);
	    traop.add(trap);
	    traop.add(trapB);
	    
	    //add heuristic options, initial and target selection 
		left.add(Box.createRigidArea(new Dimension(0,20)));
	   	left.add(init);
		left.add(tgt);
		left.add(heuPane);
		
		//add show traverse option
		left.add(traop);
		
		//add buttons to start
		left.add(Box.createRigidArea(new Dimension(0,50)));
		left.add(startf);
		
		//add disabled point for selection
		left.add(Box.createRigidArea(new Dimension(0,20)));
		left.add(disablP);
		
		//add process control button
		left.add(Box.createRigidArea(new Dimension(0,30)));
		//left.add(process);
		left.add(Box.createRigidArea(new Dimension(0,60)));

		int leftx=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int lefty=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		left.setPreferredSize(new Dimension(leftx/6,lefty));
		
		
		//create graph panel
		points right=new points();
		right.setPreferredSize(new Dimension(leftx*3/5, lefty));
		
		//status panel
		JScrollPane stat=new JScrollPane();
		//stat.setBackground(new Color(21,9,80));
		stat.setBackground(Color.YELLOW);
		stat.setPreferredSize(new Dimension(leftx/5,lefty));
		//add everything to panel
		rst.add(left);
	    rst.add(right);
	    rst.add(stat);
	    
	    //set font for all
	    setFontforall(rst,font);
	    
	    //add actionlistener for buttons
	    
	    
	    startf.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	//clear the graph panel
	        	right.repEverythin();
	        	//get target and start point and mark them
	        	String start1 = (String)initc.getSelectedItem();
	        	String target1=(String) tgtc.getSelectedItem();
	        	start=allNode.get(allName.indexOf(start1));
	        	target=allNode.get(allName.indexOf(target1));
	        	right.drawtarget(start, target);
	        	
	        	//set option for showing traverse
	        	String topt=(String)trapB.getSelectedItem();
	        	if(topt.equals("Show travese process")){
	        		right.set_trav_op(true);
	        	}
	        	else
	        		right.set_trav_op(false);
	        	//get disabled node
	        	//set their color
	        	ArrayList<String> disabledNode=new ArrayList<String>();
	            for(JCheckBox x:checks){
	            	if(x.isSelected()==true){
	            		int indx=allName.indexOf(x.getText());
	            		right.addDisabled(allNode.get(indx));
	            		disabledNode.add(x.getText());
	            	}
	            }
	            String heuristic=(String) heut.getSelectedItem();
	            ArrayList<JLabel> stat_list;
	            if(heuristic.equals("Straight Line Distance")){
	            	stat_list =a_star_node(disabledNode,right,0);
	            }
	            else{
	            	stat_list =a_star_node(disabledNode,right,1);
	            }
	           
	            
	            JPanel statP=new JPanel();
	            statP.setLayout(new BoxLayout(statP,BoxLayout.PAGE_AXIS));
	            for(JLabel x:stat_list)
	            	statP.add(x);
	            
	            //statSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	            stat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	            stat.setViewportBorder(new LineBorder(Color.RED));
	            stat.setVisible(true);
	            stat.setViewportView(statP);
	            stat.revalidate();
	            stat.repaint();
	        }
	    });
		return rst;
	}
	
	//create new panels for graphic representation of nodes
	public class points extends JPanel{

		private final LinkedList<Node> path = new LinkedList<Node>();
		private final LinkedList<Node> targ=new LinkedList<Node>();
		private final LinkedList<Node> disabled=new LinkedList<Node>();
		private final LinkedList<Node> trav=new LinkedList<Node>();
		int flag_animation=-1;
		private final  AffineTransform forma = AffineTransform.getTranslateInstance(10,50);
		private boolean show_trave=true;
        private static final long serialVersionUID = 1L;
        Timer timer;
        
        points() {
        	//timer to control drawing travese path step by step
        	//dealy of 1 second
            setForeground(Color.white);
             timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {  
                    setLayout(new BorderLayout()); 
                	setForeground(Color.white);
                    flag_animation++;
                    repaint();
                }
            });
            timer.start();
            
        }
       //to show the traverse path
       public void set_trav_op(boolean x){
    	   show_trave=x;
       }
       
       //add a travese node into trave linked list
        public void addtrav(Node tmp){
        	 trav.add(tmp);
        }
        
        //add node to final path
        public void addpath(Node tmp){
        	 path.add(tmp);
        }
        
        //add disabled linked list
        public void addDisabled(Node x){
        	disabled.add(x);
        	//repaint();
        }
        
        //repaint everything for new search path
        public void repEverythin(){
        	flag_animation=-1;
        	disabled.clear();
        	path.clear();
        	targ.clear();
        	trav.clear();
        	//repaint();
        }
        
        //add target for coloring 
        public void drawtarget(Node x, Node y){
        	targ.add(x);
        	targ.add(y);
        	//repaint();
        }
        
        //paint green for start
        //red for final node
        public void painttarget(Graphics g){
        	Graphics2D g2d=(Graphics2D) g;
        	int r=29;
        	//set initial color and target color
            int flag=0;
            for(Node x:targ){
            	//paint initial first
            	//colored green
            	if (flag==0){
            		flag=1;
            		int[] cord1=x.getCord();
            		g2d.setColor(Color.green);
                    g2d.fillOval(cord1[0]-r/2, cord1[1]-r/2, r, r);
                    
            	}
            	//paint target color as red
            	else{
            		int[] cord2=x.getCord();
            		g2d.setColor(Color.red);
            		g2d.fillOval(cord2[0]-r/2, cord2[1]-r/2, r, r);
                	flag=0;
            	}
            }
        }
        
        //draw hint color for each catagory of node
        public void drawhint(Graphics g){
        	Graphics2D g2d = (Graphics2D) g;
            g2d.drawString("initial point :", -2, -25);
            g2d.drawString("target point :",130, -25);
            g2d.drawString("traverse path :", 270, -25);
            g2d.drawString("final path :",420, -25);
            g2d.drawString("regular node :",555, -25);
            g2d.drawString("disabled node :",700, -25);
            g2d.setColor(Color.green);
            g2d.fillRect(75, -40, 50, 20);
            g2d.setColor(Color.red);
            g2d.fillRect(215, -40, 50, 20);
            g2d.setColor(Color.blue);
            g2d.fillRect(360, -40, 50, 20);
            g2d.setColor(Color.yellow);
            g2d.fillRect(495, -40, 50, 20);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(640, -40, 50, 20);
            g2d.setColor(Color.darkGray);
            g2d.fillRect(790, -40, 50, 20);
            
        }
       
        @Override
        public void paintComponent(Graphics g) {
        	super.paintComponent(g); 
            Graphics2D g2d = (Graphics2D) g;
            //set the format of graphic
            g2d.transform(forma);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //draw axis
            g2d.setColor(Color.white);
            g2d.drawLine(0, 0, 800, 0);
            g2d.drawLine(0, 0, 0, 800);
            g2d.drawString("x", 778, -2);
            g2d.drawString("y", -10, 785);
            g2d.drawString("0,0", -2, -2);
            g2d.setStroke(new BasicStroke(1));
            
          //draw color hint
            drawhint(g);
           //set r for node circle
            int r=29;
            //draw node circle
        	for(Node x:allNode){
            	int x1=x.getCord()[0];
            	int y1=x.getCord()[1];
            	int m=x1-r/2;
            	int n=y1-r/2;
            	g2d.setColor(Color.LIGHT_GRAY);
            	g2d.fillOval(m,n,r,r);
            	for(Node y:x.getChild()){
            		int x2[]=y.getCord();
            		drawArrowLine(g,x1, y1, x2[0], x2[1]);
            	}
            	
            }
          //color target point different
        	painttarget(g);
        	
            //set color of disabled
            for(Node x:disabled){
            	g2d.setColor(Color.darkGray);
                g2d.fillOval(x.getCord()[0]-r/2, x.getCord()[1]-r/2, r, r);
                g2d.setColor(Color.black);
            }
            
            if(show_trave){
            //print traverse path one by one in blue color
            	g2d.setColor(Color.blue);
            	if(!trav.isEmpty()&&flag_animation<trav.size()){
            		for(int ni=0;ni<=flag_animation;ni++){
            			Node x=trav.get(ni);
            			g2d.fillOval(x.getCord()[0]-r/2, x.getCord()[1]-r/2, r, r);
            		}
            	}
            }
            else
            	flag_animation=trav.size();
            
          //print final path in pink color
          if(flag_animation>=trav.size())
            	{
        	  	g2d.setColor(Color.blue);
        	  	for(Node x:trav)
        		  g2d.fillOval(x.getCord()[0]-r/2, x.getCord()[1]-r/2, r, r);
        	  	
        	  	
            	g2d.setColor(Color.yellow);
            	for(Node x:path)
            		 g2d.fillOval(x.getCord()[0]-r/2, x.getCord()[1]-r/2, r, r);
            	}
            //make sure the target and inital point color remains the same
            painttarget(g);
            
            //reprint node name for better view
            paintstring(g);
        }

        //paint name for each node
        public void paintstring(Graphics g){
        	Graphics2D g2d=(Graphics2D) g;
        	g2d.setColor(Color.black);
            for(Node x:allNode){
            	int x1=x.getCord()[0];
            	int y1=x.getCord()[1];
            	g2d.drawString(x.getName(), x1-12, y1+2);
            }
        }

        //draw arrow line from x1,y1 to x2,y2
        //referenced from http://stackoverflow.com/questions/4112701/drawing-a-line-with-arrow-in-java
        void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2) {
            Graphics2D g2d = (Graphics2D) g.create();
            double dx = x2 - x1;
            double dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int lenth = (int) Math.sqrt(dx*dx + dy*dy);
            //rotate the screen to the angle of line between two point
            AffineTransform aft = AffineTransform.getTranslateInstance(x1, y1);
            aft.concatenate(AffineTransform.getRotateInstance(angle));
            g2d.transform(aft);
            //draw curved line
            g2d.draw(new QuadCurve2D.Double(0,0,(lenth-29/2)/2,8,lenth-29/2, 0));
            //draw arrow head
            g2d.drawLine(lenth-29/2-8, 8, lenth-29/2, 0);
            g2d.drawLine(lenth-29/2-8, -8, lenth-29/2, 0);
        }
    
    }

	//calculate distance between x and y
	public double dis(int[] x, int[] y){
		double rst;
		rst=Math.sqrt((x[0]-y[0])*(x[0]-y[0])+(x[1]-y[1])*(x[1]-y[1]));
		return rst;
	}

	//calculate straight line distance between x and target
	public double heuristic_distance(Node x){
		double rst=dis(x.getCord(),target.getCord());
		return rst;
	}
	//always return 1 as its under estimate
	public double heuristic_points(){
		return 1;
	}
	
	//finding the best alternative
	public Node getopen(HashMap<Node, Double> open, int heuristic){
		double min=999999999;//bigest number possbile for double
		Node minkey=new Node("NADA");
		if(heuristic==0){
			for(Node x:open.keySet())
				if(open.get(x)+heuristic_distance(x)<(min)){
					min=open.get(x)+heuristic_distance(x);
					minkey=x;}
		}
		//else for fewest linked 
		else{
			for(Node x:open.keySet())
				if(open.get(x)+heuristic_points()<(min)){
					min=open.get(x)+heuristic_points();
					minkey=x;
				}
		}
		//System.out.println("min is "+ min);
		return minkey;
	}
	//generate alternetives
	public void addopen(Node x,HashMap<Node, Double> open, int heuristic, Node tmp, double min){
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
		if(heuristic==0)
			open.put(tmpnewx,open.get(tmp)+dis(x.getCord(),tmp.getCord()));
		else
			open.put(tmpnewx,open.get(tmp)+1);
	}
	//a* algorithm 
	public ArrayList<JLabel> a_star_node(ArrayList<String> disabledNode, points right, int heuristic){
		ArrayList<JLabel> stat=new ArrayList<JLabel> ();
		ArrayList<Node> rst=new ArrayList<Node> ();
		
		//exist stores all seen node
		ArrayList<String> exist=new ArrayList<String> ();
		//open stores all the unexplored node
		//open has point as key, distance so far as content
		HashMap<Node, Double> open=new HashMap<Node, Double> ();
		open.put(start, 0.0);
		exist.add(start.getName());
		//add disabled point to exist, so searching path will ignore those node
		for(String mii:disabledNode)
			exist.add(mii);
		
		ArrayList<String> inpath=new ArrayList<String> ();
		//insert the path for start point
		start.setPath(inpath);
		//flag_find is to determine if a soultion is found
		int flag_find=0;
		
		while(open.size()!=0){
			
			Node minkey=getopen(open,heuristic);
			double min=999999;
			if (heuristic==0)
				min=open.get(minkey)+heuristic_distance(minkey);
			else
				min=open.get(minkey)+1;
			Node tmp=minkey;
			//if it's the target
			
			
			if(tmp.getName().equals(target.getName())){
				flag_find=1;
				stat.add(new JLabel("---------------------------"));
				JLabel meow1=new JLabel("************reached the target*********** ");
				stat.add(new JLabel("---------------------------"));
				
				
				stat.add(meow1);
				int ind;
				for(String meow:tmp.getPath())
					{
					ind=allName.indexOf(meow);
					rst.add(allNode.get(ind));
					}
			     
				//add final path to status log
				 String final_path="Final path: "+start.getName();
				 for(Node x:rst){
		            	right.addpath(x);
		            	final_path+="->"+x.getName();
				 }
				stat.add(new JLabel(final_path));
				stat.add(new JLabel("░░░░░░░░░░░░░░░░░████████████"));
				stat.add(new JLabel("░░ ███████░░░░░███▒▒▒▒▒▒▒▒▒▒▒▒▒▒███"));
				stat.add(new JLabel("░░░█▒▒▒▒▒▒█░░░██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██"));
				stat.add(new JLabel("░░░░█▒▒▒▒▒█░░██▒▒▒▒▒██▒▒▒▒▒▒██▒▒▒▒▒███"));
				stat.add(new JLabel("░░░░░█▒▒▒█░░█▒▒▒▒▒▒████▒▒▒▒████▒▒▒▒▒██"));
				stat.add(new JLabel("░█████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██"));
				stat.add(new JLabel("░░░█▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒▒▒▒▒▒▒█▒▒▒▒▒▒▒▒▒▒▒██"));
				stat.add(new JLabel("░██▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒██▒▒▒▒▒▒▒▒▒▒██▒▒▒▒██"));
				stat.add(new JLabel("█████████████▒▒▒▒██▒▒▒▒▒▒▒▒▒██▒▒▒██"));
				stat.add(new JLabel("█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒▒▒▒████████▒▒▒▒▒▒██"));
				stat.add(new JLabel("██▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██"));
				stat.add(new JLabel("░█▒▒▒█████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██"));
				stat.add(new JLabel("░██▒▒▒▒▒▒▒▒▒▒███▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█"));
				stat.add(new JLabel("░░████████████░████████████"));
				return stat;
				
			}
			else{
				
				if(!tmp.getName().equals(start.getName())){
					right.addtrav(tmp);
					}
				
				exist.add(tmp.getName());
				for(Node x:tmp.getChild())
					if(!exist.contains(x.getName()))
						//add to exist list
						addopen(x,open,heuristic,tmp,min);
				
				
				//print infos about chose node
				String chose_path="Path: "+start.getName();
				for(String meow: tmp.getPath())
					chose_path+="->"+meow;
				//add status log
				stat.add(new JLabel("-------------------------------"));
				stat.add(new JLabel("reached node "+tmp.getName()));
				stat.add(new JLabel("---------------------------"));
				stat.add(new JLabel(chose_path));
				stat.add(new JLabel("estimated distance: "+ Double.toString(min)));
				stat.add(new JLabel("by now done distance: "+ Double.toString(open.get(minkey))));
				stat.add(new JLabel("open list: (name, distance walked so far, total estimation): "));
				
				
				//remove the original one
				open.remove(minkey);
				
				//print the open list
				int openflag=0;
				JLabel tmp5[]=new JLabel[open.keySet().size()];
				for(Node x:open.keySet()){
					double heu;
					if(heuristic==0)
						heu=open.get(x)+dis(x.getCord(),target.getCord());
					else
						heu=open.get(x)+1;
					tmp5[openflag]=new JLabel(x.getName()+","+Double.toString(open.get(x))+",		"+heu);
					stat.add(tmp5[openflag]);
					openflag++;
				}
				
			}
		}//end of check open
		//System.out.println("finished");
		if(flag_find==0)
			JOptionPane.showMessageDialog(yuFrame,
				    "No path can be found, sorry");
			//System.out.println("didn't find a path to the target.");
		return stat;
		
	}
	

}
