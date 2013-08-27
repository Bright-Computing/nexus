package com.bright.cmcall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class cmgetVersion {



                private int uniqueKey;
                private int oldLocalUniqueKey;
                private String revision;
                private Boolean modified;
                private Boolean toBeRemoved;
                private int databaseVersion;
                private String cmVersion;
                private String cmdaemonVersion;
                private String cmdaemonBuild;

                public int getUniqueKey() {
                        return uniqueKey;
                }
                public int getOldLocalUniqueKey() {
                        return oldLocalUniqueKey;
                }
                public String getRevision() {
                        return revision;
                }
                public Boolean getModified() {
                        return modified;
                }
                public Boolean getToBeRemoved() {
                        return toBeRemoved;
                }
                public int getDatabaseVersion() {
                        return databaseVersion;
                }
                public String getCmVersion() {
                        return cmVersion;
                }
                public String getCmdaemonVersion() {
                        return cmdaemonVersion;
                }
                public String getCmdaemonBuild() {
                        return cmdaemonBuild;
                }




}
