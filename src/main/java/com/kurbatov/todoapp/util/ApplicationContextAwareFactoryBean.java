
package com.kurbatov.todoapp.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.function.Supplier;

/**
 * {@link FactoryBean} with access to {@link ApplicationContext} with lazy
 * initialization
 *
 * @param <T> - type of bean
 */
public abstract class ApplicationContextAwareFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware, InitializingBean {

    /**
     * Application context holder
     */
    private ApplicationContext applicationContext;

    /**
     * Supplier of bean to be created
     */
    private Supplier<T> beanSupplier;

	private Supplier<T> cachedSupplier;

    /**
     * Whether is bean to be creates going to be singleton
     */
    private boolean singleton = true;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public T getObject() {
        return beanSupplier.get();
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    /**
     * Instantiates supplier for bean to be created. This makes possible
     * lazy-initialization
     */
    @Override
    public void afterPropertiesSet() {
        Supplier<T> supplier = this::createInstance;

        if (isSingleton()) {
        	if (cachedSupplier == null) {
				cachedSupplier = supplier;
			}
        	this.beanSupplier = cachedSupplier;
		} else {
        	this.beanSupplier = supplier;
		}
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Template method that subclasses must override to construct the object
     * returned by this factory.
     * <p>
     * Invoked on initialization of this FactoryBean in case of a singleton;
     * else, on each {@link #getObject()} call.
     *
     * @return the object returned by this factory
     * @see #getObject()
     */
    protected abstract T createInstance();
}
