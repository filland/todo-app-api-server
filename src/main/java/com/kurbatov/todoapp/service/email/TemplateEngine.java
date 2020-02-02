package com.kurbatov.todoapp.service.email;

import java.util.Map;

/**
 * Base interface for template engines
 */
public interface TemplateEngine {

    /**
     * Populates the template with provided data set
     *
     * @param template Template name
     * @param data     Dataset to populate the template
     * @return Populated template
     */
    String merge(String template, Map<?, ?> data);

}
