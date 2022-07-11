package com.citrix.conformative.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

// The asynchronous client proxy for the RPC service.
public interface GameServiceAsync
{
   public void requestService(String input, AsyncCallback<String> callback)
   throws IllegalArgumentException;
}
