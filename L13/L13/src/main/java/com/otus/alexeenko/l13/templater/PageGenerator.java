package com.otus.alexeenko.l13.templater;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Vsevolod on 26/06/2017.
 */
public class PageGenerator {
    private final ServletContext context;
    private final Configuration cfg;

    public PageGenerator(ServletContext context) {
        this.context = context;
        this.cfg = new Configuration(Configuration.getVersion());
        this.cfg.setServletContextForTemplateLoading(context, "/");
    }

    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();

        try {
            Template template = cfg.getTemplate(File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        return stream.toString();
    }

    public String getPage(String filename) {
        String page = "";

        try {
            page = new String(Files.readAllBytes(Paths.get(context.getRealPath(filename))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return page;
    }
}