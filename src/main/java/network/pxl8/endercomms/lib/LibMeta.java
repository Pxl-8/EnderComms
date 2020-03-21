package network.pxl8.endercomms.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LibMeta {
    public static final String MOD_ID = "endercomms";
    public static final String VERSION = "1.0.0";

    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    public static final String CLIENT_PROXY = "network.pxl8.endercomms.proxy.ClientProxy";
    public static final String SERVER_PROXY = "network.pxl8.endercomms.proxy.CommonProxy";
}
