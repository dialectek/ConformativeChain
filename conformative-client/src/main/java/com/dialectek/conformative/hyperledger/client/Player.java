// Conformative game player client.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.dialectek.conformative.hyperledger.shared.DelimitedString;
import com.dialectek.conformative.hyperledger.shared.Shared;

public class Player extends JFrame implements ActionListener
{
   private static final long serialVersionUID = 1L;
	   
   // GUI: home, claim, and audit roles.
   private JTabbedPane        roleTabPanel;
   private JPanel             homePanel;
   private JPanel             playerPanel;   
   private Label              playerNameLabel;
   private TextField          playerNameTextBox;
   private Button             playerJoinQuitButton;
   private JPanel             gameCodePanel;
   private Label              gameCodeLabel;
   private TextField          gameCodeTextBox;
   private JPanel             homeResourcesCaptionPanel;
   private JPanel             homeResourcesTopPanel;
   private Label              homeResourcesActualLabel;
   private TextField          homeResourcesActualTextBox;
   private Label              homeResourcesPersonalLabel;
   private TextField          homeResourcesPersonalTextBox;
   private Label              homeResourcesCommonLabel;
   private TextField          homeResourcesCommonTextBox;
   private JPanel             homeResourcesBottomPanel;
   private Label              homeResourcesEntitledLabel;
   private TextField          homeResourcesEntitledTextBox;
   private JPanel             hostChatCaptionPanel;
   private TextArea           hostChatTextArea;
   private JPanel             hostChatClearPanel;
   private Button             hostChatClearButton;
   private TextField          hostChatTextBox;
   private JPanel             hostChatSendPanel;   
   private Button             hostChatSendButton;
   private JPanel             claimHistoryCaptionPanel;
   private TextArea           claimHistoryTextArea;
   private JPanel             auditHistoryCaptionPanel;
   private TextArea           auditHistoryTextArea;
   private JPanel             claimPanel;
   private JPanel             claimDistributionCaptionPanel;
   private Canvas             claimDistributionCanvas;
   private NormalDistribution claimDistribution;
   private JPanel             claimDistributionTestPanel;
   private Label              claimDistributionTestValueLabel;
   private TextField          claimDistributionTestValueTextBox;
   private Button             claimDistributionTestButton;
   private TextField          claimDistributionTestProbabilityTextBox;
   private JPanel             claimResourcesCaptionPanel;
   private JPanel             claimResourcesEntitledPanel;   
   private Label              claimResourcesEntitledLabel;
   private TextField          claimResourcesEntitledTextBox;
   private Label              claimResourcesEntitledEqualsLabel;
   private TextField          claimResourcesEntitledPerPlayerTextBox;
   private Label              claimResourcesEntitledTimesLabel;
   private TextField          claimResourcesEntitledNumPlayersTextBox;
   private Label              claimResourcesEntitledPlayersLabel;
   private JPanel             claimResourcesClaimPanel;   
   private Label              claimResourcesClaimLabel;
   private TextField          claimResourcesClaimTextBox;
   private Button             claimResourcesSetButton;
   private JPanel             claimResourcesGrantPanel;   
   private Label              claimResourcesGrantLabel;
   private TextField          claimResourcesGrantTextBox;
   private JPanel             claimResourcesPenaltyPanel;   
   private Label              claimResourcesPenaltyLabel;
   private TextField          claimResourcesPenaltyTextBox;
   private JPanel             claimResourcesDonatePanel;   
   private Label              claimResourcesDonateLabel;
   private TextField          claimResourcesDonateTextBox;
   private Label              claimResourcesDonateBeneficiaryLabel;
   private TextField          claimResourcesDonateBeneficiaryTextBox;
   private Button             claimResourcesDonateButton;
   private JPanel             claimFinishPanel;
   private Button             claimFinishButton;
   private JPanel             auditorChatCaptionPanel;
   private TextArea           auditorChatTextArea;
   private TextField          auditorChatTextBox;
   private JPanel             auditorChatSendPanel;
   private Button             auditorChatSendButton;
   private JPanel             auditPanel;
   private JPanel             claimantNamePanel;
   private Label              claimantNameLabel;
   private TextField          claimantNameTextBox;
   private JPanel             auditDistributionCaptionPanel;
   private Canvas             auditDistributionCanvas;
   private NormalDistribution auditDistribution;
   private JPanel             auditDistributionTestPanel;
   private Label              auditDistributionTestValueLabel;
   private TextField          auditDistributionTestValueTextBox;
   private Button             auditDistributionTestButton;
   private TextField          auditDistributionTestProbabilityTextBox;
   private JPanel             auditResourcesCaptionPanel;
   private JPanel             auditResourcesClaimPanel;
   private Label              auditResourcesClaimLabel;
   private TextField          auditResourcesClaimTextBox;
   private Label              auditResourcesClaimEqualsLabel;
   private TextField          auditResourcesClaimPerPlayerTextBox;
   private Label              auditResourcesClaimTimesLabel;
   private TextField          auditResourcesClaimNumPlayersTextBox;
   private Label              auditResourcesClaimPlayersLabel;
   private JPanel             auditResourcesGrantPanel;
   private Label              auditResourcesGrantLabel;
   private TextField          auditResourcesGrantTextBox;
   private Button             auditResourcesGrantSetButton;
   private Label              auditResourcesGrantConsensusLabel;
   private TextField          auditResourcesConsensusTextBox;
   private JPanel             auditResourcesPenaltyPanel;
   private Label              auditResourcesPenaltyLabel;
   private TextField          auditResourcesPenaltyTextBox;
   private Button             auditFinishButton;
   private JPanel             claimantChatCaptionPanel;
   private JPanel             claimantChatPanel;
   private TextArea           claimantChatTextArea;
   private TextField          claimantChatTextBox;
   private Button             claimantChatSendButton;
   private Font               labelFont;
   private boolean UIlocked = true;
   
   private static final int HOME_TAB  = 0;
   private static final int CLAIM_TAB = 1;
   private static final int AUDIT_TAB = 2;

   // Player name and game code.
   private String playerName;
   private String gameCode;

   // Game state.
   private int gameState;
   
   // Player state.
   private boolean playerState;
   
   // Transaction state.
   public enum TRANSACTION_STATE
   {
      INACTIVE,
      PENDING,
      WAITING,
      FINISHED
   }

   // Claim transaction state.
   private TRANSACTION_STATE claimState;

   // Audit transaction state.
   private TRANSACTION_STATE auditState;

   // Transaction number.
   private int transactionNumber;

   // Timer.
   private final int timerInterval_ms = 500;
   private Timer     timer;
   private final int syncFreq = 10;
   private int syncCounter = 0;
   
   // Constructor.
   public Player(String gameCode, String playerName, int transactionNumber) throws Exception
   {
	  this.gameCode = gameCode;
	  if (Shared.isVoid(gameCode))
	  {
          JOptionPane.showMessageDialog(this, "Invalid game code: " + gameCode);
          throw new Exception("Invalid game code");
	  }
	  this.playerName = playerName;
	  if (Shared.isVoid(playerName) ||
			  playerName.contains(DelimitedString.DELIMITER) ||
			  playerName.equals(Shared.ALL_PLAYERS))
	  {
          JOptionPane.showMessageDialog(this, "Invalid player name: " + playerName);
          throw new Exception("Invalid player name");
	  }
	  if (!Pattern.matches("[a-zA-Z0-9]+", playerName)) 
	  {
          JOptionPane.showMessageDialog(this, "Invalid player name: " + playerName);
          throw new Exception("Invalid player name");
	  }
	  try 
	  {
		    Integer.parseInt(playerName);
	        JOptionPane.showMessageDialog(this, "Invalid player name: " + playerName);
	        throw new Exception("Invalid player name");
	  } catch (NumberFormatException e) {}	  
	  this.transactionNumber = transactionNumber;
	  if (transactionNumber < -1)
	  {
          JOptionPane.showMessageDialog(this, "Invalid transaction number: " + transactionNumber);
          throw new Exception("Invalid transaction number");
	  }
	  
      // Title.
      setTitle("Conformative Game Player");
      
      // Set fixed-width font for labels.
 	  labelFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);      

