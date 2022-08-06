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
import java.awt.event.ItemEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

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
   private boolean UIinit = false;
   
   private static final int HOME_TAB  = 0;
   private static final int CLAIM_TAB = 1;
   private static final int AUDIT_TAB = 2;

   // Player name and game code.
   private String playerName;
   private String gameCode;

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
   public Player(String gameCode, String playerName, int transactionNumber)
   {
	  this.gameCode = gameCode;
	  this.playerName = playerName;
	  this.transactionNumber = transactionNumber;
	  
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
      playerNameLabel = newLabel("Name:");      
      playerPanel.add(playerNameLabel);
      playerNameTextBox = new TextField(50);
      playerPanel.add(playerNameTextBox);
      playerJoinQuitButton = new Button("Join");
      playerJoinQuitButton.addActionListener(this);
      playerPanel.add(playerJoinQuitButton);
      gameCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      homePanel.add(gameCodePanel);
      gameCodeLabel = newLabel("Code:");
      gameCodePanel.add(gameCodeLabel);
      gameCodeTextBox = new TextField(30);
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
      UIinit = true;
      //enableUI();
      
      // Initialize state.
      playerName        = "";
      gameCode          = "";
      claimState        = TRANSACTION_STATE.INACTIVE;
      auditState        = TRANSACTION_STATE.INACTIVE;
      transactionNumber = -1;

      // Synchronize player with network.
      //syncPlayer();

      // Start timer.
      TimerTask task = new TimerTask() 
      {
          public void run() 
          {
        	  syncCounter++;
        	  if (syncCounter >= syncFreq)
        	  {
        		  syncCounter = 0;
        		  //syncMessages();
        	  }
              animateWaitTextBox(claimResourcesGrantTextBox);
              animateWaitTextBox(claimResourcesPenaltyTextBox);
              animateWaitTextBox(auditResourcesConsensusTextBox);
              animateWaitTextBox(auditResourcesPenaltyTextBox);
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
   } // flibber
   /*
     // Join/quit game.
     if (event.getSource() == playerJoinQuitButton)
     {
        playerName = playerNameTextBox.getText().trim();
        if (Shared.isVoid(playerName))
        {
           Window.alert("Please enter name");
           playerName = "";
           return;
        }
        if (playerName.contains(DelimitedString.DELIMITER))
        {
           Window.alert("Invalid name character: " + DelimitedString.DELIMITER);
           playerName = "";
           return;
        }
        if (playerName.equals(Shared.ALL_PLAYERS))
        {
           Window.alert("Invalid name");
           playerName = "";
           return;
        }
        playerNameTextBox.setText(playerName);
        gameCode = gameCodeTextBox.getText().trim();
        if (Shared.isVoid(gameCode))
        {
           Window.alert("Please enter game code");
           gameCode = "";
           return;
        }
        if (gameCode.contains(DelimitedString.DELIMITER))
        {
           Window.alert("Invalid game code character: " + DelimitedString.DELIMITER);
           gameCode = "";
           return;
        }
        gameCodeTextBox.setText(gameCode);
        disableUI();
        if (channel == null)
        {
           // Join.
           DelimitedString joinRequest = new DelimitedString(Shared.JOIN_GAME);
           joinRequest.add(gameCode);
           joinRequest.add(playerName);
           gameService.requestService(joinRequest.toString(),
                                      new AsyncCallback<String>()
                                      {
                                         public void onFailure(Throwable caught)
                                         {
                                            Window.alert("Error joining game: " + caught.getMessage());
                                            enableUI();
                                         }

                                         public void onSuccess(String result)
                                         {
                                            if (Shared.isVoid(result))
                                            {
                                               Window.alert("Cannot join game: void channel token data");
                                            }
                                            else
                                            {
                                               if (Shared.isError(result))
                                               {
                                                  Window.alert(result);
                                               }
                                               else
                                               {
                                                  // Create server communication channel.
                                                  String[] args = new DelimitedString(result).parse();
                                                  if (args.length < 4)
                                                  {
                                                     Window.alert("Cannot join game: invalid channel token data: " + result);
                                                  }
                                                  else
                                                  {
                                                     String token = args[0];
                                                     for (int i = 1, j = args.length - 3; i < j; i++)
                                                     {
                                                        token = token + DelimitedString.DELIMITER + args[i];
                                                     }
                                                     ChannelFactory channelFactory = new ChannelFactoryImpl();
                                                     channel = channelFactory.createChannel(token);
                                                     if (channel == null)
                                                     {
                                                        Window.alert("Cannot join game: cannot create channel for token: " + token);
                                                     }
                                                     else
                                                     {
                                                        channelSocket = channel.open(new ChannelSocketListener());
                                                        playerJoinQuitButton.setText("Quit");
                                                        String personalResources = args[args.length - 3];
                                                        String commonResources = args[args.length - 2];
                                                        String entitledResources = args[args.length - 1];
                                                        showHomeResources(personalResources, commonResources, entitledResources);
                                                        Window.alert("Welcome!");
                                                     }
                                                  }
                                               }
                                            }
                                            enableUI();
                                         }
                                      }
                                      );
        }
        else
        {
           // Quit.
           DelimitedString quitRequest = new DelimitedString(Shared.QUIT_GAME);
           quitRequest.add(gameCode);
           quitRequest.add(playerName);
           gameService.requestService(quitRequest.toString(),
                                      new AsyncCallback<String>()
                                      {
                                         public void onFailure(Throwable caught)
                                         {
                                            Window.alert("Error quitting game: " + caught.getMessage());
                                            enableUI();
                                         }

                                         public void onSuccess(String result)
                                         {
                                            if (!Shared.isOK(result))
                                            {
                                               if (Shared.isError(result))
                                               {
                                                  Window.alert(result);
                                               }
                                               else
                                               {
                                                  Window.alert("Error quitting game");
                                               }
                                            }
                                            else
                                            {
                                               if (channelSocket != null)
                                               {
                                                  channelSocket.close();
                                               }
                                               channel = null;
                                               playerJoinQuitButton.setText("Join");
                                               clearHomeResources();
                                               roleTabPanel.selectTab(HOME_TAB);
                                               roleTabBar.setTabEnabled(CLAIM_TAB, false);
                                               roleTabBar.setTabEnabled(AUDIT_TAB, false);
                                               claimState = TRANSACTION_STATE.INACTIVE;
                                               auditState = TRANSACTION_STATE.INACTIVE;
                                               Window.alert("Goodbye!");
                                            }
                                            enableUI();
                                         }
                                      }
                                      );
        }
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
           Window.alert("Invalid character: " + DelimitedString.DELIMITER);
           return;
        }
        if (channel == null)
        {
           Window.alert("Please join game!");
           return;
        }
        disableUI();
        DelimitedString chatRequest = new DelimitedString(Shared.HOST_CHAT);
        chatRequest.add(gameCode);
        chatRequest.add(playerName);
        chatRequest.add(chatText);
        gameService.requestService(chatRequest.toString(),
                                   new AsyncCallback<String>()
                                   {
                                      public void onFailure(Throwable caught)
                                      {
                                         Window.alert("Error sending chat: " + caught.getMessage());
                                         enableUI();
                                      }

                                      public void onSuccess(String result)
                                      {
                                         if (!Shared.isOK(result))
                                         {
                                            if (Shared.isError(result))
                                            {
                                               Window.alert(result);
                                            }
                                            else
                                            {
                                               Window.alert("Error sending chat");
                                            }
                                         }
                                         else
                                         {
                                            hostChatTextArea.setText(hostChatTextArea.getText() +
                                                                     playerName + ": " +
                                                                     hostChatTextBox.getText() + "\n");
                                            hostChatTextBox.setText("");
                                         }
                                         enableUI();
                                      }
                                   }
                                   );
     }

     // Send chat to auditors.
     else if (event.getSource() == auditorChatSendButton)
     {
        String chatText = auditorChatTextBox.getText();
        if (Shared.isVoid(chatText))
        {
           return;
        }
        if (channel == null)
        {
           Window.alert("Please create game!");
           return;
        }
        if (chatText.contains(DelimitedString.DELIMITER))
        {
           Window.alert("Invalid character: " + DelimitedString.DELIMITER);
           return;
        }
        if (channel == null)
        {
           Window.alert("Please join game!");
           return;
        }
        disableUI();
        DelimitedString chatRequest = new DelimitedString(Shared.AUDITOR_CHAT);
        chatRequest.add(gameCode);
        chatRequest.add(transactionNumber);
        chatRequest.add(chatText);
        gameService.requestService(chatRequest.toString(),
                                   new AsyncCallback<String>()
                                   {
                                      public void onFailure(Throwable caught)
                                      {
                                         Window.alert("Error sending chat: " + caught.getMessage());
                                         enableUI();
                                      }

                                      public void onSuccess(String result)
                                      {
                                         if (!Shared.isOK(result))
                                         {
                                            if (Shared.isError(result))
                                            {
                                               Window.alert(result);
                                            }
                                            else
                                            {
                                               Window.alert("Error sending chat");
                                            }
                                         }
                                         else
                                         {
                                            auditorChatTextArea.setText(auditorChatTextArea.getText() +
                                                                        "claimant: " +
                                                                        auditorChatTextBox.getText() + "\n");
                                            auditorChatTextBox.setText("");
                                         }
                                         enableUI();
                                      }
                                   }
                                   );
     }

     // Send chat to claimant.
     else if (event.getSource() == claimantChatSendButton)
     {
        String chatText = claimantChatTextBox.getText();
        if (Shared.isVoid(chatText))
        {
           return;
        }
        if (channel == null)
        {
           Window.alert("Please create game!");
           return;
        }
        if (chatText.contains(DelimitedString.DELIMITER))
        {
           Window.alert("Invalid character: " + DelimitedString.DELIMITER);
           return;
        }
        if (channel == null)
        {
           Window.alert("Please join game!");
           return;
        }
        disableUI();
        DelimitedString chatRequest = new DelimitedString(Shared.CLAIMANT_CHAT);
        chatRequest.add(gameCode);
        chatRequest.add(transactionNumber);
        chatRequest.add(chatText);
        gameService.requestService(chatRequest.toString(),
                                   new AsyncCallback<String>()
                                   {
                                      public void onFailure(Throwable caught)
                                      {
                                         Window.alert("Error sending chat: " + caught.getMessage());
                                         enableUI();
                                      }

                                      public void onSuccess(String result)
                                      {
                                         if (!Shared.isOK(result))
                                         {
                                            if (Shared.isError(result))
                                            {
                                               Window.alert(result);
                                            }
                                            else
                                            {
                                               Window.alert("Error sending chat");
                                            }
                                         }
                                         else
                                         {
                                            claimantChatTextArea.setText(claimantChatTextArea.getText() +
                                                                         "auditor: " +
                                                                         claimantChatTextBox.getText() + "\n");
                                            claimantChatTextBox.setText("");
                                         }
                                         enableUI();
                                      }
                                   }
                                   );
     }

     else if (event.getSource() == claimDistributionTestButton)
     {
        String valueText = claimDistributionTestValueTextBox.getText().trim();
        if (Shared.isVoid(valueText))
        {
           Window.alert("Please enter test value");
           return;
        }
        double value;
        try {
           value = Double.parseDouble(valueText);
        }
        catch (NumberFormatException e) {
           Window.alert("Invalid test value");
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
           Window.alert("Please enter test value");
           return;
        }
        double value;
        try {
           value = Double.parseDouble(valueText);
        }
        catch (NumberFormatException e) {
           Window.alert("Invalid test value");
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
           Window.alert("Please enter claim value");
           return;
        }
        double claim;
        try {
           claim = Double.parseDouble(claimText);
        }
        catch (NumberFormatException e) {
           Window.alert("Invalid claim value");
           return;
        }
        if (claim < 0.0)
        {
           Window.alert("Invalid claim value");
           return;
        }
        claimState = TRANSACTION_STATE.WAITING;
        claimResourcesGrantTextBox.setText("waiting");
        disableUI();
        DelimitedString claimRequest = new DelimitedString(Shared.SET_CLAIM);
        claimRequest.add(gameCode);
        claimRequest.add(transactionNumber);
        claimRequest.add(claim);
        gameService.requestService(claimRequest.toString(),
                                   new AsyncCallback<String>()
                                   {
                                      public void onFailure(Throwable caught)
                                      {
                                         Window.alert("Error sending claim: " + caught.getMessage());
                                         enableUI();
                                      }

                                      public void onSuccess(String result)
                                      {
                                         if (!Shared.isOK(result))
                                         {
                                            if (Shared.isError(result))
                                            {
                                               Window.alert(result);
                                            }
                                            else
                                            {
                                               Window.alert("Error sending claim");
                                            }
                                         }
                                         enableUI();
                                      }
                                   }
                                   );
     }
     else if (event.getSource() == auditResourcesGrantSetButton)
     {
        String grantText = auditResourcesGrantTextBox.getText().trim();
        if (Shared.isVoid(grantText))
        {
           Window.alert("Please enter grant value");
           return;
        }
        double grant;
        try {
           grant = Double.parseDouble(grantText);
        }
        catch (NumberFormatException e) {
           Window.alert("Invalid grant value");
           return;
        }
        if (grant < 0.0)
        {
           Window.alert("Invalid grant value");
           return;
        }
        double claim;
        try {
           claim = Double.parseDouble(auditResourcesClaimTextBox.getText());
        }
        catch (NumberFormatException e) {
           Window.alert("Invalid claim value");
           return;
        }
        if (grant > claim)
        {
           Window.alert("Grant cannot be greater than claim");
           return;
        }
        auditState = TRANSACTION_STATE.WAITING;
        auditResourcesConsensusTextBox.setText("waiting");
        disableUI();
        DelimitedString grantRequest = new DelimitedString(Shared.SET_GRANT);
        grantRequest.add(gameCode);
        grantRequest.add(transactionNumber);
        grantRequest.add(grant);
        grantRequest.add(playerNameTextBox.getText());
        gameService.requestService(grantRequest.toString(),
                                   new AsyncCallback<String>()
                                   {
                                      public void onFailure(Throwable caught)
                                      {
                                         Window.alert("Error sending grant: " + caught.getMessage());
                                         enableUI();
                                      }

                                      public void onSuccess(String result)
                                      {
                                         if (!Shared.isOK(result))
                                         {
                                            if (Shared.isError(result))
                                            {
                                               Window.alert(result);
                                            }
                                            else
                                            {
                                               Window.alert("Error sending grant");
                                            }
                                         }
                                         enableUI();
                                      }
                                   }
                                   );
     }
     else if (event.getSource() == claimResourcesDonateButton)
     {
        String donateText = claimResourcesDonateTextBox.getText().trim();
        if (Shared.isVoid(donateText))
        {
           Window.alert("Please enter donation value");
           return;
        }
        double donation;
        try {
           donation = Double.parseDouble(donateText);
        }
        catch (NumberFormatException e) {
           Window.alert("Invalid donation value");
           return;
        }
        if (donation < 0.0)
        {
           Window.alert("Invalid donation value");
           return;
        }
        String beneficiary = claimResourcesDonateBeneficiaryTextBox.getText().trim();
        if (Shared.isVoid(beneficiary))
        {
           Window.alert("Please enter beneficiary value");
           return;
        }
        if (beneficiary.equals(playerNameTextBox.getText()))
        {
           Window.alert("Cannot donate to self");
           return;
        }
        disableUI();
        DelimitedString donationRequest = new DelimitedString(Shared.SET_DONATION);
        donationRequest.add(gameCode);
        donationRequest.add(transactionNumber);
        donationRequest.add(donation);
        donationRequest.add(beneficiary);
        gameService.requestService(donationRequest.toString(),
                                   new AsyncCallback<String>()
                                   {
                                      public void onFailure(Throwable caught)
                                      {
                                         Window.alert("Error sending donation: " + caught.getMessage());
                                         enableUI();
                                      }

                                      public void onSuccess(String result)
                                      {
                                         if (!Shared.isOK(result))
                                         {
                                            if (Shared.isError(result))
                                            {
                                               Window.alert(result);
                                            }
                                            else
                                            {
                                               Window.alert("Error sending donation");
                                            }
                                         }
                                         else
                                         {
                                            claimResourcesDonateTextBox.setText("");
                                            claimResourcesDonateBeneficiaryTextBox.setText("");
                                         }
                                         enableUI();
                                      }
                                   }
                                   );
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
        DelimitedString finishRequest = new DelimitedString(Shared.FINISH_TRANSACTION);
        finishRequest.add(gameCode);
        finishRequest.add(transactionNumber);
        finishRequest.add(playerNameTextBox.getText());
        gameService.requestService(finishRequest.toString(),
                                   new AsyncCallback<String>()
                                   {
                                      public void onFailure(Throwable caught)
                                      {
                                         Window.alert("Error sending finish: " + caught.getMessage());
                                         enableUI();
                                      }

                                      public void onSuccess(String result)
                                      {
                                         if (!Shared.isOK(result))
                                         {
                                            if (Shared.isError(result))
                                            {
                                               Window.alert(result);
                                            }
                                            else
                                            {
                                               Window.alert("Error sending finish");
                                            }
                                         }
                                         enableUI();
                                      }
                                   }
                                   );
     }
   }

   private void clearHomeResources()
   {
      homeResourcesActualTextBox.setText("");
      homeResourcesPersonalTextBox.setText("");
      homeResourcesCommonTextBox.setText("");
      homeResourcesEntitledTextBox.setText("");
   }


   private void showHomeResources(String personal, String common, String entitled)
   {
      double actual = Double.parseDouble(personal) + Double.parseDouble(common);

      homeResourcesActualTextBox.setText(doubleToString(actual));
      homeResourcesPersonalTextBox.setText(personal);
      homeResourcesCommonTextBox.setText(common);
      homeResourcesEntitledTextBox.setText(entitled);
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
      NumberFormat decimalFormat = NumberFormat.getFormat(".##");

      return(decimalFormat.format(value));
   }


   // Disable UI.
   private void disableUI()
   {
      playerNameTextBox.setReadOnly(true);
      playerJoinQuitButton.setEnabled(false);
      gameCodeTextBox.setReadOnly(true);
      hostChatTextBox.setReadOnly(true);
      hostChatSendButton.setEnabled(false);
      claimResourcesClaimTextBox.setReadOnly(true);
      claimResourcesSetButton.setEnabled(false);
      claimResourcesDonateTextBox.setReadOnly(true);
      claimResourcesDonateBeneficiaryTextBox.setReadOnly(true);
      claimResourcesDonateButton.setEnabled(false);
      claimFinishButton.setEnabled(false);
      auditorChatTextBox.setReadOnly(true);
      auditorChatSendButton.setEnabled(false);
      auditResourcesGrantTextBox.setReadOnly(true);
      auditResourcesGrantSetButton.setEnabled(false);
      auditFinishButton.setEnabled(false);
      claimantChatTextBox.setReadOnly(true);
      claimantChatSendButton.setEnabled(false);
   }


   // Enable UI.
   private void enableUI()
   {
      playerJoinQuitButton.setEnabled(true);
      if (channel == null)
      {
         playerNameTextBox.setReadOnly(false);
         gameCodeTextBox.setReadOnly(false);
         hostChatTextBox.setReadOnly(true);
         hostChatSendButton.setEnabled(false);
         claimResourcesClaimTextBox.setReadOnly(true);
         claimResourcesSetButton.setEnabled(false);
         claimResourcesDonateTextBox.setReadOnly(true);
         claimResourcesDonateBeneficiaryTextBox.setReadOnly(true);
         claimResourcesDonateButton.setEnabled(false);
         claimFinishButton.setEnabled(false);
         auditorChatTextBox.setReadOnly(true);
         auditorChatSendButton.setEnabled(false);
         auditResourcesGrantTextBox.setReadOnly(true);
         auditResourcesGrantSetButton.setEnabled(false);
         auditFinishButton.setEnabled(false);
         claimantChatTextBox.setReadOnly(true);
         claimantChatSendButton.setEnabled(false);
      }
      else
      {
         playerNameTextBox.setReadOnly(true);
         gameCodeTextBox.setReadOnly(true);
         hostChatTextBox.setReadOnly(false);
         hostChatSendButton.setEnabled(true);
         switch (claimState)
         {
         case INACTIVE:
            claimResourcesClaimTextBox.setReadOnly(true);
            claimResourcesSetButton.setEnabled(false);
            claimResourcesDonateTextBox.setReadOnly(true);
            claimResourcesDonateBeneficiaryTextBox.setReadOnly(true);
            claimResourcesDonateButton.setEnabled(false);
            claimFinishButton.setEnabled(false);
            auditorChatTextBox.setReadOnly(true);
            auditorChatSendButton.setEnabled(false);
            break;

         case PENDING:
            claimResourcesClaimTextBox.setReadOnly(false);
            claimResourcesSetButton.setEnabled(true);
            claimResourcesDonateTextBox.setReadOnly(true);
            claimResourcesDonateBeneficiaryTextBox.setReadOnly(true);
            claimResourcesDonateButton.setEnabled(false);
            claimFinishButton.setEnabled(false);
            auditorChatTextBox.setReadOnly(true);
            auditorChatSendButton.setEnabled(false);
            break;

         case WAITING:
            claimResourcesClaimTextBox.setReadOnly(true);
            claimResourcesSetButton.setEnabled(false);
            claimResourcesDonateTextBox.setReadOnly(true);
            claimResourcesDonateBeneficiaryTextBox.setReadOnly(true);
            claimResourcesDonateButton.setEnabled(false);
            claimFinishButton.setEnabled(false);
            auditorChatTextBox.setReadOnly(false);
            auditorChatSendButton.setEnabled(true);
            break;

         case FINISHED:
            claimResourcesClaimTextBox.setReadOnly(true);
            claimResourcesSetButton.setEnabled(false);
            claimResourcesDonateTextBox.setReadOnly(false);
            claimResourcesDonateBeneficiaryTextBox.setReadOnly(false);
            claimResourcesDonateButton.setEnabled(true);
            claimFinishButton.setEnabled(true);
            auditorChatTextBox.setReadOnly(false);
            auditorChatSendButton.setEnabled(true);
            break;
         }
         switch (auditState)
         {
         case INACTIVE:
            auditResourcesGrantTextBox.setReadOnly(true);
            auditResourcesGrantSetButton.setEnabled(false);
            auditFinishButton.setEnabled(false);
            claimantChatTextBox.setReadOnly(true);
            claimantChatSendButton.setEnabled(false);
            break;

         case PENDING:
            auditResourcesGrantTextBox.setReadOnly(false);
            auditResourcesGrantSetButton.setEnabled(true);
            auditFinishButton.setEnabled(false);
            claimantChatTextBox.setReadOnly(false);
            claimantChatSendButton.setEnabled(true);
            break;

         case WAITING:
            auditResourcesGrantTextBox.setReadOnly(true);
            auditResourcesGrantSetButton.setEnabled(false);
            auditFinishButton.setEnabled(false);
            claimantChatTextBox.setReadOnly(false);
            claimantChatSendButton.setEnabled(true);
            break;

         case FINISHED:
            auditResourcesGrantTextBox.setReadOnly(true);
            auditResourcesGrantSetButton.setEnabled(false);
            auditFinishButton.setEnabled(true);
            claimantChatTextBox.setReadOnly(false);
            claimantChatSendButton.setEnabled(true);
            break;
         }
      }
   }


   // Channel socket listener.
   class ChannelSocketListener implements SocketListener
   {
      @Override
      public void onOpen()
      {
      }


      @Override
      public void onMessage(String message)
      {
         if (Shared.isVoid(message))
         {
            return;
         }
         String[] args = new DelimitedString(message).parse();
         if (args.length == 0)
         {
            return;
         }
         String operation = args[0];
         if (operation.equals(Shared.QUIT_GAME) && (args.length == 2))
         {
            // Forced quit.
            disableUI();
            if (channelSocket != null)
            {
               channelSocket.close();
            }
            channel = null;
            playerJoinQuitButton.setText("Join");
            clearHomeResources();
            roleTabPanel.selectTab(HOME_TAB);
            roleTabBar.setTabEnabled(CLAIM_TAB, false);
            roleTabBar.setTabEnabled(AUDIT_TAB, false);
            claimState = TRANSACTION_STATE.INACTIVE;
            auditState = TRANSACTION_STATE.INACTIVE;
            Window.alert(args[1]);
            enableUI();
         }
         else if (operation.equals(Shared.SET_PLAYER_RESOURCES) && (args.length == 4))
         {
            // Set player resources.
            double personalResources = Double.parseDouble(args[1]);
            double commonResources   = Double.parseDouble(args[2]);
            double entitledResources = Double.parseDouble(args[3]);
            showHomeResources(personalResources, commonResources, entitledResources);
         }
         else if (operation.equals(Shared.PLAYER_CHAT) &&
                  ((args.length == 3) || (args.length == 4)))
         {
            // Chat from host.
            if (args.length == 3)
            {
               String chatText = args[2];
               hostChatTextArea.setText(
                  hostChatTextArea.getText() + "host: " + chatText + "\n");
            }
            else
            {
               String chatText = args[3];
               hostChatTextArea.setText(
                  hostChatTextArea.getText() + "host: " + chatText + "\n");
            }
         }
         else if (operation.equals(Shared.PLAYER_ALERT) &&
                  ((args.length == 3) || (args.length == 4)))
         {
            // Alert from host.
            if (args.length == 3)
            {
               String alertText = args[2];
               Window.alert("host: " + alertText);
            }
            else
            {
               String alertText = args[3];
               Window.alert("host: " + alertText);
            }
         }
         else if (operation.equals(Shared.CLAIMANT_CHAT) && (args.length == 4))
         {
            // Chat from auditor.
            String chatText = args[3];
            auditorChatTextArea.setText(
               auditorChatTextArea.getText() + "auditor: " + chatText + "\n");
         }
         else if (operation.equals(Shared.AUDITOR_CHAT) && (args.length == 4))
         {
            // Chat from claimant.
            String chatText = args[3];
            claimantChatTextArea.setText(
               claimantChatTextArea.getText() + "claimant: " + chatText + "\n");
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
            roleTabBar.setTabEnabled(CLAIM_TAB, true);
            roleTabPanel.selectTab(CLAIM_TAB);
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
            roleTabBar.setTabEnabled(AUDIT_TAB, true);
            roleTabPanel.selectTab(AUDIT_TAB);
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
               roleTabPanel.selectTab(HOME_TAB);
               roleTabBar.setTabEnabled(CLAIM_TAB, false);
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
               roleTabPanel.selectTab(HOME_TAB);
               roleTabBar.setTabEnabled(AUDIT_TAB, false);
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
               roleTabPanel.selectTab(HOME_TAB);
               roleTabBar.setTabEnabled(CLAIM_TAB, false);
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
               roleTabPanel.selectTab(HOME_TAB);
               roleTabBar.setTabEnabled(AUDIT_TAB, false);
               auditState = TRANSACTION_STATE.INACTIVE;
            }
            Window.alert("Transaction aborted!");
            enableUI();
         }
      }


      @Override
      public void onError(ChannelError error)
      {
         Window.alert("Channel error: " + error.getCode() +
                      " : " + error.getDescription());
      }


      @Override
      public void onClose()
      {
      }
   }
   */
}
