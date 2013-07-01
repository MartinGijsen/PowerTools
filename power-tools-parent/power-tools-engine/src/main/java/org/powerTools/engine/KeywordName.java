package org.powerTools.engine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention (RetentionPolicy.RUNTIME)
@Target ({ElementType.METHOD})
public @interface KeywordName {
	String value();
}