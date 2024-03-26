package com.behl.encryptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation indicating the target field carries sensitive information.
 * This annotation can only be applied to fields of type {@code String}.
 * 
 * Fields annotated with `@Encryptable` are identified by
 * {@link com.behl.encryptor.utility.FieldEncryptionManager} as targets for
 * encryption and decryption operations, allowing for automated and secure
 * handling of sensitive data.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Encryptable {
	
}
