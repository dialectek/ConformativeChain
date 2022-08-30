// Conformative game role client.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.dialectek.conformative.hyperledger.shared.DelimitedString;
import com.dialectek.conformative.hyperledger.shared.Shared;

public class ConformativeGame extends JFrame implements ActionListener
{
   private static final long serialVersionUID = 1L;

   private JPanel rootPanel;
   private Button hostButton;
   private Button playerButton;

   private ArrayList<Host>   hosts;
   private ArrayList<Player> players;

   private String gameCode          = null;
   private String blockchainAddress = null;

   public ConformativeGame()
   {
      hosts   = new ArrayList<Host>();
      players = new ArrayList<Player>();

      // Set title.
      setTitle("Conformative Game Roles");
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter()
                        {
                           @Override
                           public void windowClosing(WindowEvent e)
                           {
                              for (Host host : hosts)
                              {
                                 host.terminate();
                              }
                              for (Player player : players)
                              {
                                 player.terminate();
                              }
                              System.exit(0);
                           }
                        }
                        );

      // Set fixed-width font.
      setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

      // Create GUI.
      rootPanel = new JPanel();
      rootPanel.setLayout(new FlowLayout());
      add(rootPanel);
      hostButton = new Button("Host");
      hostButton.addActionListener(this);
      rootPanel.add(hostButton);
      playerButton = new Button("Player");
      playerButton.addActionListener(this);
      rootPanel.add(playerButton);

      // Show.
      pack();
      setSize(350, 80);
      setLocationRelativeTo(null);
      setVisible(true);
   }


   // Button listener.
   public void actionPerformed(ActionEvent event)
   {
      if (event.getSource() == hostButton)
      {
         // Get host information.
         if (blockchainAddress == null)
         {
            JTextField gameCodeText = new JTextField();
            if (gameCode != null) { gameCodeText.setText(gameCode); }
            JTextField blockchainAddressText = new JTextField();

            Object[] message =
            {
               "Game code:",          gameCodeText,
               "Blockchain address:", blockchainAddressText
            };
            int option = JOptionPane.showConfirmDialog(null, message, "Enter host information", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
               gameCode = gameCodeText.getText();
               if (Shared.isVoid(gameCode) || gameCode.contains(DelimitedString.DELIMITER))
               {
                  gameCode = null;
                  JOptionPane.showMessageDialog(null, "Invalid game code");
                  return;
               }
               blockchainAddress = blockchainAddressText.getText();
               if (Shared.isVoid(blockchainAddress))
               {
                  blockchainAddress = null;
                  JOptionPane.showMessageDialog(null, "Invalid blockchain address");
                  return;
               }

               // Connect to network.
               try
               {
                  if (!NetworkClient.init(blockchainAddress))
                  {
                     blockchainAddress = null;
                     JOptionPane.showMessageDialog(null, "Cannot connect to network");
                  }
               }
               catch (Exception e)
               {
                  blockchainAddress = null;
                  JOptionPane.showMessageDialog(null, "Cannot connect to network: " + e.getMessage());
               }

               // Run host client.
               try
               {
                  hosts.add(new Host(gameCode));
               }
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(this, "Error running host: " + e.getMessage());
               }
            }
         }
         else
         {
            JTextField gameCodeText = new JTextField();
            Object[] message =
            {
               "Game code:", gameCodeText
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Enter host information", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
               gameCode = gameCodeText.getText();
               if (Shared.isVoid(gameCode) || gameCode.contains(DelimitedString.DELIMITER))
               {
                  gameCode = null;
                  JOptionPane.showMessageDialog(this, "Invalid game code");
                  return;
               }

               // Run host client.
               try
               {
                  hosts.add(new Host(gameCode));
               }
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(this, "Error running host: " + e.getMessage());
               }
            }
         }
         return;
      }

      if (event.getSource() == playerButton)
      {
         // Get player information.
         if (blockchainAddress == null)
         {
            JTextField gameCodeText = new JTextField();
            if (gameCode != null) { gameCodeText.setText(gameCode); }
            JTextField playerNameText        = new JTextField();
            JTextField blockchainAddressText = new JTextField();
            Object[] message =
            {
               "Game code:",          gameCodeText,
               "Player name:",        playerNameText,
               "Blockchain address:", blockchainAddressText
            };
            int option = JOptionPane.showConfirmDialog(null, message, "Enter player information", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
               gameCode = gameCodeText.getText();
               if (Shared.isVoid(gameCode) || gameCode.contains(DelimitedString.DELIMITER))
               {
                  gameCode = null;
                  JOptionPane.showMessageDialog(null, "Invalid game code");
                  return;
               }
               String playerName = playerNameText.getText();
               if (Shared.isVoid(playerName) ||
                   playerName.contains(DelimitedString.DELIMITER) ||
                   playerName.equals(Shared.ALL_PLAYERS))
               {
                  JOptionPane.showMessageDialog(this, "Invalid player name");
                  return;
               }
               blockchainAddress = blockchainAddressText.getText();
               if (Shared.isVoid(blockchainAddress))
               {
                  blockchainAddress = null;
                  JOptionPane.showMessageDialog(null, "Invalid blockchain address");
                  return;
               }

               // Connect to network.
               try
               {
                  if (!NetworkClient.init(blockchainAddress))
                  {
                     blockchainAddress = null;
                     JOptionPane.showMessageDialog(null, "Cannot connect to network");
                  }
               }
               catch (Exception e)
               {
                  blockchainAddress = null;
                  JOptionPane.showMessageDialog(null, "Cannot connect to network: " + e.getMessage());
               }

               // Register user.
               try
               {
                  NetworkClient.registerUser(playerName);
               }
               catch (Exception e)
               {
                  String emessage = e.getMessage();
                  if (!emessage.contains("is already registered"))
                  {
                     JOptionPane.showMessageDialog(this, "Cannot register player as network user: " + emessage);
                  }
               }

               // Run player client.
               try
               {
                  players.add(new Player(gameCode, playerName));
               }
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(this, "Error running player: " + e.getMessage());
               }
            }
         }
         else
         {
            JTextField gameCodeText = new JTextField();
            if (gameCode != null) { gameCodeText.setText(gameCode); }
            JTextField playerNameText = new JTextField();
            Object[] message =
            {
               "Game code:",   gameCodeText,
               "Player name:", playerNameText
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Enter player information", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
               String gameCode = gameCodeText.getText();
               if (Shared.isVoid(gameCode) || gameCode.contains(DelimitedString.DELIMITER))
               {
                  gameCode = null;
                  JOptionPane.showMessageDialog(this, "Invalid game code");
                  return;
               }
               String playerName = playerNameText.getText();
               if (Shared.isVoid(playerName) ||
                   playerName.contains(DelimitedString.DELIMITER) ||
                   playerName.equals(Shared.ALL_PLAYERS))
               {
                  JOptionPane.showMessageDialog(this, "Invalid player name");
                  return;
               }

               // Register user.
               try
               {
                  NetworkClient.registerUser(playerName);
               }
               catch (Exception e)
               {
                  String emessage = e.getMessage();
                  if (!emessage.contains("is already registered"))
                  {
                     JOptionPane.showMessageDialog(this, "Cannot register player as network user: " + emessage);
                  }
               }

               // Run player client.
               try
               {
                  players.add(new Player(gameCode, playerName));
               }
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(this, "Error running player: " + e.getMessage());
               }
            }
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
