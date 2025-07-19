package com.trevorism.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trevorism.AlertClient
import com.trevorism.TestErrorClient
import com.trevorism.data.FastDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.https.SecureHttpClient
import com.trevorism.model.Alert
import com.trevorism.model.App
import com.trevorism.model.TestError
import com.trevorism.model.User
import jakarta.inject.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.time.Instant
import java.time.temporal.ChronoUnit

@jakarta.inject.Singleton
class DefaultEventTestService implements EventTestService {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventTestService.class.name)

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
    private SecureHttpClient secureHttpClient
    private Repository<User> userRepository
    private Repository<App> appRepository
    private AlertClient alertClient
    private TestErrorClient testErrorClient

    DefaultEventTestService(@Named("eventTesterSecureHttpClient") SecureHttpClient secureHttpClient) {
        this.secureHttpClient = secureHttpClient
        this.userRepository = new FastDatastoreRepository<>(User, secureHttpClient)
        this.appRepository = new FastDatastoreRepository<>(App, secureHttpClient)
        this.alertClient = new AlertClient(secureHttpClient)
        this.testErrorClient = new TestErrorClient(secureHttpClient)
    }

    @Override
    boolean ensureUsersNotExpiring() {
        List<User> users = userRepository.list()

        List<User> soonToExpireUsers = users.findAll { User user ->
            user.isActive() && user.dateExpired && user.dateExpired.before(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
        }

        List<User> expiredUsers = users.findAll { User user ->
            user.isActive() && user.dateExpired && user.dateExpired.before(Date.from(Instant.now()))
        }

        if (soonToExpireUsers) {
            Alert alert = new Alert([subject: "Users will expire soon",
                                     body   : "The following users are set to expire soon: " + soonToExpireUsers.collect { it.toString() }.join(",\n")])
            this.alertClient.sendAlert(alert)
        }

        if (expiredUsers) {
            TestError testError = new TestError([source : "expiration-tester",
                                                 message: "The following users have expired: " + expiredUsers.collect { it.toString() }.join(",\n"),
                                                 details: [kind:"web"]])
            this.testErrorClient.addTestError(testError)
        }

        return !expiredUsers
    }

    @Override
    boolean ensureAppsNotExpiring() {
        List<App> apps = appRepository.list()

        List<App> soonToExpireApps = apps.findAll { App app ->
            app.active && app.dateExpired && app.dateExpired.before(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
        }

        List<App> expiredApps = apps.findAll { App app ->
            app.active && app.dateExpired && app.dateExpired.before(Date.from(Instant.now()))
        }

        if (soonToExpireApps) {
            Alert alert = new Alert([subject: "Apps will expire soon",
                                     body   : "The following apps are set to expire soon: " + soonToExpireApps.collect { it.toString() }.join(",\n")])
            this.alertClient.sendAlert(alert)
        }
        if (expiredApps) {
            TestError testError = new TestError([source : "expiration-tester",
                                                 message: "The following apps have expired: " + expiredApps.collect { it.toString() }.join(",\n"),
                                                 details: [kind:"web"]])
            this.testErrorClient.addTestError(testError)
        }

        return !expiredApps
    }
}
