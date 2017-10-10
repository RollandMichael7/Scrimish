import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * Class for a simple text version of the game Scrimish via JPanel, with functionality for displaying input text from the user and displaying output text.
 * 
 * Info about the game Scrimish: http://www.scrimish.com/
 * 
 * @author Michael Rolland
 * @version 2017.10.10
 *
 */
public class ScrimishFrame extends JFrame implements KeyListener{
	private JPanel panel;
	private JTextArea output;
	private JTextArea input;
	private JScrollPane scroll;
	private Stack<String> inputs;
	
	public ScrimishFrame(String name) {
		super(name);
		
		inputs = new Stack<String>();
		panel = new JPanel();
		
		output = new JTextArea(20,75);
		output.setEditable(false);
		output.setFont(output.getFont().deriveFont(14f));
		scroll = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// Make the area automatically scroll as text is added.
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		input = new JTextArea(1,75);
		input.addKeyListener(this);
		
		setSize(900, 470);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		panel.add(scroll);
		panel.add(input);
		add(panel);
		setVisible(true);
		
		input.requestFocusInWindow();
	}
	
	/**
	 * Add text, and add a new line afterward.
	 */
	public void addTextln(String message) {
		output.append(message+"\n");
	}
	
	/**
	 * Add text
	 */
	public void addText(String message) {
		output.append(message);
	}
	
	/**
	 * Get the next input from the user
	 */
	public String getInput() {
		String s;
		while (true) {
			if (!inputs.isEmpty()) {
				s = inputs.pop();
				break;
			}
		}
		if (s.toUpperCase().equals("QUIT")) {
			JOptionPane.showMessageDialog(this, "Thanks for playing!");
			System.exit(0);
		}
		return s;
	}
	
	/**
	 * When the enter key is pressed, display the input on the output field and store it in the input stack.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			input.setEditable(false);
			String message = input.getText();
			input.setText("");
			output.append("> "+message+"\n");
			inputs.push(message);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			input.setEditable(true);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
