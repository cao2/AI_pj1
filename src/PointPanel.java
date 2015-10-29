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
	    JLabel location=new JLabel("coordinate: ");
	    JTextField locationf=new JTextField(20);
	    locationf.setText("/Users/cao2/Documents/workspace/AI_pj1/locsamp.txt");
	    JButton locationb=new JButton("..");
	    loc.add(location);
	    loc.add(locationf);
	    loc.add(locationb);
	    //connection section
	    JPanel cor=new JPanel();
	    JLabel cord=new JLabel("connection: ");
	    JTextField corf=new JTextField(20);
	    corf.setText("/Users/cao2/Documents/workspace/AI_pj1/connsamp.txt");
	    JButton corb=new JButton("..");
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
	    		   
	    		    yuFrame.remove(loadPanel);
	    		    yuFrame.setLayout(new FlowLayout());
	    		    yuFrame.setContentPane(drawpoint());
	    		    yuFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	    		    //yuFrame.setUndecorated(true);
	    		    yuFrame.setLocationRelativeTo(null);
	    		    yuFrame.setBackground(Color.black);
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
		Font font=new Font("Serif", Font.BOLD, 20);
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
	    disablP.setLayout(new BoxLayout(disablP,BoxLayout.PAGE_AXIS));
	    JLabel disable=new JLabel("points to disable:             ");
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
	    
	    //add heuristic options, initial and target selection 
		left.add(Box.createRigidArea(new Dimension(0,90)));
	   	left.add(init);
		left.add(tgt);
		left.add(heuPane);
		//add buttons to start
		left.add(Box.createRigidArea(new Dimension(0,50)));
		left.add(startf);
		
		//add disabled point for selection
		left.add(Box.createRigidArea(new Dimension(0,50)));
		left.add(disablP);
		
		//add process control button
		left.add(Box.createRigidArea(new Dimension(0,30)));
		left.add(process);
		left.add(Box.createRigidArea(new Dimension(0,600)));

		int leftx=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int lefty=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		left.setPreferredSize(new Dimension(leftx/6,lefty));
		
		
		//create graph panel
		points right=new points();
		right.setPreferredSize(new Dimension(leftx*3/5, lefty));
		
		//status panel
		JPanel stat=new JPanel();
		stat.setBackground(new Color(21,9,80));
		stat.setPreferredSize(new Dimension(leftx/5,lefty));
		
		//add everything to panel
		rst.add(left);
	    rst.add(right);
	    rst.add(stat);
	    
	    //set font for all
	    setFontforall(rst,font);
	    
	    //add actionlistener for buttons
	    palse.addActionListener(new ActionListener(){
	    	@Override
	    	public void actionPerformed(ActionEvent e){
		    	right.pause();
		    }
	    });
	    resume.addActionListener(new ActionListener(){
	    	@Override
	    	public void actionPerformed(ActionEvent e){
		    	right.resume();
		    }
	    });
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
	            		right.addDisabled(allNode.get(indx));
	            		disabledNode.add(x.getText());
	            	}
	            }
	            String heuristic=(String) heut.getSelectedItem();
	            ArrayList<Node> path;
	            if(heuristic.equals("Distance")){
	            	path =a_star_node(disabledNode,right,0);
	            }
	            else{
	            	path =a_star_node(disabledNode,right,1);
	            }
	           
	            
	            for(Node x:path){
	            	
	            	right.addpath(x);
	            }
	        }
	    });
		return rst;
	}
	public class points extends JPanel{

		private final LinkedList<Node> path = new LinkedList<Node>();
		private final LinkedList<Node> targ=new LinkedList<Node>();
		private final LinkedList<Node> disabled=new LinkedList<Node>();
		private final LinkedList<Node> trav=new LinkedList<Node>();
		int flag_animation=-1;
		int leftx=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		private  final  AffineTransform forma = AffineTransform.getTranslateInstance(20,100);
		
        private static final long serialVersionUID = 1L;
        Timer timer;
        
        points() {
        	forma.scale(1.5, 1.5);
            //setLayout(new BorderLayout()); 
            setForeground(Color.white);
             timer = new Timer(2000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {  
                    setLayout(new BorderLayout()); 
                	setForeground(Color.white);
                    flag_animation++;
                    repaint();
                }
            });
            timer.start();
            
        }
       public void pause(){
    	   timer.stop();
       }
       public void resume(){
    	   timer.start();
       }
        public void addtrav(Node tmp){
        	 trav.add(tmp);
        	 //repaint();
        }
        public void addpath(Node tmp){
        	 
        	 path.add(tmp);
        	 //repaint();
        }
        
        public void addDisabled(Node x){
        	disabled.add(x);
        	//repaint();
        }
        public void repEverythin(){
        	flag_animation=-1;
        	disabled.clear();
        	path.clear();
        	targ.clear();
        	trav.clear();
        	//repaint();
        }
        public void drawtarget(Node x, Node y){
        	targ.add(x);
        	targ.add(y);
        	//repaint();
        }
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
                    g2d.setColor(Color.black);
                	g2d.drawString(x.getName(), cord1[0]-10, cord1[1]-1);
            	}
            	//paint target color as red
            	else{
            		int[] cord2=x.getCord();
            		g2d.setColor(Color.red);
            		g2d.fillOval(cord2[0]-r/2, cord2[1]-r/2, r, r);
            		g2d.setColor(Color.black);
                	g2d.drawString(x.getName(), cord2[0]-10, cord2[1]-1);
                	flag=0;
            	}
            }
        }
        @Override
        public void paintComponent(Graphics g) {
        	super.paintComponent(g); 
            Graphics2D g2d = (Graphics2D) g;
            g2d.transform(forma);
            //draw axis
            g2d.setColor(Color.white);
            g2d.drawLine(0, 0, 800, 0);
            g2d.drawLine(0, 0, 0, 800);
            g2d.drawString("x", 778, -2);
           // g2d.drawString("800", 800, 0);
           // g2d.drawString("800", 0, 800);
            g2d.drawString("y", -10, 785);
            g2d.drawString("0,0", -2, -2);
            g2d.setStroke(new BasicStroke(3));
            //draw color hint
            
            g2d.drawString("initial point :", -2, -25);
            g2d.drawString("target point :", 200, -25);
            g2d.drawString("traverse path :", 400, -25);
            g2d.drawString("final point :", 600, -25);
            g2d.setColor(Color.green);
            g2d.fillRect(122, -40, 50, 20);
            g2d.setColor(Color.red);
            g2d.fillRect(330, -40, 50, 20);
            g2d.setColor(Color.blue);
            g2d.fillRect(530, -40, 50, 20);
            g2d.setColor(Color.pink);
            g2d.fillRect(730, -40, 50, 20);
            int r=29;
        	for(Node x:allNode){
            	int x1=x.getCord()[0];
            	int y1=x.getCord()[1];
            	int m=x1-r/2;
            	int n=y1-r/2;
            	g2d.setColor(Color.LIGHT_GRAY);
            	g2d.fillOval(m,n,r,r);
            	for(Node y:x.getChild()){
            		int x2[]=y.getCord();
            		g2d.drawLine(x1, y1, x2[0], x2[1]);
            	}
            	g2d.setColor(Color.black);
            	g2d.drawString(x.getName(), x1-10, y1+2);
            }
          
        	painttarget(g);
            //set color of disabled
            for(Node x:disabled){
            	g2d.setColor(Color.GRAY);
                g2d.fillOval(x.getCord()[0]-r/2, x.getCord()[1]-r/2, r, r);
                g2d.setColor(Color.black);
                g2d.drawString(x.getName(), x.getCord()[0]-10, x.getCord()[1]-1);

            }
            
            //print traverse path one by one in blue color
            g2d.setColor(Color.blue);
            if(!trav.isEmpty()&&flag_animation<trav.size()){
            	for(int ni=0;ni<=flag_animation;ni++){
            		Node x=trav.get(ni);
            		 g2d.fillOval(x.getCord()[0]-r/2, x.getCord()[1]-r/2, r, r);
                    
            	}
            	}
            //print final path in red color
            else if(flag_animation>=trav.size())
            	{
            	
            	g2d.setColor(Color.pink);
            	for(Node x:path)
            		 g2d.fillOval(x.getCord()[0]-r/2, x.getCord()[1]-r/2, r, r);
                	
            	
            	}
            
            painttarget(g);
            
            //reprint node name for better view
            g2d.setColor(Color.black);
            for(Node x:allNode){
            	int x1=x.getCord()[0];
            	int y1=x.getCord()[1];
            	g2d.drawString(x.getName(), x1-10, y1+2);

            }
        }

		
    }

	public double dis(int[] x, int[] y){
		double rst;
		rst=Math.sqrt((x[0]-y[0])*(x[0]-y[0])+(x[1]-y[1])*(x[1]-y[1]));
		return rst;
	}

	public double heuristic_distance(Node x){
		double rst=dis(x.getCord(),target.getCord());
		return rst;
	}
	public double heuristic_points(){
		return 1;
	}
	public ArrayList<Node> a_star_node(ArrayList<String> disabledNode, points right, int heuristic){
		ArrayList<Node> rst=new ArrayList<Node> ();
		while(true){
		//start of A* algorithm
		//exist stores all seen node
		ArrayList<String> exist=new ArrayList<String> ();
		//open stores all the unexplored node
		//open has distance gone so far as key, and the point as key's content
		HashMap<Node, Double> open=new HashMap<Node, Double> ();
		open.put(start, 0.0);
		exist.add(start.getName());
		for(String mii:disabledNode)
			exist.add(mii);
		ArrayList<String> inpath=new ArrayList<String> ();
		//insert the path for start point
		//inpath.add(start.getName());
		start.setPath(inpath);
		//flag_find is to determine if a soultion is found
		int flag_find=0;
		while(open.size()!=0){
			//find the best in open
			//heuristic is stored in node 
			//min is the smallest estimation
			//minkey is the distance done so far
			double min=999999999;//bigest number possbile for double
			Node minkey=new Node("NADA");
			if(heuristic==0){
				for(Node x:open.keySet())
					if(open.get(x)+heuristic_distance(x)<(min)){
						min=open.get(x)+heuristic_distance(x);
						minkey=x;
					}
			}
			else{
				for(Node x:open.keySet())
					if(open.get(x)+heuristic_points()<(min)){
						min=open.get(x)+heuristic_points();
						minkey=x;
					}
			}
			Node tmp=minkey;
			
			
			//if it's the target
			if(tmp.getName().equals(target.getName())){
				flag_find=1;
				System.out.print("reached the target by path: ");
				int ind;
				for(String meow:tmp.getPath())
					{
					ind=allName.indexOf(meow);
					rst.add(allNode.get(ind));
					}
			
				right.addtrav(tmp);
				return rst;
				
			}
			else{
				
				if(!tmp.getName().equals(start.getName())){
					
					right.addtrav(tmp);
					}
				exist.add(tmp.getName());
				for(Node x:tmp.getChild()){
					if(!exist.contains(x.getName()))
						{
						//add to exist list
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
							open.put( tmpnewx,open.get(minkey)+dis(tmpnewx.getCord(),tmp.getCord()));
						else
							open.put( tmpnewx,open.get(minkey)+1);
						
						}
				}//end of for
				
				//remove the original one
				open.remove(minkey);
				//testing the open list
				
			}
		}//end of check open
		//System.out.println("finished");
		if(flag_find==0)
			System.out.println("didn't find a path to the target.");
			return rst;
		}
	}
	

}
