package com.cliqr.qa.user.test;

import com.tascape.qa.th.comm.WebServiceException;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NA
 */
public class UserUpdateTests extends AbstractUserTests {
    private static final Logger LOG = LoggerFactory.getLogger(UserUpdateTests.class);

    @Test
    public void testDisabledUser() throws Exception {
        expectedException.expect(WebServiceException.class);
        LOG.debug("user-under-test {} cannot get its own info, because it is not enabled", userId);
        test.getUser(userId, false);
    }

    @Test
    public void testEnableUser() throws Exception {
        LOG.debug("enable user-under-test", userId);
        userUnderTest.put("enabled", true);
        String reqId = UUID.randomUUID().toString();
        JSONObject user = admin.putUser(userUnderTest, reqId);
        this.putResultMetric("user-management", "add-user",
            admin.getWebServiceCommunication().getResponseTime(reqId));
        LOG.debug("user-under-test updated\n{}", user.toString(2));

        LOG.debug("user-under-test {} can get its own detail, after getting enabled", userId);
        reqId = UUID.randomUUID().toString();
        user = test.getUser(userId, true, reqId);
        this.putResultMetric("user-management", "get-user-detail",
            test.getWebServiceCommunication().getResponseTime(reqId));
        LOG.debug("user-under-test detail\n{}", user.toString(2));
    }
}
