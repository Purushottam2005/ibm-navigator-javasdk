
#Command line samples for IBM Content Navigator Java SDK

## Description

The command line samples are part of the larger suite of SDK samples which also include samples 
for mobile development.  The command line sample shows the most simple use case of the SDK in a 
desktop environment.  The sample shows how to authenticate, retrieve the desktop, get the repository object, 
get the root folder of the repository and then get a list of the children of the root.  All of the examples 
demonstrate using the native SDK asynchronous calls and wrapping them to make them behave as blocking 
synchronous calls.   See the com.ibm.ecm.api.Sample.java file for more details.  


## Setting up your build
After obtaining the SDK jar file from your distribution, Instructions [here](https://ibm-ecm.github.io/ibm-navigator-javasdk/site/download/index.html) 
you will need to add the jar to your Maven repository
so that any pom.xml based project can list it as a dependency. 

**This is very important ** to do on any development machine where you are 
going to be using mvn (or gradle) to manage your project. 

### Adding the the SDK jar to the local mvn repo  ***  (only need to do once per develop machine)
Substitute your path to the jar for './lib/IBMECMCore.jar' below

    mvn install:install-file -Dfile=./lib/IBMECMCore.jar -DgroupId=com.ibm.ecm -DartifactId=IBMECMCore -Dversion=1.0.0 -Dpackaging=jar

### Sample.properties file setttings
The following settings must be filled in (before you compile) in your `src/main/resources/sample.properties` file for the sample to operate:

`sdk.test.icn.url` : The url of your Navigator listener. 
e.g. `sdk.test.icn.url=https://your.icn.server.here:9443/navigator`

`sdk.userName` : The user name to use for authentication. 
e.g. `sdk.userName=user123`

`sdk.password` : The password to use for authentication. 
e.g. `sdk.password=password456`


### Compiling and assembling the jar (with dependencies) 
    mvn clean compile assembly:single
  
### clean , compile, assemble jar and run **
    mvn clean compile assembly:single exec:java

### Executing from the command line without maven:
    java  -jar  ./target/IBMECMCoreSample-1.0-SNAPSHOT-jar-with-dependencies.jar <optional parameters for app here >
  

## Optional steps:
(optional) Importing the sample into Eclipse
Select import (from maven sub menu)
Select import existing maven project (select directory where the Sample project's pom.xml is located)

### produce just the jar with no dependencies (optional)
    mvn clean package


### Logging
You will notice the following message in your output:

    SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
    SLF4J: Defaulting to no-operation (NOP) logger implementation
    SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

If you would like to see the logger output from your sample (and all of the SDK code which can be large) 
comment out the

    <type>pom</type>
    
line in the dependency for 

    <artifactId>slf4j-simple</artifactId>
    
This will include the slf4j simple logger class in the classpath (jar file) that is created.        
            
            
 

# Sample run 
    (without the logger enabled to save space)
    ***********************************************************

    Jays-MBP:command.line.simple jbrown$ mvn clean compile assembly:single exec:java

    [INFO] Scanning for projects...
    [INFO]                                                                         
    [INFO] ------------------------------------------------------------------------
    [INFO] Building IBMECMCoreSample 1.0-SNAPSHOT
    [INFO] ------------------------------------------------------------------------
    [INFO] 
    [INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ IBMECMCoreSample ---
    [INFO] Deleting /Users/jbrown/Documents/IBM/mobile.team/sdk/samples/command.line.simple/target
    [INFO] 
    [INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ IBMECMCoreSample ---
    [WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
    [INFO] Copying 2 resources
    [INFO] 
    [INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ IBMECMCoreSample ---
    [INFO] Changes detected - recompiling the module!
    [WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
    [INFO] Compiling 3 source files to /Users/jbrown/Documents/IBM/mobile.team/sdk/samples/command.line.simple/target/classes
    [INFO] 
    [INFO] --- maven-assembly-plugin:2.2-beta-5:single (default-cli) @ IBMECMCoreSample ---

    <truncated [INFO] maven messages here>

    Reading system property config = null
    Reading test configuration file /sample.properties
    loaded propertysdk.test.icn.url val=https://cm-bengalvm33.dub.usoh.ibm.com:9443/navigator
    loaded propertysdk.userName val=suser
    loaded propertysdk.password val=Genius1
    SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
    SLF4J: Defaulting to no-operation (NOP) logger implementation
    SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
    Synchronous (wrapped) login was successful.
    IBMECMDesktop loaded:  .id = MobileAutomation, .name = Mobile AutomationcurrentDesktop = MobileAutomation
    getRootFolderObject
    IBMECMContentItem | .id = Folder,{D49CFB16-1DEC-4AF1-BC72-97233F34BDED},{0F1E2D3C-4B5A-6978-8796-A5B4C3D2E1F0},.name = RootRoot folder ID = Folder,{D49CFB16-1DEC-4AF1-BC72-97233F34BDED},{0F1E2D3C-4B5A-6978-8796-A5B4C3D2E1F0}
    Dumping item properties.
    IBMECMItemProperty | .id = CmIndexingFailureCode,.value = null
    IBMECMItemProperty | .id = IndexationId,.value = null
    IBMECMItemProperty | .id = Creator,.value = null
    IBMECMItemProperty | .id = CmHoldRelationships,.value = null
    IBMECMItemProperty | .id = DateCreated,.value = 2016-01-04T21:58:30.915Z
    IBMECMItemProperty | .id = CoordinatedTasks,.value = null
    IBMECMItemProperty | .id = LastModifier,.value = null
    IBMECMItemProperty | .id = PathName,.value = /
    IBMECMItemProperty | .id = CmIsMarkedForDeletion,.value = false
    IBMECMItemProperty | .id = DateLastModified,.value = null
    IBMECMItemProperty | .id = {NAME},.value = 
    IBMECMItemProperty | .id = FolderName,.value = 
    IBMECMItemProperty | .id = Id,.value = {0F1E2D3C-4B5A-6978-8796-A5B4C3D2E1F0}
    IBMECMItemProperty | .id = ActiveMarkings,.value = null
    Item properties <end>
    First page of folder children count = 10
    Child item names:
    Item name:!!!MobileApp-AutoRuntime
    Item name:!!!MobileApp-Automation
    Item name:!!!MobileApp-Automation-EditProp-QAAll
    Item name:!!!MobileApp-StoredSearches
    Item name:!!!MobileApp-UTAutomation
    Item name:!!AndroidSDKAutomationFolder1526919201
    Item name:!!IOSUTAutomation_COHPbFIYcYBt5D23
    Item name:!AndroidSdkSandbox
    Item name:!Demo
    Item name:!MobileUIAutomation3
    Asynchronous examples follow.


    Initiating login for url: https://cm-bengalvm33.dub.usoh.ibm.com:9443/navigator (as user):suser
    Login completed successfully for username: suser
    Attempting to load default desktop for username: suser
    IBMECMDesktop | .id = MobileAutomation,.name = Mobile Automation, .isConnected = true
    IBMECMDesktop | .userId = suser, .mobileAdminEmailAddress = 
    IBMECMDesktop | .securityToken = -4970462022620885083, .serverVersion = icn203.800.330
    IBMECMDesktop | .userId = suser, .userDisplayName = Mobile Automation
    IBMECMDesktop | .isMobileAccessAllowed = true, .isMobileApplicationEnabled = true
    IBMECMDesktop | .isAddDocFoldersToRepositoryFromMobileEnabled = true, .isAddDocFoldersToRepositoryFromMobileEnabled = true
    IBMECMDesktop | .isOpenDocumentFromOtherApplicationEnabled = true, .isSyncServerEnabled = true
    IBMECMDesktop | .isConnected = true
    IBMECMRepository | .id = MobileAutomation, .name = MobileAutomation
    IBMECMRepository | .connectionPoint = , .hostName = iiop://cm-bengalvm58:2809/FileNet/Engine
    IBMECMRepository | .objectStoreId = null, .objectStoreDisplayName = Mobile Automation
    IBMECMRepository | .repositoryType = p8, .serverDomainId = {4850CC1B-668F-435E-B83F-3B09D1E90CEA}
    IBMECMRepository | .isConnected: true
    IBMECMRepository | .id = AndroidAutomation, .name = AndroidAutomation
    IBMECMRepository | .connectionPoint = , .hostName = iiop://cm-bengalvm58:2809/FileNet/Engine
    IBMECMRepository | .objectStoreId = null, .objectStoreDisplayName = Android Automation
    IBMECMRepository | .repositoryType = p8, .serverDomainId = {4850CC1B-668F-435E-B83F-3B09D1E90CEA}
    IBMECMRepository | .isConnected: false
    default repo: MobileAutomation
    Terminating.
    Jays-MBP:command.line.simple jbrown$ 

 
 
 
