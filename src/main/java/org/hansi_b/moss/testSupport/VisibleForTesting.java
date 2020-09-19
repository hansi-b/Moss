package org.hansi_b.moss.testSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an element as having increased visibility so it can be referenced from
 * tests.
 *
 * Duplicated from countless other VisibleForTesting's so I would not have to
 * drag an extra dependency in just for this.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface VisibleForTesting {
	// empty
}
