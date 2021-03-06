package org.kohsuke.args4j.spi;

import org.kohsuke.args4j.CmdLineParser;

import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import org.kohsuke.args4j.Argument;

/**
 * Factory of {@link Setter}s.
 *
 * @author Kohsuke Kawaguchi
 */
public class Setters {
    
    private Setters() {
        // no instance allowed
    }
    
    public static Setter create(CmdLineParser parser, AccessibleObject fieldOrMethod, Object bean) {
        if(fieldOrMethod instanceof Method) {

            Argument a = ((Method) fieldOrMethod).getAnnotation(Argument.class);

            boolean isMultiValued = (a == null) ? true : a.multiValued();
            
            return new MethodSetter(parser, bean, (Method) fieldOrMethod, isMultiValued);
        } else {
            return create((Field) fieldOrMethod, bean);
        }
    }

    public static Setter create(Field f, Object bean) {
        if(f.getType().isArray())
            return new ArrayFieldSetter(bean,f);
        if(List.class.isAssignableFrom(f.getType()))
            return new MultiValueFieldSetter(bean,f);
        else
            return new FieldSetter(bean,f);
    }
}
