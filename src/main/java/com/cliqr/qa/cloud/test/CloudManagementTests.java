package com.cliqr.qa.cloud.test;

import com.cliqr.qa.cloud.driver.CloudManagementWebService;
import com.tascape.qa.th.comm.WebServiceException;
import com.tascape.qa.th.driver.TestDriver;
import com.tascape.qa.th.test.AbstractTest;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NA
 */
public class CloudManagementTests extends AbstractTest {
    private static final Logger LOG = LoggerFactory.getLogger(CloudManagementTests.class);

    public static final TestDriver SERVICE = new TestDriver(CloudManagementTests.class, CloudManagementWebService.class);

    private final CloudManagementWebService service;

    public CloudManagementTests() {
        this.globalTimeout = new Timeout(10, TimeUnit.SECONDS);
        this.service = super.getEntityDriver(SERVICE);
    }

    @Test
    public void testAllUsers() throws Exception {
        JSONObject res = this.service.getAllUsers();
        LOG.debug("users\n{}", res.toString(2));
        JSONArray users = res.getJSONArray("users");
        int len = users.length();
        LOG.debug("Verify each password is masked.");
        for (int i = 0; i < len; i++) {
            Assert.assertEquals("password is not masked,",
                "== red-acted ==",
                users.getJSONObject(i).getString("password"));
        }
    }

    @Test
    public void testPostUser() throws Exception {
        JSONObject user = service.newUser();
        LOG.debug("User\n{}", user.toString(2));
        this.service.postUser(user);
        Assert.assertNotNull("user did not get created", this.service.getUserId(user.getString("emailAddr")));
    }

    @Test
    public void testPostUserNegative() throws Exception {
        JSONObject user = service.newUser();
        LOG.debug("User\n{}", user.toString(2));
        this.service.postUser(user);

        this.expectedException.expect(WebServiceException.class);
        String errorMsg = "The user with email address " + user.getString("emailAddr") + " already exists.";
        this.expectedException.expectMessage(errorMsg);
        this.service.postUser(user);
    }

    @Test
    public void testDeleteUser() throws Exception {
        JSONObject user = service.newUser();
        LOG.debug("User\n{}", user.toString(2));
        this.service.postUser(user);

        String email = user.getString("emailAddr");
        String id = this.service.getUserId(email);
        this.service.deleteUser(id);
        Assert.assertNull("user did not get deleted", this.service.getUserId(email));
        LOG.debug("user got deleted");
    }

    @Override
    public String getApplicationUnderTest() {
        return this.service.getName();
    }

    @After
    public void cleanup() {
    }
}
