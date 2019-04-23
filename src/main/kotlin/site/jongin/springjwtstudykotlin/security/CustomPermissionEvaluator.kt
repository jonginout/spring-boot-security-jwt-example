package site.jongin.springjwtstudykotlin.security

import org.slf4j.LoggerFactory
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import java.io.Serializable

class CustomPermissionEvaluator : PermissionEvaluator {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun hasPermission(
        authentication: Authentication?,
        targetDomainObject: Any?,
        permission: Any?
    ): Boolean {
        log.info("hasPermission1 : $targetDomainObject, $permission")

        if (authentication == null || targetDomainObject == null || permission !is String) {
            return false
        }

        return this._hasPrivilege(authentication, targetDomainObject::class.simpleName!!, permission)
    }

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        log.info("hasPermission1 : $targetId, $targetType, $permission")

        if (authentication == null || targetType == null || permission !is String) {
            return false
        }

        return this._hasPrivilege(authentication, targetType, permission)
    }

    private fun _hasPrivilege(auth: Authentication, targetType: String, permission: String): Boolean {
        log.info("hasPrivilege : $targetType, $permission")

        auth.authorities.forEach {
            if (
                it.authority.startsWith(targetType, true) &&
                it.authority.contains(permission, true)
            )
            {
                return true
            }
        }

        return false
    }
}
