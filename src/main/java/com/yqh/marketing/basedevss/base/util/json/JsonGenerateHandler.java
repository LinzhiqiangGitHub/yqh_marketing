package com.yqh.marketing.basedevss.base.util.json;


import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;

public interface JsonGenerateHandler {
    void generate(JsonGenerator jg) throws IOException;
}
