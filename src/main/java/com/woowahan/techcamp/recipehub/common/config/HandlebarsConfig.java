package com.woowahan.techcamp.recipehub.common.config;

import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.context.annotation.Configuration;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;

@Configuration
public class HandlebarsConfig {

    @HandlebarsHelper
    public class EqualsHelper {
        public Object eq(Object a, final Options options) throws IOException {
            Object b = options.param(0, null);
            boolean result = new EqualsBuilder().append(a, b).isEquals();
            if (options.tagType == TagType.SECTION) {
                return result ? options.fn() : options.inverse();
            }
            return result
                    ? options.hash("yes", true)
                    : options.hash("no", false);
        }
    }
}
