package com.ibm.ecm.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.ecm.api.coresdk.collection.IBMECMItemProperties;
import com.ibm.ecm.api.coresdk.collection.IBMECMResultSet;
import com.ibm.ecm.api.coresdk.exception.IBMECMRuntimeException;
import com.ibm.ecm.api.coresdk.factory.IBMECMFactory;
import com.ibm.ecm.api.coresdk.model.IBMECMApplication;
import com.ibm.ecm.api.coresdk.model.IBMECMCompletionHandler;
import com.ibm.ecm.api.coresdk.model.IBMECMContentItem;
import com.ibm.ecm.api.coresdk.model.IBMECMDesktop;
import com.ibm.ecm.api.coresdk.model.IBMECMItemProperty;
import com.ibm.ecm.api.coresdk.model.IBMECMRepository;
import com.ibm.ecm.api.testutils.ConfigUtils;
import com.ibm.ecm.api.testutils.SynchronousWrappers;

public class Sample {

    protected final static IBMECMFactory factory = IBMECMFactory.getInstance();
    protected final static int SERVICE_TIMEOUT = 10;
    
    // Load all of the connection information we need for our ICN server
    // Edit /src/main/resources/sample.properties to set these values
    // as appropriate for your local test ICN server.
    protected static Map<String, String> configMap = ConfigUtils.loadTestProperties();
    private final static Logger logger = LoggerFactory.getLogger(Sample.class);

