// Conformative game host client.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import com.dialectek.conformative.hyperledger.shared.Shared;

public class Host implements ActionListener, ItemListener
{
   private JFrame          	  frame;
   private Label              conformativeGameLabel;
   private JTabbedPane        roleTabPanel;
   private JPanel             homePanel;
   private JTable             homeFlexTable;
   private Label              gameCodeLabel;
   private TextField          gameCodeTextBox;
   private Label              gameResourcesLabel;
   private TextField          gameResourcesTextBox;
   private Button             gameCreateDeleteButton;
   private Label              gameStateLabel;
   private JComboBox          gameStateListBox;
   private JPanel             playersCaptionPanel;
   private JPanel             playersPanel;
   private JPanel             playersJoinedPanel;
   private Label              playersJoinedLabel;
   private TextField          playersJoinedTextBox;
   private JComboBox          playersListBox;
   private Button             playerRemoveButton;
   private JPanel             playerResourceCaptionPanel;
   private JPanel             playerResourcePanel;
   private Label              playerTotalResourceLabel;
   private TextField          playerTotalResourceTextBox;
   private Label              playerPersonalResourceLabel;
   private TextField          playerPersonalResourceTextBox;
   private Label              playerCommonResourceLabel;
   private TextField          playerCommonResourceTextBox;
   private JPanel             playerChatCaptionPanel;
   private JPanel             playerChatPanel;
   private TextArea           playerChatTextArea;
   private Button             playerChatClearButton;
   private TextField          playerChatTextBox;
   private JTable             playerChatButtonsFlexTable;
   private Button             playerChatSendButton;
   private Button             playerChatAlertButton;
   private JPanel             transactionHistoryCaptionPanel;
   private TextArea           transactionHistoryTextArea;
   private JPanel             transactionPanel;
   private JPanel             transactionParticipantsCaptionPanel;
   private JPanel             transactionParticipantsPanel;
   private JPanel             transactionParticipantsClaimantCaptionPanel;
   private JPanel             transactionParticipantsClaimantPanel;
   private JComboBox          transactionParticipantsClaimantCandidateListBox;
   private Label              transactionParticipantsClaimantLabel;
   private TextField          transactionParticipantsClaimantTextBox;
   private JPanel             transactionParticipantsAuditorCaptionPanel;
   private JPanel             transactionParticipantsAuditorPanel;
   private JComboBox          transactionParticipantsAuditorCandidateListBox;
   private Label              transactionParticipantsAuditorLabel;
   private JComboBox          transactionParticipantsAuditorListBox;
   private Button             transactionParticipantsSetButton;
   private JPanel             transactionClaimCaptionPanel;
   private JPanel             transactionClaimPanel;
   private JPanel             transactionClaimDistributionCaptionPanel;
   private JPanel             transactionClaimDistributionPanel;
   private Canvas             transactionClaimDistributionCanvas;
   private NormalDistribution transactionClaimDistribution;
   private JTable             transactionClaimDistributionParameterFlexTable;
   private Label              transactionClaimDistributionMeanLabel;
   private TextField          transactionClaimDistributionMeanTextBox;
   private Label              transactionClaimDistributionSigmaLabel;
   private TextField          transactionClaimDistributionSigmaTextBox;
   private Button             transactionClaimDistributionParameterSetButton;
   private Label              transactionClaimDistributionTestValueLabel;
   private TextField          transactionClaimDistributionTestValueTextBox;
   private Button             transactionClaimDistributionTestButton;
   private TextField          transactionClaimDistributionTestProbabilityTextBox;
   private JTable             transactionClaimFlexTable;
   private Label              transactionClaimEntitlementLabel;
   private TextField          transactionClaimEntitlementTextBox;
   private Button             transactionClaimEntitlementGenerateButton;
   private Button             transactionClaimEntitlementSetButton;
   private Label              transactionClaimAmountLabel;
   private TextField          transactionClaimAmountTextBox;
   private JPanel             transactionGrantCaptionPanel;
   private JTable             transactionGrantFlexTable;
   private Label              transactionGrantAuditorWorkingLabel;
   private JComboBox          transactionGrantAuditorWorkingListBox;
   private Label              transactionGrantAuditorCompletedLabel;
   private JComboBox          transactionGrantAuditorCompletedListBox;
   private Label              transactionGrantAuditorAmountLabel;
   private TextField          transactionGrantAuditorAmountTextBox;
   private Label              transactionGrantClaimantLabel;
   private TextField          transactionGrantClaimantTextBox;
   private JPanel             transactionPenaltyCaptionPanel;
   private JPanel             transactionPenaltyPanel;
   private JPanel             transactionPenaltyParameterCaptionPanel;
   private JTable             transactionPenaltyParameterFlexTable;
   private Label              transactionPenaltyClaimantParameterLabel;
   private TextField          transactionPenaltyClaimantParameterTextBox;
   private Label              transactionPenaltyAuditorParameterLabel;
   private TextField          transactionPenaltyAuditorParameterTextBox;
   private Button             transactionPenaltySetButton;
   private JTable             transactionPenaltyFlexTable;
   private Label              transactionPenaltyAuditorLabel;
   private JComboBox          transactionPenaltyAuditorListBox;
   private Label              transactionPenaltyAuditorAmountLabel;
   private TextField          transactionPenaltyAuditorAmountTextBox;
   private Label              transactionPenaltyClaimantLabel;
   private TextField          transactionPenaltyClaimantTextBox;
   private JPanel             transactionFinishCaptionPanel;
   private JTable             transactionFinishFlexTable;
   private Label              transactionFinishPendingParticipantsLabel;
   private JComboBox          transactionFinishPendingParticipantsListBox;
   private Label              transactionFinishedLabel;
   private JComboBox          transactionFinishedParticipantsListBox;
   private JPanel             transactionCompletionPanel;
   private Button             transactionFinishButton;
   private Button             transactionAbortButton;

   private static final int HOME_TAB        = 0;
   private static final int TRANSACTION_TAB = 1;

   // Game code.
   private String gameCode;

   // Game state.
   private int gameState;

   // Resources.
   private double resources;

   // Entitlement probability distribution parameters.
   private double mean;
   private double sigma;

   // Default penalty parameters.
   public static final double DEFAULT_CLAIMANT_PENALTY_PARAMETER = 0.1;
   private double             claimantPenaltyParameter;
   public static final double DEFAULT_AUDITOR_PENALTY_PARAMETER = 0.1;
   private double             auditorPenaltyParameter;

   // Transaction state.
   public enum TRANSACTION_STATE
   {
      UNAVAILABLE,
      INACTIVE,
      CLAIM_DISTRIBUTION,
      CLAIM_ENTITLEMENT,
      CLAIM_WAIT,
      GRANT_WAIT,
      PENALTY_PARAMETERS,
      PENALTY_WAIT,
      FINISH_WAIT,
      FINISHED
   }
   private TRANSACTION_STATE transactionState;

   // Transaction number.
   private int transactionNumber;

   // Player removal.
   private String removePlayer;

   // Auditor grants and penalties.
   private ArrayList<String> auditorNames;
   private ArrayList<String> auditorGrants;
   private ArrayList<String> auditorPenalties;

   // Timer.
   private final int timerInterval_ms = 500;
   private Timer     timer;

