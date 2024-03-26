package com.behl.encryptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation indicating the target field represents an encrypted data
 * key. This annotation must be applied to field of type {@code String}.
 * 
 * This is used by {@link com.behl.encryptor.utility.FieldEncryptionManager}
 * component to facilitate encryption and decryption operations.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptedDataKey {

}
