package com.cliqr.qa.user.suite;

import com.cliqr.qa.user.driver.UserManagementService;
import com.cliqr.qa.user.test.AbstractUserTests;
import com.cliqr.qa.user.test.UserActionTests;
import com.tascape.qa.th.suite.AbstractSuite;
import com.cliqr.qa.user.test.UserUpdateTests;
import com.tascape.qa.th.comm.WebServiceCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NA
 */
public class UserManagementSuite extends AbstractSuite {
    private static final Logger LOG = LoggerFactory.getLogger(UserManagementSuite.class);

    private UserManagementService admin;

    private UserManagementService test;

    @Override
    public void setUpTestClasses() {
        this.addTestClass(UserUpdateTests.class);
        this.addTestClass(UserActionTests.class);
    }

    @Override
    public String getProductUnderTest() {
        return "cloud management";
    }

    @Override
    protected void setUpEnvironment() throws Exception {
        /*
         * The following system properties are required for admin user.
         * qa.th.comm.ws.HOST
         * qa.th.comm.ws.PORT
         * qa.th.comm.ws.USER
         * qa.th.comm.ws.PASS
         */
        this.admin = new UserManagementService();
        WebServiceCommunication wsc = WebServiceCommunication.newInstance();
        this.admin.setWebServiceComminication(wsc);

        /*
         * The test user is just a placeholder, no user/pass. And the communcation to server is not established, yet.
         */
        this.test = new UserManagementService();
        this.test.setWebServiceComminication(new WebServiceCommunication(wsc.getHttpHost()));

        this.putTestDirver(AbstractUserTests.ADMIN, admin);
        this.putTestDirver(AbstractUserTests.TEST, test);
    }

    @Override
    protected void tearDownEnvironment() {
        LOG.warn("NA");
    }
}
