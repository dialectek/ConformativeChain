// Conformative game host client.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.gateway.ContractException;
import com.dialectek.conformative.hyperledger.shared.DelimitedString;
import com.dialectek.conformative.hyperledger.shared.Shared;

public class Host extends JFrame implements ActionListener, ItemListener
{
   private static final long serialVersionUID = 1L;

   private JTabbedPane        roleTabPanel;
   private JPanel             homePanel;
   private JPanel             gameCaptionPanel;
   private JPanel             gameCodePanel;
   private JLabel             gameCodeLabel;
   private JTextField         gameCodeTextBox;
   private JLabel             gameResourcesLabel;
   private JTextField         gameResourcesTextBox;
   private JButton            gameCreateDeleteButton;
   private JPanel             gameStatePanel;
   private JLabel             gameStateLabel;
   private JComboBox<String>  gameStateListBox;
   private JPanel             playersCaptionPanel;
   private JPanel             playersJoinedPanel;
   private JLabel             playersJoinedLabel;
   private JTextField         playersJoinedTextBox;
   private JComboBox<String>  playersListBox;
   private JButton            playerRemoveButton;
   private JPanel             playerResourceCaptionPanel;
   private JLabel             playerTotalResourceLabel;
   private JTextField         playerTotalResourceTextBox;
   private JLabel             playerPersonalResourceLabel;
   private JTextField         playerPersonalResourceTextBox;
   private JLabel             playerCommonResourceLabel;
   private JTextField         playerCommonResourceTextBox;
   private JPanel             playerChatCaptionPanel;
   private JTextArea          playerChatTextArea;
   private JScrollPane        playerChatScrollPane;
   private JPanel             playerChatClearPanel;
   private JButton            playerChatClearButton;
   private JTextField         playerChatTextBox;
   private JPanel             playerChatSendPanel;
   private JButton            playerChatSendButton;
   private JPanel             transactionHistoryCaptionPanel;
   private JTextArea          transactionHistoryTextArea;
   private JScrollPane        transactionHistoryScrollPane;
   private JPanel             transactionPanel;
   private JTabbedPane        transactionTabPanel;
   private JPanel             transactionParticipantsCaptionPanel;
   private JPanel             transactionParticipantsPanel;
   private JPanel             transactionParticipantsClaimantCaptionPanel;
   private JPanel             transactionParticipantsClaimantPanel;
   private JComboBox<String>  transactionParticipantsClaimantCandidateListBox;
   private JLabel             transactionParticipantsClaimantLabel;
   private JTextField         transactionParticipantsClaimantTextBox;
   private JButton            transactionParticipantsClaimantResetButton;
   private JPanel             transactionParticipantsAuditorCaptionPanel;
   private JPanel             transactionParticipantsAuditorPanel;
   private JComboBox<String>  transactionParticipantsAuditorCandidateListBox;
   private JLabel             transactionParticipantsAuditorLabel;
   private JComboBox<String>  transactionParticipantsAuditorListBox;
   private JButton            transactionParticipantsAuditorsResetButton;
   private JPanel             transactionParticipantsSetButtonPanel;
   private JButton            transactionParticipantsSetButton;
   private JPanel             transactionClaimCaptionPanel;
   private JPanel             transactionClaimPanel;
   private JPanel             transactionClaimDistributionCaptionPanel;
   private JPanel             transactionClaimDistributionPanel;
   private Canvas             transactionClaimDistributionCanvas;
   private NormalDistribution transactionClaimDistribution;
   private JLabel             transactionClaimDistributionMeanLabel;
   private JTextField         transactionClaimDistributionMeanTextBox;
   private JLabel             transactionClaimDistributionSigmaLabel;
   private JTextField         transactionClaimDistributionSigmaTextBox;
   private JButton            transactionClaimDistributionParameterTestButton;
   private JButton            transactionClaimDistributionParameterSetButton;
   private JPanel             transactionClaimDistributionTestPanel;
   private JLabel             transactionClaimDistributionTestValueLabel;
   private JTextField         transactionClaimDistributionTestValueTextBox;
   private JLabel             transactionClaimDistributionTestLabel;
   private JButton            transactionClaimDistributionTestButton;
   private JTextField         transactionClaimDistributionTestProbabilityTextBox;
   private JPanel             transactionClaimEntitlementPanel;
   private JLabel             transactionClaimEntitlementLabel;
   private JTextField         transactionClaimEntitlementTextBox;
   private JButton            transactionClaimEntitlementGenerateButton;
   private JButton            transactionClaimEntitlementSetButton;
   private JPanel             transactionClaimAmountPanel;
   private JLabel             transactionClaimAmountLabel;
   private JTextField         transactionClaimAmountTextBox;
   private JPanel             transactionGrantCaptionPanel;
   private JPanel             transactionGrantPanel;
   private JPanel             transactionGrantAuditorPanel;
   private JLabel             transactionGrantAuditorWorkingLabel;
   private JComboBox<String>  transactionGrantAuditorWorkingListBox;
   private JLabel             transactionGrantAuditorCompletedLabel;
   private JComboBox<String>  transactionGrantAuditorCompletedListBox;
   private JLabel             transactionGrantAuditorAmountLabel;
   private JTextField         transactionGrantAuditorAmountTextBox;
   private JPanel             transactionGrantClaimantPanel;
   private JLabel             transactionGrantClaimantLabel;
   private JTextField         transactionGrantClaimantTextBox;
   private JPanel             transactionPenaltyCaptionPanel;
   private JPanel             transactionPenaltyPanel;
   private JPanel             transactionPenaltyParameterCaptionPanel;
   private JPanel             transactionPenaltyClaimantParameterPanel;
   private JLabel             transactionPenaltyClaimantParameterLabel;
   private JTextField         transactionPenaltyClaimantParameterTextBox;
   private JLabel             transactionPenaltyAuditorParameterLabel;
   private JTextField         transactionPenaltyAuditorParameterTextBox;
   private JButton            transactionPenaltySetButton;
   private JPanel             transactionPenaltyAuditorPanel;
   private JLabel             transactionPenaltyAuditorLabel;
   private JComboBox<String>  transactionPenaltyAuditorListBox;
   private JLabel             transactionPenaltyAuditorAmountLabel;
   private JTextField         transactionPenaltyAuditorAmountTextBox;
   private JLabel             transactionPenaltyClaimantLabel;
   private JTextField         transactionPenaltyClaimantTextBox;
   private JPanel             transactionFinishCaptionPanel;
   private JPanel             transactionFinishPanel;
   private JLabel             transactionFinishPendingParticipantsLabel;
   private JComboBox<String>  transactionFinishPendingParticipantsListBox;
   private JLabel             transactionFinishedLabel;
   private JComboBox<String>  transactionFinishedParticipantsListBox;
   private JPanel             transactionCompletionPanel;
   private JButton            transactionCommitButton;
   private JButton            transactionAbortButton;
   private Font               textFont;
   private boolean            UIlocked = true;

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

   // Transaction tabs.
   private static final int PARTICIPANTS_TAB = 0;
   private static final int CLAIM_TAB        = 1;
   private static final int GRANT_TAB        = 2;
   private static final int PENALTY_TAB      = 3;
   private static final int FINISH_TAB       = 4;

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
   private final int syncFreq    = 10;
   private int       syncCounter = 0;

   final static Logger logger = Logger.getLogger(Host.class );

   // Constructor.
   public Host(String gameCode) throws Exception
   {
      this.gameCode = gameCode;
      if (Shared.isVoid(gameCode))
      {
         JOptionPane.showMessageDialog(this, "Invalid game code: " + gameCode);
         throw new Exception("Invalid game code");
      }
      transactionNumber = -1;

      // Title.
      setTitle("Conformative Game Host");

      // Termination.
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter()
                        {
                           @Override
                           public void windowClosing(WindowEvent e)
                           {
                              terminate();
                              System.exit(0);
                           }
                        }
                        );

      // Set fixed-width font for text.
      textFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

      // Role tabs.
      roleTabPanel = new JTabbedPane();