      // Role tabs.
      roleTabPanel = new JTabbedPane();

      // Home tab.
      homePanel = new JPanel();
      homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS)); 
      roleTabPanel.add(homePanel, "Home");
      playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      homePanel.add(playerPanel);
      playerNameLabel = newLabel("Player name:");      
      playerPanel.add(playerNameLabel);
      playerNameTextBox = new TextField(30);
      playerNameTextBox.setEditable(false);
      playerNameTextBox.setText(playerName);
      playerPanel.add(playerNameTextBox);
      playerJoinQuitButton = new Button("Join");
      playerJoinQuitButton.addActionListener(this);
      playerPanel.add(playerJoinQuitButton);
      gameCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      homePanel.add(gameCodePanel);
      gameCodeLabel = newLabel("Game code:  ");
      gameCodePanel.add(gameCodeLabel);
      gameCodeTextBox = new TextField(30);
      gameCodeTextBox.setEditable(false);
      gameCodeTextBox.setText(gameCode);
      gameCodePanel.add(gameCodeTextBox);
      homeResourcesCaptionPanel = new JPanel();
      homeResourcesCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resources"));
      homeResourcesCaptionPanel.setLayout(new BoxLayout(homeResourcesCaptionPanel, BoxLayout.Y_AXIS));      
      homePanel.add(homeResourcesCaptionPanel);
      homeResourcesTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      homeResourcesCaptionPanel.add(homeResourcesTopPanel);
      homeResourcesActualLabel = newLabel("Actual:  ");
      homeResourcesTopPanel.add(homeResourcesActualLabel);
      homeResourcesActualTextBox = new TextField(10);
      homeResourcesActualTextBox.setEditable(false);
      homeResourcesTopPanel.add(homeResourcesActualTextBox);
      homeResourcesPersonalLabel = newLabel(" = Personal:");
      homeResourcesTopPanel.add(homeResourcesPersonalLabel);
      homeResourcesPersonalTextBox = new TextField(10);
      homeResourcesPersonalTextBox.setEditable(false);
      homeResourcesTopPanel.add(homeResourcesPersonalTextBox);
      homeResourcesCommonLabel = newLabel(" + Common:");
      homeResourcesTopPanel.add(homeResourcesCommonLabel);
      homeResourcesCommonTextBox = new TextField(10);
      homeResourcesCommonTextBox.setEditable(false);
      homeResourcesTopPanel.add(homeResourcesCommonTextBox);
      homeResourcesBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      homeResourcesCaptionPanel.add(homeResourcesBottomPanel);
      homeResourcesEntitledLabel = newLabel("Entitled:");
      homeResourcesBottomPanel.add(homeResourcesEntitledLabel);
      homeResourcesEntitledTextBox = new TextField(10);
      homeResourcesEntitledTextBox.setEditable(false);
      homeResourcesBottomPanel.add(homeResourcesEntitledTextBox);
      hostChatCaptionPanel = new JPanel();
      hostChatCaptionPanel.setBorder(BorderFactory.createTitledBorder("Host chat"));
      hostChatCaptionPanel.setLayout(new BoxLayout(hostChatCaptionPanel, BoxLayout.Y_AXIS));      
      homePanel.add(hostChatCaptionPanel);
      hostChatTextArea = new TextArea(10, 50);     
      hostChatTextArea.setEditable(false);     
      hostChatCaptionPanel.add(hostChatTextArea);
      hostChatClearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      hostChatCaptionPanel.add(hostChatClearPanel);
      hostChatClearButton = new Button("Clear");
      hostChatClearButton.addActionListener(this);
      hostChatClearPanel.add(hostChatClearButton);
      hostChatTextBox = new TextField(50);
      hostChatCaptionPanel.add(hostChatTextBox);      
      hostChatSendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      hostChatCaptionPanel.add(hostChatSendPanel);
      hostChatSendButton = new Button("Send");
      hostChatSendButton.addActionListener(this);
      hostChatSendPanel.add(hostChatSendButton);
      claimHistoryCaptionPanel = new JPanel();
      claimHistoryCaptionPanel.setBorder(BorderFactory.createTitledBorder("Claim history"));
      claimHistoryCaptionPanel.setLayout(new BoxLayout(claimHistoryCaptionPanel, BoxLayout.Y_AXIS));      
      homePanel.add(claimHistoryCaptionPanel);
      claimHistoryTextArea = new TextArea(5, 50);
      claimHistoryTextArea.setEditable(false);
      claimHistoryCaptionPanel.add(claimHistoryTextArea);
      auditHistoryCaptionPanel = new JPanel();
      auditHistoryCaptionPanel.setBorder(BorderFactory.createTitledBorder("Audit history"));
      auditHistoryCaptionPanel.setLayout(new BoxLayout(auditHistoryCaptionPanel, BoxLayout.Y_AXIS));      
      homePanel.add(auditHistoryCaptionPanel);
      auditHistoryTextArea = new TextArea(5, 50);
      auditHistoryTextArea.setEditable(false);
      auditHistoryCaptionPanel.add(auditHistoryTextArea);

      // Claim tab.
      claimPanel = new JPanel();
      claimPanel.setLayout(new BoxLayout(claimPanel, BoxLayout.Y_AXIS));        
      roleTabPanel.add(claimPanel, "Claim");
      claimDistributionCaptionPanel = new JPanel();
      claimDistributionCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resource entitlement probability"));
      claimDistributionCaptionPanel.setLayout(new BoxLayout(claimDistributionCaptionPanel, BoxLayout.Y_AXIS));      
      claimPanel.add(claimDistributionCaptionPanel);
      claimDistributionCanvas = new Canvas();
      claimDistribution       = new NormalDistribution(claimDistributionCanvas);
      claimDistribution.draw();
      claimDistributionCaptionPanel.add(claimDistributionCanvas);
      claimDistributionTestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimDistributionCaptionPanel.add(claimDistributionTestPanel);
      claimDistributionTestValueLabel = newLabel("Test value:");
      claimDistributionTestPanel.add(claimDistributionTestValueLabel);
      claimDistributionTestValueTextBox = new TextField(10);
      claimDistributionTestPanel.add(claimDistributionTestValueTextBox);
      claimDistributionTestButton = new Button("Probability:");
      claimDistributionTestPanel.add(claimDistributionTestButton);
      claimDistributionTestButton.addActionListener(this);
      claimDistributionTestProbabilityTextBox = new TextField(10);
      claimDistributionTestProbabilityTextBox.setEditable(false);
      claimDistributionTestPanel.add(claimDistributionTestProbabilityTextBox);
      claimResourcesCaptionPanel = new JPanel();
      claimResourcesCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resource transaction"));
      claimResourcesCaptionPanel.setLayout(new BoxLayout(claimResourcesCaptionPanel, BoxLayout.Y_AXIS));        
      claimPanel.add(claimResourcesCaptionPanel);
      claimResourcesEntitledPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimResourcesCaptionPanel.add(claimResourcesEntitledPanel);
      claimResourcesEntitledLabel = newLabel("Entitled:");
      claimResourcesEntitledPanel.add(claimResourcesEntitledLabel);
      claimResourcesEntitledTextBox = new TextField(10);
      claimResourcesEntitledTextBox.setEditable(false);
      claimResourcesEntitledPanel.add(claimResourcesEntitledTextBox);
      claimResourcesEntitledEqualsLabel = newLabel("=");
      claimResourcesEntitledPanel.add(claimResourcesEntitledEqualsLabel);
      claimResourcesEntitledPerPlayerTextBox = new TextField(10);
      claimResourcesEntitledPerPlayerTextBox.setEditable(false);
      claimResourcesEntitledPanel.add(claimResourcesEntitledPerPlayerTextBox);
      claimResourcesEntitledTimesLabel = newLabel("X");
      claimResourcesEntitledPanel.add(claimResourcesEntitledTimesLabel);
      claimResourcesEntitledNumPlayersTextBox = new TextField(10);
      claimResourcesEntitledNumPlayersTextBox.setEditable(false);
      claimResourcesEntitledPanel.add(claimResourcesEntitledNumPlayersTextBox);
      claimResourcesEntitledPlayersLabel = newLabel("players");
      claimResourcesEntitledPanel.add(claimResourcesEntitledPlayersLabel);
      claimResourcesClaimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimResourcesCaptionPanel.add(claimResourcesClaimPanel);     
      claimResourcesClaimLabel = newLabel("Claim:   ");
      claimResourcesClaimPanel.add(claimResourcesClaimLabel);
      claimResourcesClaimTextBox = new TextField(10);
      claimResourcesClaimPanel.add(claimResourcesClaimTextBox);
      claimResourcesSetButton = new Button("Set");
      claimResourcesClaimPanel.add(claimResourcesSetButton);
      claimResourcesSetButton.addActionListener(this);     
      claimResourcesGrantPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimResourcesCaptionPanel.add(claimResourcesGrantPanel);       
      claimResourcesGrantLabel = newLabel("Grant:   ");
      claimResourcesGrantPanel.add(claimResourcesGrantLabel);
      claimResourcesGrantTextBox = new TextField(10);
      claimResourcesGrantTextBox.setEditable(false);
      claimResourcesGrantPanel.add(claimResourcesGrantTextBox);
      claimResourcesPenaltyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimResourcesCaptionPanel.add(claimResourcesPenaltyPanel); 
      claimResourcesPenaltyLabel = newLabel("Penalty: ");
      claimResourcesPenaltyPanel.add(claimResourcesPenaltyLabel);
      claimResourcesPenaltyTextBox = new TextField(10);
      claimResourcesPenaltyTextBox.setEditable(false);
      claimResourcesPenaltyPanel.add(claimResourcesPenaltyTextBox);
      claimResourcesDonatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimResourcesCaptionPanel.add(claimResourcesDonatePanel);
      claimResourcesDonateLabel = newLabel("Donate:  ");
      claimResourcesDonatePanel.add(claimResourcesDonateLabel);
      claimResourcesDonateTextBox = new TextField(10);
      claimResourcesDonatePanel.add(claimResourcesDonateTextBox);
      claimResourcesDonateBeneficiaryLabel = newLabel("To:");
      claimResourcesDonatePanel.add(claimResourcesDonateBeneficiaryLabel);
      claimResourcesDonateBeneficiaryTextBox = new TextField(10);
      claimResourcesDonatePanel.add(claimResourcesDonateBeneficiaryTextBox);
      claimResourcesDonateButton = new Button("Send");
      claimResourcesDonateButton.setLabel("Go");
      claimResourcesDonateButton.addActionListener(this);
      claimResourcesDonatePanel.add(claimResourcesDonateButton);
      claimFinishPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimResourcesCaptionPanel.add(claimFinishPanel);           
      claimFinishButton = new Button("Finish");
      claimFinishButton.addActionListener(this);
      claimFinishPanel.add(claimFinishButton);
      auditorChatCaptionPanel = new JPanel();
      auditorChatCaptionPanel.setBorder(BorderFactory.createTitledBorder("Auditor chat"));
      auditorChatCaptionPanel.setLayout(new BoxLayout(auditorChatCaptionPanel, BoxLayout.Y_AXIS));      
      claimPanel.add(auditorChatCaptionPanel);
      auditorChatTextArea = new TextArea(5, 50);
      auditorChatTextArea.setEditable(false);
      auditorChatCaptionPanel.add(auditorChatTextArea);
      auditorChatTextBox = new TextField(50);
      auditorChatCaptionPanel.add(auditorChatTextBox);
      auditorChatSendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      auditorChatCaptionPanel.add(auditorChatSendPanel);       
      auditorChatSendButton = new Button("Send");
      auditorChatSendPanel.add(auditorChatSendButton);
      auditorChatSendButton.addActionListener(this);

      // Audit tab.
      auditPanel = new JPanel();
      auditPanel.setLayout(new BoxLayout(auditPanel, BoxLayout.Y_AXIS));       
      roleTabPanel.add(auditPanel, "Audit");
      claimantNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      auditPanel.add(claimantNamePanel);
      claimantNameLabel = newLabel("Claimant:");
      claimantNamePanel.add(claimantNameLabel);
      claimantNameTextBox = new TextField(30);
      claimantNameTextBox.setEditable(false);
      claimantNamePanel.add(claimantNameTextBox);
      auditDistributionCaptionPanel = new JPanel();
      auditDistributionCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resource entitlement probability"));
      auditDistributionCaptionPanel.setLayout(new BoxLayout(auditDistributionCaptionPanel, BoxLayout.Y_AXIS));      
      auditPanel.add(auditDistributionCaptionPanel);
      auditDistributionCanvas = new Canvas();
      auditDistribution       = new NormalDistribution(auditDistributionCanvas);
      auditDistribution.draw();
      auditDistributionCaptionPanel.add(auditDistributionCanvas);
      auditDistributionTestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      auditDistributionCaptionPanel.add(auditDistributionTestPanel);
      auditDistributionTestValueLabel = newLabel("Test value:");
      auditDistributionTestPanel.add(auditDistributionTestValueLabel);
      auditDistributionTestValueTextBox = new TextField(10);
      auditDistributionTestPanel.add(auditDistributionTestValueTextBox);
      auditDistributionTestButton = new Button("Probability:");
      auditDistributionTestPanel.add(auditDistributionTestButton);
      auditDistributionTestButton.addActionListener(this);
      auditDistributionTestProbabilityTextBox = new TextField(10);
      auditDistributionTestProbabilityTextBox.setEditable(false);
      auditDistributionTestPanel.add(auditDistributionTestProbabilityTextBox);
      auditResourcesCaptionPanel = new JPanel();
      auditResourcesCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resource transaction"));
      auditResourcesCaptionPanel.setLayout(new BoxLayout(auditResourcesCaptionPanel, BoxLayout.Y_AXIS));      
      auditPanel.add(auditResourcesCaptionPanel);
      auditResourcesClaimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      auditResourcesCaptionPanel.add(auditResourcesClaimPanel);
      auditResourcesClaimLabel = newLabel("Claim:  ");
      auditResourcesClaimPanel.add(auditResourcesClaimLabel);
      auditResourcesClaimTextBox = new TextField(10);
      auditResourcesClaimTextBox.setEditable(false);
      auditResourcesClaimPanel.add(auditResourcesClaimTextBox);
      auditResourcesClaimEqualsLabel = newLabel("=");
      auditResourcesClaimPanel.add(auditResourcesClaimEqualsLabel);
      auditResourcesClaimPerPlayerTextBox = new TextField(10);
      auditResourcesClaimPerPlayerTextBox.setEditable(false);
      auditResourcesClaimPanel.add(auditResourcesClaimPerPlayerTextBox);
      auditResourcesClaimTimesLabel = newLabel("X");
      auditResourcesClaimPanel.add(auditResourcesClaimTimesLabel);
      auditResourcesClaimNumPlayersTextBox = new TextField(10);
      auditResourcesClaimNumPlayersTextBox.setEditable(false);
      auditResourcesClaimPanel.add(auditResourcesClaimNumPlayersTextBox);
      auditResourcesClaimPlayersLabel = newLabel("players");
      auditResourcesClaimPanel.add(auditResourcesClaimPlayersLabel);
      auditResourcesGrantPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      auditResourcesCaptionPanel.add(auditResourcesGrantPanel);
      auditResourcesGrantLabel = newLabel("Grant:  ");
      auditResourcesGrantPanel.add(auditResourcesGrantLabel);
      auditResourcesGrantTextBox = new TextField(10);
      auditResourcesGrantPanel.add(auditResourcesGrantTextBox);
      auditResourcesGrantSetButton = new Button("Set");
      auditResourcesGrantSetButton.addActionListener(this);
      auditResourcesGrantPanel.add(auditResourcesGrantSetButton);
      auditResourcesGrantConsensusLabel = newLabel("Consensus:");
      auditResourcesGrantPanel.add(auditResourcesGrantConsensusLabel);
      auditResourcesConsensusTextBox = new TextField(10);
      auditResourcesConsensusTextBox.setEditable(false);
      auditResourcesGrantPanel.add(auditResourcesConsensusTextBox);
      auditResourcesPenaltyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      auditResourcesCaptionPanel.add(auditResourcesPenaltyPanel);
      auditResourcesPenaltyLabel = newLabel("Penalty:");
      auditResourcesPenaltyPanel.add(auditResourcesPenaltyLabel);
      auditResourcesPenaltyTextBox = new TextField(10);
      auditResourcesPenaltyTextBox.setEditable(false);
      auditResourcesPenaltyPanel.add(auditResourcesPenaltyTextBox);
      auditFinishButton = new Button("Finish");
      auditFinishButton.addActionListener(this);
      auditResourcesPenaltyPanel.add(auditFinishButton);
      claimantChatCaptionPanel = new JPanel();
      claimantChatCaptionPanel.setBorder(BorderFactory.createTitledBorder("Claimant chat"));
      claimantChatCaptionPanel.setLayout(new BoxLayout(claimantChatCaptionPanel, BoxLayout.Y_AXIS));        
      auditPanel.add(claimantChatCaptionPanel);
      claimantChatTextArea = new TextArea(5, 50);
      claimantChatTextArea.setEditable(false);
      claimantChatCaptionPanel.add(claimantChatTextArea);
      claimantChatTextBox = new TextField(50);
      claimantChatCaptionPanel.add(claimantChatTextBox);
      claimantChatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      claimantChatCaptionPanel.add(claimantChatPanel);
      claimantChatSendButton = new Button("Send");
      claimantChatSendButton.addActionListener(this);
      claimantChatPanel.add(claimantChatSendButton);

      add(roleTabPanel);
      roleTabPanel.setEnabledAt(CLAIM_TAB, false);
      roleTabPanel.setEnabledAt(AUDIT_TAB, false);      
      
      // Initialize state.
      playerName        = "";
      gameCode          = "";      
      gameState         = 0;
      playerState       = false;
      claimState        = TRANSACTION_STATE.INACTIVE;
      auditState        = TRANSACTION_STATE.INACTIVE;
      transactionNumber = -1;

      // Synchronize player with network.
      syncPlayer();
      
      // Enable user interface.
      UIlocked = false;
      enableUI();
      
      // Start timer.
      TimerTask task = new TimerTask() 
      {
          public void run() 
          {
        	  syncCounter++;
        	  if (syncCounter >= syncFreq)
        	  {
        		  syncCounter = 0;
        		  syncMessages();
        	  }
              animateWaitTextBox(claimResourcesGrantTextBox);
              animateWaitTextBox(claimResourcesPenaltyTextBox);
              animateWaitTextBox(auditResourcesConsensusTextBox);
              animateWaitTextBox(auditResourcesPenaltyTextBox);
              claimDistribution.draw();
              auditDistribution.draw();
          }
      };
      timer = new Timer("Timer");
      timer.schedule(task, 0, timerInterval_ms);
      
      // Show.      
      pack(); 
      setVisible(true);     
   }
   
   // Make label with font.
   private Label newLabel(String text)
   {
	   Label label = new Label(text);
	   label.setFont(labelFont);
	   return label;
   }

   // Animate wait text box.
   private void animateWaitTextBox(TextField textBox)
   {
      String text = textBox.getText();

      if (text.startsWith("waiting"))
      {
         if (text.equals("waiting..."))
         {
            textBox.setText("waiting");
         }
         else if (text.equals("waiting.."))
         {
            textBox.setText("waiting...");
         }
         else if (text.equals("waiting."))
         {
            textBox.setText("waiting..");
         }
         else if (text.equals("waiting"))
         {
            textBox.setText("waiting.");
         }
      }
   }

   // Button handler.
   public void actionPerformed(ActionEvent event)
   {
	  if (UIlocked) return;
	  
     // Join/quit game.
     if (event.getSource() == playerJoinQuitButton)
     {
        disableUI();
        if (gameState == Shared.JOINING)
        {
            if (!playerState)
            {
	           // Join.
	           try
	           {
	               DelimitedString request = new DelimitedString(Shared.JOIN_GAME);
	               request.add(gameCode);
	               request.add(playerName);
	   			   byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
	   			   if (response != null && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
	   			   {
	   				   playerState = true;
	                   playerJoinQuitButton.setLabel("Quit");
	                   JOptionPane.showMessageDialog(this, "Welcome!");
	   			   } else {
	   				   if (response != null)
	   				   {
	   					   JOptionPane.showMessageDialog(this, "Error joining game: " + new String(response, StandardCharsets.UTF_8));
	   				   } else {
	   					   JOptionPane.showMessageDialog(this, "Error joining game");	   					   
	   				   }	   				   
	   			   }
	           } catch (Exception e)
	           {
	        	   JOptionPane.showMessageDialog(this, "Error joining game: " + e.getMessage());	   					     	               
	           }          
	        }
	        else
	        {
	           // Quit.
		       try
		       {	        	
		           DelimitedString request = new DelimitedString(Shared.QUIT_GAME);
		           request.add(gameCode);
		           request.add(playerName);
	   			   byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
	   			   if (response != null && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
	   			   {
	   				   playerState = false;
	                   playerJoinQuitButton.setLabel("Join");
                       clearHomeResources();
                       roleTabPanel.setSelectedIndex(HOME_TAB);
                       roleTabPanel.setEnabledAt(CLAIM_TAB, false);
                       roleTabPanel.setEnabledAt(AUDIT_TAB, false);
                       claimState = TRANSACTION_STATE.INACTIVE;
                       auditState = TRANSACTION_STATE.INACTIVE;
	                   JOptionPane.showMessageDialog(this, "Goodbye!");
	   			   } else {
	   				   if (response != null)
	   				   {
	   					   JOptionPane.showMessageDialog(this, "Error quitting game: " + new String(response, StandardCharsets.UTF_8));
	   				   } else {
	   					   JOptionPane.showMessageDialog(this, "Error quitting game");	   					   
	   				   }	   				   
	   			   }
	           } catch (Exception e)
	           {
	        	   JOptionPane.showMessageDialog(this, "Error quitting game: " + e.getMessage());	   					     	               
	           }       			   
	        }
        } else {
        	if (gameState == 0)
        	{
        		JOptionPane.showMessageDialog(this, "Game not found");        		
        	} else {
        		if (gameState == Shared.PENDING)
        		{
        			if (!playerState)
        			{
                		JOptionPane.showMessageDialog(this, "Game currently not accepting players");         				       				
        			}
        		} else {
        			if (playerState)
        			{
                		JOptionPane.showMessageDialog(this, "Cannot quit game after running");         				
        			} else {
                		JOptionPane.showMessageDialog(this, "Cannot join game after running");         				
        			}
        		}
        	}
        }
        enableUI();
     }

     // Clear chat.
     else if (event.getSource() == hostChatClearButton)
     {
        hostChatTextArea.setText("");
     }

     // Send chat to host.
     else if (event.getSource() == hostChatSendButton)
     {
        String chatText = hostChatTextBox.getText();
        if (Shared.isVoid(chatText))
        {
           return;
        }
        if (chatText.contains(DelimitedString.DELIMITER))
        {
           JOptionPane.showMessageDialog(this, "Invalid character: " + DelimitedString.DELIMITER);
           return;
        }
        if (gameState == 0)
        {
           JOptionPane.showMessageDialog(this, "Please join game!");
           return;
        }
        disableUI();
	    try
	    {	        
	        DelimitedString request = new DelimitedString(Shared.PLAYER_CHAT_MESSAGE);
	        request.add(gameCode);
	        request.add(playerName);
	        request.add(chatText);
		    byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
		    if (response != null && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
		    {
	            hostChatTextArea.setText(hostChatTextArea.getText() +
	                    playerName + ": " +
	                    hostChatTextBox.getText() + "\n");
	            hostChatTextBox.setText("");
		    } else {
			   if (response != null)
			   {
				   JOptionPane.showMessageDialog(this, "Error sending chat: " + new String(response, StandardCharsets.UTF_8));
			   } else {
				   JOptionPane.showMessageDialog(this, "Error sending chat");	   					   
			   }	   				   
		    }
        } catch (Exception e)
        {
     	   JOptionPane.showMessageDialog(this, "Error sending chat: " + e.getMessage());	   					     	               
        } 
	    enableUI();
     }

     // Send claimant chat to auditors.
     else if (event.getSource() == auditorChatSendButton)
     {
        String chatText = auditorChatTextBox.getText();
        if (Shared.isVoid(chatText))
        {
           return;
        }
        if (chatText.contains(DelimitedString.DELIMITER))
        {
           JOptionPane.showMessageDialog(this, "Invalid character: " + DelimitedString.DELIMITER);
           return;
        }
        if (gameState == 0)
        {
           JOptionPane.showMessageDialog(this, "Please join game!");
           return;
        }
        if (transactionNumber == -1)
        {
           JOptionPane.showMessageDialog(this, "Invalid transaction");
           return;
        }        
        disableUI();
	    try
	    {	        
	        DelimitedString request = new DelimitedString(Shared.CLAIMANT_CHAT_MESSAGE);
	        request.add(gameCode);
	        request.add(transactionNumber);
	        request.add(chatText);
		    byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
		    if (response != null && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
		    {
                auditorChatTextArea.setText(auditorChatTextArea.getText() +
                        "claimant (" + transactionNumber + "): " +
                        auditorChatTextBox.getText() + "\n");
                auditorChatTextBox.setText("");
		    } else {
			   if (response != null)
			   {
				   JOptionPane.showMessageDialog(this, "Error sending claimant chat: " + new String(response, StandardCharsets.UTF_8));
			   } else {
				   JOptionPane.showMessageDialog(this, "Error sending claimant chat");	   					   
			   }	   				   
		    }
        } catch (Exception e)
        {
     	   JOptionPane.showMessageDialog(this, "Error sending claimant chat: " + e.getMessage());	   					     	               
        } 
	    enableUI();
     }

     // Send auditor chat to claimant.
     else if (event.getSource() == claimantChatSendButton)
     {
        String chatText = claimantChatTextBox.getText();
        if (Shared.isVoid(chatText))
        {
           return;
        }
        if (chatText.contains(DelimitedString.DELIMITER))
        {
           JOptionPane.showMessageDialog(this, "Invalid character: " + DelimitedString.DELIMITER);
           return;
        }
        if (gameState == 0)
        {
           JOptionPane.showMessageDialog(this, "Please join game!");
           return;
        }
        if (transactionNumber == -1)
        {
           JOptionPane.showMessageDialog(this, "Invalid transaction");
           return;
        }              
        disableUI();
	    try
	    {	        
	        DelimitedString request = new DelimitedString(Shared.AUDITOR_CHAT_MESSAGE);
	        request.add(gameCode);
	        request.add(transactionNumber);
	        request.add(chatText);
		    byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
		    if (response != null && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
		    {
                claimantChatTextArea.setText(claimantChatTextArea.getText() +
                        "auditor (" + transactionNumber + "): " +
                        claimantChatTextBox.getText() + "\n");
                claimantChatTextBox.setText("");
		    } else {
			   if (response != null)
			   {
				   JOptionPane.showMessageDialog(this, "Error sending auditor chat: " + new String(response, StandardCharsets.UTF_8));
			   } else {
				   JOptionPane.showMessageDialog(this, "Error sending auditor chat");	   					   
			   }	   				   
		    }
        } catch (Exception e)
        {
     	   JOptionPane.showMessageDialog(this, "Error sending auditor chat: " + e.getMessage());	   					     	               
        }         
	    enableUI();
     }

     else if (event.getSource() == claimDistributionTestButton)
     {
        String valueText = claimDistributionTestValueTextBox.getText().trim();
        if (Shared.isVoid(valueText))
        {
           JOptionPane.showMessageDialog(this, "Please enter test value");
           return;
        }
        double value;
        try {
           value = Double.parseDouble(valueText);
        }
        catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Invalid test value");
           return;
        }
        double probability = claimDistribution.phi(value);
        claimDistributionTestProbabilityTextBox.setText(doubleToString(probability));
     }
     else if (event.getSource() == auditDistributionTestButton)
     {
        String valueText = auditDistributionTestValueTextBox.getText().trim();
        if (Shared.isVoid(valueText))
        {
           JOptionPane.showMessageDialog(this, "Please enter test value");
           return;
        }
        double value;
        try {
           value = Double.parseDouble(valueText);
        }
        catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Invalid test value");
           return;
        }
        double probability = auditDistribution.phi(value);
        auditDistributionTestProbabilityTextBox.setText(doubleToString(probability));
     }
     else if (event.getSource() == claimResourcesSetButton)
     {
        String claimText = claimResourcesClaimTextBox.getText().trim();
        if (Shared.isVoid(claimText))
        {
           JOptionPane.showMessageDialog(this, "Please enter claim value");
           return;
        }
        double claim;
        try {
           claim = Double.parseDouble(claimText);
        }
        catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Invalid claim value");
           return;
        }
        if (claim < 0.0)
        {
           JOptionPane.showMessageDialog(this, "Invalid claim value");
           return;
        }
        claimState = TRANSACTION_STATE.WAITING;
        claimResourcesGrantTextBox.setText("waiting");
        disableUI();
	    try
	    {	        
	        DelimitedString request = new DelimitedString(Shared.SET_CLAIM);
	        request.add(gameCode);
	        request.add(transactionNumber);
	        request.add(claim);
		    byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
		    if (response == null || Shared.isError(new String(response, StandardCharsets.UTF_8)))
		    {
			   if (response != null)
			   {
				   JOptionPane.showMessageDialog(this, "Error sending claim: " + new String(response, StandardCharsets.UTF_8));
			   } else {
				   JOptionPane.showMessageDialog(this, "Error sending claim");	   					   
			   }	   				   
		    }
        } catch (Exception e)
        {
     	   JOptionPane.showMessageDialog(this, "Error sending claim: " + e.getMessage());	   					     	               
        }         
	    enableUI();        
     }
     else if (event.getSource() == auditResourcesGrantSetButton)
     {
        String grantText = auditResourcesGrantTextBox.getText().trim();
        if (Shared.isVoid(grantText))
        {
           JOptionPane.showMessageDialog(this, "Please enter grant value");
           return;
        }
        double grant;
        try {
           grant = Double.parseDouble(grantText);
        }
        catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Invalid grant value");
           return;
        }
        if (grant < 0.0)
        {
           JOptionPane.showMessageDialog(this, "Invalid grant value");
           return;
        }
        double claim;
        try {
           claim = Double.parseDouble(auditResourcesClaimTextBox.getText());
        }
        catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Invalid claim value");
           return;
        }
        if (grant > claim)
        {
           JOptionPane.showMessageDialog(this, "Grant cannot be greater than claim");
           return;
        }
        auditState = TRANSACTION_STATE.WAITING;
        auditResourcesConsensusTextBox.setText("waiting");
        disableUI();
	    try
	    {	        
	        DelimitedString request = new DelimitedString(Shared.SET_GRANT);
	        request.add(gameCode);
	        request.add(transactionNumber);
	        request.add(grant);
	        request.add(playerNameTextBox.getText());
		    byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
		    if (response == null || Shared.isError(new String(response, StandardCharsets.UTF_8)))
		    {
			   if (response != null)
			   {
				   JOptionPane.showMessageDialog(this, "Error sending grant: " + new String(response, StandardCharsets.UTF_8));
			   } else {
				   JOptionPane.showMessageDialog(this, "Error sending grant");	   					   
			   }	   				   
		    }
        } catch (Exception e)
        {
     	   JOptionPane.showMessageDialog(this, "Error sending grant: " + e.getMessage());	   					     	               
        }         
	    enableUI();             
     }
     else if (event.getSource() == claimResourcesDonateButton)
     {
        String donateText = claimResourcesDonateTextBox.getText().trim();
        if (Shared.isVoid(donateText))
        {
           JOptionPane.showMessageDialog(this, "Please enter donation value");
           return;
        }
        double donation;
        try {
           donation = Double.parseDouble(donateText);
        }
        catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Invalid donation value");
           return;
        }
        if (donation < 0.0)
        {
           JOptionPane.showMessageDialog(this, "Invalid donation value");
           return;
        }
        String beneficiary = claimResourcesDonateBeneficiaryTextBox.getText().trim();
        if (Shared.isVoid(beneficiary))
        {
           JOptionPane.showMessageDialog(this, "Please enter beneficiary value");
           return;
        }
        if (beneficiary.equals(playerNameTextBox.getText()))
        {
           JOptionPane.showMessageDialog(this, "Cannot donate to self");
           return;
        }
        disableUI();
	    try
	    {	        
	        DelimitedString request = new DelimitedString(Shared.SET_DONATION);
	        request.add(gameCode);
	        request.add(transactionNumber);
	        request.add(donation);
	        request.add(beneficiary);
		    byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
		    if (response != null && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
		    {
                claimResourcesDonateTextBox.setText("");
                claimResourcesDonateBeneficiaryTextBox.setText("");	
		    } else {
			   if (response != null)
			   {
				   JOptionPane.showMessageDialog(this, "Error sending donation: " + new String(response, StandardCharsets.UTF_8));
			   } else {
				   JOptionPane.showMessageDialog(this, "Error sending donation");	   					   
			   }	   				   
		    }
        } catch (Exception e)
        {
     	   JOptionPane.showMessageDialog(this, "Error sending donation: " + e.getMessage());	   					     	               
        }         
	    enableUI();            
     }
     else if ((event.getSource() == claimFinishButton) || (event.getSource() == auditFinishButton))
     {
        if (event.getSource() == claimFinishButton)
        {
           claimState = TRANSACTION_STATE.WAITING;
        }
        else
        {
           auditState = TRANSACTION_STATE.WAITING;
        }
        disableUI();
	    try
	    {	        
	        DelimitedString request = new DelimitedString(Shared.FINISH_TRANSACTION);
	        request.add(gameCode);
	        request.add(transactionNumber);
	        request.add(playerNameTextBox.getText());
		    byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
		    if (response == null || Shared.isError(new String(response, StandardCharsets.UTF_8)))
		    {
			   if (response != null)
			   {
				   JOptionPane.showMessageDialog(this, "Error sending finish: " + new String(response, StandardCharsets.UTF_8));
			   } else {
				   JOptionPane.showMessageDialog(this, "Error sending finish");	   					   
			   }	   				   
		    }
        } catch (Exception e)
        {
     	   JOptionPane.showMessageDialog(this, "Error sending finish: " + e.getMessage());	   					     	               
        }         
	    enableUI();         
     }
   }

   private void clearHomeResources()
   {
      homeResourcesActualTextBox.setText("");
      homeResourcesPersonalTextBox.setText("");
      homeResourcesCommonTextBox.setText("");
      homeResourcesEntitledTextBox.setText("");
   }

   private void showHomeResources(double personal, double common, double entitled)
   {
      double actual = personal + common;

      homeResourcesActualTextBox.setText(doubleToString(actual));
      homeResourcesPersonalTextBox.setText(doubleToString(personal));
      homeResourcesCommonTextBox.setText(doubleToString(common));
      homeResourcesEntitledTextBox.setText(doubleToString(entitled));
   }

   private String doubleToString(double value)
   {
      DecimalFormat decimalFormat = new DecimalFormat("#.##");
      return(decimalFormat.format(value));
   }

   // Disable UI.
   private void disableUI()
   {
	  if (UIlocked) return;	   
      playerJoinQuitButton.setEnabled(false);
      hostChatTextBox.setEditable(false);
      hostChatSendButton.setEnabled(false);
      claimResourcesClaimTextBox.setEditable(false);
      claimResourcesSetButton.setEnabled(false);
      claimResourcesDonateTextBox.setEditable(false);
      claimResourcesDonateBeneficiaryTextBox.setEditable(false);
      claimResourcesDonateButton.setEnabled(false);
      claimFinishButton.setEnabled(false);
      auditorChatTextBox.setEditable(false);
      auditorChatSendButton.setEnabled(false);
      auditResourcesGrantTextBox.setEditable(false);
      auditResourcesGrantSetButton.setEnabled(false);
      auditFinishButton.setEnabled(false);
      claimantChatTextBox.setEditable(false);
      claimantChatSendButton.setEnabled(false);
   }


   // Enable UI.
   private void enableUI()
   {
	  if (UIlocked) return; 
      playerJoinQuitButton.setEnabled(true);
      if (gameState == 0 || !playerState)
      {
          playerJoinQuitButton.setLabel("Join");
          clearHomeResources();
          roleTabPanel.setSelectedIndex(HOME_TAB);
          roleTabPanel.setEnabledAt(CLAIM_TAB, false);
          roleTabPanel.setEnabledAt(AUDIT_TAB, false);
          claimState = TRANSACTION_STATE.INACTIVE;
          auditState = TRANSACTION_STATE.INACTIVE;
         hostChatTextBox.setEditable(false);
         hostChatSendButton.setEnabled(false);
         claimResourcesClaimTextBox.setEditable(false);
         claimResourcesSetButton.setEnabled(false);
         claimResourcesDonateTextBox.setEditable(false);
         claimResourcesDonateBeneficiaryTextBox.setEditable(false);
         claimResourcesDonateButton.setEnabled(false);
         claimFinishButton.setEnabled(false);
         auditorChatTextBox.setEditable(false);
         auditorChatSendButton.setEnabled(false);
         auditResourcesGrantTextBox.setEditable(false);
         auditResourcesGrantSetButton.setEnabled(false);
         auditFinishButton.setEnabled(false);
         claimantChatTextBox.setEditable(false);
         claimantChatSendButton.setEnabled(false);
      }
      else
      {
         playerJoinQuitButton.setLabel("Quit");	  
         hostChatTextBox.setEditable(true);
         hostChatSendButton.setEnabled(true);
         switch (claimState)
         {
         case INACTIVE:
            claimResourcesClaimTextBox.setEditable(false);
            claimResourcesSetButton.setEnabled(false);
            claimResourcesDonateTextBox.setEditable(false);
            claimResourcesDonateBeneficiaryTextBox.setEditable(false);
            claimResourcesDonateButton.setEnabled(false);
            claimFinishButton.setEnabled(false);
            auditorChatTextBox.setEditable(false);
            auditorChatSendButton.setEnabled(false);
            break;

         case PENDING:
            claimResourcesClaimTextBox.setEditable(true);
            claimResourcesSetButton.setEnabled(true);
            claimResourcesDonateTextBox.setEditable(false);
            claimResourcesDonateBeneficiaryTextBox.setEditable(false);
            claimResourcesDonateButton.setEnabled(false);
            claimFinishButton.setEnabled(false);
            auditorChatTextBox.setEditable(false);
            auditorChatSendButton.setEnabled(false);
            break;

         case WAITING:
            claimResourcesClaimTextBox.setEditable(false);
            claimResourcesSetButton.setEnabled(false);
            claimResourcesDonateTextBox.setEditable(false);
            claimResourcesDonateBeneficiaryTextBox.setEditable(false);
            claimResourcesDonateButton.setEnabled(false);
            claimFinishButton.setEnabled(false);
            auditorChatTextBox.setEditable(true);
            auditorChatSendButton.setEnabled(true);
            break;

         case FINISHED:
            claimResourcesClaimTextBox.setEditable(false);
            claimResourcesSetButton.setEnabled(false);
            claimResourcesDonateTextBox.setEditable(true);
            claimResourcesDonateBeneficiaryTextBox.setEditable(true);
            claimResourcesDonateButton.setEnabled(true);
            claimFinishButton.setEnabled(true);
            auditorChatTextBox.setEditable(true);
            auditorChatSendButton.setEnabled(true);
            break;
         }
         switch (auditState)
         {
         case INACTIVE:
            auditResourcesGrantTextBox.setEditable(false);
            auditResourcesGrantSetButton.setEnabled(false);
            auditFinishButton.setEnabled(false);
            claimantChatTextBox.setEditable(false);
            claimantChatSendButton.setEnabled(false);
            break;

         case PENDING:
            auditResourcesGrantTextBox.setEditable(true);
            auditResourcesGrantSetButton.setEnabled(true);
            auditFinishButton.setEnabled(false);
            claimantChatTextBox.setEditable(true);
            claimantChatSendButton.setEnabled(true);
            break;

         case WAITING:
            auditResourcesGrantTextBox.setEditable(false);
            auditResourcesGrantSetButton.setEnabled(false);
            auditFinishButton.setEnabled(false);
            claimantChatTextBox.setEditable(true);
            claimantChatSendButton.setEnabled(true);
            break;

         case FINISHED:
            auditResourcesGrantTextBox.setEditable(false);
            auditResourcesGrantSetButton.setEnabled(false);
            auditFinishButton.setEnabled(true);
            claimantChatTextBox.setEditable(true);
            claimantChatSendButton.setEnabled(true);
            break;
         }
      }
   }

   
   // Synchronize player with network.
   private void syncPlayer()
   {
	   if (Shared.isVoid(gameCode) || Shared.isVoid(playerName)) return;
	    disableUI(); 
   		try
   		{
   	       	DelimitedString request = new DelimitedString(Shared.SYNC_PLAYER);
   	        request.add(gameCode);
   	        request.add(playerName);
   			if (transactionNumber != -1)
   			{
   				request.add(transactionNumber);
   			}
   			byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
   	   		if (response == null || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
   	   		{
   	   			if (response == null)
   	   			{  	 
   	   				JOptionPane.showMessageDialog(this, "Cannot sync player");
   	   			} else {
   	   				JOptionPane.showMessageDialog(this, "Cannot sync player: " + new String(response, StandardCharsets.UTF_8));  	   				
   	   			}
   	   		} else {
   	   			String[] args = new DelimitedString(new String(response, StandardCharsets.UTF_8)).parse();
   	   			try
   	   			{
   	   				gameState = Integer.parseInt(args[1]);
   	   			} catch (Exception e)
   	   			{
   	   				JOptionPane.showMessageDialog(this, "Error syncing player: " + e.getMessage());
   	   				gameState = 0;
   	   				playerState = false;
   	   			}
   	   			if (gameState != 0)
   	   			{
   	   				if (Integer.parseInt(args[2]) == 1)
   	   				{
   	   					playerState = true;
   	   				} else {
   	   					playerState = false;
   	   				}
   	   				if (playerState)
   	   				{
	   	   	            // Set player resources.
	   	   	            double personalResources = Double.parseDouble(args[3]);
	   	   	            double commonResources   = Double.parseDouble(args[4]);
	   	   	            double entitledResources = Double.parseDouble(args[5]);
	   	   	            showHomeResources(personalResources, commonResources, entitledResources);   	   					
	                    // TODO: process transaction.
   	   				}
   	   			}
   	   		}
   		}
   		catch(Exception e)
   		{
   			JOptionPane.showMessageDialog(this, "Cannot sync player: " + e.getMessage());
   		}
   		enableUI();
   }
   
   // Synchronize player with status and messages.
   private void syncMessages()
   {
	   if (Shared.isVoid(gameCode) || Shared.isVoid(playerName)) return;
   		try
   		{
            DelimitedString request = new DelimitedString(Shared.PLAYER_SYNC_MESSAGES);
            request.add(gameCode);
            request.add(playerName);
			byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
   	   		if (response != null)
   	   		{
   	   			String messages = new String(response, StandardCharsets.UTF_8);
   	   			System.out.println("player " + playerName + " messages: " + messages); // flibber
   	   			if (Shared.isOK(messages))
   	   			{
   	   				String[] args = new DelimitedString(messages).parse();   	   				
   	   				if (args.length >= 3)
   	   				{
   	   					int g = Integer.parseInt(args[1]);
   	   					boolean p = (Integer.parseInt(args[2]) == 1);
   	   					boolean enable = false;
   	   					if (g != gameState || p != playerState) enable = true;
   	   					gameState = g;
   	   					playerState = p;
   	   					if (gameState == 0 || !playerState)
   	   					{
   	   						if (enable) enableUI();
   	   					} else {
		   	   				if (args.length > 3)
		   	   				{
		   	    	   			disableUI();
		   	    	   			update(messages);
		   	    	   			enableUI();   	   					
		   	   				} else {
		   	   					if (enable) enableUI();
		   	   				}
   	   					}
   	   				} else {
   	   					JOptionPane.showMessageDialog(this, "Invalid sync messages: " + new String(response, StandardCharsets.UTF_8));
   	   	   				gameState = 0;
   	   	   				playerState = false;
   	   	   				enableUI();  	   					
   	   				}
   	   			} else {
   	   				gameState = 0;
   	   				playerState = false;
   	   				enableUI();
   	   			}
   	   		}
   		}
   		catch(Exception e) {}
   }
 
   // Update player with messages.
  public void update(String messages)
  {
     if (Shared.isVoid(messages.toString()))
     {
        return;
     }
     if (Shared.isError(messages))
     {
    	 gameState = 0;
    	 playerState = false;
    	 return;
     }    
     String[] fields = messages.split(DelimitedString.DELIMITER);
     if (fields == null || fields.length <= 3)
     {
        return;
     }     
     for (int n = 3; n < fields.length; )
     {
    	 int c = 0;
    	 for (int i = n; i < fields.length; i++)
    	 {
    		 if (fields[i].equals(Shared.MESSAGE_DELIMITER)) break;
    		 c++;
    	 }
    	 if (c < 2)
		 {
    		JOptionPane.showMessageDialog(this, "Invalid message from network: " + messages);
    		return;
		 }
    	 String[] args = new String[c];
    	 for (int j = 0; j < c; j++)
    	 {
    		 args[j] = fields[j + n];
    	 }
    	 n += (c + 1);
    	 String operation = args[0];
         if (operation.equals(Shared.SET_PLAYER_RESOURCES) && (args.length == 4))
         {
            // Set player resources.
            double personalResources = Double.parseDouble(args[1]);
            double commonResources   = Double.parseDouble(args[2]);
            double entitledResources = Double.parseDouble(args[3]);
            showHomeResources(personalResources, commonResources, entitledResources);
         }
         else if (operation.equals(Shared.CHAT_MESSAGE) && (args.length == 2))
         {
            // Chat from host.
           String chatText = args[1];
           hostChatTextArea.setText(
              hostChatTextArea.getText() + "host: " + chatText + "\n");
         }
         else if (operation.equals(Shared.AUDITOR_CHAT_MESSAGE) && (args.length == 3))
         {
            // Chat from auditor.
        	String transactionNumber = args[1];
            String chatText = args[2];
            claimantChatTextArea.setText(
               claimantChatTextArea.getText() + "auditor (" + transactionNumber + "): " + chatText + "\n");
         }
         else if (operation.equals(Shared.CLAIMANT_CHAT_MESSAGE) && (args.length == 3))
         {
            // Chat from claimant.
        	String transactionNumber = args[1];
            String chatText = args[2];
            auditorChatTextArea.setText(
               auditorChatTextArea.getText() + "claimant (" + transactionNumber + "): " + chatText + "\n");
         }
         else if (operation.equals(Shared.START_CLAIM) && (args.length == 6))
         {
            // Start a claim.
            transactionNumber = Integer.parseInt(args[1]);
            double mean  = Double.parseDouble(args[2]);
            double sigma = Double.parseDouble(args[3]);
            claimDistribution.setMean(mean);
            claimDistribution.setSigma(sigma);
            claimDistribution.draw();
            double entitlement = Double.parseDouble(args[4]);
            int    numPlayers  = Integer.parseInt(args[5]);
            claimResourcesEntitledTextBox.setText(entitlement + "");
            if (numPlayers > 0)
            {
               claimResourcesEntitledPerPlayerTextBox.setText(doubleToString(entitlement / (double)numPlayers));
            }
            else
            {
               claimResourcesEntitledPerPlayerTextBox.setText("");
            }
            claimResourcesEntitledNumPlayersTextBox.setText(numPlayers + "");
            roleTabPanel.setEnabledAt(CLAIM_TAB, true);
            roleTabPanel.setSelectedIndex(CLAIM_TAB);
            claimState = TRANSACTION_STATE.PENDING;
            enableUI();
         }
         else if (operation.equals(Shared.START_AUDIT) && (args.length == 7))
         {
            // Start audit.
            transactionNumber = Integer.parseInt(args[1]);
            claimantNameTextBox.setText(args[2]);
            double mean  = Double.parseDouble(args[3]);
            double sigma = Double.parseDouble(args[4]);
            auditDistribution.setMean(mean);
            auditDistribution.setSigma(sigma);
            auditDistribution.draw();
            double claim      = Double.parseDouble(args[5]);
            int    numPlayers = Integer.parseInt(args[6]);
            auditResourcesClaimTextBox.setText(claim + "");
            if (numPlayers > 0)
            {
               auditResourcesClaimPerPlayerTextBox.setText(doubleToString(claim / (double)numPlayers));
            }
            else
            {
               auditResourcesClaimPerPlayerTextBox.setText("");
            }
            auditResourcesClaimNumPlayersTextBox.setText(numPlayers + "");
            roleTabPanel.setEnabledAt(AUDIT_TAB, true);
            roleTabPanel.setSelectedIndex(AUDIT_TAB);            
            auditState = TRANSACTION_STATE.PENDING;
            enableUI();
         }
         else if (operation.equals(Shared.SET_GRANT) && (args.length == 3))
         {
            // Set grant.
            transactionNumber = Integer.parseInt(args[1]);
            if (claimState == TRANSACTION_STATE.WAITING)
            {
               claimResourcesGrantTextBox.setText(args[2]);
               claimResourcesPenaltyTextBox.setText("waiting");
            }
            else
            {
               auditResourcesConsensusTextBox.setText(args[2]);
               auditResourcesPenaltyTextBox.setText("waiting");
            }
            enableUI();
         }
         else if (operation.equals(Shared.SET_PENALTY) && (args.length == 3))
         {
            // Set penalty.
            transactionNumber = Integer.parseInt(args[1]);
            if (claimState == TRANSACTION_STATE.WAITING)
            {
               claimState = TRANSACTION_STATE.FINISHED;
               claimResourcesPenaltyTextBox.setText(args[2]);
            }
            else
            {
               auditState = TRANSACTION_STATE.FINISHED;
               auditResourcesPenaltyTextBox.setText(args[2]);
            }
            enableUI();
         }
         else if (operation.equals(Shared.FINISH_TRANSACTION) && (args.length == 5))
         {
            String transactionText = new Date().toString() + ":";
            transactionText += "transaction number=" + transactionNumber;
            if (claimState == TRANSACTION_STATE.WAITING)
            {
               transactionText += ";mean=" + claimDistribution.getMean();
               transactionText += ";sigma=" + claimDistribution.getSigma();
               transactionText += ";entitlement=" + claimResourcesEntitledTextBox.getText();
               transactionText += ";claim=" + claimResourcesClaimTextBox.getText();
               transactionText += ";grant=" + claimResourcesGrantTextBox.getText();
               transactionText += ";penalty=" + claimResourcesPenaltyTextBox.getText();
               transactionText += "\n";
               claimHistoryTextArea.setText(claimHistoryTextArea.getText() + transactionText);
            }
            if (auditState == TRANSACTION_STATE.WAITING)
            {
               transactionText += ";mean=" + auditDistribution.getMean();
               transactionText += ";sigma=" + auditDistribution.getSigma();
               transactionText += ";claim=" + auditResourcesClaimTextBox.getText();
               transactionText += ";grant=" + auditResourcesGrantTextBox.getText();
               transactionText += ";penalty=" + auditResourcesPenaltyTextBox.getText();
               transactionText += "\n";
               auditHistoryTextArea.setText(auditHistoryTextArea.getText() + transactionText);
            }
            transactionNumber = -1;
            if (claimState != TRANSACTION_STATE.INACTIVE)
            {
               auditorChatTextArea.setText("");
               claimDistributionTestValueTextBox.setText("");
               claimDistributionTestProbabilityTextBox.setText("");
               claimResourcesEntitledTextBox.setText("");
               claimResourcesEntitledPerPlayerTextBox.setText("");
               claimResourcesEntitledNumPlayersTextBox.setText("");
               claimResourcesClaimTextBox.setText("");
               claimResourcesGrantTextBox.setText("");
               claimResourcesPenaltyTextBox.setText("");
               claimResourcesDonateTextBox.setText("");
               claimResourcesDonateBeneficiaryTextBox.setText("");
               claimDistribution.setMean(NormalDistribution.DEFAULT_MEAN);
               claimDistribution.setSigma(NormalDistribution.DEFAULT_SIGMA);
               claimDistribution.draw();
               auditorChatTextBox.setText("");
               roleTabPanel.setSelectedIndex(HOME_TAB);
               roleTabPanel.setEnabledAt(CLAIM_TAB, false);
               claimState = TRANSACTION_STATE.INACTIVE;
            }
            if (auditState != TRANSACTION_STATE.INACTIVE)
            {
               claimantChatTextArea.setText("");
               auditDistributionTestValueTextBox.setText("");
               auditDistributionTestProbabilityTextBox.setText("");
               auditResourcesClaimTextBox.setText("");
               auditResourcesClaimPerPlayerTextBox.setText("");
               auditResourcesClaimNumPlayersTextBox.setText("");
               auditResourcesGrantTextBox.setText("");
               auditResourcesConsensusTextBox.setText("");
               auditResourcesPenaltyTextBox.setText("");
               auditDistribution.setMean(NormalDistribution.DEFAULT_MEAN);
               auditDistribution.setSigma(NormalDistribution.DEFAULT_SIGMA);
               auditDistribution.draw();
               claimantChatTextBox.setText("");
               roleTabPanel.setSelectedIndex(HOME_TAB);
               roleTabPanel.setEnabledAt(AUDIT_TAB, false);
               auditState = TRANSACTION_STATE.INACTIVE;
            }
            double personalResources = Double.parseDouble(args[2]);
            double commonResources   = Double.parseDouble(args[3]);
            double entitledResources = Double.parseDouble(args[4]);
            showHomeResources(personalResources, commonResources, entitledResources);
            enableUI();
         }
         else if (operation.equals(Shared.ABORT_TRANSACTION) && (args.length == 2))
         {
            transactionNumber = -1;
            if (claimState != TRANSACTION_STATE.INACTIVE)
            {
               auditorChatTextArea.setText("");
               claimDistributionTestValueTextBox.setText("");
               claimDistributionTestProbabilityTextBox.setText("");
               claimResourcesEntitledTextBox.setText("");
               claimResourcesEntitledPerPlayerTextBox.setText("");
               claimResourcesEntitledNumPlayersTextBox.setText("");
               claimResourcesClaimTextBox.setText("");
               claimResourcesGrantTextBox.setText("");
               claimResourcesPenaltyTextBox.setText("");
               claimResourcesDonateTextBox.setText("");
               claimResourcesDonateBeneficiaryTextBox.setText("");
               claimDistribution.setMean(NormalDistribution.DEFAULT_MEAN);
               claimDistribution.setSigma(NormalDistribution.DEFAULT_SIGMA);
               claimDistribution.draw();
               auditorChatTextBox.setText("");
               roleTabPanel.setSelectedIndex(HOME_TAB);
               roleTabPanel.setEnabledAt(CLAIM_TAB, false);
               claimState = TRANSACTION_STATE.INACTIVE;
            }
            if (auditState != TRANSACTION_STATE.INACTIVE)
            {
               claimantChatTextArea.setText("");
               auditDistributionTestValueTextBox.setText("");
               auditDistributionTestProbabilityTextBox.setText("");
               auditResourcesClaimTextBox.setText("");
               auditResourcesClaimPerPlayerTextBox.setText("");
               auditResourcesClaimNumPlayersTextBox.setText("");
               auditResourcesGrantTextBox.setText("");
               auditResourcesConsensusTextBox.setText("");
               auditResourcesPenaltyTextBox.setText("");
               auditDistribution.setMean(NormalDistribution.DEFAULT_MEAN);
               auditDistribution.setSigma(NormalDistribution.DEFAULT_SIGMA);
               auditDistribution.draw();
               claimantChatTextBox.setText("");
               roleTabPanel.setSelectedIndex(HOME_TAB);
               roleTabPanel.setEnabledAt(AUDIT_TAB, false);
               auditState = TRANSACTION_STATE.INACTIVE;
            }
            JOptionPane.showMessageDialog(this, "Transaction aborted!");
            enableUI();
         }
      }
   }
}