   // Constructor.
   public Host(JFrame frame)
   {
	  // Access network.
	  //NetworkClient.init();
	  
      // Title.
      this.frame        = frame;
      frame.setTitle("Conformative Game Host");

      // Role tabs.
      roleTabPanel = new JTabbedPane();

      // Home tab.
      homePanel = new JPanel();
      roleTabPanel.add(homePanel, "Home");

      homeFlexTable = new JTable(2, 5);
      homePanel.add(homeFlexTable);
      gameCodeLabel = new Label("Code:");
      homeFlexTable.setValueAt(gameCodeLabel, 0, 0);
      gameCodeTextBox = new TextField(120);
      homeFlexTable.setValueAt(gameCodeTextBox, 0, 1);
      gameResourcesLabel = new Label("Resources:");
      homeFlexTable.setValueAt(gameResourcesLabel, 0, 2);
      gameResourcesTextBox = new TextField(60);
      homeFlexTable.setValueAt(gameResourcesTextBox, 0, 3);
      gameCreateDeleteButton = new Button("Create");
      homeFlexTable.setValueAt(gameCreateDeleteButton, 0, 4);
      gameCreateDeleteButton.addActionListener(this);
      gameStateLabel = new Label("State:");
      homeFlexTable.setValueAt(gameStateLabel, 1, 0);
      gameStateListBox = new JComboBox();
      homeFlexTable.setValueAt(gameStateListBox, 1, 1);
      gameStateListBox.setEnabled(false);
      gameStateListBox.addItemListener(this);
      gameStateListBox.addItem("Pending");
      gameStateListBox.addItem("Joining");
      gameStateListBox.addItem("Running");
      gameStateListBox.addItem("Completed");
      gameStateListBox.setSelectedIndex(Shared.PENDING);
      playersCaptionPanel = new JPanel();
      playersCaptionPanel.setBorder(BorderFactory.createTitledBorder("Players"));
      homePanel.add(playersCaptionPanel);
      playersCaptionPanel.setSize(450, playersCaptionPanel.getSize().height);
      playersPanel = new JPanel();
      playersCaptionPanel.add(playersPanel);
      playersPanel.setSize(440, playersPanel.getSize().height);
      playersJoinedPanel = new JPanel();
      playersPanel.add(playersJoinedPanel);
      playersJoinedPanel.setSize(235, playersJoinedPanel.getSize().height);
      playersJoinedLabel = new Label("Joined:");
      playersJoinedPanel.add(playersJoinedLabel);
      playersJoinedPanel.setLayout(new BoxLayout(playersJoinedPanel, BoxLayout.Y_AXIS));
      playersJoinedLabel.setSize(50, playersJoinedLabel.getSize().height);
      playersJoinedTextBox = new TextField();
      playersJoinedTextBox.setEnabled(false);
      playersJoinedTextBox.setText("0");
      playersJoinedPanel.add(playersJoinedTextBox);
      playersJoinedTextBox.setSize(40, playersJoinedTextBox.getSize().height);
      playersListBox = new JComboBox();
      playersJoinedPanel.add(playersListBox);
      playersListBox.addItemListener(this);
      playersListBox.addItem(Shared.ALL_PLAYERS);
      playerRemoveButton = new Button("Remove");
      playersJoinedPanel.add(playerRemoveButton);
      playerRemoveButton.addActionListener(this);
      playerResourceCaptionPanel = new JPanel();
      playerResourceCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resources"));      
      playersPanel.add(playerResourceCaptionPanel);
      playerResourceCaptionPanel.setSize(430, playerResourceCaptionPanel.getSize().height);
      playerResourcePanel = new JPanel();
      playerResourceCaptionPanel.add(playerResourcePanel);
      playerResourcePanel.setSize(415, playerResourcePanel.getSize().height);
      playerTotalResourceLabel = new Label("Total:");
      playerResourcePanel.add(playerTotalResourceLabel);
      playerResourcePanel.setLayout(new BoxLayout(playerResourcePanel, BoxLayout.Y_AXIS));      
      playerTotalResourceLabel.setSize(40, playerTotalResourceLabel.getSize().height);
      playerTotalResourceTextBox = new TextField();
      playerTotalResourceTextBox.setEditable(false);
      playerResourcePanel.add(playerTotalResourceTextBox);
      playerTotalResourceTextBox.setSize(60, playerTotalResourceTextBox.getSize().height);
      playerPersonalResourceLabel = new Label(" = Personal:");
      playerResourcePanel.add(playerPersonalResourceLabel);
      playerPersonalResourceLabel.setSize(70, playerPersonalResourceLabel.getSize().height);
      playerPersonalResourceTextBox = new TextField();
      playerPersonalResourceTextBox.setEditable(false);
      playerResourcePanel.add(playerPersonalResourceTextBox);
      playerPersonalResourceTextBox.setSize(60, playerPersonalResourceTextBox.getSize().height);
      playerCommonResourceLabel = new Label(" + Common:");
      playerResourcePanel.add(playerCommonResourceLabel);
      playerCommonResourceLabel.setSize(70, playerCommonResourceLabel.getSize().height);
      playerCommonResourceTextBox = new TextField();
      playerCommonResourceTextBox.setEditable(false);
      playerResourcePanel.add(playerCommonResourceTextBox);
      playerCommonResourceTextBox.setSize(60, playerCommonResourceTextBox.getSize().height);
      playerChatCaptionPanel = new JPanel();
      playerChatCaptionPanel.setBorder(BorderFactory.createTitledBorder("Player chat"));      
      homePanel.add(playerChatCaptionPanel);
      playerChatCaptionPanel.setSize(450, playerChatCaptionPanel.getSize().height);
      playerChatPanel = new JPanel();
      playerChatCaptionPanel.add(playerChatPanel);
      playerChatTextArea = new TextArea();
      playerChatTextArea.setEditable(false);
      playerChatTextArea.setText("Note: prepend <player name>/ to send to specific player\n");
      playerChatPanel.add(playerChatTextArea);
      playerChatTextArea.setSize(430, 100);
      playerChatClearButton = new Button("Clear");
      playerChatPanel.add(playerChatClearButton);
      playerChatClearButton.addActionListener(this);
      playerChatTextBox = new TextField();
      playerChatPanel.add(playerChatTextBox);
      playerChatTextBox.setSize(430, playerChatTextBox.getSize().height);
      playerChatButtonsFlexTable = new JTable(1, 2);
      playerChatPanel.add(playerChatButtonsFlexTable);
      playerChatSendButton = new Button("Send");
      playerChatButtonsFlexTable.setValueAt(playerChatSendButton, 0, 0);
      playerChatSendButton.addActionListener(this);
      playerChatAlertButton = new Button("Alert");
      playerChatButtonsFlexTable.setValueAt(playerChatAlertButton, 0, 1);
      playerChatAlertButton.addActionListener(this);
      transactionHistoryCaptionPanel = new JPanel();
      transactionHistoryCaptionPanel.setBorder(BorderFactory.createTitledBorder("Transaction history"));            
      homePanel.add(transactionHistoryCaptionPanel);
      transactionHistoryCaptionPanel.setSize(450, transactionHistoryCaptionPanel.getSize().height);
      transactionHistoryTextArea = new TextArea();
      transactionHistoryTextArea.setEditable(false);
      transactionHistoryCaptionPanel.add(transactionHistoryTextArea);
      transactionHistoryTextArea.setSize(430, 50);

      // Transaction tab.
      transactionPanel = new JPanel();
      roleTabPanel.add(transactionPanel, "Transaction");
      transactionParticipantsCaptionPanel = new JPanel();
      transactionParticipantsCaptionPanel.setBorder(BorderFactory.createTitledBorder("1. Participants"));      
      transactionPanel.add(transactionParticipantsCaptionPanel);
      transactionParticipantsCaptionPanel.setSize(450, transactionParticipantsCaptionPanel.getSize().height);
      transactionParticipantsPanel = new JPanel();
      transactionParticipantsPanel.setVisible(false);
      transactionParticipantsCaptionPanel.add(transactionParticipantsPanel);
      transactionParticipantsClaimantCaptionPanel = new JPanel();
      transactionParticipantsClaimantCaptionPanel.setBorder(BorderFactory.createTitledBorder("Claimant"));       
      transactionParticipantsPanel.add(transactionParticipantsClaimantCaptionPanel);
      transactionParticipantsClaimantCaptionPanel.setSize(430, transactionParticipantsClaimantCaptionPanel.getSize().height);
      transactionParticipantsClaimantPanel = new JPanel();
      transactionParticipantsClaimantCaptionPanel.add(transactionParticipantsClaimantPanel);
      transactionParticipantsClaimantCandidateListBox = new JComboBox();
      transactionParticipantsClaimantCandidateListBox.addItemListener(this);
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantCandidateListBox);
      transactionParticipantsClaimantCandidateListBox.setSize(65, transactionParticipantsClaimantCandidateListBox.getSize().height);
      transactionParticipantsClaimantLabel = new Label("select->");
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantLabel);
      transactionParticipantsClaimantPanel.setLayout(new BoxLayout(transactionParticipantsClaimantPanel, BoxLayout.Y_AXIS)); 
      transactionParticipantsClaimantLabel.setSize(60, transactionParticipantsClaimantLabel.getSize().height);
      transactionParticipantsClaimantTextBox = new TextField();
      transactionParticipantsClaimantTextBox.setEditable(false);
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantTextBox);
      transactionParticipantsClaimantTextBox.setSize(300, transactionParticipantsClaimantTextBox.getSize().height);
      transactionParticipantsAuditorCaptionPanel = new JPanel();
      transactionParticipantsAuditorCaptionPanel.setBorder(BorderFactory.createTitledBorder("Auditors"));       
      transactionParticipantsPanel.add(transactionParticipantsAuditorCaptionPanel);
      transactionParticipantsAuditorCaptionPanel.setSize(430, transactionParticipantsAuditorCaptionPanel.getSize().height);
      transactionParticipantsAuditorPanel = new JPanel();
      transactionParticipantsAuditorCaptionPanel.add(transactionParticipantsAuditorPanel);
      transactionParticipantsAuditorCandidateListBox = new JComboBox();
      transactionParticipantsAuditorCandidateListBox.addItemListener(this);
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorCandidateListBox);
      transactionParticipantsAuditorCandidateListBox.setSize(65, transactionParticipantsAuditorCandidateListBox.getSize().height);
      transactionParticipantsAuditorLabel = new Label("select->");
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorLabel);
      transactionParticipantsAuditorPanel.setLayout(new BoxLayout(transactionParticipantsClaimantPanel, BoxLayout.Y_AXIS)); 
      transactionParticipantsAuditorLabel.setSize(60, transactionParticipantsAuditorLabel.getSize().height);
      transactionParticipantsAuditorListBox = new JComboBox();
      transactionParticipantsAuditorListBox.addItemListener(this);
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorListBox);
      transactionParticipantsAuditorListBox.setSize(65, transactionParticipantsAuditorListBox.getSize().height);
      transactionParticipantsSetButton = new Button("Set");
      transactionParticipantsPanel.add(transactionParticipantsSetButton);
      transactionParticipantsSetButton.addActionListener(this);
      transactionClaimCaptionPanel = new JPanel();
      transactionClaimCaptionPanel.setBorder(BorderFactory.createTitledBorder("2. Claim"));      
      transactionPanel.add(transactionClaimCaptionPanel);
      transactionClaimCaptionPanel.setSize(450, transactionClaimCaptionPanel.getSize().height);
      transactionClaimPanel = new JPanel();
      transactionClaimPanel.setVisible(false);
      transactionClaimCaptionPanel.add(transactionClaimPanel);
      transactionClaimPanel.setSize(430, transactionClaimPanel.getSize().height);
      transactionClaimDistributionCaptionPanel = new JPanel();
      transactionClaimDistributionCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resource entitlement probability"));      
      transactionClaimPanel.add(transactionClaimDistributionCaptionPanel);
      transactionClaimDistributionCaptionPanel.setSize(430, transactionClaimDistributionCaptionPanel.getSize().height);
      transactionClaimDistributionPanel = new JPanel();
      transactionClaimDistributionCaptionPanel.add(transactionClaimDistributionPanel);
      transactionClaimDistributionCanvas = new Canvas();
      transactionClaimDistribution       = new NormalDistribution(transactionClaimDistributionCanvas);
      if (transactionClaimDistributionCanvas != null)
      {
         transactionClaimDistribution.draw();
         transactionClaimDistributionPanel.add(transactionClaimDistributionCanvas);
      }
      else
      {
         String warning = "Your browser does not support the HTML5 Canvas";
         transactionClaimDistributionPanel.add(new Label(warning));
         Window.alert(warning);
      }
      transactionClaimDistributionParameterFlexTable = new JTable(2, 5);
      transactionClaimDistributionPanel.add(transactionClaimDistributionParameterFlexTable);
      transactionClaimDistributionMeanLabel = new Label("Mean:");
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionMeanLabel, 0, 0);
      transactionClaimDistributionMeanTextBox = new TextField();
      transactionClaimDistributionMeanTextBox.setSize(60, transactionClaimDistributionMeanTextBox.getSize().height);
      mean = NormalDistribution.DEFAULT_MEAN;
      transactionClaimDistributionMeanTextBox.setText(mean + "");
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionMeanTextBox, 0, 1);
      transactionClaimDistributionSigmaLabel = new Label("Sigma:");
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionSigmaLabel, 0, 2);
      transactionClaimDistributionSigmaTextBox = new TextField();
      transactionClaimDistributionSigmaTextBox.setSize(60, transactionClaimDistributionSigmaTextBox.getSize().height);
      sigma = NormalDistribution.DEFAULT_SIGMA;
      transactionClaimDistributionSigmaTextBox.setText(sigma + "");
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionSigmaTextBox, 0, 3);
      transactionClaimDistributionParameterSetButton = new Button("Set");
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionParameterSetButton, 0, 4);
      transactionClaimDistributionParameterSetButton.addActionListener(this);
      transactionClaimDistributionTestValueLabel = new Label("Test value:");
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionTestValueLabel, 1, 0);
      transactionClaimDistributionTestValueTextBox = new TextField();
      transactionClaimDistributionTestValueTextBox.setSize(60, transactionClaimDistributionTestValueTextBox.getSize().height);
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionTestValueTextBox, 1, 1);
      transactionClaimDistributionTestButton = new Button("Probability:");
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionTestButton, 1, 2);
      transactionClaimDistributionTestButton.addActionListener(this);
      transactionClaimDistributionTestProbabilityTextBox = new TextField();
      transactionClaimDistributionTestProbabilityTextBox.setSize(60, transactionClaimDistributionTestProbabilityTextBox.getSize().height);
      transactionClaimDistributionTestProbabilityTextBox.setEditable(false);
      transactionClaimDistributionParameterFlexTable.setValueAt(transactionClaimDistributionTestProbabilityTextBox, 1, 3);
      transactionClaimFlexTable = new JTable(2, 4);
      transactionClaimPanel.add(transactionClaimFlexTable);
      transactionClaimEntitlementLabel = new Label("Entitlement:");
      transactionClaimFlexTable.setValueAt(transactionClaimEntitlementLabel, 0, 0);
      transactionClaimEntitlementTextBox = new TextField();
      transactionClaimEntitlementTextBox.setSize(60, transactionClaimEntitlementTextBox.getSize().height);
      transactionClaimFlexTable.setValueAt(transactionClaimEntitlementTextBox, 0, 1);
      transactionClaimEntitlementGenerateButton = new Button("Generate");
      transactionClaimEntitlementGenerateButton.addActionListener(this);
      transactionClaimFlexTable.setValueAt(transactionClaimEntitlementGenerateButton, 0, 2);
      transactionClaimEntitlementSetButton = new Button("Set");
      transactionClaimEntitlementSetButton.addActionListener(this);
      transactionClaimFlexTable.setValueAt(transactionClaimEntitlementSetButton, 0, 3);
      transactionClaimAmountLabel = new Label("Claim:");
      transactionClaimFlexTable.setValueAt(transactionClaimAmountLabel, 1, 0);
      transactionClaimAmountTextBox = new TextField();
      transactionClaimAmountTextBox.setEditable(false);
      transactionClaimAmountTextBox.setSize(60, transactionClaimAmountTextBox.getSize().height);
      transactionClaimFlexTable.setValueAt(transactionClaimAmountTextBox, 1, 1);
      transactionGrantCaptionPanel = new JPanel();
      transactionGrantCaptionPanel.setBorder(BorderFactory.createTitledBorder("3. Grant"));            
      transactionPanel.add(transactionGrantCaptionPanel);
      transactionGrantCaptionPanel.setSize(450, transactionGrantCaptionPanel.getSize().height);
      transactionGrantFlexTable = new JTable(2, 6);
      transactionGrantFlexTable.setVisible(false);
      transactionGrantCaptionPanel.add(transactionGrantFlexTable);
      transactionGrantAuditorWorkingLabel = new Label("Auditing:");
      transactionGrantFlexTable.setValueAt(transactionGrantAuditorWorkingLabel, 0, 0);
      transactionGrantAuditorWorkingListBox = new JComboBox();
      transactionGrantFlexTable.setValueAt(transactionGrantAuditorWorkingListBox, 0, 1);
      transactionGrantAuditorWorkingListBox.setSize(65, transactionGrantAuditorWorkingListBox.getSize().height);
      transactionGrantAuditorCompletedLabel = new Label("Completed:");
      transactionGrantFlexTable.setValueAt(transactionGrantAuditorCompletedLabel, 0, 2);
      transactionGrantAuditorCompletedListBox = new JComboBox();
      transactionGrantAuditorCompletedListBox.addItemListener(this);
      transactionGrantFlexTable.setValueAt(transactionGrantAuditorCompletedListBox, 0, 3);
      transactionGrantAuditorCompletedListBox.setSize(65, transactionGrantAuditorCompletedListBox.getSize().height);
      transactionGrantAuditorAmountLabel = new Label("amount->");
      transactionGrantFlexTable.setValueAt(transactionGrantAuditorAmountLabel, 0, 4);
      transactionGrantAuditorAmountTextBox = new TextField();
      transactionGrantAuditorAmountTextBox.setEditable(false);
      transactionGrantAuditorAmountTextBox.setSize(60, transactionGrantAuditorAmountTextBox.getSize().height);
      transactionGrantFlexTable.setValueAt(transactionGrantAuditorAmountTextBox, 0, 5);
      transactionGrantClaimantLabel = new Label("Claimant:");
      transactionGrantFlexTable.setValueAt(transactionGrantClaimantLabel, 1, 0);
      transactionGrantClaimantTextBox = new TextField();
      transactionGrantClaimantTextBox.setEditable(false);
      transactionGrantClaimantTextBox.setSize(60, transactionGrantClaimantTextBox.getSize().height);
      transactionGrantFlexTable.setValueAt(transactionGrantClaimantTextBox, 1, 1);
      transactionPenaltyCaptionPanel = new JPanel();
      transactionPenaltyCaptionPanel.setBorder(BorderFactory.createTitledBorder("4. Penalty"));      
      transactionPanel.add(transactionPenaltyCaptionPanel);
      transactionPenaltyCaptionPanel.setSize(450, transactionPenaltyCaptionPanel.getSize().height);
      transactionPenaltyPanel = new JPanel();
      transactionPenaltyPanel.setVisible(false);
      transactionPenaltyCaptionPanel.add(transactionPenaltyPanel);
      transactionPenaltyParameterCaptionPanel = new JPanel();
      transactionPenaltyParameterCaptionPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));      
      transactionPenaltyPanel.add(transactionPenaltyParameterCaptionPanel);
      transactionPenaltyParameterFlexTable = new JTable(1, 5);
      transactionPenaltyParameterCaptionPanel.add(transactionPenaltyParameterFlexTable);
      transactionPenaltyClaimantParameterLabel = new Label("Claimant:");
      transactionPenaltyParameterFlexTable.setValueAt(transactionPenaltyClaimantParameterLabel, 0, 0);
      transactionPenaltyClaimantParameterTextBox = new TextField();
      transactionPenaltyClaimantParameterTextBox.setSize(60, transactionPenaltyClaimantParameterTextBox.getSize().height);
      claimantPenaltyParameter = DEFAULT_CLAIMANT_PENALTY_PARAMETER;
      transactionPenaltyClaimantParameterTextBox.setText(claimantPenaltyParameter + "");
      transactionPenaltyParameterFlexTable.setValueAt(transactionPenaltyClaimantParameterTextBox, 0, 1);
      transactionPenaltyAuditorParameterLabel = new Label("Auditor:");
      transactionPenaltyParameterFlexTable.setValueAt(transactionPenaltyAuditorParameterLabel, 0, 2);
      transactionPenaltyAuditorParameterTextBox = new TextField();
      transactionPenaltyAuditorParameterTextBox.setSize(60, transactionPenaltyAuditorParameterTextBox.getSize().height);
      auditorPenaltyParameter = DEFAULT_AUDITOR_PENALTY_PARAMETER;
      transactionPenaltyAuditorParameterTextBox.setText(auditorPenaltyParameter + "");
      transactionPenaltyParameterFlexTable.setValueAt(transactionPenaltyAuditorParameterTextBox, 0, 3);
      transactionPenaltySetButton = new Button("Set");
      transactionPenaltySetButton.addActionListener(this);
      transactionPenaltyParameterFlexTable.setValueAt(transactionPenaltySetButton, 0, 4);
      transactionPenaltyFlexTable = new JTable(1, 6);
      transactionPenaltyPanel.add(transactionPenaltyFlexTable);
      transactionPenaltyAuditorLabel = new Label("Auditor:");
      transactionPenaltyFlexTable.setValueAt(transactionPenaltyAuditorLabel, 0, 0);
      transactionPenaltyAuditorListBox = new JComboBox();
      transactionPenaltyAuditorListBox.addItemListener(this);
      transactionPenaltyFlexTable.setValueAt(transactionPenaltyAuditorListBox, 0, 1);
      transactionPenaltyAuditorListBox.setSize(65, transactionPenaltyAuditorListBox.getSize().height);
      transactionPenaltyAuditorAmountLabel = new Label("amount->");
      transactionPenaltyFlexTable.setValueAt(transactionPenaltyAuditorAmountLabel, 0, 2);
      transactionPenaltyAuditorAmountTextBox = new TextField();
      transactionPenaltyAuditorAmountTextBox.setEditable(false);
      transactionPenaltyAuditorAmountTextBox.setSize(60, transactionPenaltyAuditorAmountTextBox.getSize().height);
      transactionPenaltyFlexTable.setValueAt(transactionPenaltyAuditorAmountTextBox, 0, 3);
      transactionPenaltyClaimantLabel = new Label("Claimant:");
      transactionPenaltyFlexTable.setValueAt(transactionPenaltyClaimantLabel, 0, 4);
      transactionPenaltyClaimantTextBox = new TextField();
      transactionPenaltyClaimantTextBox.setEditable(false);
      transactionPenaltyClaimantTextBox.setSize(60, transactionPenaltyClaimantTextBox.getSize().height);
      transactionPenaltyFlexTable.setValueAt(transactionPenaltyClaimantTextBox, 0, 5);
      transactionFinishCaptionPanel = new JPanel();
      transactionFinishCaptionPanel.setBorder(BorderFactory.createTitledBorder("5. Finish"));            
      transactionPanel.add(transactionFinishCaptionPanel);
      transactionFinishCaptionPanel.setSize(450, transactionFinishCaptionPanel.getSize().height);
      transactionFinishFlexTable = new JTable(1, 4);
      transactionFinishFlexTable.setVisible(false);
      transactionFinishCaptionPanel.add(transactionFinishFlexTable);
      transactionFinishPendingParticipantsLabel = new Label("Participant:");
      transactionFinishFlexTable.setValueAt(transactionFinishPendingParticipantsLabel, 0, 0);
      transactionFinishPendingParticipantsListBox = new JComboBox();
      transactionFinishFlexTable.setValueAt(transactionFinishPendingParticipantsListBox, 0, 1);
      transactionFinishPendingParticipantsListBox.setSize(65, transactionFinishPendingParticipantsListBox.getSize().height);
      transactionFinishedLabel = new Label("finished->");
      transactionFinishFlexTable.setValueAt(transactionFinishedLabel, 0, 2);
      transactionFinishedParticipantsListBox = new JComboBox();
      transactionFinishFlexTable.setValueAt(transactionFinishedParticipantsListBox, 0, 3);
      transactionFinishedParticipantsListBox.setSize(65, transactionFinishedParticipantsListBox.getSize().height);
      transactionCompletionPanel = new JPanel();
      transactionPanel.add(transactionCompletionPanel);
      transactionCompletionPanel.setSize(110, transactionCompletionPanel.getSize().height);
      transactionFinishButton = new Button("Commit");
      transactionCompletionPanel.add(transactionFinishButton);
      transactionFinishButton.addActionListener(this);
      transactionAbortButton = new Button("Abort");
      transactionCompletionPanel.add(transactionAbortButton);
      transactionAbortButton.addActionListener(this);

      roleTabPanel.setSelectedIndex(HOME_TAB);
      roleTabPanel.setSize(454, 413);
      frame.add(roleTabPanel, 0, 37);

      // Initialize state.
      gameCode          = "";
      gameState         = Shared.PENDING;
      resources         = 0.0;
      auditorNames      = new ArrayList<String>();
      auditorGrants     = new ArrayList<String>();
      auditorPenalties  = new ArrayList<String>();
      transactionNumber = -1;
      resetTransaction();

      // Enable UI.
      enableUI();

      // Start timer.
      timer = new Timer()
      {
         @Override
         public void run()
         {
            doUpdate();
         }
      };
      timer.scheduleRepeating(timerInterval_ms);
   }


   // Update.
   private void doUpdate()
   {
      animateWaitTextBox(transactionClaimAmountTextBox);
      animateWaitTextBox(transactionGrantClaimantTextBox);
   }


   // Animate wait text box.
   private void animateWaitTextBox(TextBox textBox)
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


	@Override
	// Button handler.
	public void actionPerformed(ActionEvent event) 
	{
         // Create/delete game.
         if (event.getSource() == gameCreateDeleteButton)
         {
            gameCode = gameCodeTextBox.getText().trim();
            if ((Shared.isVoid(gameCode)))
            {
               Window.alert("Please enter game code");
               gameCode = "";
               return;
            }
            if (gameCode.contains(DelimitedString.DELIMITER))
            {
               Window.alert("Invalid code character: " + DelimitedString.DELIMITER);
               gameCode = "";
               return;
            }
            gameCodeTextBox.setText(gameCode);
            String r = gameResourcesTextBox.getText().trim();
            if (Shared.isVoid(r))
            {
               Window.alert("Please enter resources");
               return;
            }
            try {
               resources = Double.parseDouble(r.trim());
            }
            catch (NumberFormatException e) {
               Window.alert("Resources must be a non-negative number");
               resources = 0.0;
               return;
            }
            if (resources < 0.0)
            {
               Window.alert("Resources must be a non-negative number");
               resources = 0.0;
               return;
            }
            disableUI();
            if (channel == null)
            {
               // Create game.
               DelimitedString createRequest = new DelimitedString(Shared.CREATE_GAME);
               createRequest.add(gameCode);
               createRequest.add(resources);
               gameService.requestService(createRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error creating game: " + caught.getMessage());
                                                enableUI();
                                             }

                                             public void onSuccess(String token)
                                             {
                                                if (Shared.isVoid(token))
                                                {
                                                   Window.alert("Error creating game: bad channel token");
                                                }
                                                else
                                                {
                                                   if (token.startsWith(Shared.ERROR_PREFIX))
                                                   {
                                                      Window.alert(token);
                                                   }
                                                   else
                                                   {
                                                      // Create server communication channel.
                                                      ChannelFactory channelFactory = new ChannelFactoryImpl();
                                                      channel = channelFactory.createChannel(token);
                                                      if (channel == null)
                                                      {
                                                         Window.alert("Error creating game: cannot create channel");
                                                      }
                                                      else
                                                      {
                                                         channelSocket = channel.open(new ChannelSocketListener());
                                                         gameCreateDeleteButton.setText("Delete");
                                                         showPlayerResources(0.0, resources);
                                                         Window.alert("Game created");
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
               // Delete game.
               if ((transactionState != TRANSACTION_STATE.UNAVAILABLE) &&
                   (transactionState != TRANSACTION_STATE.INACTIVE))
               {
                  Window.alert("Please finish transaction");
                  enableUI();
                  return;
               }
               DelimitedString deleteRequest = new DelimitedString(Shared.DELETE_GAME);
               deleteRequest.add(gameCode);
               gameService.requestService(deleteRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error deleting game: " + caught.getMessage());
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
                                                      Window.alert("Error deleting game");
                                                   }
                                                }
                                                else
                                                {
                                                   if (channelSocket != null)
                                                   {
                                                      channelSocket.close();
                                                   }
                                                   channel = null;
                                                   gameCreateDeleteButton.setText("Create");
                                                   gameState = Shared.PENDING;
                                                   gameStateListBox.setSelectedIndex(gameState);
                                                   playersListBox.clear();
                                                   playersListBox.insertItem(Shared.ALL_PLAYERS, 0);
                                                   clearPlayerResources();
                                                   resetTransaction();
                                                   Window.alert("Game deleted");
                                                }
                                                enableUI();
                                             }
                                          }
                                          );
            }
         }

         // Remove player.
         else if (event.getSource() == playerRemoveButton)
         {
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            if ((transactionState != TRANSACTION_STATE.UNAVAILABLE) &&
                (transactionState != TRANSACTION_STATE.INACTIVE))
            {
               Window.alert("Please finish transaction");
               return;
            }
            disableUI();
            removePlayer = null;
            for (int i = 0; i < playersListBox.getItemCount(); i++)
            {
               try
               {
                  if (playersListBox.isItemSelected(i))
                  {
                     removePlayer = playersListBox.getItemText(i);
                     break;
                  }
               }
               catch (IndexOutOfBoundsException e) {}
            }
            if (removePlayer == null)
            {
               enableUI();
            }
            else
            {
               DelimitedString removeRequest = new DelimitedString(Shared.REMOVE_PLAYER);
               removeRequest.add(gameCode);
               removeRequest.add(removePlayer);
               gameService.requestService(removeRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error removing player " +
                                                             removePlayer + ": " + caught.getMessage());
                                                removePlayer = null;
                                                enableUI();
                                             }

                                             public void onSuccess(String result)
                                             {
                                                if (!Shared.isOK(result))
                                                {
                                                   if (Shared.isError(result))
                                                   {
                                                      Window.alert(result + " ");
                                                   }
                                                   else
                                                   {
                                                      Window.alert("Error removing player " + removePlayer);
                                                   }
                                                }
                                                else
                                                {
                                                   if (removePlayer.equals(Shared.ALL_PLAYERS))
                                                   {
                                                      playersListBox.clear();
                                                      playersListBox.insertItem(Shared.ALL_PLAYERS, 0);
                                                   }
                                                   else
                                                   {
                                                      for (int i = 1; i < playersListBox.getItemCount(); i++)
                                                      {
                                                         try
                                                         {
                                                            if (removePlayer.equals(playersListBox.getItemText(i)))
                                                            {
                                                               playersListBox.removeItem(i);
                                                               break;
                                                            }
                                                         }
                                                         catch (IndexOutOfBoundsException e) {}
                                                      }
                                                   }
                                                   clearPlayerResources();
                                                   playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
                                                }
                                                removePlayer = null;
                                                enableUI();
                                             }
                                          }
                                          );
            }
         }

         // Clear chat.
         else if (event.getSource() == playerChatClearButton)
         {
            playerChatTextArea.setText("Note: prepend <player name>/ to send to specific player\n");
         }

         // Send chat to player.
         else if (event.getSource() == playerChatSendButton)
         {
            String chatText = playerChatTextBox.getText();
            if (Shared.isVoid(chatText))
            {
               return;
            }
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            disableUI();
            DelimitedString chatRequest = new DelimitedString(Shared.PLAYER_CHAT);
            chatRequest.add(gameCode);
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
                                                playerChatTextArea.setText(playerChatTextArea.getText() +
                                                                           "host: " +
                                                                           playerChatTextBox.getText() + "\n");
                                                playerChatTextBox.setText("");
                                             }
                                             enableUI();
                                          }
                                       }
                                       );
         }

         // Send alert to player.
         else if (event.getSource() == playerChatAlertButton)
         {
            String chatText = playerChatTextBox.getText();
            if (Shared.isVoid(chatText))
            {
               return;
            }
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            disableUI();
            DelimitedString chatRequest = new DelimitedString(Shared.PLAYER_ALERT);
            chatRequest.add(gameCode);
            chatRequest.add(chatText);
            gameService.requestService(chatRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error sending alert: " + caught.getMessage());
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
                                                   Window.alert("Error sending alert");
                                                }
                                             }
                                             else
                                             {
                                                playerChatTextArea.setText(playerChatTextArea.getText() +
                                                                           "alert: " +
                                                                           playerChatTextBox.getText() + "\n");
                                                playerChatTextBox.setText("");
                                             }
                                             enableUI();
                                          }
                                       }
                                       );
         }

         // Set transaction participants.
         else if (event.getSource() == transactionParticipantsSetButton)
         {
            String claimant = transactionParticipantsClaimantTextBox.getText();
            if (Shared.isVoid(claimant))
            {
               Window.alert("Please select a claimant");
               return;
            }
            for (int i = 1; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
            {
               if (claimant.equals(transactionParticipantsAuditorListBox.getItemText(i)))
               {
                  Window.alert("Claimant cannot be an auditor");
                  return;
               }
            }
            transactionNumber++;
            transactionState = TRANSACTION_STATE.CLAIM_DISTRIBUTION;
            transactionClaimPanel.setVisible(true);
            enableUI();
         }

         // Set entitlement probability distribution parameters.
         else if (event.getSource() == transactionClaimDistributionParameterSetButton)
         {
            String meanText = transactionClaimDistributionMeanTextBox.getText().trim();
            if (Shared.isVoid(meanText))
            {
               Window.alert("Please enter mean");
               return;
            }
            String sigmaText = transactionClaimDistributionSigmaTextBox.getText().trim();
            if (Shared.isVoid(sigmaText))
            {
               Window.alert("Please enter sigma");
               return;
            }
            double mean;
            try {
               mean = Double.parseDouble(meanText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid mean");
               return;
            }
            if (mean <= 0.0)
            {
               Window.alert("Invalid mean");
               return;
            }
            double sigma;
            try {
               sigma = Double.parseDouble(sigmaText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid sigma");
               return;
            }
            if (sigma <= 0.0)
            {
               Window.alert("Invalid sigma");
               return;
            }
            transactionClaimDistribution.setMean(mean);
            transactionClaimDistribution.setSigma(sigma);
            transactionClaimDistribution.draw();
            transactionState = TRANSACTION_STATE.CLAIM_ENTITLEMENT;
            enableUI();
         }

         // Test a probability distribution value.
         else if (event.getSource() == transactionClaimDistributionTestButton)
         {
            String valueText = transactionClaimDistributionTestValueTextBox.getText().trim();
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
            double probability = transactionClaimDistribution.phi(value);
            transactionClaimDistributionTestProbabilityTextBox.setText(doubleToString(probability));
         }

         // Generate a value from the entitlement probability distribution.
         else if (event.getSource() == transactionClaimEntitlementGenerateButton)
         {
            transactionClaimEntitlementTextBox.setText(doubleToString(transactionClaimDistribution.nextValue()));
         }

         // Set entitlement value.
         else if (event.getSource() == transactionClaimEntitlementSetButton)
         {
            String entitlementText = transactionClaimEntitlementTextBox.getText().trim();
            if (Shared.isVoid(entitlementText))
            {
               Window.alert("Please enter entitlement");
               return;
            }
            double entitlement;
            try {
               entitlement = Double.parseDouble(entitlementText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid entitlement");
               return;
            }
            if (entitlement < 0.0)
            {
               Window.alert("Invalid entitlement");
               return;
            }
            transactionState = TRANSACTION_STATE.CLAIM_WAIT;
            transactionClaimAmountTextBox.setText("waiting");
            disableUI();
            DelimitedString startClaimRequest = new DelimitedString(Shared.START_CLAIM);
            startClaimRequest.add(gameCode);
            startClaimRequest.add(transactionNumber);
            startClaimRequest.add(transactionParticipantsClaimantTextBox.getText().trim());
            startClaimRequest.add(transactionClaimDistributionMeanTextBox.getText().trim());
            startClaimRequest.add(transactionClaimDistributionSigmaTextBox.getText().trim());
            startClaimRequest.add(transactionClaimEntitlementTextBox.getText().trim());
            gameService.requestService(startClaimRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error starting transaction: " + caught.getMessage());
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
                                                   Window.alert("Error starting transaction");
                                                }
                                             }
                                             enableUI();
                                          }
                                       }
                                       );
         }

         // Set penalties.
         else if (event.getSource() == transactionPenaltySetButton)
         {
            String auditorParameterText = transactionPenaltyAuditorParameterTextBox.getText().trim();
            if (Shared.isVoid(auditorParameterText))
            {
               Window.alert("Please enter auditor penalty parameter");
               return;
            }
            double auditorPenaltyParameter;
            try {
               auditorPenaltyParameter = Double.parseDouble(auditorParameterText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid auditor penalty parameter");
               return;
            }
            if (auditorPenaltyParameter < 0.0)
            {
               Window.alert("Invalid auditor penalty parameter");
               return;
            }
            String claimantParameterText = transactionPenaltyClaimantParameterTextBox.getText().trim();
            if (Shared.isVoid(claimantParameterText))
            {
               Window.alert("Please enter claimant penalty parameter");
               return;
            }
            double claimantPenaltyParameter;
            try {
               claimantPenaltyParameter = Double.parseDouble(claimantParameterText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid claimant penalty parameter");
               return;
            }
            if (claimantPenaltyParameter < 0.0)
            {
               Window.alert("Invalid claimant penalty parameter");
               return;
            }
            transactionState = TRANSACTION_STATE.FINISH_WAIT;
            transactionFinishFlexTable.setVisible(true);
            disableUI();
            double claim   = Double.parseDouble(transactionClaimAmountTextBox.getText());
            double grant   = Double.parseDouble(transactionGrantClaimantTextBox.getText());
            double penalty = (claim - grant) * claimantPenaltyParameter;
            transactionPenaltyClaimantTextBox.setText(penalty + "");
            transactionPenaltyAuditorListBox.addItem("<player>");
            transactionFinishPendingParticipantsListBox.addItem("<player>");
            transactionFinishPendingParticipantsListBox.addItem(transactionParticipantsClaimantTextBox.getText());
            transactionFinishedParticipantsListBox.addItem("<player>");
            for (int i = 0; i < auditorNames.size(); i++)
            {
               transactionPenaltyAuditorListBox.addItem(auditorNames.get(i));
               auditorPenalties.add((Math.abs(grant - Double.parseDouble(auditorGrants.get(i))) * auditorPenaltyParameter) + "");
               transactionFinishPendingParticipantsListBox.addItem(auditorNames.get(i));
            }
            transactionPenaltyAuditorListBox.setSelectedIndex(0);
            transactionPenaltyAuditorAmountTextBox.setText("");
            transactionFinishPendingParticipantsListBox.setSelectedIndex(0);
            transactionFinishedParticipantsListBox.setSelectedIndex(0);
            DelimitedString setPenaltyRequest = new DelimitedString(Shared.SET_PENALTY);
            setPenaltyRequest.add(gameCode);
            setPenaltyRequest.add(transactionNumber);
            setPenaltyRequest.add(auditorPenaltyParameter);
            setPenaltyRequest.add(claimantPenaltyParameter);
            gameService.requestService(setPenaltyRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error setting penalties: " + caught.getMessage());
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
                                                   Window.alert("Error setting penalties");
                                                }
                                             }
                                             enableUI();
                                          }
                                       }
                                       );
         }

         // Commit transaction.
         else if (event.getSource() == transactionFinishButton)
         {
            String transactionText = new Date().toString() + ":";
            transactionText += "transaction number=" + transactionNumber;
            transactionText += ";claimant=" + transactionParticipantsClaimantTextBox.getText();
            transactionText += ";auditors=";
            for (int i = 0; i < auditorNames.size(); i++)
            {
               if (i == 0)
               {
                  transactionText += auditorNames.get(i);
               }
               else
               {
                  transactionText += "," + auditorNames.get(i);
               }
            }
            transactionText += ";mean=" + transactionClaimDistributionMeanTextBox.getText();
            transactionText += ";sigma=" + transactionClaimDistributionSigmaTextBox.getText();
            transactionText += ";entitlement=" + transactionClaimEntitlementTextBox.getText();
            transactionText += ";claim=" + transactionClaimAmountTextBox.getText();
            transactionText += ";grant=" + transactionGrantClaimantTextBox.getText() + "(";
            for (int i = 0; i < auditorGrants.size(); i++)
            {
               if (i == 0)
               {
                  transactionText += auditorGrants.get(i);
               }
               else
               {
                  transactionText += "," + auditorGrants.get(i);
               }
            }
            transactionText += ")";
            transactionText += ";penalty parameters:claimant=" + transactionPenaltyClaimantParameterTextBox.getText();
            transactionText += ",auditor=" + transactionPenaltyAuditorParameterTextBox.getText();
            transactionText += ";penalty=" + transactionPenaltyClaimantTextBox.getText() + "(";
            for (int i = 0; i < auditorPenalties.size(); i++)
            {
               if (i == 0)
               {
                  transactionText += auditorPenalties.get(i);
               }
               else
               {
                  transactionText += "," + auditorPenalties.get(i);
               }
            }
            transactionText += ")\n";
            transactionHistoryTextArea.setText(transactionHistoryTextArea.getText() + transactionText);
            transactionState = TRANSACTION_STATE.INACTIVE;
            clearPlayerResources();
            disableUI();
            DelimitedString finishRequest = new DelimitedString(Shared.FINISH_TRANSACTION);
            finishRequest.add(gameCode);
            finishRequest.add(transactionNumber);
            gameService.requestService(finishRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error finishing transaction: " + caught.getMessage());
                                             resetTransaction();
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
                                                   Window.alert("Error finishing transaction");
                                                }
                                             }
                                             resetTransaction();
                                          }
                                       }
                                       );
         }

         // Abort transaction.
         else if (event.getSource() == transactionAbortButton)
         {
            switch (transactionState)
            {
            case UNAVAILABLE:
            case INACTIVE:
               resetTransaction();
               return;

            case CLAIM_DISTRIBUTION:
            case CLAIM_ENTITLEMENT:
               transactionNumber--;
               resetTransaction();
               return;

            default:
               break;
            }
            disableUI();
            DelimitedString abortRequest = new DelimitedString(Shared.ABORT_TRANSACTION);
            abortRequest.add(gameCode);
            abortRequest.add(transactionNumber);
            abortRequest.add(transactionParticipantsClaimantTextBox.getText());
            if (transactionState != TRANSACTION_STATE.CLAIM_WAIT)
            {
               for (int i = 1; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
               {
                  abortRequest.add(transactionParticipantsClaimantTextBox.getText());
               }
            }
            gameService.requestService(abortRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error aborting transaction: " + caught.getMessage());
                                             transactionNumber--;
                                             resetTransaction();
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
                                                   Window.alert("Error aborting transaction");
                                                }
                                             }
                                             transactionNumber--;
                                             resetTransaction();
                                          }
                                       }
                                       );
         }
   }

   // ListBox handler.
   @Override
   public void itemStateChanged(ItemEvent event) 
   {
       if (event.getStateChange() == ItemEvent.SELECTED) 
       {
         if (event.getSource() == gameStateListBox)
         {
            // Update game state.
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            int nextState = gameStateListBox.getSelectedIndex();
            if (nextState == gameState)
            {
               return;
            }
            if (nextState != (gameState + 1))
            {
               gameStateListBox.setSelectedIndex(gameState);
               Window.alert("Invalid state transition");
               return;
            }
            if ((transactionState != TRANSACTION_STATE.UNAVAILABLE) &&
                (transactionState != TRANSACTION_STATE.INACTIVE))
            {
               gameStateListBox.setSelectedIndex(gameState);
               Window.alert("Please finish transaction");
               return;
            }
            disableUI();
            DelimitedString updateRequest = new DelimitedString(Shared.UPDATE_GAME);
            updateRequest.add(gameCode);
            updateRequest.add(gameStateListBox.getSelectedIndex());
            gameService.requestService(updateRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             gameStateListBox.setSelectedIndex(gameState);
                                             Window.alert("Error updating game: " + caught.getMessage());
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
                                                   Window.alert("Error updating game");
                                                }
                                             }
                                             else
                                             {
                                                int nextState = gameStateListBox.getSelectedIndex();
                                                if (nextState != gameState)
                                                {
                                                   gameState = nextState;
                                                   resetTransaction();
                                                }
                                             }
                                             gameStateListBox.setSelectedIndex(gameState);
                                             enableUI();
                                          }
                                       }
                                       );
         }

         // Update selected player resources.
         else if (event.getSource() == playersListBox)
         {
            updatePlayerResources();
         }

         // Select transaction participants.
         else if (event.getSource() == transactionParticipantsClaimantCandidateListBox)
         {
            int i = transactionParticipantsClaimantCandidateListBox.getSelectedIndex();
            if ((i > 0) && (i < transactionParticipantsClaimantCandidateListBox.getItemCount()))
            {
               String name = transactionParticipantsClaimantCandidateListBox.getItemText(i);
               transactionParticipantsClaimantTextBox.setText(name);
            }
         }
         else if (event.getSource() == transactionParticipantsAuditorCandidateListBox)
         {
            int i = transactionParticipantsAuditorCandidateListBox.getSelectedIndex();
            if ((i > 0) && (i < transactionParticipantsAuditorCandidateListBox.getItemCount()))
            {
               String name = transactionParticipantsAuditorCandidateListBox.getItemText(i);
               transactionParticipantsAuditorCandidateListBox.removeItem(i);
               transactionParticipantsAuditorCandidateListBox.setSelectedIndex(0);
               for (i = 1; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
               {
                  if (name.compareTo(transactionParticipantsAuditorListBox.getItemText(i)) < 0)
                  {
                     break;
                  }
               }
               transactionParticipantsAuditorListBox.insertItem(name, i);
               transactionParticipantsAuditorListBox.setSelectedIndex(0);
            }
         }
         else if (event.getSource() == transactionParticipantsAuditorListBox)
         {
            int i = transactionParticipantsAuditorListBox.getSelectedIndex();
            if ((i > 0) && (i < transactionParticipantsAuditorListBox.getItemCount()))
            {
               String name = transactionParticipantsAuditorListBox.getItemText(i);
               transactionParticipantsAuditorListBox.removeItem(i);
               transactionParticipantsAuditorListBox.setSelectedIndex(0);
               for (i = 1; i < transactionParticipantsAuditorCandidateListBox.getItemCount(); i++)
               {
                  if (name.compareTo(transactionParticipantsAuditorCandidateListBox.getItemText(i)) < 0)
                  {
                     break;
                  }
               }
               transactionParticipantsAuditorCandidateListBox.insertItem(name, i);
               transactionParticipantsAuditorCandidateListBox.setSelectedIndex(0);
            }
         }
         else if (event.getSource() == transactionGrantAuditorCompletedListBox)
         {
            int i = transactionGrantAuditorCompletedListBox.getSelectedIndex();
            transactionGrantAuditorAmountTextBox.setText("");
            if ((i > 0) && (i < transactionGrantAuditorCompletedListBox.getItemCount()))
            {
               String name = transactionGrantAuditorCompletedListBox.getItemText(i);
               for (int j = 0; j < auditorNames.size(); j++)
               {
                  if (name.equals(auditorNames.get(j)))
                  {
                     transactionGrantAuditorAmountTextBox.setText(auditorGrants.get(j));
                     break;
                  }
               }
            }
         }
         else if (event.getSource() == transactionPenaltyAuditorListBox)
         {
            int i = transactionPenaltyAuditorListBox.getSelectedIndex();
            transactionPenaltyAuditorAmountTextBox.setText("");
            if ((i > 0) && (i < transactionPenaltyAuditorListBox.getItemCount()))
            {
               String name = transactionPenaltyAuditorListBox.getItemText(i);
               for (int j = 0; j < auditorNames.size(); j++)
               {
                  if (name.equals(auditorNames.get(j)))
                  {
                     transactionPenaltyAuditorAmountTextBox.setText(auditorPenalties.get(j));
                     break;
                  }
               }
            }
         }
      }
   }

   // Update player resources.
   private void updatePlayerResources()
   {
      String playerName = null;

      for (int i = 0; i < playersListBox.getItemCount(); i++)
      {
         try
         {
            if (playersListBox.isItemSelected(i))
            {
               playerName = playersListBox.getItemText(i);
               break;
            }
         }
         catch (IndexOutOfBoundsException e) {}
      }
      if (playerName == null)
      {
         clearPlayerResources();
         return;
      }
      disableUI();
      DelimitedString updateRequest = new DelimitedString(Shared.GET_PLAYER_RESOURCES);
      updateRequest.add(gameCode);
      updateRequest.add(playerName);
      gameService.requestService(updateRequest.toString(),
                                 new AsyncCallback<String>()
                                 {
                                    public void onFailure(Throwable caught)
                                    {
                                       Window.alert("Error getting player resources: " + caught.getMessage());
                                       enableUI();
                                    }

                                    public void onSuccess(String result)
                                    {
                                       if (Shared.isVoid(result))
                                       {
                                          Window.alert("Error getting player resources");
                                       }
                                       else
                                       {
                                          if (Shared.isError(result))
                                          {
                                             Window.alert(result);
                                          }
                                          else
                                          {
                                             String[] args = new DelimitedString(result).parse();
                                             if (args.length != 3)
                                             {
                                                Window.alert("Error getting player resources");
                                             }
                                             else
                                             {
                                                showPlayerResources(args[0], args[1]);
                                             }
                                          }
                                       }
                                       enableUI();
                                    }
                                 }
                                 );
   }


   private void clearPlayerResources()
   {
      playerTotalResourceTextBox.setText("");
      playerPersonalResourceTextBox.setText("");
      playerCommonResourceTextBox.setText("");
   }


   private void showPlayerResources(String personal, String common)
   {
      double total = Double.parseDouble(personal) + Double.parseDouble(common);

      playerTotalResourceTextBox.setText(doubleToString(total));
      playerPersonalResourceTextBox.setText(personal);
      playerCommonResourceTextBox.setText(common);
   }


   private void showPlayerResources(double personal, double common)
   {
      double total = personal + common;

      playerTotalResourceTextBox.setText(doubleToString(total));
      playerPersonalResourceTextBox.setText(doubleToString(personal));
      playerCommonResourceTextBox.setText(doubleToString(common));
   }


   private String doubleToString(double value)
   {
      NumberFormat decimalFormat = NumberFormat.getFormat(".##");

      return(decimalFormat.format(value));
   }


   // Reset transaction.
   private void resetTransaction()
   {
      if (gameState == Shared.RUNNING)
      {
         transactionState = TRANSACTION_STATE.INACTIVE;
         transactionParticipantsPanel.setVisible(true);
         transactionParticipantsClaimantCandidateListBox.clear();
         transactionParticipantsClaimantTextBox.setText("");
         transactionParticipantsAuditorCandidateListBox.clear();
         transactionParticipantsClaimantCandidateListBox.insertItem("<player>", 0);
         transactionParticipantsAuditorCandidateListBox.insertItem("<player>", 0);
         for (int i = 1; i < playersListBox.getItemCount(); i++)
         {
            String name = playersListBox.getItemText(i);
            transactionParticipantsClaimantCandidateListBox.insertItem(name, i);
            transactionParticipantsAuditorCandidateListBox.insertItem(name, i);
         }
         transactionParticipantsClaimantCandidateListBox.setSelectedIndex(0);
         transactionParticipantsAuditorCandidateListBox.setSelectedIndex(0);
         transactionParticipantsAuditorListBox.clear();
         transactionParticipantsAuditorListBox.insertItem("<player>", 0);
      }
      else
      {
         transactionState = TRANSACTION_STATE.UNAVAILABLE;
         transactionParticipantsPanel.setVisible(false);
         transactionParticipantsClaimantCandidateListBox.clear();
         transactionParticipantsClaimantTextBox.setText("");
         transactionParticipantsAuditorCandidateListBox.clear();
         transactionParticipantsAuditorListBox.clear();
      }
      transactionClaimPanel.setVisible(false);
      if (transactionClaimDistribution != null)
      {
         transactionClaimDistributionMeanTextBox.setText(mean + "");
         transactionClaimDistributionSigmaTextBox.setText(sigma + "");
         transactionClaimDistribution.draw();
      }
      else
      {
         transactionClaimDistributionMeanTextBox.setText("");
         transactionClaimDistributionSigmaTextBox.setText("");
      }
      transactionClaimDistributionTestValueTextBox.setText("");
      transactionClaimDistributionTestProbabilityTextBox.setText("");
      transactionClaimEntitlementTextBox.setText("");
      transactionClaimAmountTextBox.setText("");
      transactionGrantFlexTable.setVisible(false);
      transactionGrantAuditorWorkingListBox.clear();
      transactionGrantAuditorCompletedListBox.clear();
      transactionGrantAuditorAmountTextBox.setText("");
      transactionGrantClaimantTextBox.setText("");
      transactionPenaltyPanel.setVisible(false);
      transactionPenaltyClaimantParameterTextBox.setText(claimantPenaltyParameter + "");
      transactionPenaltyAuditorParameterTextBox.setText(auditorPenaltyParameter + "");
      transactionPenaltyAuditorListBox.clear();
      transactionPenaltyAuditorAmountTextBox.setText("");
      transactionPenaltyClaimantTextBox.setText("");
      transactionFinishFlexTable.setVisible(false);
      transactionFinishPendingParticipantsListBox.clear();
      transactionFinishedParticipantsListBox.clear();
      enableUI();
   }


   // Disable UI.
   private void disableUI()
   {
      gameCodeTextBox.setReadOnly(true);
      gameResourcesTextBox.setReadOnly(true);
      gameCreateDeleteButton.setEnabled(false);
      gameStateListBox.setEnabled(false);
      playersListBox.setEnabled(false);
      playerRemoveButton.setEnabled(false);
      playerChatTextBox.setReadOnly(true);
      playerChatSendButton.setEnabled(false);
      playerChatAlertButton.setEnabled(false);
      transactionParticipantsClaimantCandidateListBox.setEnabled(false);
      transactionParticipantsAuditorCandidateListBox.setEnabled(false);
      transactionParticipantsAuditorListBox.setEnabled(false);
      transactionParticipantsSetButton.setEnabled(false);
      transactionClaimDistributionMeanTextBox.setReadOnly(true);
      transactionClaimDistributionSigmaTextBox.setReadOnly(true);
      transactionClaimDistributionParameterSetButton.setEnabled(false);
      transactionClaimEntitlementTextBox.setReadOnly(true);
      transactionClaimEntitlementGenerateButton.setEnabled(false);
      transactionClaimEntitlementSetButton.setEnabled(false);
      transactionGrantAuditorWorkingListBox.setEnabled(false);
      transactionGrantAuditorCompletedListBox.setEnabled(false);
      transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
      transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
      transactionPenaltySetButton.setEnabled(false);
      transactionPenaltyAuditorListBox.setEnabled(false);
      transactionFinishPendingParticipantsListBox.setEnabled(false);
      transactionFinishedParticipantsListBox.setEnabled(false);
      transactionFinishButton.setEnabled(false);
      transactionAbortButton.setEnabled(false);
   }


   // Enable UI.
   private void enableUI()
   {
      gameCreateDeleteButton.setEnabled(true);
      if (channel == null)
      {
         gameCodeTextBox.setReadOnly(false);
         gameResourcesTextBox.setReadOnly(false);
         gameStateListBox.setEnabled(false);
         playersListBox.setEnabled(false);
         playerRemoveButton.setEnabled(false);
         playerChatTextBox.setReadOnly(true);
         playerChatSendButton.setEnabled(false);
         playerChatAlertButton.setEnabled(false);
         transactionParticipantsClaimantCandidateListBox.setEnabled(false);
         transactionParticipantsAuditorCandidateListBox.setEnabled(false);
         transactionParticipantsAuditorListBox.setEnabled(false);
         transactionParticipantsSetButton.setEnabled(false);
         transactionClaimDistributionMeanTextBox.setReadOnly(true);
         transactionClaimDistributionSigmaTextBox.setReadOnly(true);
         transactionClaimDistributionParameterSetButton.setEnabled(false);
         transactionClaimEntitlementTextBox.setReadOnly(true);
         transactionClaimEntitlementGenerateButton.setEnabled(false);
         transactionClaimEntitlementSetButton.setEnabled(false);
         transactionGrantAuditorWorkingListBox.setEnabled(false);
         transactionGrantAuditorCompletedListBox.setEnabled(false);
         transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
         transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
         transactionPenaltySetButton.setEnabled(false);
         transactionPenaltyAuditorListBox.setEnabled(false);
         transactionFinishPendingParticipantsListBox.setEnabled(false);
         transactionFinishedParticipantsListBox.setEnabled(false);
         transactionFinishButton.setEnabled(false);
         transactionAbortButton.setEnabled(false);
      }
      else
      {
         gameStateListBox.setEnabled(true);
         playersListBox.setEnabled(true);
         playerRemoveButton.setEnabled(true);
         playerChatTextBox.setReadOnly(false);
         playerChatSendButton.setEnabled(true);
         playerChatAlertButton.setEnabled(true);
         switch (transactionState)
         {
         case UNAVAILABLE:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case INACTIVE:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(true);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case CLAIM_DISTRIBUTION:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(false);
            transactionClaimDistributionSigmaTextBox.setReadOnly(false);
            transactionClaimDistributionParameterSetButton.setEnabled(true);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case CLAIM_ENTITLEMENT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(false);
            transactionClaimEntitlementGenerateButton.setEnabled(true);
            transactionClaimEntitlementSetButton.setEnabled(true);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case CLAIM_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case GRANT_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case PENALTY_PARAMETERS:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(false);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(false);
            transactionPenaltySetButton.setEnabled(true);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case PENALTY_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(true);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case FINISH_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(true);
            transactionFinishPendingParticipantsListBox.setEnabled(true);
            transactionFinishedParticipantsListBox.setEnabled(true);
            transactionFinishButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case FINISHED:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setReadOnly(true);
            transactionClaimDistributionSigmaTextBox.setReadOnly(true);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setReadOnly(true);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setReadOnly(true);
            transactionPenaltyAuditorParameterTextBox.setReadOnly(true);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(true);
            transactionFinishPendingParticipantsListBox.setEnabled(true);
            transactionFinishedParticipantsListBox.setEnabled(true);
            transactionFinishButton.setEnabled(true);
            transactionAbortButton.setEnabled(true);
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
         if (operation.equals(Shared.JOIN_GAME) && (args.length == 3))
         {
            // Player joining game.
            String playerName = args[2];
            int    i          = 1;
            for ( ; i < playersListBox.getItemCount(); i++)
            {
               if (playerName.compareTo(playersListBox.getItemText(i)) < 0)
               {
                  break;
               }
            }
            playersListBox.insertItem(playerName, i);
            playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
            updatePlayerResources();
         }
         else if (operation.equals(Shared.QUIT_GAME) && (args.length == 3))
         {
            // Player quitting game.
            String playerName = args[2];
            for (int i = 1; i < playersListBox.getItemCount(); i++)
            {
               try
               {
                  if (playersListBox.getItemText(i).equals(playerName))
                  {
                     playersListBox.removeItem(i);
                     break;
                  }
               }
               catch (IndexOutOfBoundsException e) {}
            }
            playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
            updatePlayerResources();
         }
         else if (operation.equals(Shared.HOST_CHAT) && (args.length == 4))
         {
            // Chat from player.
            String playerName = args[2];
            String chatText   = args[3];
            playerChatTextArea.setText(playerChatTextArea.getText() +
                                       playerName + ": " + chatText + "\n");
         }
         else if (operation.equals(Shared.SET_CLAIM) && (args.length == 3))
         {
            // Set claim.
            transactionNumber = Integer.parseInt(args[1]);
            double claim = Double.parseDouble(args[2]);
            transactionClaimAmountTextBox.setText(claim + "");
            transactionState = TRANSACTION_STATE.GRANT_WAIT;
            transactionGrantFlexTable.setVisible(true);
            auditorNames.clear();
            auditorGrants.clear();
            auditorPenalties.clear();
            disableUI();
            DelimitedString auditRequest = new DelimitedString(Shared.START_AUDIT);
            auditRequest.add(gameCode);
            auditRequest.add(transactionNumber);
            if (transactionParticipantsAuditorListBox.getItemCount() == 1)
            {
               transactionGrantClaimantTextBox.setText(claim + "");
               transactionState = TRANSACTION_STATE.PENALTY_PARAMETERS;
               transactionPenaltyPanel.setVisible(true);
            }
            else
            {
               transactionGrantClaimantTextBox.setText("waiting");
               for (int i = 0; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
               {
                  transactionGrantAuditorWorkingListBox.addItem(transactionParticipantsAuditorListBox.getItemText(i));
                  if (i == 0)
                  {
                     transactionGrantAuditorCompletedListBox.addItem(transactionParticipantsAuditorListBox.getItemText(i));
                  }
                  else
                  {
                     auditRequest.add(transactionParticipantsAuditorListBox.getItemText(i));
                  }
               }
               transactionGrantAuditorWorkingListBox.setSelectedIndex(0);
               transactionGrantAuditorCompletedListBox.setSelectedIndex(0);
            }
            gameService.requestService(auditRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error starting transaction: " + caught.getMessage());
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
                                                   Window.alert("Error starting transaction");
                                                }
                                             }
                                             enableUI();
                                          }
                                       }
                                       );
         }
         else if (operation.equals(Shared.SET_GRANT) && (args.length == 4))
         {
            // Set auditor grant.
            transactionNumber = Integer.parseInt(args[1]);
            String grantText   = args[2];
            String auditorName = args[3];
            auditorNames.add(auditorName);
            auditorGrants.add(grantText);
            int i = 1;
            for ( ; i < transactionGrantAuditorCompletedListBox.getItemCount(); i++)
            {
               if (auditorName.compareTo(transactionGrantAuditorCompletedListBox.getItemText(i)) < 0)
               {
                  break;
               }
            }
            transactionGrantAuditorCompletedListBox.insertItem(auditorName, i);
            transactionGrantAuditorCompletedListBox.setSelectedIndex(0);
            for (i = 1; i < transactionGrantAuditorWorkingListBox.getItemCount(); i++)
            {
               if (auditorName.equals(transactionGrantAuditorWorkingListBox.getItemText(i)))
               {
                  transactionGrantAuditorWorkingListBox.removeItem(i);
                  transactionGrantAuditorWorkingListBox.setSelectedIndex(0);
                  break;
               }
            }
            if (transactionGrantAuditorWorkingListBox.getItemCount() == 1)
            {
               // Consensus grant.
               double grant = 0.0;
               for (i = 0; i < auditorGrants.size(); i++)
               {
                  grant += Double.parseDouble(auditorGrants.get(i));
               }
               grant /= (double)(i);
               transactionGrantClaimantTextBox.setText(grant + "");
               transactionState = TRANSACTION_STATE.PENALTY_PARAMETERS;
               transactionPenaltyPanel.setVisible(true);
               disableUI();
               DelimitedString grantRequest = new DelimitedString(Shared.SET_GRANT);
               grantRequest.add(gameCode);
               grantRequest.add(transactionNumber);
               grantRequest.add(grant);
               gameService.requestService(grantRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error setting grant: " + caught.getMessage());
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
                                                      Window.alert("Error setting grant");
                                                   }
                                                }
                                                enableUI();
                                             }
                                          }
                                          );
            }
         }
         else if (operation.equals(Shared.FINISH_TRANSACTION) && (args.length == 3))
         {
            // Participant transaction finish.
            transactionNumber = Integer.parseInt(args[1]);
            String playerName = args[2];
            int    i          = 1;
            for ( ; i < transactionFinishedParticipantsListBox.getItemCount(); i++)
            {
               if (playerName.compareTo(transactionGrantAuditorCompletedListBox.getItemText(i)) < 0)
               {
                  break;
               }
            }
            transactionFinishedParticipantsListBox.insertItem(playerName, i);
            transactionFinishedParticipantsListBox.setSelectedIndex(0);
            for (i = 1; i < transactionFinishPendingParticipantsListBox.getItemCount(); i++)
            {
               if (playerName.equals(transactionFinishPendingParticipantsListBox.getItemText(i)))
               {
                  transactionFinishPendingParticipantsListBox.removeItem(i);
                  transactionFinishPendingParticipantsListBox.setSelectedIndex(0);
                  break;
               }
            }
            if (transactionFinishPendingParticipantsListBox.getItemCount() == 1)
            {
               // Finish transaction.
               transactionState = TRANSACTION_STATE.FINISHED;
               enableUI();
            }
         }
      }
   }
}
