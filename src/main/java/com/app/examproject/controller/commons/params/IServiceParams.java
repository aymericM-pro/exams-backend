package com.app.examproject.controller.commons.params;

import com.app.examproject.errors.BusinessException;

/**
 * The base interface implemented by all service parameters classes.
 *
 * @param <T> the formalized service parameters object's type.
 */
public interface IServiceParams <T extends IServiceParams<T>>
{
    /**
     * Validate the parameters (setting some default values if needed), and
     * return the fully initialized and validated service parameters object.
     *
     * @return The formalized service parameters object.
     *
     * @throws BusinessException If the parameters validation failed.
     *
     * @see ServiceParams#validateAndSetup()
     */
    public T formalize() throws BusinessException;
}
