package com.ruomm.javax.encryptx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.security.Provider;
import java.security.Security;

public class BCProviderLoadUtil {
    private final static Log log = LogFactory.getLog(BCProviderLoadUtil.class);

    public static void loadProvider() {
        try {
            Provider bcProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
            if (null == Security.getProvider(bcProvider.getName())) {
                Security.addProvider(bcProvider);
            } else {
                bcProvider = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:BCProviderLoad.loadProvider->BouncyCastleProvider install error!", e);
        }

    }
}
