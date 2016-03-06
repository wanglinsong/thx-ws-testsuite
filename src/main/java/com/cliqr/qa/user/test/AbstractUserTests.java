package com.cliqr.qa.user.test;

import com.cliqr.qa.user.driver.UserManagementService;
import com.tascape.qa.th.driver.TestDriver;
import com.tascape.qa.th.test.AbstractTest;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.rules.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NA
 */
public class AbstractUserTests extends AbstractTest {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractUserTests.class);

    public static final TestDriver ADMIN = new TestDriver(AbstractUserTests.class, UserManagementService.class);

    public static final TestDriver TEST = new TestDriver(AbstractUserTests.class, UserManagementService.class);

    protected final UserManagementService admin;

    protected final UserManagementService test;

    protected JSONObject userUnderTest;

    protected String userId;

    public AbstractUserTests() {
        this.globalTimeout = new Timeout(600, TimeUnit.SECONDS);

        this.admin = super.getEntityDriver(ADMIN);
        this.test = super.getEntityDriver(TEST);
    }

    @Before
    public void setup() throws Exception {
        JSONObject user = admin.newUser(false);
        LOG.debug("new user\n{}", user.toString(2));
        userUnderTest = this.admin.postUser(user);
        LOG.debug("user-under-test\n{}", userUnderTest.toString(2));
        this.userId = userUnderTest.getString("id");

        String name = userUnderTest.getString("username");
        String apiKey = admin.getAaccessKeys(userId).getJSONObject("apiKey").getString("key");
        LOG.debug("api key {}", apiKey);
        test.updateUserPass(name, apiKey);
    }

    @After
    public void cleanup() throws Exception {
        if (userId != null) {
            if (userUnderTest.getBoolean("enabled")) {
                LOG.debug("disable user {}", userId);
                userUnderTest.put("enabled", false);
                admin.putUser(userUnderTest);
            }
            admin.deleteUser(userId);
        }
    }

    @Override
    public String getApplicationUnderTest() {
        return admin.getName();
    }
}
