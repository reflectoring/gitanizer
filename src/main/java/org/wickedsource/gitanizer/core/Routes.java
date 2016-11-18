package org.wickedsource.gitanizer.core;

/**
 * Lists the paths to all HTTP endpoints.
 */
public class Routes {

    private Routes() {

    }

    public static final String ROOT = "/";

    public static final String LIST_MIRRORS = "/mirrors/list";

    public static final String CREATE_MIRROR = "/mirrors/create";

    public static final String EDIT_MIRROR = "/mirrors/{id}/edit";

    public static final String redirect(String route) {
        return "redirect:" + route;
    }

}
