package sogorae.billage.controller;

import java.util.Locale;

public enum AllowOrDeny {
    ALLOW, DENY;

    public boolean isAllow() {
        return this == ALLOW;
    }

    public static AllowOrDeny from(String value) {
        return valueOf(value.toUpperCase(Locale.ENGLISH));
    }
}