    public static void main(String[] args) {
        
        final String userName = configMap.get("sdk.userName");
        final String password = configMap.get("sdk.password");
        
        // synchronous example
        final IBMECMApplication application =  factory.getApplication(configMap.get("sdk.test.icn.url"), true);
        boolean success = SynchronousWrappers.loginSync(application, userName, password);
        if (success) {
            System.out.println("Synchronous (wrapped) login was successful." );
            logger.info("Sample application starting. (output in logger)");
            
            IBMECMDesktop desktopCurrent = SynchronousWrappers.loadDesktopSync(application);
            if (desktopCurrent != null) {
                System.out.println("currentDesktop = " + desktopCurrent.getDefaultRepository().getName());
                
                IBMECMContentItem rootFolder = SynchronousWrappers.getRootFolderObjectSync(desktopCurrent.getDefaultRepository());
                System.out.println("Root folder ID = " + rootFolder.getId());
                dumpItemProperties(rootFolder);
                
                IBMECMResultSet<IBMECMContentItem> folderChildren = SynchronousWrappers.retrieveFolderContentSync(rootFolder, 10);
                List<IBMECMContentItem> childrenList = folderChildren.getItems();
                System.out.println("First page of folder children count = " + childrenList.size());
                
                System.out.println("Child item names:" );
                for (IBMECMContentItem item : childrenList) {
                    System.out.println("Item name:" + item.getName());
                }
            }
        }
        
        
        System.out.println("Asynchronous examples follow.\n\n" );
        loginAndLoadDesktopAsync(userName, password);
        
        // process any optional parameters
        // for ( int i = 0; i < args.length; i++ )
        // System.out.println( args[i] );
        
        System.out.println("Terminating.");
        System.exit(0);
    }
    
    
    public static void loginAndLoadDesktopAsync(String userName, String password) {        
                final CountDownLatch signal = new CountDownLatch(1);
              
                 
                final IBMECMApplication application =  factory.getApplication(configMap.get("sdk.test.icn.url"), true);
                System.out.format("Initiating login for url: %s (as user):%s\n", configMap.get("sdk.test.icn.url"), configMap.get("sdk.userName"));
                
                
                // Callbacks may be defined like this (loadcurrentDesktopHandler) or anonymously like the example below with application.login
                IBMECMCompletionHandler<IBMECMDesktop> loadcurrentDesktopHandler = new IBMECMCompletionHandler<IBMECMDesktop>() {
                    @Override
                    public void onError(IBMECMRuntimeException exception) {
                        signal.countDown();
                        System.out.format("Failure getting desktop. Error: %s\n", exception.getMessage());
                    }

                    @Override
                    public void onCompleted(IBMECMDesktop model) {
                        dumpDesktop(model);
                        signal.countDown();
                    }
                };
                
                // Here we define our callback - anonymous / in line 
                application.login(userName, password, new IBMECMCompletionHandler<Boolean>() {
                    @Override
                    public void onCompleted(Boolean result) {
                        System.out.format("Login completed successfully for username: %s\n", userName);
                        System.out.format("Attempting to load default desktop for username: %s\n", userName);
                        application.loadCurrentDesktop(loadcurrentDesktopHandler);
                    }

                    @Override
                    public void onError(IBMECMRuntimeException exception) {
                        if (exception != null) {
                            System.out.format("Failure logging in. Error: %s\n", exception.getMessage());
                        }
                        signal.countDown();
                    }
                });

                try {
                    signal.await(SERVICE_TIMEOUT, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
    public static void dumpItemProperties(IBMECMContentItem item) {
        IBMECMItemProperties properties = item.getProperties();
        System.out.println("Dumping item properties.");
        if (properties != null && properties.size() > 0) {
            for (IBMECMItemProperty property : properties) {
                System.out.format("IBMECMItemProperty | .id = %s,.value = %s\n", property.getId(), property.getValue());
            }
        }
        System.out.println("Item properties <end>");
    }
    
    public static void dumpDesktop(IBMECMDesktop desktop) {
        System.out.format("IBMECMDesktop | .id = %s,.name = %s, .isConnected = %s\n", desktop.getId(), desktop.getName(), desktop.isConnected());
        System.out.format("IBMECMDesktop | .userId = %s, .mobileAdminEmailAddress = %s\n", desktop.getUserId(), desktop.getMobileAdminEmailAddress());
        System.out.format("IBMECMDesktop | .securityToken = %s, .serverVersion = %s\n", desktop.getSecurityToken(), desktop.getServerVersion());
        System.out.format("IBMECMDesktop | .userId = %s, .userDisplayName = %s\n", desktop.getUserId(), desktop.getUserDisplayName());
        System.out.format("IBMECMDesktop | .isMobileAccessAllowed = %s, .isMobileApplicationEnabled = %s\n", desktop.isMobileAccessAllowed(), desktop.isMobileApplicationEnabled());
        System.out.format("IBMECMDesktop | .isAddDocFoldersToRepositoryFromMobileEnabled = %s, .isAddDocFoldersToRepositoryFromMobileEnabled = %s\n", desktop.isAddDocFoldersToRepositoryFromMobileEnabled(), desktop.isAddPhotoFromMobileEnabled());
        System.out.format("IBMECMDesktop | .isOpenDocumentFromOtherApplicationEnabled = %s, .isSyncServerEnabled = %s\n", desktop.isOpenDocumentFromOtherApplicationEnabled(), desktop.isSyncServerEnabled());
        System.out.format("IBMECMDesktop | .isConnected = %b\n", desktop.isConnected());
        
        IBMECMRepository[] repositories = desktop.getRepositories();
        for (IBMECMRepository repository : repositories) {
            System.out.format("IBMECMRepository | .id = %s, .name = %s\n", repository.getId(), repository.getName());
            System.out.format("IBMECMRepository | .connectionPoint = %s, .hostName = %s\n", repository.getConnectionPoint(), repository.getHostName());
            System.out.format("IBMECMRepository | .objectStoreId = %s, .objectStoreDisplayName = %s\n", repository.getObjectStoreId(), repository.getObjectStoreDisplayName());
            System.out.format("IBMECMRepository | .repositoryType = %s, .serverDomainId = %s\n", repository.getRepositoryType().getValue(), repository.getServerDomainId());
            System.out.format("IBMECMRepository | .isConnected: %b\n", repository.isConnected());
        }         
        IBMECMRepository repository = desktop.getDefaultRepository();  
        System.out.println("default repo: " + repository.getName() );
    }
       
}
