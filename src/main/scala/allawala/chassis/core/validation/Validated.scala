package allawala.chassis.core.validation

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, ValidatedNel}

trait ValidateRequired {
  protected def required[T](name: String, value: Option[T]): ValidatedNel[ValidationError, T] = value match {
    case Some(v) => Valid(v)
    case None => Invalid(NonEmptyList.of(RequiredField(name)))
  }

  protected def requiredString(name: String, value: Option[String]): ValidatedNel[ValidationError, String] =
    required[String](name, value)
}

trait ValidateUnexpected {
  protected def unexpected[T](name: String, value: Option[T]): ValidatedNel[ValidationError, Option[T]] = value match {
    case Some(v) => Invalid(NonEmptyList.of(UnexpectedField(name)))
    case None => Valid(value)
  }

  protected def unexpectedString(name: String, value: Option[String]): ValidatedNel[ValidationError, Option[String]] =
    unexpected[String](name, value)
}

trait ValidateNotBlank {
  protected def notBlank(name: String, value: String): ValidatedNel[ValidationError, String] =
    if (value.trim.isEmpty) Invalid(NonEmptyList.of(NotBlank(name))) else Valid(value)

  protected def notBlank(name: String, value: Option[String]): ValidatedNel[ValidationError, Option[String]] = value match {
    case Some(v) => notBlank(name, v).map(_ => value)
    case None => Invalid(NonEmptyList.of(NotBlank(name)))
  }
}

trait ValidateMinLength {
  protected def minLength(name: String, value: String, min: Int): ValidatedNel[ValidationError, String] =
    if (value.trim.length < min) Invalid(NonEmptyList.of(MinLength(name, min))) else Valid(value)

  protected def minLength(name: String, value: Option[String], min: Int): ValidatedNel[ValidationError, Option[String]] = value match {
    case Some(v) => minLength(name, v, min).map(_ => value)
    case None => Invalid(NonEmptyList.of(MinLength(name, min)))
  }
}

trait ValidateMaxLength {
  protected def maxLength(name: String, value: String, max: Int): ValidatedNel[ValidationError, String] =
    if (value.trim.length > max) Invalid(NonEmptyList.of(MaxLength(name, max))) else Valid(value)

  protected def maxLength(name: String, value: Option[String], max: Int): ValidatedNel[ValidationError, Option[String]] = value match {
    case Some(v) => maxLength(name, v, max).map(_ => value)
    case None => Invalid(NonEmptyList.of(MaxLength(name, max)))
  }
}

trait Validate
  extends ValidateRequired
    with ValidateUnexpected
    with ValidateNotBlank
    with ValidateMinLength
    with ValidateMaxLength