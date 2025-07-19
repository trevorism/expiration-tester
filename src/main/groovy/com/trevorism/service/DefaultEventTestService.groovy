package com.trevorism.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trevorism.https.SecureHttpClient
import jakarta.inject.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@jakarta.inject.Singleton
class DefaultEventTestService implements EventTestService {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventTestService.class.name)

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
    private SecureHttpClient secureHttpClient

    DefaultEventTestService(@Named("eventTesterSecureHttpClient") SecureHttpClient secureHttpClient) {
        this.secureHttpClient = secureHttpClient
    }

    @Override
    boolean ensureUsersNotExpiring() {
        return true
    }

    @Override
    boolean ensureAppsNotExpiring() {
        return true
    }
}
