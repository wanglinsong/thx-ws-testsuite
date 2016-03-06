package com.cliqr.qa.rest.suite;

import com.cliqr.qa.rest.driver.CloudManagementWebService;
import com.tascape.qa.th.suite.AbstractSuite;
import com.cliqr.qa.rest.test.CloudManagementTests;
import com.tascape.qa.th.comm.RestCommunication;
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
         * qa.th.comm.rest.HOST
         * qa.th.comm.rest.PORT
         * qa.th.comm.rest.USER
         * qa.th.comm.rest.PASS
         */
        RestCommunication rc = new RestCommunication();
        rc.connect();
        this.service = new CloudManagementWebService();
        service.setRestCommunication(rc);
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
