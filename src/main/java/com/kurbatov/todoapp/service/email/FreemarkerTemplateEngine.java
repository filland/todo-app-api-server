package com.kurbatov.todoapp.service.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Component
public class FreemarkerTemplateEngine implements TemplateEngine {

    @Autowired
    private Configuration configuration;

    @Override
    public String merge(String template, Map<?, ?> data) {
        try (StringWriter writer = new StringWriter()) {
            Template tmpl = configuration.getTemplate(template);
            tmpl.process(data, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("Unable to process template '" + template + "'", e);
        }
    }
}
