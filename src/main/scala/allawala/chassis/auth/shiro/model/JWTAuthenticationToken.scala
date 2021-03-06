package allawala.chassis.auth.shiro.model

import allawala.chassis.auth.model.{JWTSubject, PrincipalType}
import org.apache.shiro.authc.AuthenticationToken

case class Principal(principalType: PrincipalType, principal: String)

/*
  The actual jwt token itself is set as the credential
 */
case class JWTAuthenticationToken(subject: JWTSubject) extends AuthenticationToken {
  override def getPrincipal: AnyRef = Principal(subject.principalType, subject.principal)
  override def getCredentials: AnyRef = subject.credentials
}
