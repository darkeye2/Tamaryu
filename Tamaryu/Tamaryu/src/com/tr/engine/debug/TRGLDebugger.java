package com.tr.engine.debug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.tr.engine.grf.gl.TRGLRenderContext;
import com.tr.engine.grf.gl.TRGLScene;

/* Debugger console using java swing gui.
 * Error output on console
 * Warning output on console
 * 
 */
public class TRGLDebugger extends TRDebugger {
	private JFrame gui = new JFrame("GLDebugger Console");
	private JEditorPane console = new JEditorPane();
	private JScrollPane scroll = new JScrollPane();
	private JTextField tf = new JTextField();
	
	private ArrayList<String> history = new ArrayList<String>();
	private int curH = history.size();

	private ArrayList<String> commands = new ArrayList<String>();
	
	private DebugPrintStream consoleOut = new DebugPrintStream(new DebugOutputStream(), Color.green);
	private DebugPrintStream consoleErr = new DebugPrintStream(new DebugOutputStream(), Color.red);
	
	private PrintStream defaultOut, defaultErr;
	
	private StringBuilder sb = new StringBuilder("");
	private boolean pause = false;

	public TRGLDebugger(TRGLRenderContext rc) {
		super(rc);
		defaultOut = System.out;
		defaultErr = System.err;
		System.setOut(consoleOut);
		System.setErr(consoleErr);

		//init gui
		gui.setSize(new Dimension(405,315));
		gui.getContentPane().setLayout(new BorderLayout());
		//DefaultCaret caret = (DefaultCaret)console.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		tf.setPreferredSize(new Dimension(gui.getSize().width, 25));
		tf.setMargin(new Insets(0,10,0,0));
		tf.setCaretColor(Color.white);
		tf.setBackground(Color.black);
		tf.setForeground(Color.white);
		tf.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					getNext();
				}
				if(e.getKeyCode() == KeyEvent.VK_UP){
					getLast();
				}
				
			}
			
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String s = tf.getText();
					write("> "+s);
					addHistory(tf.getText());
					tf.setText("");
					String[] str = split(s);
					if(commands.contains(str[0])){
						handleCmd(str[0], str);
					}
				}
			}
			
		});
		gui.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				tf.setPreferredSize(new Dimension(gui.getSize().width, 25));
			}
		});
		gui.addWindowFocusListener(new WindowAdapter(){

			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				tf.requestFocus();
				
			}});
		console.setContentType("text/html");
		console.setForeground(Color.GREEN);
		console.setBackground(Color.BLACK);
		console.setMargin(new Insets(1, 10, 2, 1));
		/*console.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                int height = Math.min(console.getPreferredSize().height, MAX_HEIGHT_RSZ);
                Dimension preferredSize = scroll.getPreferredSize();
                preferredSize.height = height;
                scroll.setPreferredSize(preferredSize);
                gui.validate();
            }
        });*/
		//console.setLineWrap(true);
	    //console.setWrapStyleWord(true);
		scroll.setPreferredSize(new Dimension(400, 300));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.gray, Color.darkGray));
		scroll.setViewportView(console);
		scroll.setAutoscrolls(true);
		console.setEditable(false);
		
		initCmd();
		
		gui.add(scroll, BorderLayout.CENTER);
		gui.add(tf, BorderLayout.SOUTH);
		gui.setVisible(true);
	}
	
	private void initCmd(){
		commands.add("throw");
		commands.add("show");
		commands.add("clear");
		commands.add("close");
		commands.add("info");
		commands.add("help");
		commands.add("titel");
		commands.add("rp");
		commands.add("pause");
	}
	
	public void stop(){
		System.setOut(defaultOut);
		System.setErr(defaultErr);
		gui.setVisible(false);
		gui.dispose();
		history.clear();
		commands.clear();
		this.rc = null;
	}

	@Override
	public void log(String msg) {
		System.out.println(msg);
	}

	@Override
	public void logErr(String err) {
		System.err.println(err);
	}
	
	private void handleCmd(String cmd, String[] args){
		if(cmd.equalsIgnoreCase("clear")){
			this.clear();
			return;
		}else if(cmd.equalsIgnoreCase("throw")){
			if(args.length < 2){
				System.err.println("Not anough parameter for throw command");
				return;
			}
			if(args[1].equalsIgnoreCase("error")){
				Exception e = new Exception("This ist a test Exception");
				e.printStackTrace();
				System.out.print("\r\n");
				return;
			}
		}else if(cmd.equalsIgnoreCase("close")){
			System.exit(0);
		}else if(cmd.equalsIgnoreCase("info")){
			printInfo();
			return;
		}else if(cmd.equalsIgnoreCase("help")){
			System.out.print("Known Commands:");
			for(String c : commands){
				System.out.print("\t"+c);
			}
			System.out.print("\r\n");
			return;
		}else if(cmd.equalsIgnoreCase("titel")){
			if(args.length < 2){
				System.out.println("Window Titel: "+rc.createWindow().getTitel());
				return;
			}
			rc.createWindow().setTitel(args[1].replaceAll("\"", ""));
			return;
		}else if(cmd.equalsIgnoreCase("rp")){
			if(args.length < 3){
				System.err.println("Not anough parameter for rp command (rp propName Value)");
				return;
			}
			boolean b = !args[2].equalsIgnoreCase("0");
			if(args[1].equalsIgnoreCase("antialising")){
				((TRGLScene) rc.getScene()).setAntialising(b);
				return;
			}else if(args[1].equalsIgnoreCase("wiremode")){
				((TRGLScene) rc.getScene()).setWireMode(b);
				return;
			}
			System.err.println("Unknown propertie: "+args[1]);
			return;
		}else if(cmd.equalsIgnoreCase("pause")){
			this.pause = !this.pause;
		}
		
		System.err.println("Command "+cmd+" is not defined!");
	}
	
	public void clear(){
		sb.delete(0, sb.length()-1);
		console.setText("");
	}
	
	private void printInfo(){
		System.out.print("TRGLDebugger Info (Used memory is heap size!):");
		System.out.print("\tUsed Mem: "+Runtime.getRuntime().totalMemory());
		System.out.print("\tFree Mem: "+Runtime.getRuntime().freeMemory()+"\r\n");
	}
	
	private void addHistory(String s){
		if(history.size()>50){
			history.remove(0);
		}
		
		if (history.size() <= 0 || !history.get(history.size()-1).equals(s)) {
			history.add(s);
		}

		curH = history.size();
	}

	private void getLast(){
		if(history.size()>0 && curH>0){
			tf.setText(history.get(--curH));
		}
	}

	private void getNext(){
		if(history.size()>0 && curH<(history.size()-1)){
			tf.setText(history.get(++curH));
		}else{
			tf.setText("");
			curH = history.size();
			
		}
	}
	
	public void write(String s){
		System.out.println(s);
		//console.setCaretPosition(console.getText().length());
		/*if(console.getLineCount()>20){
			while(console.getLineCount()>1500){
				try {
					console.replaceRange("", 0, console.getLineEndOffset(0));
				} catch (BadLocationException e) {
					write("[ERROR in GuiLog.write]  "+e.getMessage()+"\r\n");
				}
			}
		}*/
	}
	
	public String escapeString(String s){
		return escapeString(s, false);
	}
	
	public static String[] split(String s){
		String regex = "\"([^\"]*)\"|(\\S+)";
		ArrayList<String> allMatches = new ArrayList<String>();

	    Matcher m = Pattern.compile(regex).matcher(s);
	    while (m.find()) {
	    	allMatches.add(m.group());
	    }
	    
	    return allMatches.toArray(new String[allMatches.size()]);
	}
	
	public String escapeString(String s, boolean elf){
		//special html chars
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("  ", " &nbsp;");
		//s = s.replaceAll("[ ]{2, }", " $nbsp;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("\t", " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		
		//uml
		s = s.replaceAll("Ä", "&Auml;");
		s = s.replaceAll("ä", "&auml;");
		s = s.replaceAll("Ö", "&Ouml;");
		s = s.replaceAll("ö", "&ouml;");
		s = s.replaceAll("Ü", "&Uuml;");
		s = s.replaceAll("ü", "&uuml;");
		s = s.replaceAll("ß", "&szlig;");
		
		//line feed
		/*if(elf &&s.endsWith("\r\n")){
			s = s.substring(0, s.length()-2);
		}else if(elf && s.endsWith("\r") || s.endsWith("\n")){
			s = s.substring(0, s.length()-1);
		}*/
		s = s.replaceAll("\r\n", "<br>");
		s = s.replaceAll("\r", "<br>");
		s = s.replaceAll("\n", "<br>");
		
		
		return s;
	}
	
	
	private class DebugPrintStream extends PrintStream{
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
		Color c = Color.green;
		String cString = null;
		boolean lf = false;

		public DebugPrintStream(OutputStream arg0, Color c) {
			super(arg0);
			this.c = c;
			cString = "#"+Integer.toHexString(c.getRGB());
			cString = cString.substring(3);
		}
		
		public void print(String s){
			if(pause)
				return;
			if(s.startsWith("\t")){
				super.print("<br>");
			}else if(s.equalsIgnoreCase("\r\n")){
				super.print("<br>");
				this.flush();
				return;
			}
				
			s = escapeString(s);
			
			super.print("<font color='"+cString+"'>"+sdf.format(new Date())+"   "+s+"</font>");
			this.flush();
		}
		
		public void println(String s){
			if(pause)
				return;
			lf = true;
			print(s);
			super.print("<br>");
		}
		
		
	}
	
	
	private class DebugOutputStream extends OutputStream{
		
		public DebugOutputStream(){
			
		}

		@Override
		public void write(int arg0) throws IOException {
			sb.append(""+(char)arg0);
			//console.setText("<HTML><BODY>"+sb.toString()+"</BODY></HTML>");
			/*HTMLDocument doc = (HTMLDocument) console.getDocument();
		     try {
				doc.insert .insertHTML(doc.getLength(), ""+(char)arg0, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}*/
			//console.append(""+(char)arg0);
		}
		
		public void flush() throws IOException{
			super.flush();
			console.setText("<HTML><BODY>"+sb.toString()+"</BODY></HTML>");
			console.setCaretPosition(console.getDocument().getLength());
		}
		
	}
	
	
	
	
	public static void main(String[] args){
		new TRGLDebugger(null);
	}
	
	



}









