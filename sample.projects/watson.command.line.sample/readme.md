
## Watson Command line samples for IBM Content Navigator Java SDK 

## Description

The command line samples are part of the larger suite of SDK samples which also include samples 
for mobile development.  The Watson command sample shows a simple integration with the Watson Alchemy services.  The sample walks a given directory on your ICN server looking for text and image files.  For every one that it finds it performs either a Sentiment analysis (if text) or a image tagging (if it's an image) and places the resulting tags in a comment on the document in ICN. 

For example: 

When it encounters an image it adds a comment to the document  with tagging information about the image like this example for a puppy’s image:
```
Comment: [animal- score=(0.999999) ] [mammal- score=(0.999997) ] [dog- score=(0.768525) ] [puppy- score=(0.331812) ] [beagle- score=(0.28905) ] 
```

When it encounters a text file it performs both Sentiment and Entity analysis as in this example for the The_Illiad.txt:
```
Comment: Sentiment: type=NEGATIVE
 Entities:[Homer] [Homer] [ACHILLES] [Peisistratus] [Grote] [Peisistratus] [Greece] [Melesigenes] [Greece] [HECTOR] [JUNO] [Mr. Payne Knight] [F. A. Wolf] [School of Homer] [JUPITER] [Alexander Pope] [THETIS] [writer] [PARIS] [GODS] [Cumae] [HELEN] [Melesigenes] [Phemius] [Rev. Theodore Alois Buckley] [Archilochus] [Thucydides] [Troy] [Character set] [Ithaca] [CHAMBER OF PARIS] [Solon] [Chian] [Bentley] [Flaxman] [NEPTUNE] [Bentley] [FUNERAL] [Wolfian] [MINERVA] [Lachmann] [POLYDAMAS] [Thebais] [Romulus] [Socrates] [Solon] [Thestorides] [Smyrna] [HERALDS] [Glaucus] 
```

These comments can then be viewed by any Navigator client (mobile or desktop) when examining the file. 



## Setting up your build
After obtaining the SDK jar file from your distribution, 
(IBMECMCore.jar can be found in the lib directory of this sample) 
you will need to add the jar to your Maven repository
so that any pom.xml based project can list it as a dependency. 

**This is very important ** to do on any development machine where you are 
going to be using mvn (or gradle) to manage your project. 

### Adding the the SDK jar to the local mvn repo  ***  (only need to do once per develop machine)
Substitute your path to the jar for './lib/IBMECMCore.jar' below

    mvn install:install-file -Dfile=./lib/IBMECMCore.jar -DgroupId=com.ibm.ecm -DartifactId=IBMECMCore -Dversion=1.0.0 -Dpackaging=jar

### Compiling and assembling the jar (with dependencies) 
    mvn clean compile assembly:single
  
### clean , compile, assemble jar and run **
    mvn clean compile assembly:single exec:java

### Executing from the command line without maven:
    java  -jar  ./target/IBMECMCoreSample-1.0-SNAPSHOT-jar-with-dependencies.jar <optional parameters for app here >

### Sample.properties file setttings
The following settings must be filled in in  your sample.properties file for the Watson service to operate:

`directoryToScan` : This is the path of the directory to scan.  In our example we placed some text format book from project [Gutenberg](http://www.gutenberg.org/) and some images. Note the text files must be named something.txt (anything with .txt at the end) or something.jpg/png (any image with .png or .jpg at the end) This is the way the code identifies acceptable test files.  

`watson.api.key` : Before running the sample you must also get your own Alchemy API key from Bluemix.   See [link](http://blog.alchemyapi.com/getting-started-with-alchemyapi-on-bluemix) for instructions.    


## POM file changes for Alchemy
Note that the `pom.xml` file has the dependency already added for the Java Alchemy apis. 

```
        <dependency>
          <groupId>com.ibm.watson.developer_cloud</groupId>
          <artifactId>java-sdk</artifactId>
          <version>3.0.1</version>
        </dependency>
```

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
            

## Alchemy API info
Please see the following [link](http://www.alchemyapi.com/api/calling-the-api) for more information about the Alchemy API. 
 

# Sample run 
   
The following is output from a sample run of the application. 
   
```
Reading system property config = null
Reading test configuration file /sample.properties
loaded propertysdk.test.icn.url val=https://cm-bengalvm33.dub.usoh.ibm.com:9443/navigator
loaded propertydirectoryToScan val=/watson.test
loaded propertysdk.userName val=user
loaded propertywatson.api.key val=xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
loaded propertysdk.password val=Password

Synchronous (wrapped) login was successful.
IBMECMDesktop loaded:  .id = MobileAutomation, .name = Mobile AutomationcurrentDesktop = MobileAutomation

Processing text item: AliceInWonderland.txt
Comment: Sentiment: type=NEGATIVE
 Entities:[Alice] [Rabbit] [Dinah] [ALICE] [the house] [Rabbit] [Dodo] [Lewis Carroll] [Lory] [Mabel] [FATHER WILLIAM] [Eaglet] [Christmas] [Nile] [Caterpillar] [Mary Ann] [thehouse] [Latitude] [New Zealand] [Mercia] [archbishop of Canterbury] [chrysalis] [fanning] [Edgar Atheling] [William] [Bill] [inquisitively] [Mercia] [Stigand] [Australia] [crown. William] [thimble] [Duck] [Shakespeare] [Rome] [London] [Pat] [Morcar] [Edwin] [Northumbria] [Brandy] [Paris] [nine feet] [four
thousand miles] [fifteen inches] [hundred pounds] [four inches] [ten minutes] [ten inches] [one foot] 

Processing text item: Beowolf.txt
Comment: Sentiment: type=NEGATIVE
 Entities:[persecutes Hrothgar] [Hrothgar] [Beowulf] [Grendel] [Danes] [Scyld] [Higelac] [Scyldings] [Scyld] [King Hrothgar] [Higelac] [Geats] [Hrothgar] [Hrothgar Moralizes] [Heort] [Ongentheow] [King Hrothgar] [Heorot] [Wiglaf Plunders] [Hrethel] [Long] [Hnæf] [Boston] [Act of Congress] [Finn] [Ohthere] [Anglo-Saxon Poetry] [Sweden] [Geats] [LESSLIE HALL] [London] [Lesslie Hall] [Sievers] [Queen Wealhtheow] [Halga] [Breca] [Wielder of Glory] [Librarian of Congress] [Freaware] [Heatholaf] [Geat-land] [Garmund] [t. B.] [Thrytho] [Franks.] [Washington] [Kl.] [Professor of English] [Cain] [General Use xviii] 

Processing text item: Dracula_BramStoker.txt
Comment: Sentiment: type=NEGATIVE
 Entities:[Jonathan Harker] [Carpathians] [London] [DRACULA] [Bukovina] [Dr. Seward] [windows] [United States] [Count Dracula] [Transylvania] [Bram Stoker] [Mina Murray] [mein Herr] [Mina Harker] [clerk! Mina] [Bistritz] [Bistritz] [Lucy Westenra] [NEW YORK] [Internet Archive] [Vienna] [Congress] [GARDEN CITY] [Mr. Hawkins] [Klausenburgh] [Chuck Greif] [Borgo Pass] [Attila] [British Museum] [Europe] [N.Y] [Munich] [Online Distributed] [apple] [America] [Mittel Land] [Van Helsing] [P. M.] [Borgo Pass] [the house] [white shirt-sleeves] [Hotel Royale] [Borgo] [Szekelys] [China] [Borgo Prund] [rose] [Golden Mediasch] [St. George] [Satan] 

Processing text item: Metamorphosis_kafka.txt
Comment: Sentiment: type=NEGATIVE
 Entities:[Gregor Samsa] [windows] [pain] [Franz Kafka] [Samsa] [David Wyllie] [illustrated magazine] [salesman] [insurancecompany] [unselfconsciously] [Anna] [urge to move] [fifteen years] [five years] [six years] [two days] 

Processing text item: TheCountOfMonteCristo.txt
Comment: Sentiment: type=NEGATIVE
 Entities:[Edmond Dantès] [Edmond] [M. Danglars] [Policar Morrel] [Marseilles] [Mercédès] [Danglars. Fernand] [Captain Leclere] [Edmond] [Caderousse] [Mercédès] [Dantès] [Caderousse] [Dantès] [Monte Cristo] [The House] [Morrel Family] [Morrel] [Danglars] [Alexandre Dumas] [Dan Muller] [Rome] [Italy] [Hundred Days] [MONTE CRISTO] [MONTE CRISTO] [partner] [Cape de Morgion] [Island of Tiboulen] [Auteuil] [La Mazzolata] [Deputy Procureur du Roi] [Two Prisoners] [Pont du Gard Inn] [David Widger] [Notre-Dame de la Garde] [Monsieur Bertuccio] [Character set] [Dappled Grays] [Secret Cave] [Roman Bandits] [Saint Sebastian] [Paris] [Phocee] [Burglary] [Danglars] [fever] [Andrea Cavalcanti] [M. Noirtier de Villefort] [Island of Elba] 

Processing text item: The_Iliad.txt
Comment: Sentiment: type=NEGATIVE
 Entities:[Homer] [Homer] [ACHILLES] [Peisistratus] [Grote] [Peisistratus] [Greece] [Melesigenes] [Greece] [HECTOR] [JUNO] [Mr. Payne Knight] [F. A. Wolf] [School of Homer] [JUPITER] [Alexander Pope] [THETIS] [writer] [PARIS] [GODS] [Cumae] [HELEN] [Melesigenes] [Phemius] [Rev. Theodore Alois Buckley] [Archilochus] [Thucydides] [Troy] [Character set] [Ithaca] [CHAMBER OF PARIS] [Solon] [Chian] [Bentley] [Flaxman] [NEPTUNE] [Bentley] [FUNERAL] [Wolfian] [MINERVA] [Lachmann] [POLYDAMAS] [Thebais] [Romulus] [Socrates] [Solon] [Thestorides] [Smyrna] [HERALDS] [Glaucus] 

Processing image item: cat.png
Comment: [animal- score=(0.999325) ] [cat- score=(0.997268) ] [mammal- score=(0.992608) ] [kitten- score=(0.645656) ] [kitty- score=(0.377541) ] [pet- score=(0.354344) ] [tabby- score=(0.331812) ] [feline- score=(0.28905) ] 

Processing text item: prideAndPrejudice.txt
Comment: Sentiment: type=NEGATIVE
 Entities:[Elizabeth Bennet] [characteristic. Bingley] [Mr. Darcy] [Sir William Lucas] [Jane Austen] [Lydia] [Sir William] [Miss Eliza] [Meryton] [Mrs. Hurst] [Lizzy] [Netherfield] [Mary] [Longbourn] [the house] [Mrs. Long] [London] [partner] [principal] [Catherine] [England] [Netherfield] [Netherfield Park] [Colonel Forster] [Miss King] [cough] [Mr. Morris] [Charlotte] [Mrs. Phillips] [Meryton assembly] [Miss Watson] [Netherfield House] [Miss Lucases] [Hertfordshire] [Kitty fretfully] [resentfully] [dancing] [Aye] [Netherfield] [Derbyshire] [felicity] [Captain Carter] [pounds] [Clarke] [Mr. Robinson] [Louisa] [Lucas Lodge] [England] [Jones] [James] 

Processing image item: puppy.jpg
Comment: [animal- score=(0.999999) ] [mammal- score=(0.999997) ] [dog- score=(0.768525) ] [puppy- score=(0.331812) ] [beagle- score=(0.28905) ] 

Processing image item: rabbit.jpg
Comment: [animal- score=(0.990987) ] [mammal- score=(0.908877) ] [rabbit- score=(0.817574) ] [white- score=(0.524979) ] [cat- score=(0.524979) ] [pet- score=(0.401312) ] [bunny- score=(0.377541) ] [bird- score=(0.268941) ] 
Terminating.

```
 
 
