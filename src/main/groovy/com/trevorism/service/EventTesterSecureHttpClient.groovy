package com.trevorism.service

import com.trevorism.https.AppClientSecureHttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.https.SecureHttpClientBase
import com.trevorism.https.token.ObtainTokenStrategy
import jakarta.inject.Named

@jakarta.inject.Singleton
@Named("eventTesterSecureHttpClient")
class EventTesterSecureHttpClient extends SecureHttpClientBase implements SecureHttpClient{
    EventTesterSecureHttpClient() {
        super(new ObtainTokenWithAudience())
    }
}
