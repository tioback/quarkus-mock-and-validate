# Mockito and Validation interference

This project is an attempt to demonstrate how the use of Mockito's `InjectMock` and `InjectSpy` is affecting the correct behavior of Java Bean Validation.

---

From what I could tell, when either `InjectMock` or `InjectSpy` are used, the generated subclasses are called in an order that affects the Validation behaviour.

More specifically, at `validateParameters(T, java.lang.reflect.Executable, java.lang.Object[], java.lang.Class<?>...)`:

```java
// Dependency: hibernate-validator-6.2.0.Final.jar
package org.hibernate.validator.internal.engine;

public class ValidatorImpl {

    private <T> Set<ConstraintViolation<T>> validateParameters(T object, Executable executable, Object[] parameterValues, Class<?>... groups) {
        sanityCheckGroups(groups);

        @SuppressWarnings("unchecked")
        Class<T> rootBeanClass = object != null ? (Class<T>) object.getClass() : (Class<T>) executable.getDeclaringClass();
        BeanMetaData<T> rootBeanMetaData = beanMetaDataManager.getBeanMetaData(rootBeanClass);

        if (!rootBeanMetaData.hasConstraints()) {
            return Collections.emptySet();
        }

        ExecutableValidationContext<T> validationContext = getValidationContextBuilder().forValidateParameters(
                rootBeanClass,
                rootBeanMetaData,
                object,
                executable,
                parameterValues
        );

        ValidationOrder validationOrder = determineGroupValidationOrder(groups);

        validateParametersInContext(validationContext, parameterValues, validationOrder);

        return validationContext.getFailingConstraints();
    }
}
```

At the `rootBeanMetaData.hasConstraints()` check, when using Spies or Mocks, the variable is false.

After some debugging, I noticed that this happens because Spies and Mocks cause the use of `org.hibernate.validator.internal.metadata.PredefinedScopeBeanMetaDataManager.UninitializedBeanMetaData.hasConstraints` as the BeanMetadData implementation.
Which in turn led me to this:

```java
// Dependency: hibernate-validator-6.2.0.Final.jar
package org.hibernate.validator.internal.metadata;

public class PredefinedScopeBeanMetaDataManager implements BeanMetaDataManager {
    
    @Override
    public <T> BeanMetaData<T> getBeanMetaData(Class<T> beanClass) {
        Class<?> normalizedBeanClass = beanMetaDataClassNormalizer.normalize( beanClass );
        BeanMetaData<T> beanMetaData = (BeanMetaData<T>) beanMetaDataMap.get( normalizedBeanClass );
        if ( beanMetaData == null ) {
            // note that if at least one element of the hierarchy is constrained, the child classes should really be initialized
            // otherwise they will be considered unconstrained.
            beanMetaData = (BeanMetaData<T>) beanMetaDataMap.computeIfAbsent( normalizedBeanClass, UninitializedBeanMetaData::new );
        }
        return beanMetaData;
    }
}
```

That comment about child classes seems to be the reason this is happening.

Before I start working on a solution, I was wondering if someone has a word on that.