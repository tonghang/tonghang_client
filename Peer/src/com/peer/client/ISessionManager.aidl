// ISessionManager.aidl
package com.peer.client;

// Declare any non-default types here with import statements
import com.peer.client.ISessionListener;

interface ISessionManager {

    void login(String username, String password, ISessionListener callback);    
    
    void register(String email, String username, String password, ISessionListener callback);
   
    String getUserId();
    
    String getSessionId();
 
}
