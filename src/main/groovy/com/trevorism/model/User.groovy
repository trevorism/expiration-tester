package com.trevorism.model


class User {

    String id
    String username
    String email

    boolean admin
    boolean active

    String tenantGuid
    String permissions

    Date dateCreated
    Date dateExpired

    @Override
    String toString() {
        return "${username}: with ${id} and email: ${email} expires on ${dateExpired}"
    }

}
