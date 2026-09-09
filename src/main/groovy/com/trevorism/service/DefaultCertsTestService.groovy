package com.trevorism.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trevorism.AlertClient
import com.trevorism.TestErrorClient
import com.trevorism.data.FastDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.https.AppClientSecureHttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.model.Alert
import com.trevorism.model.ManagedCertificate
import com.trevorism.model.TestError
import jakarta.inject.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.time.Instant
import java.time.temporal.ChronoUnit

@jakarta.inject.Singleton
class DefaultCertsTestService implements CertsTestService {

    private static final Logger log = LoggerFactory.getLogger(DefaultCertsTestService.class.name)

    private Repository<ManagedCertificate> managedCertificateRepository
    private AlertClient alertClient
    private TestErrorClient testErrorClient

    DefaultCertsTestService(@Named("appSecureHttpClient") SecureHttpClient secureHttpClient) {
        this.managedCertificateRepository = new FastDatastoreRepository<>(ManagedCertificate, secureHttpClient)
        this.alertClient = new AlertClient(secureHttpClient)
        this.testErrorClient = new TestErrorClient(secureHttpClient)
    }

    @Override
    boolean ensureCertsNotExpiring() {
        List<ManagedCertificate> certs = managedCertificateRepository.list()

        List<ManagedCertificate> soonToExpireCerts = certs.findAll { ManagedCertificate cert ->
            cert.enabled && cert.notAfter && parseNotAfter(cert.notAfter).isBefore(Instant.now().plus(84, ChronoUnit.DAYS))
        }

        List<ManagedCertificate> expiredCerts = certs.findAll { ManagedCertificate cert ->
            cert.enabled && cert.notAfter && parseNotAfter(cert.notAfter).isBefore(Instant.now())

        }

        if (soonToExpireCerts) {
            Alert alert = new Alert([subject: "Certs will expire soon",
                                     body   : "The following certs are set to expire soon: " + soonToExpireCerts.collect { it.toString() }.join(",\n")])
            this.alertClient.sendAlert(alert)
        }

        if (expiredCerts) {
            TestError testError = new TestError([source : "expiration-tester",
                                                 message: "The following certs have expired: " + expiredCerts.collect { it.toString() }.join(",\n"),
                                                 details: [kind: "web"]])
            this.testErrorClient.addTestError(testError)
        }

        return !expiredCerts
    }

    private static Instant parseNotAfter(String notAfter) {
        Instant.parse(notAfter)
    }
}