      // Home tab.
      homePanel = new JPanel();
      homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
      roleTabPanel.add(homePanel, "Home");
      gameCaptionPanel = new JPanel();
      gameCaptionPanel.setBorder(BorderFactory.createTitledBorder("Game"));
      gameCaptionPanel.setLayout(new BoxLayout(gameCaptionPanel, BoxLayout.Y_AXIS));
      homePanel.add(gameCaptionPanel);
      gameCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      gameCaptionPanel.add(gameCodePanel);
      gameCodeLabel = newLabel("Code: ");
      gameCodePanel.add(gameCodeLabel);
      gameCodeTextBox = newTextField(30);
      gameCodeTextBox.setText(gameCode);
      gameCodePanel.add(gameCodeTextBox);
      gameResourcesLabel = newLabel("Resources:");
      gameCodePanel.add(gameResourcesLabel);
      gameResourcesTextBox = newTextField(10);
      gameCodePanel.add(gameResourcesTextBox);
      gameCreateDeleteButton = newButton("Create");
      gameCreateDeleteButton.addActionListener(this);
      gameCodePanel.add(gameCreateDeleteButton);
      gameStatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      gameCaptionPanel.add(gameStatePanel);
      gameStateLabel = newLabel("State:");
      gameStatePanel.add(gameStateLabel);
      gameStateListBox = newComboBox();
      gameStateListBox.setOpaque(true);
      gameStateListBox.setEnabled(false);
      gameStateListBox.addItemListener(this);
      gameStateListBox.addItem("<state>");
      gameStateListBox.addItem("Pending");
      gameStateListBox.addItem("Joining");
      gameStateListBox.addItem("Running");
      gameStateListBox.addItem("Completed");
      gameStateListBox.setSelectedIndex(0);
      gameStatePanel.add(gameStateListBox);
      playersCaptionPanel = new JPanel();
      playersCaptionPanel.setBorder(BorderFactory.createTitledBorder("Players"));
      playersCaptionPanel.setLayout(new BoxLayout(playersCaptionPanel, BoxLayout.Y_AXIS));
      homePanel.add(playersCaptionPanel);
      playersJoinedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      playersCaptionPanel.add(playersJoinedPanel);
      playersJoinedLabel = newLabel("Joined:");
      playersJoinedLabel.setSize(50, playersJoinedLabel.getSize().height);
      playersJoinedPanel.add(playersJoinedLabel);
      playersJoinedTextBox = newTextField(10);
      playersJoinedTextBox.setEnabled(false);
      playersJoinedTextBox.setText("0");
      playersJoinedPanel.add(playersJoinedTextBox);
      playersListBox = newComboBox();
      playersListBox.setOpaque(true);
      playersListBox.addItemListener(this);
      playersListBox.addItem(Shared.ALL_PLAYERS);
      playersListBox.setSelectedIndex(0);
      playersJoinedPanel.add(playersListBox);
      playerRemoveButton = newButton("Remove");
      playerRemoveButton.addActionListener(this);
      playersJoinedPanel.add(playerRemoveButton);
      playerResourceCaptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      playerResourceCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resources"));
      playersCaptionPanel.add(playerResourceCaptionPanel);
      playerTotalResourceLabel = newLabel("Total:");
      playerResourceCaptionPanel.add(playerTotalResourceLabel);
      playerTotalResourceLabel.setSize(10, playerTotalResourceLabel.getSize().height);
      playerTotalResourceTextBox = newTextField(10);
      playerTotalResourceTextBox.setEditable(false);
      playerResourceCaptionPanel.add(playerTotalResourceTextBox);
      playerPersonalResourceLabel = newLabel(" = Personal:");
      playerResourceCaptionPanel.add(playerPersonalResourceLabel);
      playerPersonalResourceLabel.setSize(10, playerPersonalResourceLabel.getSize().height);
      playerPersonalResourceTextBox = newTextField(10);
      playerPersonalResourceTextBox.setEditable(false);
      playerResourceCaptionPanel.add(playerPersonalResourceTextBox);
      playerCommonResourceLabel = newLabel(" + Common:");
      playerResourceCaptionPanel.add(playerCommonResourceLabel);
      playerCommonResourceLabel.setSize(10, playerCommonResourceLabel.getSize().height);
      playerCommonResourceTextBox = newTextField(10);
      playerCommonResourceTextBox.setEditable(false);
      playerResourceCaptionPanel.add(playerCommonResourceTextBox);
      playerChatCaptionPanel = new JPanel();
      playerChatCaptionPanel.setBorder(BorderFactory.createTitledBorder("Player chat"));
      playerChatCaptionPanel.setLayout(new BoxLayout(playerChatCaptionPanel, BoxLayout.Y_AXIS));
      homePanel.add(playerChatCaptionPanel);
      playerChatTextArea = newTextArea(5, 40);
      playerChatTextArea.setEditable(false);
      playerChatTextArea.setText("Note: prepend <player name>/ to send to specific player\n");
      playerChatScrollPane = new JScrollPane(playerChatTextArea);
      playerChatCaptionPanel.add(playerChatScrollPane);
      playerChatClearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      playerChatCaptionPanel.add(playerChatClearPanel);
      playerChatClearButton = newButton("Clear");
      playerChatClearButton.addActionListener(this);
      playerChatClearPanel.add(playerChatClearButton);
      playerChatTextBox = newTextField(40);
      playerChatTextBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerChatTextBox.getPreferredSize().height));
      playerChatCaptionPanel.add(playerChatTextBox);
      playerChatSendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      playerChatCaptionPanel.add(playerChatSendPanel);
      playerChatSendButton = newButton("Send");
      playerChatSendButton.addActionListener(this);
      playerChatSendPanel.add(playerChatSendButton);
      transactionHistoryCaptionPanel = new JPanel();
      transactionHistoryCaptionPanel.setLayout(new GridLayout(1, 1));
      transactionHistoryCaptionPanel.setBorder(BorderFactory.createTitledBorder("Transaction history"));
      homePanel.add(transactionHistoryCaptionPanel);
      transactionHistoryTextArea = newTextArea(5, 40);
      transactionHistoryTextArea.setEditable(false);
      transactionHistoryScrollPane = new JScrollPane(transactionHistoryTextArea);
      transactionHistoryCaptionPanel.add(transactionHistoryScrollPane, 0, 0);

      // Transaction tab.
      transactionPanel = new JPanel();
      transactionPanel.setLayout(new BoxLayout(transactionPanel, BoxLayout.Y_AXIS));
      roleTabPanel.add(transactionPanel, "Transaction");
      transactionTabPanel = new JTabbedPane();
      transactionPanel.add(transactionTabPanel);
      transactionParticipantsCaptionPanel = new JPanel();
      transactionParticipantsCaptionPanel.setLayout(new BoxLayout(transactionParticipantsCaptionPanel, BoxLayout.Y_AXIS));
      transactionTabPanel.add(transactionParticipantsCaptionPanel, "Participants");
      transactionParticipantsPanel = new JPanel();
      transactionParticipantsPanel.setLayout(new BoxLayout(transactionParticipantsPanel, BoxLayout.Y_AXIS));
      transactionParticipantsCaptionPanel.add(transactionParticipantsPanel);
      transactionParticipantsClaimantCaptionPanel = new JPanel();
      transactionParticipantsClaimantCaptionPanel.setBorder(BorderFactory.createTitledBorder("Claimant"));
      transactionParticipantsClaimantCaptionPanel.setLayout(new BoxLayout(transactionParticipantsClaimantCaptionPanel, BoxLayout.Y_AXIS));
      transactionParticipantsPanel.add(transactionParticipantsClaimantCaptionPanel);
      transactionParticipantsClaimantPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionParticipantsClaimantCaptionPanel.add(transactionParticipantsClaimantPanel);
      transactionParticipantsClaimantCandidateListBox = newComboBox();
      transactionParticipantsClaimantCandidateListBox.setOpaque(true);
      transactionParticipantsClaimantCandidateListBox.addItem("<player name>");
      transactionParticipantsClaimantCandidateListBox.addItemListener(this);
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantCandidateListBox);
      transactionParticipantsClaimantLabel = newLabel("select->");
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantLabel);
      transactionParticipantsClaimantTextBox = newTextField(30);
      transactionParticipantsClaimantTextBox.setText("<player name>");
      transactionParticipantsClaimantTextBox.setEditable(false);
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantTextBox);
      transactionParticipantsClaimantResetButton = newButton("Reset");
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantResetButton);
      transactionParticipantsClaimantResetButton.addActionListener(this);
      transactionParticipantsAuditorCaptionPanel = new JPanel();
      transactionParticipantsAuditorCaptionPanel.setBorder(BorderFactory.createTitledBorder("Auditors"));
      transactionParticipantsAuditorCaptionPanel.setLayout(new BoxLayout(transactionParticipantsAuditorCaptionPanel, BoxLayout.Y_AXIS));
      transactionParticipantsPanel.add(transactionParticipantsAuditorCaptionPanel);
      transactionParticipantsAuditorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionParticipantsAuditorCaptionPanel.add(transactionParticipantsAuditorPanel);
      transactionParticipantsAuditorCandidateListBox = newComboBox();
      transactionParticipantsAuditorCandidateListBox.setOpaque(true);
      transactionParticipantsAuditorCandidateListBox.addItem("<player name>");
      transactionParticipantsAuditorCandidateListBox.addItemListener(this);
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorCandidateListBox);
      transactionParticipantsAuditorLabel = newLabel("select->");
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorLabel);
      transactionParticipantsAuditorListBox = newComboBox();
      transactionParticipantsAuditorListBox.setOpaque(true);
      transactionParticipantsAuditorListBox.addItem("<player name>");
      transactionParticipantsAuditorListBox.addItemListener(this);
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorListBox);
      transactionParticipantsAuditorsResetButton = newButton("Reset");
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorsResetButton);
      transactionParticipantsAuditorsResetButton.addActionListener(this);
      transactionParticipantsSetButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionParticipantsPanel.add(transactionParticipantsSetButtonPanel);
      transactionParticipantsSetButton = newButton("Set");
      transactionParticipantsSetButtonPanel.add(transactionParticipantsSetButton);
      transactionParticipantsSetButton.addActionListener(this);
      transactionClaimCaptionPanel = new JPanel();
      transactionClaimCaptionPanel.setLayout(new BoxLayout(transactionClaimCaptionPanel, BoxLayout.Y_AXIS));
      transactionTabPanel.add(transactionClaimCaptionPanel, "Claim");
      transactionClaimPanel = new JPanel();
      transactionClaimPanel.setLayout(new BoxLayout(transactionClaimPanel, BoxLayout.Y_AXIS));
      transactionClaimCaptionPanel.add(transactionClaimPanel);
      transactionClaimDistributionCaptionPanel = new JPanel();
      transactionClaimDistributionCaptionPanel.setBorder(BorderFactory.createTitledBorder("Resource entitlement probability"));
      transactionClaimDistributionCaptionPanel.setLayout(new BoxLayout(transactionClaimDistributionCaptionPanel, BoxLayout.Y_AXIS));
      transactionClaimPanel.add(transactionClaimDistributionCaptionPanel);
      transactionClaimDistributionCanvas = new Canvas();
      transactionClaimDistribution       = new NormalDistribution(transactionClaimDistributionCanvas);
      transactionClaimDistribution.draw();
      transactionClaimDistributionCaptionPanel.add(transactionClaimDistributionCanvas);
      transactionClaimDistributionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionClaimDistributionCaptionPanel.add(transactionClaimDistributionPanel);
      transactionClaimDistributionMeanLabel = newLabel("Mean:");
      transactionClaimDistributionPanel.add(transactionClaimDistributionMeanLabel);
      transactionClaimDistributionMeanTextBox = newTextField(10);
      mean = NormalDistribution.DEFAULT_MEAN;
      transactionClaimDistributionMeanTextBox.setText(mean + "");
      transactionClaimDistributionPanel.add(transactionClaimDistributionMeanTextBox);
      transactionClaimDistributionSigmaLabel = newLabel("Sigma:");
      transactionClaimDistributionPanel.add(transactionClaimDistributionSigmaLabel);
      transactionClaimDistributionSigmaTextBox = newTextField(10);
      sigma = NormalDistribution.DEFAULT_SIGMA;
      transactionClaimDistributionSigmaTextBox.setText(sigma + "");
      transactionClaimDistributionPanel.add(transactionClaimDistributionSigmaTextBox);
      transactionClaimDistributionParameterTestButton = newButton("Test");
      transactionClaimDistributionPanel.add(transactionClaimDistributionParameterTestButton);
      transactionClaimDistributionParameterTestButton.addActionListener(this);
      transactionClaimDistributionParameterSetButton = newButton("Set");
      transactionClaimDistributionPanel.add(transactionClaimDistributionParameterSetButton);
      transactionClaimDistributionParameterSetButton.addActionListener(this);
      transactionClaimDistributionTestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionClaimDistributionCaptionPanel.add(transactionClaimDistributionTestPanel);
      transactionClaimDistributionTestValueLabel = newLabel("Test value:");
      transactionClaimDistributionTestPanel.add(transactionClaimDistributionTestValueLabel);
      transactionClaimDistributionTestValueTextBox = newTextField(10);
      transactionClaimDistributionTestPanel.add(transactionClaimDistributionTestValueTextBox);
      transactionClaimDistributionTestButton = newButton("Test");
      transactionClaimDistributionTestPanel.add(transactionClaimDistributionTestButton);
      transactionClaimDistributionTestButton.addActionListener(this);
      transactionClaimDistributionTestLabel = newLabel("Probability density:");
      transactionClaimDistributionTestPanel.add(transactionClaimDistributionTestLabel);
      transactionClaimDistributionTestProbabilityTextBox = newTextField(10);
      transactionClaimDistributionTestProbabilityTextBox.setEditable(false);
      transactionClaimDistributionTestPanel.add(transactionClaimDistributionTestProbabilityTextBox);
      transactionClaimEntitlementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionClaimPanel.add(transactionClaimEntitlementPanel);
      transactionClaimEntitlementLabel = newLabel("Entitlement:");
      transactionClaimEntitlementPanel.add(transactionClaimEntitlementLabel);
      transactionClaimEntitlementTextBox = newTextField(10);
      transactionClaimEntitlementPanel.add(transactionClaimEntitlementTextBox);
      transactionClaimEntitlementGenerateButton = newButton("Generate");
      transactionClaimEntitlementGenerateButton.addActionListener(this);
      transactionClaimEntitlementPanel.add(transactionClaimEntitlementGenerateButton);
      transactionClaimEntitlementSetButton = newButton("Set");
      transactionClaimEntitlementSetButton.addActionListener(this);
      transactionClaimEntitlementPanel.add(transactionClaimEntitlementSetButton);
      transactionClaimAmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionClaimPanel.add(transactionClaimAmountPanel);
      transactionClaimAmountLabel = newLabel("Claim:      ");
      transactionClaimAmountPanel.add(transactionClaimAmountLabel);
      transactionClaimAmountTextBox = newTextField(10);
      transactionClaimAmountTextBox.setEditable(false);
      transactionClaimAmountPanel.add(transactionClaimAmountTextBox);
      transactionGrantCaptionPanel = new JPanel();
      transactionGrantCaptionPanel.setLayout(new BoxLayout(transactionGrantCaptionPanel, BoxLayout.Y_AXIS));
      transactionTabPanel.add(transactionGrantCaptionPanel, "Grant");
      transactionGrantPanel = new JPanel();
      transactionGrantPanel.setLayout(new BoxLayout(transactionGrantPanel, BoxLayout.Y_AXIS));
      transactionGrantCaptionPanel.add(transactionGrantPanel);
      transactionGrantAuditorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionGrantPanel.add(transactionGrantAuditorPanel);
      transactionGrantAuditorWorkingLabel = newLabel("Auditing:");
      transactionGrantAuditorPanel.add(transactionGrantAuditorWorkingLabel);
      transactionGrantAuditorWorkingListBox = newComboBox();
      transactionGrantAuditorWorkingListBox.setOpaque(true);
      transactionGrantAuditorWorkingListBox.addItem("<player name>");
      transactionGrantAuditorPanel.add(transactionGrantAuditorWorkingListBox);
      transactionGrantAuditorCompletedLabel = newLabel("Completed:");
      transactionGrantAuditorPanel.add(transactionGrantAuditorCompletedLabel);
      transactionGrantAuditorCompletedListBox = newComboBox();
      transactionGrantAuditorCompletedListBox.setOpaque(true);
      transactionGrantAuditorCompletedListBox.addItem("<player name>");
      transactionGrantAuditorCompletedListBox.addItemListener(this);
      transactionGrantAuditorPanel.add(transactionGrantAuditorCompletedListBox);
      transactionGrantAuditorAmountLabel = newLabel("amount->");
      transactionGrantAuditorPanel.add(transactionGrantAuditorAmountLabel);
      transactionGrantAuditorAmountTextBox = newTextField(10);
      transactionGrantAuditorAmountTextBox.setEditable(false);
      transactionGrantAuditorPanel.add(transactionGrantAuditorAmountTextBox);
      transactionGrantClaimantPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionGrantPanel.add(transactionGrantClaimantPanel);
      transactionGrantClaimantLabel = newLabel("Claimant:");
      transactionGrantClaimantPanel.add(transactionGrantClaimantLabel);
      transactionGrantClaimantTextBox = newTextField(10);
      transactionGrantClaimantTextBox.setEditable(false);
      transactionGrantClaimantPanel.add(transactionGrantClaimantTextBox);
      for (int i = 0; i < 6; i++) { transactionGrantPanel.add(new JPanel()); }
      transactionPenaltyCaptionPanel = new JPanel();
      transactionPenaltyCaptionPanel.setLayout(new BoxLayout(transactionPenaltyCaptionPanel, BoxLayout.Y_AXIS));
      transactionTabPanel.add(transactionPenaltyCaptionPanel, "Penalty");
      transactionPenaltyPanel = new JPanel();
      transactionPenaltyPanel.setLayout(new BoxLayout(transactionPenaltyPanel, BoxLayout.Y_AXIS));
      transactionPenaltyCaptionPanel.add(transactionPenaltyPanel);
      transactionPenaltyParameterCaptionPanel = new JPanel();
      transactionPenaltyParameterCaptionPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
      transactionPenaltyParameterCaptionPanel.setLayout(new BoxLayout(transactionPenaltyParameterCaptionPanel, BoxLayout.Y_AXIS));
      transactionPenaltyPanel.add(transactionPenaltyParameterCaptionPanel);
      transactionPenaltyClaimantParameterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionPenaltyParameterCaptionPanel.add(transactionPenaltyClaimantParameterPanel);
      transactionPenaltyClaimantParameterLabel = newLabel("Claimant:");
      transactionPenaltyClaimantParameterPanel.add(transactionPenaltyClaimantParameterLabel);
      transactionPenaltyClaimantParameterTextBox = newTextField(10);
      claimantPenaltyParameter = DEFAULT_CLAIMANT_PENALTY_PARAMETER;
      transactionPenaltyClaimantParameterTextBox.setText(claimantPenaltyParameter + "");
      transactionPenaltyClaimantParameterPanel.add(transactionPenaltyClaimantParameterTextBox);
      transactionPenaltyAuditorParameterLabel = newLabel("Auditor:");
      transactionPenaltyClaimantParameterPanel.add(transactionPenaltyAuditorParameterLabel);
      transactionPenaltyAuditorParameterTextBox = newTextField(10);
      auditorPenaltyParameter = DEFAULT_AUDITOR_PENALTY_PARAMETER;
      transactionPenaltyAuditorParameterTextBox.setText(auditorPenaltyParameter + "");
      transactionPenaltyClaimantParameterPanel.add(transactionPenaltyAuditorParameterTextBox);
      transactionPenaltySetButton = newButton("Set");
      transactionPenaltySetButton.addActionListener(this);
      transactionPenaltyClaimantParameterPanel.add(transactionPenaltySetButton);
      transactionPenaltyAuditorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionPenaltyPanel.add(transactionPenaltyAuditorPanel);
      transactionPenaltyAuditorLabel = newLabel("Auditor:");
      transactionPenaltyAuditorPanel.add(transactionPenaltyAuditorLabel);
      transactionPenaltyAuditorListBox = newComboBox();
      transactionPenaltyAuditorListBox.setOpaque(true);
      transactionPenaltyAuditorListBox.addItem("<player name>");
      transactionPenaltyAuditorListBox.addItemListener(this);
      transactionPenaltyAuditorPanel.add(transactionPenaltyAuditorListBox);
      transactionPenaltyAuditorAmountLabel = newLabel("amount->");
      transactionPenaltyAuditorPanel.add(transactionPenaltyAuditorAmountLabel);
      transactionPenaltyAuditorAmountTextBox = newTextField(10);
      transactionPenaltyAuditorAmountTextBox.setEditable(false);
      transactionPenaltyAuditorPanel.add(transactionPenaltyAuditorAmountTextBox);
      transactionPenaltyClaimantLabel = newLabel("Claimant:");
      transactionPenaltyAuditorPanel.add(transactionPenaltyClaimantLabel);
      transactionPenaltyClaimantTextBox = newTextField(10);
      transactionPenaltyClaimantTextBox.setEditable(false);
      transactionPenaltyAuditorPanel.add(transactionPenaltyClaimantTextBox);
      for (int i = 0; i < 5; i++) { transactionPenaltyPanel.add(new JPanel()); }
      transactionFinishCaptionPanel = new JPanel();
      transactionFinishCaptionPanel.setLayout(new BoxLayout(transactionFinishCaptionPanel, BoxLayout.Y_AXIS));
      transactionTabPanel.add(transactionFinishCaptionPanel, "Finish");
      transactionFinishPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionFinishCaptionPanel.add(transactionFinishPanel);
      transactionFinishPendingParticipantsLabel = newLabel("Participant:");
      transactionFinishPanel.add(transactionFinishPendingParticipantsLabel);
      transactionFinishPendingParticipantsListBox = newComboBox();
      transactionFinishPendingParticipantsListBox.setOpaque(true);
      transactionFinishPendingParticipantsListBox.addItem("<player name>");
      transactionFinishPanel.add(transactionFinishPendingParticipantsListBox);
      transactionFinishedLabel = newLabel("finished->");
      transactionFinishPanel.add(transactionFinishedLabel);
      transactionFinishedParticipantsListBox = newComboBox();
      transactionFinishedParticipantsListBox.setOpaque(true);
      transactionFinishedParticipantsListBox.addItem("<player name>");
      transactionFinishPanel.add(transactionFinishedParticipantsListBox);
      transactionCompletionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      transactionPanel.add(transactionCompletionPanel);
      transactionCommitButton = newButton("Commit");
      transactionCompletionPanel.add(transactionCommitButton);
      transactionCommitButton.addActionListener(this);
      transactionAbortButton = newButton("Abort");
      transactionCompletionPanel.add(transactionAbortButton);
      transactionAbortButton.addActionListener(this);
      roleTabPanel.setEnabledAt(1, false);
      roleTabPanel.setSelectedIndex(0);
      add(roleTabPanel);
      for (int i = 0; i <= FINISH_TAB; i++) { transactionTabPanel.setEnabledAt(i, false); }

      // Initialize.
      gameState        = 0;
      resources        = 0.0;
      auditorNames     = new ArrayList<String>();
      auditorGrants    = new ArrayList<String>();
      auditorPenalties = new ArrayList<String>();
      resetTransaction();

      // Synchronize game with network.
      syncGame();

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
            animateWaitTextBox(transactionClaimAmountTextBox);
            animateWaitTextBox(transactionGrantClaimantTextBox);
            transactionClaimDistribution.draw();
         }
      };
      timer = new Timer("Timer");
      timer.schedule(task, 0, timerInterval_ms);

      // Show.
      pack();
      setVisible(true);
      log("ready");
   }


   // Make label with font.
   private JLabel newLabel(String text)
   {
      JLabel label = new JLabel(text);

      label.setFont(textFont);
      return(label);
   }


   // Make text field with font.
   private JTextField newTextField(int size)
   {
      JTextField textField = new JTextField(size);

      textField.setFont(textFont);
      return(textField);
   }


   // Make text area with font.
   private JTextArea newTextArea(int rows, int cols)
   {
      JTextArea area = new JTextArea(rows, cols);

      area.setFont(textFont);
      return(area);
   }


   // Make button with font.
   private JButton newButton(String text)
   {
      JButton button = new JButton(text);

      button.setFont(textFont);
      return(button);
   }


   // Make combo box with font.
   private JComboBox<String> newComboBox()
   {
      JComboBox<String> box = new JComboBox<String>();
      box.setFont(textFont);
      return(box);
   }


   // Animate wait text box.
   private void animateWaitTextBox(JTextField textBox)
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
      if (UIlocked) { return; }

      // Create/delete game.
      if (event.getSource() == gameCreateDeleteButton)
      {
         gameCode = gameCodeTextBox.getText();
         if ((Shared.isVoid(gameCode)))
         {
            JOptionPane.showMessageDialog(this, "Please enter game code");
            gameCode = "";
            return;
         }
         if (gameCode.contains(DelimitedString.DELIMITER))
         {
            JOptionPane.showMessageDialog(this, "Invalid code character: " + DelimitedString.DELIMITER);
            gameCode = "";
            return;
         }
         gameCodeTextBox.setText(gameCode);
         String r = gameResourcesTextBox.getText().trim();
         if (Shared.isVoid(r))
         {
            JOptionPane.showMessageDialog(this, "Please enter resources");
            return;
         }
         try {
            resources = Double.parseDouble(r.trim());
         }
         catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Resources must be a non-negative number");
            resources = 0.0;
            return;
         }
         if (resources < 0.0)
         {
            JOptionPane.showMessageDialog(this, "Resources must be a non-negative number");
            resources = 0.0;
            return;
         }
         disableUI();
         if (gameState == 0)
         {
            // Create game.
            try
            {
               DelimitedString request = new DelimitedString(Shared.CREATE_GAME);
               request.add(gameCode);
               request.add(resources);
               byte[] response = sendMessage("requestService", request.toString());
               if ((response != null) && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
               {
                  gameCreateDeleteButton.setText("Delete");
                  gameState = Shared.PENDING;
                  gameStateListBox.removeItemListener(this);
                  gameStateListBox.setSelectedIndex(Shared.PENDING);
                  gameStateListBox.addItemListener(this);
                  playersJoinedTextBox.setText("0");
                  playersListBox.removeItemListener(this);
                  playersListBox.removeAllItems();
                  playersListBox.insertItemAt(Shared.ALL_PLAYERS, 0);
                  playersListBox.setSelectedIndex(0);
                  playersListBox.addItemListener(this);
                  showPlayerResources(0.0, resources);
                  resetTransaction();
                  JOptionPane.showMessageDialog(this, "Game created");
               }
               else
               {
                  if (response != null)
                  {
                     JOptionPane.showMessageDialog(this, "Error creating game: " + new String(response, StandardCharsets.UTF_8));
                  }
                  else
                  {
                     JOptionPane.showMessageDialog(this, "Error creating game");
                  }
               }
            }
            catch (Exception e)
            {
               JOptionPane.showMessageDialog(this, "Error creating game: " + e.getMessage());
            }
            enableUI();
         }
         else
         {
            // Delete game.
            if ((transactionState != TRANSACTION_STATE.UNAVAILABLE) &&
                (transactionState != TRANSACTION_STATE.INACTIVE))
            {
               JOptionPane.showMessageDialog(this, "Please finish transaction");
               enableUI();
               return;
            }
            try
            {
               DelimitedString request = new DelimitedString(Shared.DELETE_GAME);
               request.add(gameCode);
               byte[] response = sendMessage("requestService", request.toString());
               if ((response != null) && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
               {
                  gameCreateDeleteButton.setText("Create");
                  gameState = 0;
                  gameStateListBox.removeItemListener(this);
                  gameStateListBox.setSelectedIndex(0);
                  gameStateListBox.addItemListener(this);
                  playersJoinedTextBox.setText("0");
                  playersListBox.removeItemListener(this);
                  playersListBox.removeAllItems();
                  playersListBox.insertItemAt(Shared.ALL_PLAYERS, 0);
                  playersListBox.setSelectedIndex(0);
                  playersListBox.addItemListener(this);
                  clearPlayerResources();
                  resetTransaction();
                  roleTabPanel.setEnabledAt(1, false);
                  roleTabPanel.setSelectedIndex(0);
                  JOptionPane.showMessageDialog(this, "Game deleted");
               }
               else
               {
                  if (response != null)
                  {
                     JOptionPane.showMessageDialog(this, "Error deleting game: " + new String(response, StandardCharsets.UTF_8));
                  }
                  else
                  {
                     JOptionPane.showMessageDialog(this, "Error deleting game");
                  }
               }
            }
            catch (Exception e)
            {
               JOptionPane.showMessageDialog(this, "Error deleting game: " + e.getMessage());
            }
            enableUI();
         }
      }

      // Remove player.
      else if (event.getSource() == playerRemoveButton)
      {
         if (gameState == 0)
         {
            JOptionPane.showMessageDialog(this, "Please create game!");
            return;
         }
         else if (gameState != Shared.JOINING)
         {
            JOptionPane.showMessageDialog(this, "Cannot remove player in this game state");
            return;
         }
         if ((transactionState != TRANSACTION_STATE.UNAVAILABLE) &&
             (transactionState != TRANSACTION_STATE.INACTIVE))
         {
            JOptionPane.showMessageDialog(this, "Please finish transaction");
            return;
         }
         disableUI();
         removePlayer = null;
         int i = playersListBox.getSelectedIndex();
         if (i != -1)
         {
            removePlayer = playersListBox.getItemAt(i);
         }
         if (removePlayer != null)
         {
            try
            {
               DelimitedString request = new DelimitedString(Shared.REMOVE_PLAYER);
               request.add(gameCode);
               request.add(removePlayer);
               byte[] response = sendMessage("requestService", request.toString());
               if ((response != null) && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
               {
                  if (removePlayer.equals(Shared.ALL_PLAYERS))
                  {
                     playersListBox.removeItemListener(this);
                     playersListBox.removeAllItems();
                     playersListBox.insertItemAt(Shared.ALL_PLAYERS, 0);
                     playersListBox.setSelectedIndex(0);
                     playersListBox.addItemListener(this);
                     playersJoinedTextBox.setText("0");
                     clearPlayerResources();
                  }
                  else
                  {
                     for (int j = 1; j < playersListBox.getItemCount(); j++)
                     {
                        try
                        {
                           if (removePlayer.equals(playersListBox.getItemAt(j)))
                           {
                              playersListBox.removeItemListener(this);
                              playersListBox.removeItemAt(j);
                              playersListBox.setSelectedIndex(0);
                              playersListBox.addItemListener(this);
                              playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
                              clearPlayerResources();
                              break;
                           }
                        }
                        catch (IndexOutOfBoundsException e) {}
                     }
                  }
               }
               else
               {
                  if (response != null)
                  {
                     JOptionPane.showMessageDialog(this, "Error removing player: " + new String(response, StandardCharsets.UTF_8));
                  }
                  else
                  {
                     JOptionPane.showMessageDialog(this, "Error removing player");
                  }
               }
            }
            catch (Exception e)
            {
               JOptionPane.showMessageDialog(this, "Error removing player: " + e.getMessage());
            }
            removePlayer = null;
         }
         enableUI();
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
         if (chatText.contains(DelimitedString.DELIMITER))
         {
            JOptionPane.showMessageDialog(this, "Text cannot contain: " + DelimitedString.DELIMITER);
            return;
         }
         if (gameState == 0)
         {
            JOptionPane.showMessageDialog(this, "Please create game!");
            return;
         }
         String playerName = null;
         if (chatText.contains("/"))
         {
            int n = chatText.indexOf("/");
            playerName = chatText.substring(0, n);
            int i, j;
            for (i = 1, j = playersListBox.getItemCount(); i < j; i++)
            {
               if (playerName.equals(playersListBox.getItemAt(i)))
               {
                  chatText = chatText.substring(n + 1);
                  break;
               }
            }
            if (i >= j)
            {
               playerName = null;
            }
         }
         disableUI();
         try
         {
            DelimitedString request = new DelimitedString(Shared.HOST_CHAT_MESSAGE);
            request.add(gameCode);
            if (playerName != null)
            {
               request.add(playerName);
            }
            request.add(chatText);
            byte[] response = sendMessage("requestService", request.toString());
            if ((response != null) && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
            {
               playerChatTextArea.setText(playerChatTextArea.getText() +
                                          "host: " +
                                          playerChatTextBox.getText() + "\n");
               playerChatTextBox.setText("");
            }
            else
            {
               if (response != null)
               {
                  JOptionPane.showMessageDialog(this, "Error sending chat: " + new String(response, StandardCharsets.UTF_8));
               }
               else
               {
                  JOptionPane.showMessageDialog(this, "Error sending chat");
               }
            }
         }
         catch (Exception e)
         {
            JOptionPane.showMessageDialog(this, "Error sending chat: " + e.getMessage());
         }
         enableUI();
      }

      // Reset transaction claimant candidates.
      else if (event.getSource() == transactionParticipantsClaimantResetButton)
      {
         transactionParticipantsClaimantCandidateListBox.removeItemListener(this);
         transactionParticipantsClaimantCandidateListBox.removeAllItems();
         transactionParticipantsClaimantTextBox.setText("");
         transactionParticipantsClaimantCandidateListBox.insertItemAt("<player>", 0);
         for (int i = 1; i < playersListBox.getItemCount(); i++)
         {
            String name = playersListBox.getItemAt(i);
            transactionParticipantsClaimantCandidateListBox.insertItemAt(name, i);
         }
         transactionParticipantsClaimantCandidateListBox.setSelectedIndex(0);
         transactionParticipantsClaimantCandidateListBox.addItemListener(this);
      }

      // Reset transaction auditor candidates.
      else if (event.getSource() == transactionParticipantsAuditorsResetButton)
      {
         transactionParticipantsAuditorCandidateListBox.removeItemListener(this);
         transactionParticipantsAuditorCandidateListBox.removeAllItems();
         transactionParticipantsAuditorCandidateListBox.insertItemAt("<player>", 0);
         for (int i = 1; i < playersListBox.getItemCount(); i++)
         {
            String name = playersListBox.getItemAt(i);
            transactionParticipantsAuditorCandidateListBox.insertItemAt(name, i);
         }
         transactionParticipantsAuditorCandidateListBox.setSelectedIndex(0);
         transactionParticipantsAuditorCandidateListBox.addItemListener(this);
         transactionParticipantsAuditorListBox.removeItemListener(this);
         transactionParticipantsAuditorListBox.removeAllItems();
         transactionParticipantsAuditorListBox.insertItemAt("<player>", 0);
         transactionParticipantsAuditorListBox.setSelectedIndex(0);
         transactionParticipantsAuditorListBox.addItemListener(this);
      }

      // Set transaction participants.
      else if (event.getSource() == transactionParticipantsSetButton)
      {
         if (gameState != Shared.RUNNING)
         {
            JOptionPane.showMessageDialog(this, "Invalid game state");
            return;
         }
         String claimant = transactionParticipantsClaimantTextBox.getText();
         if (Shared.isVoid(claimant))
         {
            JOptionPane.showMessageDialog(this, "Please select a claimant");
            return;
         }
         for (int i = 1; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
         {
            if (claimant.equals(transactionParticipantsAuditorListBox.getItemAt(i)))
            {
               JOptionPane.showMessageDialog(this, "Claimant cannot be an auditor");
               return;
            }
         }
         transactionState = TRANSACTION_STATE.CLAIM_DISTRIBUTION;
         transactionTabPanel.setEnabledAt(CLAIM_TAB, true);
         transactionTabPanel.setSelectedIndex(CLAIM_TAB);
         enableUI();
      }

      // Test/set entitlement probability distribution parameters.
      else if ((event.getSource() == transactionClaimDistributionParameterTestButton) ||
               (event.getSource() == transactionClaimDistributionParameterSetButton))
      {
         String meanText = transactionClaimDistributionMeanTextBox.getText().trim();
         if (Shared.isVoid(meanText))
         {
            JOptionPane.showMessageDialog(this, "Please enter mean");
            return;
         }
         String sigmaText = transactionClaimDistributionSigmaTextBox.getText().trim();
         if (Shared.isVoid(sigmaText))
         {
            JOptionPane.showMessageDialog(this, "Please enter sigma");
            return;
         }
         double mean;
         try {
            mean = Double.parseDouble(meanText);
         }
         catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid mean");
            return;
         }
         if (mean <= 0.0)
         {
            JOptionPane.showMessageDialog(this, "Invalid mean");
            return;
         }
         double sigma;
         try {
            sigma = Double.parseDouble(sigmaText);
         }
         catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid sigma");
            return;
         }
         if (sigma <= 0.0)
         {
            JOptionPane.showMessageDialog(this, "Invalid sigma");
            return;
         }
         this.mean  = mean;
         this.sigma = sigma;
         transactionClaimDistribution.setMean(mean);
         transactionClaimDistribution.setSigma(sigma);
         transactionClaimDistribution.draw();
         if (event.getSource() == transactionClaimDistributionParameterSetButton)
         {
            transactionState = TRANSACTION_STATE.CLAIM_ENTITLEMENT;
            enableUI();
         }
      }

      // Test a probability distribution value.
      else if (event.getSource() == transactionClaimDistributionTestButton)
      {
         String valueText = transactionClaimDistributionTestValueTextBox.getText().trim();
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
            JOptionPane.showMessageDialog(this, "Please enter entitlement");
            return;
         }
         double entitlement;
         try {
            entitlement = Double.parseDouble(entitlementText);
         }
         catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid entitlement");
            return;
         }
         if (entitlement < 0.0)
         {
            JOptionPane.showMessageDialog(this, "Invalid entitlement");
            return;
         }
         transactionState = TRANSACTION_STATE.CLAIM_WAIT;
         transactionClaimAmountTextBox.setText("waiting");
         disableUI();
         try
         {
            transactionNumber = -1;
            DelimitedString request = new DelimitedString(Shared.START_CLAIM);
            request.add(gameCode);
            request.add(transactionParticipantsClaimantTextBox.getText().trim());
            request.add(transactionClaimDistributionMeanTextBox.getText().trim());
            request.add(transactionClaimDistributionSigmaTextBox.getText().trim());
            request.add(transactionClaimEntitlementTextBox.getText().trim());
            byte[] response = sendMessage("requestService", request.toString());
            if ((response != null) && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
            {
               try
               {
                  String[] parts    = new String(response, StandardCharsets.UTF_8).split(DelimitedString.DELIMITER, -1);
                  transactionNumber = Integer.parseInt(parts[1]);
               }
               catch (Exception e)
               {
                  transactionNumber = -1;
                  JOptionPane.showMessageDialog(this, "Error starting transaction");
               }
            }
            else
            {
               if (response != null)
               {
                  JOptionPane.showMessageDialog(this, "Error starting transaction: " + new String(response, StandardCharsets.UTF_8));
               }
               else
               {
                  JOptionPane.showMessageDialog(this, "Error starting transaction");
               }
            }
         }
         catch (Exception e)
         {
            JOptionPane.showMessageDialog(this, "Error starting transaction: " + e.getMessage());
         }
         enableUI();
      }

      // Set penalties.
      else if (event.getSource() == transactionPenaltySetButton)
      {
         String auditorParameterText = transactionPenaltyAuditorParameterTextBox.getText().trim();
         if (Shared.isVoid(auditorParameterText))
         {
            JOptionPane.showMessageDialog(this, "Please enter auditor penalty parameter");
            return;
         }
         double auditorPenaltyParameter;
         try {
            auditorPenaltyParameter = Double.parseDouble(auditorParameterText);
         }
         catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid auditor penalty parameter");
            return;
         }
         if (auditorPenaltyParameter < 0.0)
         {
            JOptionPane.showMessageDialog(this, "Invalid auditor penalty parameter");
            return;
         }
         String claimantParameterText = transactionPenaltyClaimantParameterTextBox.getText().trim();
         if (Shared.isVoid(claimantParameterText))
         {
            JOptionPane.showMessageDialog(this, "Please enter claimant penalty parameter");
            return;
         }
         double claimantPenaltyParameter;
         try {
            claimantPenaltyParameter = Double.parseDouble(claimantParameterText);
         }
         catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid claimant penalty parameter");
            return;
         }
         if (claimantPenaltyParameter < 0.0)
         {
            JOptionPane.showMessageDialog(this, "Invalid claimant penalty parameter");
            return;
         }
         transactionState = TRANSACTION_STATE.FINISH_WAIT;
         transactionTabPanel.setEnabledAt(FINISH_TAB, true);
         transactionTabPanel.setSelectedIndex(FINISH_TAB);
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
         try
         {
            DelimitedString request = new DelimitedString(Shared.SET_PENALTY);
            request.add(gameCode);
            request.add(transactionNumber);
            request.add(auditorPenaltyParameter);
            request.add(claimantPenaltyParameter);
            byte[] response = sendMessage("requestService", request.toString());
            if ((response == null) || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
            {
               if (response != null)
               {
                  JOptionPane.showMessageDialog(this, "Error setting penalties: " + new String(response, StandardCharsets.UTF_8));
               }
               else
               {
                  JOptionPane.showMessageDialog(this, "Error setting penalties");
               }
            }
         }
         catch (Exception e)
         {
            JOptionPane.showMessageDialog(this, "Error setting penalties: " + e.getMessage());
         }
         enableUI();
      }

      // Commit transaction.
      else if (event.getSource() == transactionCommitButton)
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
         try
         {
            DelimitedString request = new DelimitedString(Shared.FINISH_TRANSACTION);
            request.add(gameCode);
            request.add(transactionNumber);
            byte[] response = sendMessage("requestService", request.toString());
            if ((response == null) || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
            {
               if (response != null)
               {
                  JOptionPane.showMessageDialog(this, "Error finishing transaction: " + new String(response, StandardCharsets.UTF_8));
               }
               else
               {
                  JOptionPane.showMessageDialog(this, "Error finishing transaction");
               }
            }
            else
            {
               roleTabPanel.setSelectedIndex(0);
               playersListBox.removeItemListener(this);
               playersListBox.setSelectedIndex(0);
               playersListBox.addItemListener(this);
               updatePlayerResources();
            }
         }
         catch (Exception e)
         {
            JOptionPane.showMessageDialog(this, "Error finishing transaction: " + e.getMessage());
         }
         resetTransaction();
      }

      // Abort transaction.
      else if (event.getSource() == transactionAbortButton)
      {
         switch (transactionState)
         {
         case UNAVAILABLE:
         case INACTIVE:
         case CLAIM_DISTRIBUTION:
         case CLAIM_ENTITLEMENT:
            resetTransaction();
            transactionTabPanel.setSelectedIndex(PARTICIPANTS_TAB);
            return;

         default:
            break;
         }
         disableUI();
         try
         {
            DelimitedString request = new DelimitedString(Shared.ABORT_TRANSACTION);
            request.add(gameCode);
            request.add(transactionNumber);
            request.add(transactionParticipantsClaimantTextBox.getText());
            if (transactionState != TRANSACTION_STATE.CLAIM_WAIT)
            {
               for (int i = 1; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
               {
                  request.add(transactionParticipantsAuditorListBox.getItemAt(i));
               }
            }
            byte[] response = sendMessage("requestService", request.toString());
            if ((response == null) || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
            {
               if (response != null)
               {
                  JOptionPane.showMessageDialog(this, "Error aborting transaction: " + new String(response, StandardCharsets.UTF_8));
               }
               else
               {
                  JOptionPane.showMessageDialog(this, "Error aborting transaction");
               }
            }
         }
         catch (Exception e)
         {
            JOptionPane.showMessageDialog(this, "Error aborting transaction: " + e.getMessage());
         }
         resetTransaction();
         transactionTabPanel.setSelectedIndex(PARTICIPANTS_TAB);
         enableUI();
      }
   }


   // ListBox handler.
   @Override
   public void itemStateChanged(ItemEvent event)
   {
      if (UIlocked) { return; }

      if (event.getStateChange() == ItemEvent.SELECTED)
      {
         if (event.getSource() == gameStateListBox)
         {
            // Update game state.
            int nextState = gameStateListBox.getSelectedIndex();
            if (nextState == gameState)
            {
               return;
            }
            if (nextState != (gameState + 1))
            {
               gameStateListBox.removeItemListener(this);
               gameStateListBox.setSelectedIndex(gameState);
               gameStateListBox.addItemListener(this);
               JOptionPane.showMessageDialog(this, "Invalid state transition");
               return;
            }
            if ((transactionState != TRANSACTION_STATE.UNAVAILABLE) &&
                (transactionState != TRANSACTION_STATE.INACTIVE))
            {
               gameStateListBox.removeItemListener(this);
               gameStateListBox.setSelectedIndex(gameState);
               gameStateListBox.addItemListener(this);
               JOptionPane.showMessageDialog(this, "Please finish transaction");
               return;
            }
            disableUI();
            try
            {
               DelimitedString request = new DelimitedString(Shared.UPDATE_GAME);
               request.add(gameCode);
               request.add(nextState);
               byte[] response = sendMessage("requestService", request.toString());
               if ((response != null) && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
               {
                  gameState = nextState;
                  resetTransaction();
                  if (gameState == Shared.RUNNING)
                  {
                     roleTabPanel.setEnabledAt(1, true);
                  }
                  else
                  {
                     roleTabPanel.setEnabledAt(1, false);
                  }
               }
               else
               {
                  if (response != null)
                  {
                     JOptionPane.showMessageDialog(this, "Error updating transaction: " + new String(response, StandardCharsets.UTF_8));
                  }
                  else
                  {
                     JOptionPane.showMessageDialog(this, "Error updating transaction");
                  }
               }
            }
            catch (Exception e)
            {
               JOptionPane.showMessageDialog(this, "Error updating transaction: " + e.getMessage());
            }
            gameStateListBox.removeItemListener(this);
            gameStateListBox.setSelectedIndex(gameState);
            gameStateListBox.addItemListener(this);
            enableUI();
         }

         // Update selected player resources.
         else if (event.getSource() == playersListBox)
         {
            updatePlayerResources();
         }

         // Select transaction participants.
         else if (event.getSource() == transactionParticipantsClaimantCandidateListBox)
         {
            transactionParticipantsClaimantCandidateListBox.removeItemListener(this);
            int i = transactionParticipantsClaimantCandidateListBox.getSelectedIndex();
            if ((i > 0) && (i < transactionParticipantsClaimantCandidateListBox.getItemCount()))
            {
               String name = transactionParticipantsClaimantCandidateListBox.getItemAt(i);
               transactionParticipantsClaimantTextBox.setText(name);
            }
            else if (i == 0)
            {
               transactionParticipantsClaimantTextBox.setText("");
            }
            transactionParticipantsClaimantCandidateListBox.setSelectedIndex(0);
            transactionParticipantsClaimantCandidateListBox.addItemListener(this);
         }
         else if (event.getSource() == transactionParticipantsAuditorCandidateListBox)
         {
            transactionParticipantsAuditorCandidateListBox.removeItemListener(this);
            transactionParticipantsAuditorListBox.removeItemListener(this);
            int i = transactionParticipantsAuditorCandidateListBox.getSelectedIndex();
            if ((i > 0) && (i < transactionParticipantsAuditorCandidateListBox.getItemCount()))
            {
               String name = transactionParticipantsAuditorCandidateListBox.getItemAt(i);
               transactionParticipantsAuditorCandidateListBox.removeItemAt(i);
               for (i = 1; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
               {
                  if (name.compareTo(transactionParticipantsAuditorListBox.getItemAt(i)) < 0)
                  {
                     break;
                  }
               }
               transactionParticipantsAuditorListBox.insertItemAt(name, i);
            }
            transactionParticipantsAuditorCandidateListBox.setSelectedIndex(0);
            transactionParticipantsAuditorListBox.setSelectedIndex(0);
            transactionParticipantsAuditorCandidateListBox.addItemListener(this);
            transactionParticipantsAuditorListBox.addItemListener(this);
         }
         else if (event.getSource() == transactionParticipantsAuditorListBox)
         {
            transactionParticipantsAuditorListBox.removeItemListener(this);
            transactionParticipantsAuditorListBox.setSelectedIndex(0);
            transactionParticipantsAuditorListBox.addItemListener(this);
         }
         else if (event.getSource() == transactionGrantAuditorCompletedListBox)
         {
            int i = transactionGrantAuditorCompletedListBox.getSelectedIndex();
            transactionGrantAuditorAmountTextBox.setText("");
            if ((i > 0) && (i < transactionGrantAuditorCompletedListBox.getItemCount()))
            {
               String name = transactionGrantAuditorCompletedListBox.getItemAt(i);
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
               String name = transactionPenaltyAuditorListBox.getItemAt(i);
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

      playerName = (String)playersListBox.getSelectedItem();
      if (playerName == null)
      {
         clearPlayerResources();
         return;
      }
      disableUI();
      try
      {
         DelimitedString request = new DelimitedString(Shared.GET_PLAYER_RESOURCES);
         request.add(gameCode);
         request.add(playerName);
         byte[] response = sendMessage("requestService", request.toString());
         if ((response != null) && Shared.isOK(new String(response, StandardCharsets.UTF_8)))
         {
            String[] args = new DelimitedString(new String(response, StandardCharsets.UTF_8)).parse();
            if (args.length != 4)
            {
               JOptionPane.showMessageDialog(this, "Error getting player resources");
            }
            else
            {
               showPlayerResources(args[1], args[2]);
            }
         }
         else
         {
            if (response != null)
            {
               JOptionPane.showMessageDialog(this, "Error getting player resources: " + new String(response, StandardCharsets.UTF_8));
            }
            else
            {
               JOptionPane.showMessageDialog(this, "Error getting player resources");
            }
         }
      }
      catch (Exception e)
      {
         JOptionPane.showMessageDialog(this, "Error getting player resources: " + e.getMessage());
      }
      enableUI();
   }


   private void clearPlayerResources()
   {
      playerTotalResourceTextBox.setText("");
      playerPersonalResourceTextBox.setText("");
      playerCommonResourceTextBox.setText("");
   }


   private void showPlayerResources(String personal, String common)
   {
      showPlayerResources(Double.parseDouble(personal), Double.parseDouble(common));
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
      DecimalFormat decimalFormat = new DecimalFormat("#.##");

      return(decimalFormat.format(value));
   }


   // Reset transaction.
   private void resetTransaction()
   {
      UIlocked          = true;
      transactionNumber = -1;
      if (gameState == Shared.RUNNING)
      {
         transactionState = TRANSACTION_STATE.INACTIVE;
         transactionTabPanel.setEnabledAt(PARTICIPANTS_TAB, true);
         transactionParticipantsClaimantCandidateListBox.removeAllItems();
         transactionParticipantsClaimantTextBox.setText("");
         transactionParticipantsAuditorCandidateListBox.removeAllItems();
         transactionParticipantsClaimantCandidateListBox.insertItemAt("<player>", 0);
         transactionParticipantsAuditorCandidateListBox.insertItemAt("<player>", 0);
         for (int i = 1; i < playersListBox.getItemCount(); i++)
         {
            String name = playersListBox.getItemAt(i);
            transactionParticipantsClaimantCandidateListBox.insertItemAt(name, i);
            transactionParticipantsAuditorCandidateListBox.insertItemAt(name, i);
         }
         transactionParticipantsClaimantCandidateListBox.setSelectedIndex(0);
         transactionParticipantsAuditorCandidateListBox.setSelectedIndex(0);
         transactionParticipantsAuditorListBox.removeAllItems();
         transactionParticipantsAuditorListBox.insertItemAt("<player>", 0);
         transactionParticipantsAuditorListBox.setSelectedIndex(0);
      }
      else
      {
         transactionState = TRANSACTION_STATE.UNAVAILABLE;
         transactionTabPanel.setEnabledAt(PARTICIPANTS_TAB, false);
         transactionParticipantsClaimantCandidateListBox.removeAllItems();
         transactionParticipantsClaimantTextBox.setText("");
         transactionParticipantsAuditorCandidateListBox.removeAllItems();
         transactionParticipantsAuditorListBox.removeAllItems();
      }
      transactionTabPanel.setSelectedIndex(PARTICIPANTS_TAB);
      transactionTabPanel.setEnabledAt(CLAIM_TAB, false);
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
      transactionTabPanel.setEnabledAt(GRANT_TAB, false);
      transactionGrantAuditorWorkingListBox.removeAllItems();
      transactionGrantAuditorCompletedListBox.removeAllItems();
      transactionGrantAuditorAmountTextBox.setText("");
      transactionGrantClaimantTextBox.setText("");
      transactionTabPanel.setEnabledAt(PENALTY_TAB, false);
      transactionPenaltyClaimantParameterTextBox.setText(claimantPenaltyParameter + "");
      transactionPenaltyAuditorParameterTextBox.setText(auditorPenaltyParameter + "");
      transactionPenaltyAuditorListBox.removeAllItems();
      transactionPenaltyAuditorAmountTextBox.setText("");
      transactionPenaltyClaimantTextBox.setText("");
      transactionTabPanel.setEnabledAt(FINISH_TAB, false);
      transactionFinishPendingParticipantsListBox.removeAllItems();
      transactionFinishedParticipantsListBox.removeAllItems();
      UIlocked = false;
      enableUI();
   }


   // Disable UI.
   private void disableUI()
   {
      if (UIlocked) { return; }
      gameCodeTextBox.setEditable(false);
      gameResourcesTextBox.setEditable(false);
      gameCreateDeleteButton.setEnabled(false);
      gameStateListBox.setEnabled(false);
      playersListBox.setEnabled(false);
      playerRemoveButton.setEnabled(false);
      playerChatTextBox.setEditable(false);
      playerChatSendButton.setEnabled(false);
      transactionParticipantsClaimantCandidateListBox.setEnabled(false);
      transactionParticipantsAuditorCandidateListBox.setEnabled(false);
      transactionParticipantsAuditorListBox.setEnabled(false);
      transactionParticipantsClaimantResetButton.setEnabled(false);
      transactionParticipantsAuditorsResetButton.setEnabled(false);
      transactionParticipantsSetButton.setEnabled(false);
      transactionClaimDistributionMeanTextBox.setEditable(false);
      transactionClaimDistributionSigmaTextBox.setEditable(false);
      transactionClaimDistributionParameterTestButton.setEnabled(false);
      transactionClaimDistributionParameterSetButton.setEnabled(false);
      transactionClaimEntitlementTextBox.setEditable(false);
      transactionClaimEntitlementGenerateButton.setEnabled(false);
      transactionClaimEntitlementSetButton.setEnabled(false);
      transactionGrantAuditorWorkingListBox.setEnabled(false);
      transactionGrantAuditorCompletedListBox.setEnabled(false);
      transactionPenaltyClaimantParameterTextBox.setEditable(false);
      transactionPenaltyAuditorParameterTextBox.setEditable(false);
      transactionPenaltySetButton.setEnabled(false);
      transactionPenaltyAuditorListBox.setEnabled(false);
      transactionFinishPendingParticipantsListBox.setEnabled(false);
      transactionFinishedParticipantsListBox.setEnabled(false);
      transactionCommitButton.setEnabled(false);
      transactionAbortButton.setEnabled(false);
   }


   // Enable UI.
   private void enableUI()
   {
      if (UIlocked) { return; }
      gameCreateDeleteButton.setEnabled(true);
      gameStateListBox.removeItemListener(this);
      gameStateListBox.setSelectedIndex(gameState);
      gameStateListBox.addItemListener(this);
      if (gameState == 0)
      {
         gameCodeTextBox.setEditable(false);
         gameResourcesTextBox.setEditable(true);
         gameCreateDeleteButton.setText("Create");
         gameStateListBox.setEnabled(false);
         playersListBox.setEnabled(false);
         playerRemoveButton.setEnabled(false);
         playerChatTextBox.setEditable(false);
         playerChatSendButton.setEnabled(false);
         transactionParticipantsClaimantCandidateListBox.setEnabled(false);
         transactionParticipantsAuditorCandidateListBox.setEnabled(false);
         transactionParticipantsAuditorListBox.setEnabled(false);
         transactionParticipantsClaimantResetButton.setEnabled(false);
         transactionParticipantsAuditorsResetButton.setEnabled(false);
         transactionParticipantsSetButton.setEnabled(false);
         transactionClaimDistributionMeanTextBox.setEditable(false);
         transactionClaimDistributionSigmaTextBox.setEditable(false);
         transactionClaimDistributionParameterTestButton.setEnabled(false);
         transactionClaimDistributionParameterSetButton.setEnabled(false);
         transactionClaimEntitlementTextBox.setEditable(false);
         transactionClaimEntitlementGenerateButton.setEnabled(false);
         transactionClaimEntitlementSetButton.setEnabled(false);
         transactionGrantAuditorWorkingListBox.setEnabled(false);
         transactionGrantAuditorCompletedListBox.setEnabled(false);
         transactionPenaltyClaimantParameterTextBox.setEditable(false);
         transactionPenaltyAuditorParameterTextBox.setEditable(false);
         transactionPenaltySetButton.setEnabled(false);
         transactionPenaltyAuditorListBox.setEnabled(false);
         transactionFinishPendingParticipantsListBox.setEnabled(false);
         transactionFinishedParticipantsListBox.setEnabled(false);
         transactionCommitButton.setEnabled(false);
         transactionAbortButton.setEnabled(false);
      }
      else
      {
         gameCodeTextBox.setEditable(false);
         gameResourcesTextBox.setEditable(false);
         gameCreateDeleteButton.setText("Delete");
         gameStateListBox.setEnabled(true);
         playersListBox.setEnabled(true);
         playerRemoveButton.setEnabled(true);
         playerChatTextBox.setEditable(true);
         playerChatSendButton.setEnabled(true);
         switch (transactionState)
         {
         case UNAVAILABLE:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(false);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(false);
            break;

         case INACTIVE:
            transactionParticipantsClaimantCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorCandidateListBox.setEnabled(true);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(true);
            transactionParticipantsAuditorsResetButton.setEnabled(true);
            transactionParticipantsSetButton.setEnabled(true);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(false);
            break;

         case CLAIM_DISTRIBUTION:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(true);
            transactionClaimDistributionSigmaTextBox.setEditable(true);
            transactionClaimDistributionParameterTestButton.setEnabled(true);
            transactionClaimDistributionParameterSetButton.setEnabled(true);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case CLAIM_ENTITLEMENT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(true);
            transactionClaimEntitlementGenerateButton.setEnabled(true);
            transactionClaimEntitlementSetButton.setEnabled(true);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case CLAIM_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(false);
            transactionGrantAuditorCompletedListBox.setEnabled(false);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case GRANT_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case PENALTY_PARAMETERS:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setEditable(true);
            transactionPenaltyAuditorParameterTextBox.setEditable(true);
            transactionPenaltySetButton.setEnabled(true);
            transactionPenaltyAuditorListBox.setEnabled(false);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case PENALTY_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(true);
            transactionFinishPendingParticipantsListBox.setEnabled(false);
            transactionFinishedParticipantsListBox.setEnabled(false);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case FINISH_WAIT:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(true);
            transactionFinishPendingParticipantsListBox.setEnabled(true);
            transactionFinishedParticipantsListBox.setEnabled(true);
            transactionCommitButton.setEnabled(false);
            transactionAbortButton.setEnabled(true);
            break;

         case FINISHED:
            transactionParticipantsClaimantCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorCandidateListBox.setEnabled(false);
            transactionParticipantsAuditorListBox.setEnabled(true);
            transactionParticipantsClaimantResetButton.setEnabled(false);
            transactionParticipantsAuditorsResetButton.setEnabled(false);
            transactionParticipantsSetButton.setEnabled(false);
            transactionClaimDistributionMeanTextBox.setEditable(false);
            transactionClaimDistributionSigmaTextBox.setEditable(false);
            transactionClaimDistributionParameterTestButton.setEnabled(false);
            transactionClaimDistributionParameterSetButton.setEnabled(false);
            transactionClaimEntitlementTextBox.setEditable(false);
            transactionClaimEntitlementGenerateButton.setEnabled(false);
            transactionClaimEntitlementSetButton.setEnabled(false);
            transactionGrantAuditorWorkingListBox.setEnabled(true);
            transactionGrantAuditorCompletedListBox.setEnabled(true);
            transactionPenaltyClaimantParameterTextBox.setEditable(false);
            transactionPenaltyAuditorParameterTextBox.setEditable(false);
            transactionPenaltySetButton.setEnabled(false);
            transactionPenaltyAuditorListBox.setEnabled(true);
            transactionFinishPendingParticipantsListBox.setEnabled(true);
            transactionFinishedParticipantsListBox.setEnabled(true);
            transactionCommitButton.setEnabled(true);
            transactionAbortButton.setEnabled(true);
            break;
         }
      }
   }


   // Synchronize game with network.
   private void syncGame()
   {
      if (Shared.isVoid(gameCode)) { return; }
      disableUI();
      try
      {
         DelimitedString request = new DelimitedString(Shared.SYNC_GAME);
         request.add(gameCode);
         byte[] response = sendMessage("requestService", request.toString());
         if ((response == null) || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
         {
            if (response == null)
            {
               JOptionPane.showMessageDialog(this, "Cannot sync game");
            }
            else
            {
               JOptionPane.showMessageDialog(this, "Cannot sync game: " + new String(response, StandardCharsets.UTF_8));
            }
         }
         else
         {
            String[] args = new DelimitedString(new String(response, StandardCharsets.UTF_8)).parse();
            try
            {
               gameState = Integer.parseInt(args[1]);
            }
            catch (Exception e)
            {
               JOptionPane.showMessageDialog(this, "Error syncing game: " + e.getMessage());
               gameState = 0;
            }
            gameStateListBox.removeItemListener(this);
            gameStateListBox.setSelectedIndex(gameState);
            gameStateListBox.addItemListener(this);
            if (gameState != 0)
            {
               int i = 2;
               gameResourcesTextBox.setText(args[i]);
               i++;
               showPlayerResources(args[i], args[i + 1]);
               i += 3;
               playersJoinedTextBox.setText(args[i]);
               int n = Integer.parseInt(args[i]);
               i++;
               playersListBox.removeItemListener(this);
               playersListBox.removeAllItems();
               playersListBox.addItem(Shared.ALL_PLAYERS);
               for (int j = 0; j < n; j++)
               {
                  playersListBox.addItem(args[i + j]);
               }
               playersListBox.setSelectedIndex(0);
               playersListBox.addItemListener(this);
               resetTransaction();
               if (gameState == Shared.RUNNING)
               {
                  roleTabPanel.setEnabledAt(1, true);
               }
               else
               {
                  roleTabPanel.setEnabledAt(1, false);
                  roleTabPanel.setSelectedIndex(0);
               }
            }
            else
            {
               roleTabPanel.setEnabledAt(1, false);
               roleTabPanel.setSelectedIndex(0);
            }
         }
      }
      catch (Exception e)
      {
         JOptionPane.showMessageDialog(this, "Cannot sync game: " + e.getMessage());
      }
      enableUI();
   }


   // Synchronize host with messages.
   private void syncMessages()
   {
      if (Shared.isVoid(gameCode)) { return; }
      try
      {
         DelimitedString request = new DelimitedString(Shared.HOST_SYNC_MESSAGES);
         request.add(gameCode);
         byte[] response = NetworkClient.contract.submitTransaction("requestService", request.toString());
         if (response != null)
         {
            String messages = new String(response, StandardCharsets.UTF_8);
            if (Shared.isOK(messages))
            {
               String[] args = new DelimitedString(messages).parse();
               if (args.length >= 2)
               {
                  int     g      = Integer.parseInt(args[1]);
                  boolean enable = false;
                  if (g != gameState)
                  {
                     enable    = true;
                     gameState = g;
                     if (gameState == Shared.RUNNING)
                     {
                        resetTransaction();
                     }
                  }
                  if ((gameState > 0) && (args.length > 2))
                  {
                     log("receives: " + messages);
                     disableUI();
                     update(messages);
                     enableUI();
                  }
                  else
                  {
                     if (enable) { enableUI(); }
                  }
               }
               else
               {
                  JOptionPane.showMessageDialog(this, "Invalid sync messages: " + new String(response, StandardCharsets.UTF_8));
                  gameState = 0;
                  enableUI();
               }
            }
            else
            {
               gameState = 0;
               enableUI();
            }
         }
      }
      catch (Exception e) {}
   }


   // Update host with messages.
   public void update(String messages)
   {
      if (Shared.isVoid(messages.toString()))
      {
         return;
      }
      if (Shared.isError(messages))
      {
         gameState = 0;
         return;
      }
      String[] fields = messages.split(DelimitedString.DELIMITER, -1);
      if ((fields == null) || (fields.length == 1))
      {
         return;
      }
      for (int n = 2; n < fields.length; )
      {
         int c = 0;
         for (int i = n; i < fields.length; i++)
         {
            if (fields[i].equals(Shared.MESSAGE_DELIMITER)) { break; }
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
         if (operation.equals(Shared.JOIN_GAME) && (args.length == 2))
         {
            // Player joining game.
            String  playerName = args[1];
            boolean found      = false;
            int     i          = 1;
            for ( ; i < playersListBox.getItemCount(); i++)
            {
               if (playerName.equals(playersListBox.getItemAt(i)))
               {
                  found = true;
                  break;
               }
               if (playerName.compareTo(playersListBox.getItemAt(i)) < 0)
               {
                  break;
               }
            }
            if (!found)
            {
               playersListBox.removeItemListener(this);
               playersListBox.insertItemAt(playerName, i);
               playersListBox.setSelectedIndex(0);
               playersListBox.addItemListener(this);
               playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
               updatePlayerResources();
            }
         }
         else if (operation.equals(Shared.QUIT_GAME) && (args.length == 2))
         {
            // Player quitting game.
            String playerName = args[1];
            for (int i = 1; i < playersListBox.getItemCount(); i++)
            {
               if ((playersListBox.getItemAt(i)).equals(playerName))
               {
                  playersListBox.removeItemListener(this);
                  playersListBox.removeItemAt(i);
                  playersListBox.setSelectedIndex(0);
                  playersListBox.addItemListener(this);
                  playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
                  updatePlayerResources();
                  break;
               }
            }
         }
         else if (operation.equals(Shared.CHAT_MESSAGE) && (args.length == 3))
         {
            // Chat from player.
            String playerName = args[1];
            String chatText   = args[2];
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
            transactionTabPanel.setEnabledAt(GRANT_TAB, true);
            transactionTabPanel.setSelectedIndex(GRANT_TAB);
            auditorNames.clear();
            auditorGrants.clear();
            auditorPenalties.clear();
            DelimitedString request = new DelimitedString(Shared.START_AUDIT);
            request.add(gameCode);
            request.add(transactionNumber);
            if (transactionParticipantsAuditorListBox.getItemCount() == 1)
            {
               transactionGrantClaimantTextBox.setText(claim + "");
               transactionState = TRANSACTION_STATE.PENALTY_PARAMETERS;
               transactionTabPanel.setEnabledAt(PENALTY_TAB, true);
               transactionTabPanel.setSelectedIndex(PENALTY_TAB);
            }
            else
            {
               transactionGrantClaimantTextBox.setText("waiting");
               for (int i = 0; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
               {
                  transactionGrantAuditorWorkingListBox.addItem(transactionParticipantsAuditorListBox.getItemAt(i));
                  if (i == 0)
                  {
                     transactionGrantAuditorCompletedListBox.addItem(transactionParticipantsAuditorListBox.getItemAt(i));
                  }
                  else
                  {
                     request.add(transactionParticipantsAuditorListBox.getItemAt(i));
                  }
               }
               transactionGrantAuditorWorkingListBox.setSelectedIndex(0);
               transactionGrantAuditorCompletedListBox.setSelectedIndex(0);
            }
            try
            {
               byte[] response = sendMessage("requestService", request.toString());
               if ((response == null) || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
               {
                  if (response != null)
                  {
                     JOptionPane.showMessageDialog(this, "Error starting transaction: " + new String(response, StandardCharsets.UTF_8));
                  }
                  else
                  {
                     JOptionPane.showMessageDialog(this, "Error starting transaction");
                  }
               }
            }
            catch (Exception e)
            {
               JOptionPane.showMessageDialog(this, "Error starting transaction: " + e.getMessage());
            }
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
               if (auditorName.compareTo(transactionGrantAuditorCompletedListBox.getItemAt(i)) < 0)
               {
                  break;
               }
            }
            transactionGrantAuditorCompletedListBox.insertItemAt(auditorName, i);
            transactionGrantAuditorCompletedListBox.setSelectedIndex(0);
            for (i = 1; i < transactionGrantAuditorWorkingListBox.getItemCount(); i++)
            {
               if (auditorName.equals(transactionGrantAuditorWorkingListBox.getItemAt(i)))
               {
                  transactionGrantAuditorWorkingListBox.removeItemAt(i);
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
               transactionTabPanel.setEnabledAt(PENALTY_TAB, true);
               transactionTabPanel.setSelectedIndex(PENALTY_TAB);
               DelimitedString request = new DelimitedString(Shared.SET_GRANT);
               request.add(gameCode);
               request.add(transactionNumber);
               request.add(grant);
               try
               {
                  byte[] response = sendMessage("requestService", request.toString());
                  if ((response == null) || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
                  {
                     if (response != null)
                     {
                        JOptionPane.showMessageDialog(this, "Error setting grant: " + new String(response, StandardCharsets.UTF_8));
                     }
                     else
                     {
                        JOptionPane.showMessageDialog(this, "Error setting grant");
                     }
                  }
               }
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(this, "Error setting grant: " + e.getMessage());
               }
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
               if (playerName.compareTo(transactionGrantAuditorCompletedListBox.getItemAt(i)) < 0)
               {
                  break;
               }
            }
            transactionFinishedParticipantsListBox.insertItemAt(playerName, i);
            transactionFinishedParticipantsListBox.setSelectedIndex(0);
            for (i = 1; i < transactionFinishPendingParticipantsListBox.getItemCount(); i++)
            {
               if (playerName.equals(transactionFinishPendingParticipantsListBox.getItemAt(i)))
               {
                  transactionFinishPendingParticipantsListBox.removeItemAt(i);
                  transactionFinishPendingParticipantsListBox.setSelectedIndex(0);
                  break;
               }
            }
            if (transactionFinishPendingParticipantsListBox.getItemCount() == 1)
            {
               // Finish transaction.
               transactionState = TRANSACTION_STATE.FINISHED;
            }
         }
      }
   }


   // Terminate: abort transaction.
   public void terminate()
   {
      if ((transactionState == TRANSACTION_STATE.UNAVAILABLE) ||
          (transactionState == TRANSACTION_STATE.INACTIVE)) { return; }

      disableUI();
      try
      {
         DelimitedString request = new DelimitedString(Shared.ABORT_TRANSACTION);
         request.add(gameCode);
         request.add(transactionNumber);
         request.add(transactionParticipantsClaimantTextBox.getText());
         for (int i = 1; i < transactionParticipantsAuditorListBox.getItemCount(); i++)
         {
            request.add(transactionParticipantsAuditorListBox.getItemAt(i));
         }
         byte[] response = sendMessage("requestService", request.toString());
         if ((response == null) || !Shared.isOK(new String(response, StandardCharsets.UTF_8)))
         {
            if (response != null)
            {
               JOptionPane.showMessageDialog(this, "Error aborting transaction: " + new String(response, StandardCharsets.UTF_8));
            }
            else
            {
               JOptionPane.showMessageDialog(this, "Error aborting transaction");
            }
         }
      }
      catch (Exception e)
      {
         JOptionPane.showMessageDialog(this, "Error aborting transaction: " + e.getMessage());
      }
      resetTransaction();
      transactionTabPanel.setSelectedIndex(PARTICIPANTS_TAB);
      enableUI();
   }


   // Send message to blockchain.
   public byte[] sendMessage(String destination, String payload) throws ContractException, TimeoutException, InterruptedException
   {
      log("sends: " + payload);
      return(NetworkClient.contract.submitTransaction(destination, payload));
   }


   // Logging.
   public void log(String message)
   {
      logger.info("host " + message);
   }


   // Usage.
   public static final String Usage =
      "Usage:\n" +
      "    java com.dialectek.conformative.hyperledger.client.Host\n" +
      "      [-gameCode <code>]\n" +
      "      [-blockchainAddress <network address>]";

   // Main.
   public static void main(String[] args)
   {
      // Get options.
      String gameCode          = null;
      String blockchainAddress = null;

      for (int i = 0; i < args.length; i++)
      {
         if (args[i].equals("-gameCode"))
         {
            i++;
            if (i >= args.length)
            {
               System.err.println("Invalid gameCode option");
               System.err.println(Usage);
               System.exit(1);
            }
            gameCode = args[i];
            if (Shared.isVoid(gameCode) || gameCode.contains(DelimitedString.DELIMITER))
            {
               JOptionPane.showMessageDialog(null, "Invalid gameCode option");
               System.err.println(Usage);
               System.exit(1);
            }
            continue;
         }
         if (args[i].equals("-blockchainAddress"))
         {
            i++;
            if (i >= args.length)
            {
               System.err.println("Invalid blockchainAddress option");
               System.err.println(Usage);
               System.exit(1);
            }
            blockchainAddress = args[i];
            if (Shared.isVoid(blockchainAddress))
            {
               JOptionPane.showMessageDialog(null, "Invalid blockchainAddress option");
               System.err.println(Usage);
               System.exit(1);
            }
            continue;
         }
         if (args[i].equals("-help") || args[i].equals("-?"))
         {
            System.out.println(Usage);
            System.exit(0);
         }
         System.err.println("Invalid option: " + args[i]);
         System.err.println(Usage);
         System.exit(1);
      }

      if (gameCode == null)
      {
         // Get host information.
         JTextField gameCodeText          = new JTextField();
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
               JOptionPane.showMessageDialog(null, "Invalid game code");
               return;
            }
            blockchainAddress = blockchainAddressText.getText();
            if (Shared.isVoid(blockchainAddress))
            {
               JOptionPane.showMessageDialog(null, "Invalid blockchain address");
               return;
            }
         }
         else
         {
            System.exit(0);
         }
      }

      // Connect to network.
      try
      {
         if (blockchainAddress == null)
         {
            NetworkClient.init();
         }
         else
         {
            NetworkClient.init(blockchainAddress);
         }
      }
      catch (Exception e)
      {
         JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
      }

      // Run host.
      try
      {
         new Host(gameCode);
      }
      catch (Exception e) {}
   }
}
