import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
	    		    yuFrame.remove(loadPanel);
	    		    yuFrame.setSize(1500, 1200);
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
		
		JPanel left=new JPanel();
	    left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
	    //initial input
		JPanel init=new JPanel();
	    JTextArea ini=new JTextArea("start point: ");
	    String[] allNamelist=new String[allName.size()];
	    allNamelist=allName.toArray(allNamelist);
	    JComboBox<String> initc=new JComboBox<String>(allNamelist);
	    init.add(ini);
	    init.add(initc);
	    //target input
	    JPanel tgt=new JPanel();
	    JTextArea tgtt=new JTextArea("start point: ");
	    JComboBox<String> tgtc=new JComboBox<String>(allNamelist);
	    tgt.add(tgtt);
	    tgt.add(tgtc);
		
	    JTextArea disable=new JTextArea("mark points you want to disable: ");
	    JPanel dis=new JPanel();
	    GridLayout grid = new GridLayout(0,5);
	    dis.setLayout(grid);
	    JCheckBox[] checks=new JCheckBox[allNamelist.length];
	    for(int i=0;i<allNamelist.length;i++){
	    	checks[i]=new JCheckBox(allNamelist[i]);
	    	dis.add(checks[i]);
	    }
	    
	    
		left.add(init);
		left.add(tgt);
		left.add(disable);
		left.add(dis);

		rst.add(left);
	    
		return rst;
	}
}
