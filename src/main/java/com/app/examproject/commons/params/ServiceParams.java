package com.app.examproject.commons.params;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.ParamError;
import org.apache.commons.lang3.StringUtils;

public abstract class ServiceParams<T extends ServiceParams<T>>
        implements IServiceParams<T> {

    @Override
    @SuppressWarnings("unchecked")
    public final T formalize() throws BusinessException {
        validateAndSetup();
        return (T) this;
    }

    protected abstract void validateAndSetup() throws BusinessException;

    protected <V> V requireNotNull(V value) {
        if (value == null) {
            throw new BusinessException(ParamError.MISSING);
        }
        return value;
    }

    protected String requireNotBlank(String value) {
        if (StringUtils.isBlank(value)) {
            throw new BusinessException(ParamError.INVALID);
        }
        return value;
    }

    protected Integer parseInt(
            String value,
            boolean nullable,
            Integer min,
            Integer max
    ) {
        if (StringUtils.isBlank(value)) {
            if (nullable) {
                return null;
            }
            throw new BusinessException(ParamError.MISSING);
        }

        final int parsed;
        try {
            parsed = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BusinessException(ParamError.INVALID);
        }

        if (min != null && parsed < min) {
            throw new BusinessException(ParamError.INVALID);
        }

        if (max != null && parsed > max) {
            throw new BusinessException(ParamError.INVALID);
        }

        return parsed;
    }
}
