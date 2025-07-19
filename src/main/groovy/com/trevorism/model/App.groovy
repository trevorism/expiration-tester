package com.trevorism.model

import groovy.transform.ToString

@ToString
class App {

    static App NULL_APP = new App()

    String id
    String appName
    String clientId

    List<String> replyUrls
    List<String> logoutUrls

    String tenantGuid
    String permissions

    boolean active
    Date dateCreated
    Date dateExpired

    @Override
    String toString() {
        return "${appName}: with ${id} and clientId: ${clientId} expires on ${dateExpired}"
    }
}
