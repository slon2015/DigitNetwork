package org.digit.socnetworkrest.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class InvalidPrincipal: RuntimeException()

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class Unauthorized: RuntimeException()