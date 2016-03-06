package com.cliqr.qa.cloud.driver;

import com.tascape.qa.th.comm.WebServiceCommunication;
import com.tascape.qa.th.driver.EntityDriver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * web service.
 * http://docs.cliqr.com/display/GS1/Home
 *
 * @author NA
 */
public class CloudManagementWebService extends EntityDriver {
    private static final Logger LOG = LoggerFactory.getLogger(CloudManagementWebService.class);

    private enum Endpoint {
        STATUS("/status"),
        V1_USERS("/v1/users");

        public final String value;

        Endpoint(String value) {
            this.value = value;
        }
    }

    private WebServiceCommunication wsc;

    private final List<JSONObject> usersToCleanup = new ArrayList<>();

    public void setWebServiceComminication(WebServiceCommunication wsc) {
        this.wsc = wsc;
    }

    public String getStatus() throws IOException {
//        TODO
//        String res = this.wsc.get(Endpoint.STATUS.value);
//        return res;
        return "OK";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public JSONObject getUsers(int size) throws IOException {
        JSONObject users = this.wsc.getJsonObject(Endpoint.V1_USERS.value, "size=" + size);
        LOG.debug("total {} users", users.getJSONArray("users").length());
        return users;
    }

    public JSONObject getAllUsers() throws IOException {
        return this.getUsers(Integer.MAX_VALUE);
    }

    public void postUser(JSONObject user) throws IOException {
        this.wsc.postJson(Endpoint.V1_USERS.value, user);
    }

    public void deleteUser(String id) throws IOException {
        this.wsc.delete(Endpoint.V1_USERS.value + "/" + id);
    }

    public String getUserId(String email) throws IOException {
        JSONObject res = this.getAllUsers();
        JSONArray users = res.getJSONArray("users");
        int len = users.length();
        LOG.debug("total {} users", len);

        for (int i = 0; i < len; i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("emailAddr").equals(email)) {
                return user.getString("id");
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return CloudManagementWebService.class.getName();
    }

    @Override
    public void reset() throws Exception {
        this.usersToCleanup.forEach(user -> {
            String email = user.getString("emailAddr");
            try {
                String id = this.getUserId(email);
                if (id != null) {
                    this.deleteUser(id);
                }
            } catch (IOException ex) {
                LOG.warn("Cannot delete user {}", email);
            }
        });
    }

    public JSONObject newUser() {
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
