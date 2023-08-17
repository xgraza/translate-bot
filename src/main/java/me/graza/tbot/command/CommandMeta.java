package me.graza.tbot.command;

import me.graza.tbot.command.permission.Permission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author graza
 * @since 08/15/23
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMeta {
  String name();
  String description() default "No description was provided";
  Permission[] permissions() default Permission.EVERYONE;
}
