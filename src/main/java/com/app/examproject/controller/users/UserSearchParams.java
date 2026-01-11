package com.app.examproject.controller.users;

import com.app.examproject.domains.entities.Role;
import com.app.examproject.errors.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import com.app.examproject.controller.commons.params.ServiceParams;
import com.app.examproject.errors.errors.ParamError;


@Getter
@Setter
public class UserSearchParams extends ServiceParams<UserSearchParams> {

    private String role;
    private String firstname;

    @Override
    protected void validateAndSetup() throws BusinessException {
        if (firstname != null && firstname.length() < 2) {
            throw new BusinessException(ParamError.INVALID);
        }
    }

    public Role getRoleEnum() {
        return role != null ? Role.valueOf(role) : null;
    }
}