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
        if(tenantGuid){
            return "${username}: with ${id} and email: ${email} for tenant: ${tenantGuid} expires on ${dateExpired}"
        }
        return "${username}: with ${id} and email: ${email} expires on ${dateExpired}"
    }

}
