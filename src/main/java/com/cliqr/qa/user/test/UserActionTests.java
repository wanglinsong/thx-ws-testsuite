package com.cliqr.qa.user.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NA
 */
public class UserActionTests extends AbstractUserTests {
    private static final Logger LOG = LoggerFactory.getLogger(UserActionTests.class);

    @Test
    public void testActivateUser() throws Exception {
        LOG.debug("actiovate user-under-test", userId);
        JSONObject json = new JSONObject()
            .put("action", "ACTIVATE")
            .put("userActivationData", new JSONObject()
                .put("planId", 1)
                .put("contractId", 1)
                .put("activateRegions", new JSONArray()
                    .put(new JSONObject().put("regionId", 1))
                    .put(new JSONObject().put("regionId", 2)))
                .put("agreeToContract", true)
                .put("sendActivationEmail", false)
                .put("defaultStorageSize", 0)
                .put("importApps", new JSONArray())
            );
        LOG.debug("activate with\n{}", json.toString(2));
        String res = admin.postUser(userId, json);
        LOG.debug(res);
    }
}
