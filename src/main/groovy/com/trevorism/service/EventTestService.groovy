package com.trevorism.service

import com.trevorism.model.TestSuite

interface EventTestService {

    boolean ensureUsersNotExpiring()
    boolean ensureAppsNotExpiring()
}