package io.github.northernlightgames.zigma.command;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Documented 
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandMethod {
	String name();
	String description();
	boolean mustBePlayer() default false;
	String permission();
	String defAllowed() default "op";
	String usage();
	String permissionMessage() default "Sorry, you don't have permission to that command";
	String[] aliases() default {};
}
