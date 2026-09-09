package com.trevorism.bean


import com.trevorism.https.SecureHttpClient
import com.trevorism.https.SecureHttpClientBase
import jakarta.inject.Named

@jakarta.inject.Singleton
@Named("eventTesterSecureHttpClient")
class EventTesterSecureHttpClient extends SecureHttpClientBase implements SecureHttpClient{
    EventTesterSecureHttpClient() {
        super(new ObtainTokenWithAudience())
    }
}
