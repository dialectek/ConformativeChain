// Conformative game client.

package com.citrix.conformative.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class ConformativeGame implements EntryPoint
{
   private ButtonHandler   buttonHandler;
   private RootPanel       rootPanel;
   Label                   conformativeGameLabel;
   private HorizontalPanel buttonPanel;
   private HorizontalPanel hostButtonPanel;
   private HorizontalPanel playerButtonPanel;
   private Button          hostButton;
   private Button          playerButton;

   // Remote service proxy.
   private final GameServiceAsync gameService = GWT.create(GameService.class );

   // Entry point method.
   public void onModuleLoad()
   {
      // Button handler.
      buttonHandler = new ButtonHandler();

      // Root panel.
      rootPanel             = RootPanel.get(null);
      conformativeGameLabel = new Label("Conformative Game Roles");
      conformativeGameLabel.setStyleName("gwt-Label-Title");
      conformativeGameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      conformativeGameLabel.setSize("450px", "31px");
      rootPanel.add(conformativeGameLabel, 0, 0);
      buttonPanel = new HorizontalPanel();
      buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      rootPanel.add(buttonPanel, 0, 37);
      buttonPanel.setSize("450px", "24px");
      playerButtonPanel = new HorizontalPanel();
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
}
