package com.yqh.marketing.basedevss.base.migrate;


import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.Initializable;
import com.yqh.marketing.basedevss.base.util.InitializersHandler;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Collections;

public class Migrate {

    public static void migrate(final RecordInput in, RecordOutput out1, final RecordMigrateHandler handler) {
        migrate(in, new RecordOutput[]{out1}, handler);
    }

    public static void migrate(final RecordInput in, RecordOutput out1, RecordOutput out2, final RecordMigrateHandler handler) {
        migrate(in, new RecordOutput[]{out1, out2}, handler);
    }

    public static void migrate(final RecordInput in, final RecordOutput[] out, final RecordMigrateHandler handler) {
        Validate.notNull(in);
        Validate.notNull(out);
        Validate.notNull(handler);

        ArrayList<Initializable> objs = new ArrayList<Initializable>();
        objs.add(in);
        Collections.addAll(objs, out);
        InitializersHandler.initAndDestroy(objs.toArray(new Initializable[objs.size()]), new InitializersHandler() {
            @Override
            public void handle(Initializable[] objs) {
                Record inRec;
                Record[] outRecs = new Record[out.length];
                for (int i = 0; i < outRecs.length; i++)
                    outRecs[i] = new Record();

                while ((inRec = in.input()) != null) {
                    for (Record outRec : outRecs)
                        outRec.clear();


                    try {
                        handler.handle(inRec, outRecs);
                    } catch (MigrateStopException e) {
                        break;
                    }

                    for (int i = 0; i < outRecs.length; i++) {
                        Record outRec = outRecs[i];
                        if (!outRec.isEmpty())
                            out[i].output(outRec);
                    }
                }
            }
        });
    }

}
