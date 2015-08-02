/*************************************************************************
ChatFor2.java
Author:	Travus Helmly

The following is an application that creates a chat room for two
people.





************************************************************************/
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class ChatFor2 implements java.awt.event.ActionListener, Runnable
{
private String partnerName;

public static void main(String[] args) throws IOException
{
	System.out.println("wthelmly Travus Helmly");
	
//command-line arguments:
	
if (
(args.length > 2) ||
(args.length == 0))
{
System.out.println("\nFirst Partner:  ONLY Give your Chat Name in quotes");
System.out.println("Second Partner:  Give your Chat Name in quotes and the address displayed on your Partner's screen.");

return;
}
if (args.length == 1)
new ChatFor2(args[0]);
if (args.length == 2) {
new ChatFor2(args[0], args[1]);
}
}


private int portNumber = 5678;
private Socket s;
private ServerSocket ss;
private DataInputStream dis;
private DataOutputStream dos;
private String newLine = System.getProperty("line.separator");


//Create the GUI
private JFrame window = new JFrame("Chat For 2");
private JButton hiddenSendButton = new JButton();
private JTextArea inChatTextArea = new JTextArea(10, 40);
private JScrollPane inChatScrollPane = new JScrollPane(this.inChatTextArea);
private JTextArea outChatTextArea = new JTextArea(10, 40);
private JScrollPane outChatScrollPane = new JScrollPane(this.outChatTextArea);
private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.outChatScrollPane, this.inChatScrollPane);

//constructor for first partner
public ChatFor2(String chatName) throws IOException
{
this(chatName, null);
}

//constructor for second partner
public ChatFor2(String chatName, String partnerAddress) throws IOException
{





this.window.getContentPane().add(this.hiddenSendButton);
this.window.getContentPane().add(this.splitPane);
this.window.getContentPane().setBackground(Color.RED);


this.hiddenSendButton.setMnemonic(10);
this.outChatTextArea.setEditable(false);
this.inChatTextArea.setEditable(false);
this.outChatTextArea.setFont(new Font("Univers", 5, 25));
this.inChatTextArea.setFont(new Font("Univers", 5, 25));
this.outChatTextArea.setBackground(Color.RED);
this.inChatTextArea.setBackground(Color.RED);
this.outChatTextArea.setForeground(Color.WHITE);
this.outChatTextArea.setForeground(Color.WHITE);

this.splitPane.setDividerLocation(300);


this.hiddenSendButton.addActionListener(this);
this.window.setDefaultCloseOperation(3);


this.window.setSize(350, 500);
this.window.setVisible(true);
//Disable horizontal scrolling
this.outChatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//Start a new line automatically
this.outChatScrollPane.setPreferredSize(new Dimension(window.getWidth(),window.getHeight()));
this.outChatTextArea.setLineWrap(true);
this.outChatTextArea.setWrapStyleWord(true);

//Disable horizontal scrolling
this.inChatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//Start a new line automatically
this.inChatScrollPane.setPreferredSize(new Dimension(window.getWidth(),window.getHeight()));
this.inChatTextArea.setLineWrap(true);
this.inChatTextArea.setWrapStyleWord(true);


//Establish the connection

try
{
if (partnerAddress == null)
{
this.ss = new ServerSocket(this.portNumber);
this.outChatTextArea.setText("...Awaiting your chat partner at address " +
InetAddress.getLocalHost().getHostAddress() +
" on port " +
this.ss.getLocalPort());
this.s = this.ss.accept();
this.dis = new DataInputStream(this.s.getInputStream());
this.dos = new DataOutputStream(this.s.getOutputStream());
String firstMessage = this.dis.readUTF();
if (firstMessage.startsWith(":"))
{
this.dos.writeUTF("accepted:" + chatName);
this.partnerName = firstMessage.substring(1).trim();
this.outChatTextArea.append(String.valueOf(this.newLine) + "You are now talking with " + this.partnerName);

this.window.setTitle("You are talking with " + this.partnerName + " Use alt-Enter to send.");
this.inChatTextArea.setEditable(true);
this.inChatTextArea.requestFocus();

new Thread(this).start();
}
else
{
this.outChatTextArea.append(String.valueOf(this.newLine) + "Invalid Protocol.");
this.outChatTextArea.append(String.valueOf(this.newLine) + "Close window to exit. Then restart to accept a reconnect.");
System.out.println("Chat 2 partner rejected. First message was : " + firstMessage);
this.dos.writeUTF("Invalid Protocol.");
this.dis.close();
return;
}
}
else
{
this.outChatTextArea.setText("Connecting to chat partner at " +
partnerAddress +
" on port " +
this.portNumber);
this.s = new Socket(partnerAddress, this.portNumber);
this.dis = new DataInputStream(this.s.getInputStream());
this.dos = new DataOutputStream(this.s.getOutputStream());
this.dos.writeUTF(":" + chatName);
String serverReply = this.dis.readUTF();
if (serverReply.startsWith("accepted:"))
{
this.partnerName = serverReply.substring(9).trim();
this.outChatTextArea.append(String.valueOf(this.newLine) + "You are talking to " + this.partnerName);

this.window.setTitle("You are talking to " + this.partnerName + " Use alt-Enter to send.");
this.inChatTextArea.setEditable(true);
this.inChatTextArea.requestFocus();

new Thread(this).start();
}
else
{
this.outChatTextArea.append(String.valueOf(this.newLine) + serverReply);
this.outChatTextArea.append(String.valueOf(this.newLine) + "Check your server address and port number.");
this.outChatTextArea.append(String.valueOf(this.newLine) + "Restart the program.");
System.out.println("Chat 2 partner rejected connect request. Reply was: " + serverReply);
return;
}
}
}
catch (IOException e)
{
this.outChatTextArea.append(String.valueOf(this.newLine) + e.toString());
this.outChatTextArea.append(String.valueOf(this.newLine) + "Check your server address and port number.");
this.outChatTextArea.append(String.valueOf(this.newLine) + "Restart the program.");
throw e;
}
}






public void actionPerformed(java.awt.event.ActionEvent ae)
{
String chat = this.inChatTextArea.getText().trim();
if (chat.length() == 0) return;
this.inChatTextArea.setText("");

this.outChatTextArea.append(String.valueOf(this.newLine) + "I said: " + chat);
try {
this.dos.writeUTF(chat);
}
catch (IOException localIOException) {}
}





public void run()
{
try
{
for (;;)
{
String chatMessage = this.dis.readUTF();
this.outChatTextArea.append(String.valueOf(this.newLine) + this.partnerName + " said: " + chatMessage);
this.outChatTextArea.setCaretPosition(this.outChatTextArea.getDocument().getLength());
this.inChatTextArea.requestFocus();
}
}
catch (IOException e)
{
this.outChatTextArea.append(String.valueOf(this.newLine) + e.toString());
this.outChatTextArea.append(String.valueOf(this.newLine) + "Restart the program");
this.inChatTextArea.setEditable(false);
}
}
} 