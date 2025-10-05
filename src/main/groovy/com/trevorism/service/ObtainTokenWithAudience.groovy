package com.trevorism.service

import com.google.gson.Gson
import com.trevorism.ClasspathBasedPropertiesProvider
import com.trevorism.PropertiesProvider
import com.trevorism.http.HttpClient
import com.trevorism.https.token.InvalidTokenCredentialsException
import com.trevorism.https.token.ObtainTokenStrategy
import com.trevorism.model.AudienceTokenRequest

class ObtainTokenWithAudience implements ObtainTokenStrategy {

    private static final String TOKEN_ENDPOINT = "https://auth.trevorism.com/token"
    private static final String DATASTORE_AUDIENCE = "6ba426e4-f740-44b5-98ce-15a5bc4ed105"
    private final Gson gson = new Gson()
    private HttpClient httpClient

    void setHttpClient(HttpClient client) {
        this.httpClient = client
    }

    String getToken() {
        PropertiesProvider propertiesProvider = new ClasspathBasedPropertiesProvider()
        String clientId = propertiesProvider.getProperty("clientId")
        String clientSecret = propertiesProvider.getProperty("clientSecret")
        if (clientId != null && clientSecret != null) {
            AudienceTokenRequest tokenRequest = new AudienceTokenRequest(id: clientId, password: clientSecret, audience: DATASTORE_AUDIENCE)
            String json = this.gson.toJson(tokenRequest)

            try {
                return this.httpClient.post("https://auth.trevorism.com/token", json);
            } catch (Exception e) {
                throw new InvalidTokenCredentialsException(e)
            }
        } else {
            throw new InvalidTokenCredentialsException()
        }
    }

}
