package com.amalto.core.objects.configurationinfo.localutil;

import java.util.Properties;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.talend.mdm.commmon.util.core.MDMConfiguration;

import com.amalto.core.initdb.InitDBUtil;
import com.amalto.core.migration.AbstractMigrationTask;
import com.amalto.core.migration.MigrationRepository;
import com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJO;
import com.amalto.core.objects.configurationinfo.ejb.ConfigurationInfoPOJOPK;
import com.amalto.core.objects.configurationinfo.ejb.local.ConfigurationInfoCtrlLocal;
import com.amalto.core.server.ConfigurationInfo;
import com.amalto.core.util.Version;
import com.amalto.core.util.XtentisException;

/**
 * Performs the core upgrades
 */
public class CoreUpgrades {

    private static final Logger LOGGER = Logger.getLogger(CoreUpgrades.class);

    public CoreUpgrades() {
    }

    public static void autoUpgrade(ConfigurationInfo ctrl) throws XtentisException, NamingException, CreateException {
        // We must make sure we use a Class NOT present in z.com.amalto.core.jar
        ConfigurationInfoPOJO previousCoreConf = getPreviousCoreConfigurationInfo(ctrl);
        Version thisVersion = Version.getVersion(ConfigurationInfoCtrlLocal.class);
        if (previousCoreConf.getMajor() != -1) {
            LOGGER.info("Last run Core version was " + previousCoreConf.getVersionString() + "...");
        }
        LOGGER.info("This Core version is " + thisVersion.toString() + "...");
        boolean forceUpgrade = "true".equals(MDMConfiguration.getConfiguration()
                .getProperty("system.data.force.upgrade", "false"));
        boolean isNeedUpgrade = needUpgrade(previousCoreConf.getMajor(), previousCoreConf.getMinor(),
                previousCoreConf.getRevision(), thisVersion.getMajor(), thisVersion.getMinor(), thisVersion.getRevision());
        if (forceUpgrade || isNeedUpgrade) {
            // reset
            if (isNeedUpgrade) {
                ConfigurationHelper.removeCluster(null, AbstractMigrationTask.CLUSTER_MIGRATION);
                LOGGER.info("Reset migration history records...");
                if (forceUpgrade) {
                    MDMConfiguration.getConfiguration().setProperty("cluster_override", "true");
                }
                InitDBUtil.initDB();
            }
            // upgrading
            upgradeFrom(previousCoreConf.getMajor(), previousCoreConf.getMinor(), previousCoreConf.getRevision(), forceUpgrade);
        } else {
            LOGGER.info("No core upgrade needed...");
        }
        // update saved core conf
        previousCoreConf.setMajor(thisVersion.getMajor());
        previousCoreConf.setMinor(thisVersion.getMinor());
        previousCoreConf.setRevision(thisVersion.getRevision());
        previousCoreConf.setBuild(thisVersion.getBuild());
        previousCoreConf.setReleaseNote(thisVersion.getDescription());
        previousCoreConf.setDate(thisVersion.getDate());
        ctrl.putConfigurationInfo(previousCoreConf);
    }

    /**
     * Returns the previously run Core Configuration
     * 
     * @return a {@link ConfigurationInfoPOJO} cntainin the core Configuration
     */
    private static ConfigurationInfoPOJO getPreviousCoreConfigurationInfo(ConfigurationInfo ctrl) throws XtentisException {
        ConfigurationInfoPOJO coreConfigurationInfo = ctrl.existsConfigurationInfo(new ConfigurationInfoPOJOPK("Core"));
        if (coreConfigurationInfo == null) {
            coreConfigurationInfo = new ConfigurationInfoPOJO("Core");
            coreConfigurationInfo.setMajor(-1);
            coreConfigurationInfo.setMinor(0);
            coreConfigurationInfo.setRevision(0);
            coreConfigurationInfo.setReleaseNote("");
            try {
                Properties properties = MDMConfiguration.getConfiguration(true);
                Set<Object> keys = properties.keySet();
                for (Object key1 : keys) {
                    String key = (String) key1;
                    coreConfigurationInfo.setProperty(key, properties.getProperty(key));
                }
            } catch (Exception e) {
                String err = "Unable to read mdm.conf and assign the properties to the core" + e.getClass().getName() + ": "
                        + e.getMessage();
                LOGGER.error(err, e);
                throw new XtentisException(err);
            }
        }
        return coreConfigurationInfo;
    }

    private static void upgradeFrom(int previousMajor, int previousMinor, int previousRevision, boolean forceupgrade)
            throws XtentisException {
        // execute migration task
        MigrationRepository.getInstance().connect(forceupgrade);
        LOGGER.info("Done upgrade from " + previousMajor + "." + previousMinor + "." + previousRevision + " to current version. ");
        LOGGER.info("Core upgrades completed. ");
    }

    private static boolean needUpgrade(int previousMajor, int previousMinor, int previousRevision, int major, int minor,
            int revision) {
        return (major > previousMajor) || ((major == previousMajor) && (minor > previousMinor))
                || ((major == previousMajor) && (minor == previousMinor) && (revision > previousRevision));
    }
}
