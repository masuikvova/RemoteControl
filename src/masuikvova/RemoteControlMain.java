package masuikvova;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;



public class RemoteControlMain {
	
	public static final String APP_NAME  = "RemoteControl";
	public static final String ICON_SRC = "C:\\RemoteControl\\remote.png";
	
	private static SerialPort recivPort = null;
	private static JFrame frame;
	private static JTextArea textArea = new JTextArea(5, 20);
	private static Imitator imitator = new Imitator();
	private static int errcount = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
		        CreateFrame();
		      }
		    });		
	}
	private static void CreateFrame(){
		frame = new JFrame(APP_NAME);
	    frame.setMinimumSize(new Dimension(800, 200));
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.pack();
	    frame.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {	
			}
			public void windowClosed(WindowEvent arg0) {
			}
			public void windowClosing(WindowEvent arg0) {	
			}
			public void windowDeactivated(WindowEvent arg0) {	
			}
			public void windowDeiconified(WindowEvent arg0) {	
			}
			public void windowIconified(WindowEvent arg0) {
				frame.setVisible(false);
			}
			public void windowOpened(WindowEvent arg0) {	
			}	    	
	    });
	    JButton button = new JButton();
	    button.setText("HELP!");
	    button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(frame,
					    "1 - Mute\n"
					    + "2 - Max Volume\n"
					    + "3 - Volume Up\n"
					    + "4 - ShutDown Windows after half hour\n"
					    + "5 - Space\n"
					    + "6 - Volume Down\n"
					    + "7 - Win + D\n"
					    + "8 - Lock Windows\n"
					    + "9 - Esc\n"
					    + "* - Windows Sleep mode\n"
					    + "0 - Change mode(mouse or key)\n"
					    + "# - ShutDown Windows",
					    "HELP!!!!!!",
					    JOptionPane.WARNING_MESSAGE);
				
			}
	    	
	    });
	    button.setSize(10, 10);
	    JPanel pane = new JPanel();
	    pane.setLayout(new BorderLayout());
	    pane.add(button,BorderLayout.NORTH);
	    textArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(textArea);
	    pane.add(scrollPane,BorderLayout.CENTER);
	    frame.add(pane);
	    Connect();
	    setTrayIcon();
		
	}
	private static void setTrayIcon() {
	    if(! SystemTray.isSupported() ) {
	      return;
	    }
	    PopupMenu trayMenu = new PopupMenu();
	    MenuItem item = new MenuItem("Exit");
	    item.addActionListener(new ActionListener() {	     		
				public void actionPerformed(ActionEvent arg0) {					
					System.exit(0);				
					}
	    });
	    MenuItem item_maximize = new MenuItem("Show program");
	    item_maximize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(true);
			}	    	
	    });
	    trayMenu.add(item);
	    trayMenu.add(item_maximize);
	    Image icon = Toolkit.getDefaultToolkit().getImage(ICON_SRC);
	    TrayIcon trayIcon = new TrayIcon(icon, APP_NAME, trayMenu);
	    trayIcon.setImageAutoSize(true);
	    SystemTray tray = SystemTray.getSystemTray();
	    try {
	      tray.add(trayIcon);
	    } catch (AWTException e) {
	      e.printStackTrace();
	    }
	    trayIcon.displayMessage(APP_NAME, "Application started!",
	                            TrayIcon.MessageType.INFO);
	  }
	
	private static void Connect(){
		File fin = null;
		BufferedReader br = null;
		String port= null;
		
		textArea.append("Opening setting file.......\n");
		try {	
			fin = new File("C:\\RemoteControl\\RemoteControlSetting.txt");
			 br = new BufferedReader(new FileReader(fin));			
		} catch (FileNotFoundException e) {
			textArea.append(" Can`t open or find file C:\\RemoteControlSetting.txt "
					+ "\n If such file is not exist crate txt file "
					+ "\n with name RemoteControlSetting.txt and "
					+ "\n that must contain name of COMport to with connect "
					+ "\n arduino remote control device  example: COM6\n");
			frame.setVisible(false);
		}
		if(br !=null){
			try {
				port = br.readLine();
				System.out.println(port);
			} catch (IOException e) {
				e.printStackTrace();
				textArea.append(e.toString() + "\n");
				frame.setVisible(false);
		}
			textArea.append("Connecting to port "+ port + " ......\n" );
			recivPort = new SerialPort(port);
			try {
		    	recivPort.openPort();
		    	recivPort.setParams(SerialPort.BAUDRATE_115200,
		                             SerialPort.DATABITS_8,
		                             SerialPort.STOPBITS_1,
		                             SerialPort.PARITY_NONE);
		    	recivPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |  
		    								 SerialPort.FLOWCONTROL_RTSCTS_OUT);
		    	recivPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
		    	textArea.append("Connection to port "+ port + " : OK!\n" );	
			}
		    catch (SerialPortException e) {;
		        textArea.append(e.toString() + "\n"
		        		+ "Check device connection \n"
		        		+ "or enter valid number of COM port in RemoteContolSetting.txt\n");
		        frame.setVisible(true);
		    }
			
		}
		imitator.createImitator();
	}
	 private static class PortReader implements SerialPortEventListener {
			public void serialEvent(SerialPortEvent event) {
				if(event.isRXCHAR() && event.getEventValue() > 0){
		            try {	
		            	String data =  recivPort.readString(event.getEventValue());		            	
		            	ParseCommand(data);     
		            }
		            catch (SerialPortException e) {
		               // System.out.println(e);
		                textArea.append(e.toString());		                
		            }
		        }
			}
		}
	 
	 private static void ParseCommand(String data){
		 if(data.length()>0){
         	//System.out.print("Data : " + data);
             int i1 = data.indexOf("#");
             int i2 = data.indexOf(";");
             if(i1>=0 && i2>0){
             	 data = data.substring(i1+1,i2);
		         System.out.print("\tData : " + data);
             }
             else{ 
            	 //errcount+=1;
            	 //System.out.println("ERR : " + errcount);
            	 //System.out.println("\tData : " + "((");
             }            
             try{
	                int command = Integer.parseInt(data);
	                System.out.print("\t Int : " + command + "\n");
	                imitator.setComand(command);
	                //textArea.append(command + "\n");
             }catch(Exception e){
             	//System.out.println("NOT a number: " + e);
             }		         
         }
		 
	 }
}
