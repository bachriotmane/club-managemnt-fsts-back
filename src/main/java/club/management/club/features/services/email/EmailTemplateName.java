package club.management.club.features.services.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"),
    FORGOT_PASSWORD("forgot_password"),
    ACCOUNT_CREATION_SUCCESS("creation_account"),
    RESET_PASSWORD("reset_password");


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
