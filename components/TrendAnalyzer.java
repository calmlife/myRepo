package components;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import components.TrendAnalyzer.Start;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * This is like TableDemo, except that it substitutes a Favorite Color column
 * for the Last Name column and specifies a custom cell renderer and editor for
 * the color data.
 */
public class TrendAnalyzer extends JPanel {
	private boolean DEBUG = false;
	public static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a Z");

	public TrendAnalyzer(Vector<Object[]> data, String[] columnNames, String[] columnTypes) {

		super(new GridLayout(1, 0));

		JTable table = new JTable(new MyTableModel(data, columnNames, columnTypes));
		table.setPreferredScrollableViewportSize(new Dimension(1200, 500));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(table);

		table.setDefaultRenderer(Date.class, new MyDateRenderer(df));
//		table.setDefaultEditor(Color.class, new ColorEditor());

		add(scrollPane);
	}

	class MyTableModel extends AbstractTableModel {

		private Vector<Object[]> data;
		private String[] columnNames;
		private String[] columnTypes;

		public MyTableModel(Vector<Object[]> data, String[] columnNames,String[] columnTypes) {
			this.data = data;
			this.columnNames = columnNames;
			this.columnTypes = columnTypes;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			System.out.println(data.get(row).length + " " + col);
			return data.get(row)[col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			if(DEBUG){
//        		System.out.println("Class of Column "+c+" is: "+getValueAt(0, c).getClass());
				try {
					System.out.println("Class of Column "+c+" is: "+ Class.forName(columnTypes[c]));
				} catch (ClassNotFoundException e) { 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
			try {
				return Class.forName(columnTypes[c]);
			} catch (ClassNotFoundException e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

/*
		  public boolean isCellEditable(int row, int col) { // Note that the
		  data/cell address is constant, // no matter where the cell appears
		  onscreen. if (col < 1) { return false; } else { return true; } }
		  
		  public void setValueAt(Object value, int row, int col) { if (DEBUG) {
		  System.out.println("Setting value at " + row + "," + col + " to " +
		  value + " (an instance of " + value.getClass() + ")"); }
		  
		  data.get(row)[col] = value; fireTableCellUpdated(row, col);
		  
		  if (DEBUG) { System.out.println("New value of data:");
		  printDebugData(); } }

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i = 0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j = 0; j < numCols; j++) {
					System.out.print("  " + data.get(i)[j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
*/		
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI(Vector<Object[]> data,
			String[] columnNames, String[] columnTypes) {
		// Create and set up the window.
		JFrame frame = new JFrame("Trend Analyzer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new TrendAnalyzer(data, columnNames, columnTypes);
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static class Start implements Runnable {
		private Vector<Object[]> data;
		private String[] columnNames;
		private String[] columnTypes;

		public Start(Vector<Object[]> data, String[] columnNames, String[] columnTypes) {
			this.data = data;
			this.columnNames = columnNames;
			this.columnTypes = columnTypes;
		}

		public void run() {
			createAndShowGUI(data, columnNames, columnTypes);
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		String[] columnNames = null;
		String[] columnTypes = null;
		final Vector<Object[]> data = new Vector<Object[]>();

//		Date date = sdf.parse("10/28/2009 02:30 PM +0000");
		try {
			BufferedReader in = new BufferedReader(new FileReader("F:\\airsampling.csv"));
//			BufferedReader in = new BufferedReader(new FileReader("F:\\airsampling.in"));
//			String cnames = in.readLine();
//			String ctypes = in.readLine();
			
			String ctypes = in.readLine();
			String cnames = in.readLine();
			
			columnNames = cnames.split(",");
			columnTypes = ctypes.split(",");
			
			for(int i=0;i<columnTypes.length;i++){
				if(columnTypes[i].equals("ID"))
					columnTypes[i]="java.lang.String";
				else if(columnTypes[i].equals("CAT"))
					columnTypes[i]="java.lang.String";
				else if(columnTypes[i].equals("CAT/SENSITIVE"))
					columnTypes[i]="java.lang.String";
				else if(columnTypes[i].equals("TIME"))
					columnTypes[i]="java.util.Date";
				else if(columnTypes[i].equals("CONT"))
					columnTypes[i]="java.lang.Integer";
				else if(columnTypes[i].equals("CONT/SENSITIVE"))
					columnTypes[i]="java.lang.Integer";
//				System.out.println(columnTypes[i]);
			}
//			System.out.println();
					
			String line = null;
			while ((line = in.readLine()) != null) {				
				String[] fieldStrings = line.split(",");
				Object[] oneRowdata = new Object[fieldStrings.length];
				
				for(int i=0;i<fieldStrings.length;i++){
//					System.out.println("I'm in for-loop");
					if(columnTypes[i].equals("CONT")||columnTypes[i].equals("CONT/SENSITIVE")){
						oneRowdata[i]=Integer.parseInt(fieldStrings[i]);
					}
					else if(columnTypes[i].equals("TIME")&&(!(fieldStrings[i].equals("")))){	
						oneRowdata[i]=df.parse(fieldStrings[i]);
					}
					else{
						oneRowdata[i]=fieldStrings[i];
					}					
				}	
//				System.out.println("I'm out of for-loop");

				data.add(oneRowdata);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		javax.swing.SwingUtilities.invokeLater(new Start(data, columnNames, columnTypes));
	}
}