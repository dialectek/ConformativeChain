package com.citrix.conformative.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

// The client proxy for the RPC service.
@RemoteServiceRelativePath("request")
public interface GameService extends RemoteService
{
   public String requestService(String name) throws IllegalArgumentException;
}
