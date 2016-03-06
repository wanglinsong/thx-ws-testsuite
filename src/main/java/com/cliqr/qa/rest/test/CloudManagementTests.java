package com.cliqr.qa.rest.test;

import com.cliqr.qa.rest.driver.CloudManagementWebService;
import com.tascape.qa.th.comm.RestException;
import com.tascape.qa.th.driver.TestDriver;
import com.tascape.qa.th.test.AbstractTest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    private final List<JSONObject> usersToCleanup = new ArrayList<>();

    public CloudManagementTests() {
        this.globalTimeout = new Timeout(600, TimeUnit.SECONDS);
        this.service = this.getEntityDriver(SERVICE);
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
        JSONObject user = this.newUser();
        LOG.debug("User\n{}", user.toString(2));
        this.service.postUser(user);
        Assert.assertNotNull("user did not get created", this.service.getUserId(user.getString("emailAddr")));
    }

    @Test
    public void testPostUserNegative() throws Exception {
        JSONObject user = this.newUser();
        LOG.debug("User\n{}", user.toString(2));
        this.service.postUser(user);

        this.expectedException.expect(RestException.class);
        String errorMsg = "The user with email address " + user.getString("emailAddr") + " already exists.";
        this.expectedException.expectMessage(errorMsg);
        this.service.postUser(user);
    }

    @Test
    public void testDeleteUser() throws Exception {
        JSONObject user = this.newUser();
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

//    @Test
    public void debug() throws Exception {
        this.service.deleteUser("8");
        this.service.deleteUser("9");
        this.service.deleteUser("10");
        this.service.deleteUser("11");
        this.service.deleteUser("12");
    }

    @After
    public void cleanup() {
        this.usersToCleanup.forEach(user -> {
            String email = user.getString("emailAddr");
            try {
                String id = this.service.getUserId(email);
                if (id != null) {
                    this.service.deleteUser(id);
                }
            } catch (IOException ex) {
                LOG.warn("Cannot delete user {}", email);
            }
        });
    }

    private JSONObject newUser() {
        String id = UUID.randomUUID().toString();
        JSONObject user = new JSONObject();
        user.put("firstName", "user-" + id);
        user.put("lastName", "Cliqr");
        user.put("password", "cliqr");
        user.put("emailAddr", "user." + id + "@cliqr.com");
        user.put("companyName", "Cliqr, Inc");
        user.put("phoneNumber", "14085467899");
        user.put("externalId", "");
        user.put("tenantId", 1);
        this.usersToCleanup.add(user);
        return user;
    }
}
