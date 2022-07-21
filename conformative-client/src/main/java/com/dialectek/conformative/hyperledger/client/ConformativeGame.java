// Conformative game client.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConformativeGame extends JFrame implements ActionListener
{
   private static final long serialVersionUID = 1L;
	
   JPanel          rootPanel;
   private Button          hostButton;
   private Button          playerButton;

   public ConformativeGame()
   {
      // Set title.
      setTitle("Conformative Game Roles");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Create GUI.
      rootPanel             = new JPanel();
      rootPanel.setLayout(new FlowLayout());
      add(rootPanel);
      playerButton = new Button("Player");
      playerButton.addActionListener(this);
      rootPanel.add(playerButton);
      hostButton = new Button("Host");
      hostButton.addActionListener(this);
      rootPanel.add(hostButton); 
      
      // Show.      
      pack(); 
      setSize(300, 80); 
      setVisible(true);
   }

   // Button listener.
   public void actionPerformed(ActionEvent event)
   {
     if (event.getSource() == hostButton)
     {
        rootPanel.remove(playerButton);
        rootPanel.remove(hostButton);
        new Host(this);
        return;
     }

     if (event.getSource() == playerButton)
     {
         rootPanel.remove(playerButton);
         rootPanel.remove(hostButton);
         return;
     }
   }
   
   // Main.
   public static void main(String[] args)
   {
	   @SuppressWarnings("unused")
	   ConformativeGame game = new ConformativeGame();
   }
}
