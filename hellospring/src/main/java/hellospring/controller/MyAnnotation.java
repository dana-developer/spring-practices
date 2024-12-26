package hellospring.controller;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS) // source는 컴파일되면 떼어버린다. class는 컴파일해도 남아있지만, 코드가 로딩될 때 떼어버린다.
@Target({ TYPE, FIELD, METHOD, PARAMETER })
public @interface MyAnnotation {

}
