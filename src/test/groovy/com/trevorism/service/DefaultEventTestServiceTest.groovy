package com.trevorism.service

import com.trevorism.https.SecureHttpClient
import org.junit.jupiter.api.Test

class DefaultEventTestServiceTest {

    @Test
    void testEnsureUsersNotExpiring() {
        DefaultEventTestService service = new DefaultEventTestService([get: { x -> "[]" }, post: { x,y -> "yes" }, delete: { x -> "{}"}] as SecureHttpClient)
        assert service.ensureUsersNotExpiring()

    }

    @Test
    void testEnsureAppsNotExpiring() {
        DefaultEventTestService service = new DefaultEventTestService([get: { x -> "[]" }, post: { x,y -> "yes" }, delete: { x -> "{}"}] as SecureHttpClient)
        assert service.ensureAppsNotExpiring()
    }
}
