package com.cliqr.qa.cloud.suite;

import com.cliqr.qa.cloud.driver.CloudManagementWebService;
import com.tascape.qa.th.suite.AbstractSuite;
import com.cliqr.qa.cloud.test.CloudManagementTests;
import com.tascape.qa.th.comm.WebServiceCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NA
 */
public class CloudManagementSmokeSuite extends AbstractSuite {
    private static final Logger LOG = LoggerFactory.getLogger(CloudManagementSmokeSuite.class);

    private CloudManagementWebService service;

    @Override
    public void setUpTestClasses() {
        this.addTestClass(CloudManagementTests.class);
    }

    @Override
    public String getProductUnderTest() {
        return "cloud management";
    }

    @Override
    protected void setUpEnvironment() throws Exception {
        /*
         * The following system properties are required.
         * qa.th.comm.ws.HOST
         * qa.th.comm.ws.PORT
         * qa.th.comm.ws.USER
         * qa.th.comm.ws.PASS
         */
        this.service = new CloudManagementWebService();
        this.service.setWebServiceComminication(WebServiceCommunication.newInstance());
        this.putTestDirver(CloudManagementTests.SERVICE, service);
    }

    @Override
    protected void tearDownEnvironment() {
        try {
            this.service.reset();
        } catch (Exception ex) {
            LOG.warn("Error resetting", ex);
        }
    }
}
