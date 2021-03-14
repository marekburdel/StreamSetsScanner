package eu.profinit.manta.connector.streamsets.resolver;

import eu.profinit.manta.connector.streamsets.model.parser.IStageProcessor;
import eu.profinit.manta.connector.streamsets.resolver.model.parser.stage.StageType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mburdel
 */
public class StageTypeTest {

    @Test public void StageNameTest() {
        IStageProcessor stageProcessor = StageType
                .getStageProcessor("com_streamsets_pipeline_stage_origin_jdbc_JdbcDSource");
        Assert.assertNotNull(stageProcessor);
        Assert.assertEquals("JDBCQueryConsumerProcessor", stageProcessor.getClass().getSimpleName());
    }
}
