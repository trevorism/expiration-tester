package com.trevorism.model

class ManagedCertificate {

    String id
    String category
    String wildcard
    String gcpProject
    String appEngineCertificateId
    String probeHost
    String notAfter
    String serial
    boolean enabled = true
    String lastRotationRunId
    String lastRotatedAt
    String lastOutcome

    @Override
    String toString() {
        return "${wildcard}"
    }

}
