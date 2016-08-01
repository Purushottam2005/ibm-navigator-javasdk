package com.ibm.ecm.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.HashMap;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyVision;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.alchemy.v1.model.ImageKeyword;
import com.ibm.watson.developer_cloud.alchemy.v1.model.ImageKeywords;
import com.ibm.watson.developer_cloud.service.AlchemyService;

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
    protected final static int MAX_CHARACTERS_FOR_ANALYSIS = 500000;

    // Load all of the connection information we need for our ICN server
    // Edit /src/main/resources/sample.properties to set these values
    // as appropriate for your local test ICN server.
    protected static Map<String, String> configMap = ConfigUtils.loadTestProperties();
    private final static Logger logger = LoggerFactory.getLogger(Sample.class);

    public static void main(String[] args) {

        final String userName = configMap.get("sdk.userName");
        final String password = configMap.get("sdk.password");
        final String watsonApiKey = configMap.get("watson.api.key");
        final String directoryToScan = configMap.get("directoryToScan");

        AlchemyLanguage languageService = new AlchemyLanguage();
        languageService.setApiKey(watsonApiKey);
        AlchemyVision visionService = new AlchemyVision();
        visionService.setApiKey(watsonApiKey);
        
        // synchronous example
        final IBMECMApplication application = factory.getApplication(configMap.get("sdk.test.icn.url"), true);
        boolean success = SynchronousWrappers.loginSync(application, userName, password);
        if (success) {
            System.out.println("Synchronous (wrapped) login was successful.");
            logger.info("Sample application starting. (output in logger)");

            IBMECMDesktop desktopCurrent = SynchronousWrappers.loadDesktopSync(application);
            if (desktopCurrent != null) {
                System.out.println("currentDesktop = " + desktopCurrent.getDefaultRepository().getName());

                IBMECMContentItem contentFolderToScan = SynchronousWrappers
                        .retrieveItem(desktopCurrent.getDefaultRepository(), directoryToScan);

                if (contentFolderToScan == null) {
                    System.out.println(
                            "The directory to scan : " + contentFolderToScan + " was not found in the repository.");
                    System.exit(-1);
                }

                IBMECMResultSet<IBMECMContentItem> folderChildren = SynchronousWrappers
                        .retrieveFolderContentSync(contentFolderToScan, 25);
                List<IBMECMContentItem> childrenList = folderChildren.getItems();

                for (IBMECMContentItem item : childrenList) {
                    processItem(item, languageService, visionService);
                }
            }
        }

        System.out.println("Terminating.");
        System.exit(0);
    }

    public static boolean processItem(IBMECMContentItem item, AlchemyLanguage languageService,
            AlchemyVision visionService) {
        boolean returnVal = false;
        String commentString = null;

        if (item.getName().endsWith(".txt")) {
            System.out.println("\nProcessing text item: " + item.getName());
            // get the text out of the document (if type is text)
            // get the first [MAX_CHARACTERS_FOR_ANALYSIS] characters from the item
            
            // TBD - put this back
            String text = retrieveDocumentString(item, MAX_CHARACTERS_FOR_ANALYSIS);
            commentString = getSentiment(languageService, text);
        } else if (item.getName().endsWith(".jpg") || item.getName().endsWith(".png"))   {
            // do image recognition tagging
      
            System.out.println("\nProcessing image item: " + item.getName());
            commentString = getImageTags(visionService, item);
        } else {
            System.out.println("skipping document (not identified at text or image) : " + item.getName());
        }

        if ((commentString != null) && (!commentString.isEmpty())) {
            SynchronousWrappers.addComment(commentString, item);
            System.out.println("\nComment: " + commentString);
            returnVal = true;
        }
        return returnVal;
    }

    public static String retrieveDocumentString(IBMECMContentItem item, int sizeLimit) {
        String returnVal = null;

        File tempFile = null;
        try {
            tempFile = File.createTempFile("content", "text");
        } catch (IOException e) {
            e.printStackTrace();
            return returnVal;
        }

        item.retrieveDocumentItem(tempFile); // SDK call

        if (tempFile.exists()) {
            // read off just the amount of chars we need
            returnVal = readTopOfFile(tempFile, sizeLimit);
        }
        return returnVal;
    }

    public static File retrieveDocumentFile(IBMECMContentItem item) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("content", "text");
            item.retrieveDocumentItem(tempFile); // SDK call
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile;
    }

    public static String readTopOfFile(File file, int charactersToRead) {
        FileReader reader = null;
        char[] chars = new char[charactersToRead];
        int offset = 0;

        try {
            reader = new FileReader(file);

            while (offset < charactersToRead) {
                int charsRead;
                try {
                    charsRead = reader.read(chars, offset, charactersToRead - offset);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                if (charsRead <= 0) {
                    break;
                }
                offset += charsRead;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new String(chars);
    }

    public static String getSentiment(AlchemyLanguage service, String textIn) {
        StringBuilder outputSentiment = new StringBuilder();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.TEXT, textIn);

        DocumentSentiment sentiment = service.getSentiment(params).execute();
        outputSentiment.append("Sentiment: type=" + sentiment.getSentiment().getType().toString() + "\n Entities:");

        Entities entities = service.getEntities(params).execute();

        for (Entity e : entities.getEntities()) {
            outputSentiment.append("[" + e.getText() + "] ");
        }

        return outputSentiment.toString();
    }

    public static String getImageTags(AlchemyVision visionService, IBMECMContentItem item) {
        StringBuilder outputSentiment = new StringBuilder();

        File docFile = retrieveDocumentFile(item);
        ImageKeywords keywords = visionService.getImageKeywords(docFile, true, true).execute();

        if ((keywords != null) && (keywords.getImageKeywords() != null)) {
            for (ImageKeyword ikw : keywords.getImageKeywords()) {
                outputSentiment.append("[" + ikw.getText() +  "- score=(" + ikw.getScore().toString() + ") ] ");
            }
        } else {
            // service may be unavailable 
        }

        return outputSentiment.toString();
    }

}
