package com.trevorism.service

import com.trevorism.https.SecureHttpClient
import org.junit.jupiter.api.Test

class DefaultCertsTestServiceTest {

    @Test
    void testEnsureCertsNotExpiring() {
        DefaultCertsTestService service = new DefaultCertsTestService([get: { x -> "[]" }, post: { x,y -> "yes" }, delete: { x -> "{}"}] as SecureHttpClient)
        assert service.ensureCertsNotExpiring()
    }

}
