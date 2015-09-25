package com.ilionx.nl.soap;

import freemarker.cache.TemplateLoader;

import java.io.*;

public class FreemarkerTemplateLoader implements TemplateLoader {

    public Object findTemplateSource(String name) throws IOException {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    public long getLastModified(Object templateSource) {
        return 0;
    }

    public Reader getReader(Object templateSource, String encoding) throws IOException {
        InputStream templateFile = (InputStream) templateSource;
        return new BufferedReader(new InputStreamReader(templateFile));
    }

    public void closeTemplateSource(Object templateSource) throws IOException {
        ((InputStream) templateSource).close();
    }
}
