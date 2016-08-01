package com.ibm.ecm.api.testutils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.ibm.ecm.api.coresdk.collection.IBMECMResultSet;
import com.ibm.ecm.api.coresdk.exception.IBMECMRuntimeException;
import com.ibm.ecm.api.coresdk.model.IBMECMApplication;
import com.ibm.ecm.api.coresdk.model.IBMECMCompletionHandler;
import com.ibm.ecm.api.coresdk.model.IBMECMContentItem;
import com.ibm.ecm.api.coresdk.model.IBMECMDesktop;
import com.ibm.ecm.api.coresdk.model.IBMECMRepository;
import com.ibm.ecm.api.impl.util.Holder;

/**
 * This class contains some sample static wrappers for a few SDK async operations demonstrating 
 * how to turn them into blocking calls when needed. 
 */
public class SynchronousWrappers {
    
    protected final static int SERVICE_TIMEOUT = 10;

    /**
     * Demonstrates how to make the login function into a blocking synchronous method. 
     * This same technique can be used on all of the async calls in the SDK if your application 
     * needs to make blocking calls. 
     * 
     * @param userName
     * @param password
     * @return
     */
    public static boolean loginSync(IBMECMApplication application, String userName, String password) {
        final CountDownLatch signal = new CountDownLatch(1);
        Holder<Boolean> returnVal = new Holder<Boolean>(false);

        application.login(userName, password, new IBMECMCompletionHandler<Boolean>() {
            private Holder<Boolean> anonHolder;

            @Override
            public void onCompleted(Boolean result) {
                anonHolder.setValue(result);
                signal.countDown();
            }

            @Override
            public void onError(IBMECMRuntimeException exp) {
                signal.countDown();
                System.out.format("IBMECMRuntimeException | .code = %s,.message = %s\n", exp.getErrorCode() != null? exp.getErrorCode().getCode() : null, exp.getMessage());
            }

            private IBMECMCompletionHandler<Boolean> init(Holder<Boolean> item) {
                anonHolder = item;
                return this;
            }
        }.init(returnVal));  // ** Here we pass in the returnVal holder to the Anonymous callback

        try {
            signal.await(SERVICE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return returnVal.getValue();
    }
    
    
    public static IBMECMDesktop loadDesktopSync(IBMECMApplication application) {

        final CountDownLatch signal = new CountDownLatch(1);

        Holder<IBMECMDesktop> returnVal = new Holder<IBMECMDesktop>(null);

        application.loadCurrentDesktop(new IBMECMCompletionHandler<IBMECMDesktop>() {
            private Holder<IBMECMDesktop> anonHolder;

            @Override
            public void onCompleted(IBMECMDesktop result) {
                System.out.format("IBMECMDesktop loaded:  .id = %s, .name = %s", result.getId(), result.getName());
                anonHolder.setValue(result);
                signal.countDown();
            }

            @Override
            public void onError(IBMECMRuntimeException exp) {
                signal.countDown();
                System.out.format("IBMECMRuntimeException | .code = %s, .message = %s\n",
                        exp.getErrorCode() != null ? exp.getErrorCode().getCode() : null, exp.getMessage());
            }

            private IBMECMCompletionHandler<IBMECMDesktop> init(Holder<IBMECMDesktop> item) {
                anonHolder = item;
                return this;
            }
        }.init(returnVal)); // initialize the inner class with the return
                            // variable

        try {
            signal.await(SERVICE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return returnVal.getValue();
    }
    
    public static IBMECMResultSet<IBMECMContentItem> retrieveFolderContentSync(IBMECMContentItem contentItem, int pageSize) {
        final Holder<IBMECMResultSet<IBMECMContentItem>> returnVal = new Holder<IBMECMResultSet<IBMECMContentItem>> (null);
        final CountDownLatch signal = new CountDownLatch(1);
        contentItem.retrieveFolderContent(false, null, false, pageSize, null, new IBMECMCompletionHandler<IBMECMResultSet<IBMECMContentItem>>() {

            @Override
            public void onCompleted(IBMECMResultSet<IBMECMContentItem> result) {
                returnVal.setValue(result);
                signal.countDown();
            }

            @Override
            public void onError(IBMECMRuntimeException exp) {
                signal.countDown();
                System.out.format("IBMECMRuntimeException | .code = %s, .message = %s, explanation = %s\n", exp.getErrorCode() != null? exp.getErrorCode().getCode() : null, exp.getMessage(), exp.getExplanation());
            }

        });

        try {
            signal.await(SERVICE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       
        return returnVal.getValue();
    }

    
    public static IBMECMContentItem getRootFolderObjectSync(IBMECMRepository repository) {
        System.out.format("getRootFolderObject\n");

        final CountDownLatch signal = new CountDownLatch(1);
        Holder<IBMECMContentItem> returnVal = new Holder<>(null);
        String docId = "/";

        repository.retrieveItem(docId, new IBMECMCompletionHandler<IBMECMContentItem>() {
            private Holder<IBMECMContentItem> anonHolder;

            @Override
            public void onCompleted(IBMECMContentItem result) {
                System.out.format("IBMECMContentItem | .id = %s,.name = %s", result.getId(), result.getName());

                anonHolder.setValue(result);
                signal.countDown();
            }

            @Override
            public void onError(IBMECMRuntimeException exp) {
                System.out.format("IBMECMRuntimeException | .code = %s,.message = %s\n", exp.getErrorCode().getCode(),
                        exp.getMessage());
                signal.countDown();
            }

            private IBMECMCompletionHandler<IBMECMContentItem> init(Holder<IBMECMContentItem> item) {
                anonHolder = item;
                return this;
            }
        }.init(returnVal));

        try {
            signal.await(SERVICE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return returnVal.getValue();
    }
    
}
