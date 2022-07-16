// Conformative game client.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Button;
import java.awt.Label;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConformativeGame extends JFrame
{
   private static final long serialVersionUID = 1L;
	
   private ButtonHandler   buttonHandler;
   private JPanel          rootPanel;
   Label                   conformativeGameLabel;
   private JPanel          buttonPanel;
   private JPanel          hostButtonPanel;
   private JPanel          playerButtonPanel;
   private Button          hostButton;
   private Button          playerButton;

   public ConformativeGame()
   {
      // Button handler.
      buttonHandler = new ButtonHandler();

      // Root panel.
      rootPanel             = new JPanel();
      conformativeGameLabel = new Label("Conformative Game Roles");
      conformativeGameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      conformativeGameLabel.setSize("450px", "31px");
      rootPanel.add(conformativeGameLabel, 0, 0);
      buttonPanel = new JPanel();
      buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      rootPanel.add(buttonPanel, 0, 37);
      buttonPanel.setSize("450px", "24px");
      playerButtonPanel = new JPanel();
      buttonPanel.add(playerButtonPanel);
      playerButton = new Button("Player");
      playerButtonPanel.add(playerButton);
      hostButtonPanel = new HorizontalPanel();
      hostButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      buttonPanel.add(hostButtonPanel);
      hostButton = new Button("Host");
      hostButtonPanel.add(hostButton);
      hostButton.addClickHandler(buttonHandler);
      playerButton.addClickHandler(buttonHandler);
   }


   // Button handler.
   private class ButtonHandler implements ClickHandler
   {
      public void onClick(ClickEvent event)
      {
         if (event.getSource() == hostButton)
         {
            rootPanel.remove(conformativeGameLabel);
            rootPanel.remove(buttonPanel);
            new Host(gameService, rootPanel);
            return;
         }

         if (event.getSource() == playerButton)
         {
            rootPanel.remove(conformativeGameLabel);
            rootPanel.remove(buttonPanel);
            new Player(gameService, rootPanel);
            return;
         }
      }
   }
   
   // Main.
   public static void main(String[] args)
   { 
	   ConformativeGame game = new ConformativeGame();
   }
}
