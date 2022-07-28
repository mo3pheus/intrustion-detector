package hungry.monkey.intrusion.detector.domain;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class IpDataTest {
    private IpData ipData;

    @Test
    public void testIpDataStringRep() {
        ipData = new IpData();
        ipData.setIpAddress("3.4.5.6");
        ipData.setCount(10);

        String ipDataString = "{\"ipAddress\":\"3.4.5.6\",\"id\":null,\"count\":10}";
        Assert.isTrue(ipData.toString().equals(ipDataString), "String representation is not as expected");
    }
}
