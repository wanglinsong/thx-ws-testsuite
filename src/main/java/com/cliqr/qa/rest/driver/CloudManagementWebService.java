package com.cliqr.qa.rest.driver;

import com.tascape.qa.th.comm.RestCommunication;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.tascape.qa.th.driver.EntityDriver;
import java.io.IOException;
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
        V1_USERS("/v1/users");

        private final String value;

        Endpoint(String value) {
            this.value = value;
        }
    }

    private RestCommunication rc;

    public void setRestCommunication(RestCommunication rc) {
        this.rc = rc;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public JSONObject getUsers(int size) throws IOException {
        Response res = rc.given().get(Endpoint.V1_USERS.value + "?size=" + size);
        RestCommunication.checkResponse(res);

        JSONObject users = new JSONObject(res.asString());
        LOG.debug("total {} users", users.getJSONArray("users").length());
        return users;
    }

    public JSONObject getAllUsers() throws IOException {
        return this.getUsers(Integer.MAX_VALUE);
    }

    public void postUser(JSONObject user) throws IOException {
        Response res = rc.given().contentType(ContentType.JSON).body(user.toString())
            .post(Endpoint.V1_USERS.value).andReturn();
        RestCommunication.checkResponse(res);
    }

    public void deleteUser(String id) throws IOException {
        Response res = rc.given().delete(Endpoint.V1_USERS.value + "/" + id);
        RestCommunication.checkResponse(res);
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
        LOG.debug("NA");
    }
}
