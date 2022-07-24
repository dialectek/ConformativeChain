// Conformative game client.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dialectek.conformative.hyperledger.shared.Shared;

public class ConformativeGame extends JFrame implements ActionListener
{
   private static final long serialVersionUID = 1L;
	
   JPanel          rootPanel;
   private Button          hostButton;
   private Button          playerButton;

   public ConformativeGame()
   {
	  // Connect to network.
	  try
	  {
		  NetworkClient.init();
	  } catch (Exception e)
	  {
  		  JOptionPane.showMessageDialog(this, "Cannot connect to network");
		  //System.exit(1);
	  }
	  
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
        JTextField gameCodeText = new JTextField();
        JTextField transactionNumberText = new JTextField();
        Object[] message = {
            "Game code:", gameCodeText,
            "Transaction number (optional):", transactionNumberText
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Enter host information", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) 
        {
        	String gameCode = gameCodeText.getText();
        	if (Shared.isVoid(gameCode))
        	{
        		JOptionPane.showMessageDialog(this, "Please enter game code");
        		return;
        	}
        	String transactionNumber = transactionNumberText.getText();
        	if (!Shared.isVoid(transactionNumber))
        	{
        		try
        		{
        			if (Integer.parseInt(transactionNumber) < 0)
        			{
                		JOptionPane.showMessageDialog(this, "Invalid transaction number: " + transactionNumber);
                		return;         				
        			}
        		} catch (NumberFormatException e)
        		{
            		JOptionPane.showMessageDialog(this, "Invalid transaction number: " + transactionNumber);
            		return;        			
        		}
        	}
        	
        	// Get game state.
        	byte[] response = null;
    		try
    		{
    			if (Shared.isVoid(transactionNumber))
    			{
    				response = NetworkClient.contract.submitTransaction("requestService", Shared.SYNC_GAME, gameCode);
    			} else {
    				response = NetworkClient.contract.submitTransaction("requestService", Shared.SYNC_GAME, gameCode, transactionNumber);    				
    			}   			
    		}
    		catch(Exception e)
    		{
    			JOptionPane.showMessageDialog(this, "Cannot get game state");
    			return;
    		}
    		if (response == null)
    		{
        		JOptionPane.showMessageDialog(this, "Cannot get game state"); 
        		return;
    		}
        	new Host(response.toString());
        }        
        return;
     }

     if (event.getSource() == playerButton)
     {
         JTextField gameCodeText = new JTextField();
         JTextField playerNameText = new JTextField();
         JTextField transactionNumberText = new JTextField();
         Object[] message = {
             "Game code:", gameCodeText,
             "Player name:", playerNameText,
             "Transaction number (optional):", transactionNumberText
         };
         int option = JOptionPane.showConfirmDialog(this, message, "Enter player information", JOptionPane.OK_CANCEL_OPTION);
         if (option == JOptionPane.OK_OPTION) 
         {
         	String gameCode = gameCodeText.getText();
         	if (Shared.isVoid(gameCode))
         	{
         		JOptionPane.showMessageDialog(this, "Please enter game code");
         		return;
         	}
         	String playerName = playerNameText.getText();
         	if (Shared.isVoid(playerName))
         	{
         		JOptionPane.showMessageDialog(this, "Please enter player name");
         		return;
         	}         	
         	String transactionNumber = transactionNumberText.getText();
         	if (!Shared.isVoid(transactionNumber))
         	{
         		try
         		{
         			if (Integer.parseInt(transactionNumber) < 0)
         			{
                 		JOptionPane.showMessageDialog(this, "Invalid transaction number: " + transactionNumber);
                 		return;         				
         			}
         		} catch (NumberFormatException e)
         		{
             		JOptionPane.showMessageDialog(this, "Invalid transaction number: " + transactionNumber);
             		return;        			
         		}
         	}
         	
         	// Get player state.
         	byte[] response = null;
     		try
     		{
     			if (Shared.isVoid(transactionNumber))
     			{
     				response = NetworkClient.contract.submitTransaction("requestService", Shared.SYNC_PLAYER, gameCode, playerName);
     			} else {
     				response = NetworkClient.contract.submitTransaction("requestService", Shared.SYNC_PLAYER, gameCode, playerName, transactionNumber);    				
     			}   			
     		}
     		catch(Exception e)
     		{
     			JOptionPane.showMessageDialog(this, "Cannot get player state");
     			return;
     		}
     		if (response == null)
     		{
         		JOptionPane.showMessageDialog(this, "Cannot get player state"); 
         		return;
     		}
         	//new Player(response.toString());
         }
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