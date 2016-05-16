/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.phenix.pct;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ProgressV9 implements ProgressProcedures {
    private static final String BUNDLE_NAME = "com.phenix.pct.ProgressV9"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    @Override
    public String getCompileProcedure() {
        return "pct/v9/pctCompile.p";
    }

    @Override
    public String getIncrementalProcedure() {
        return "pct/v9/dump_inc.p";
    }

    @Override
    public String getDumpUsersProcedure() {
        return "pct/v9/dmpUsers.p";
    }

    @Override
    public String getLoadUsersProcedure() {
        return "pct/v9/loadUsers.p";
    }

    @Override
    public String getLoadSchemaProcedure() {
        return "pct/loadSch.p";
    }

    @Override
    public String getLoadMultipleTablesDataProcedure() {
        return "pct/loadData1.p";
    }

    @Override
    public String getLoadSingleTableDataProcedure() {
        return "pct/v9/loadData2.p";
    }

    @Override
    public boolean needRedirector() {
        return false;
    }

    @Override
    public String getInitString() {
        return getString("ProgressV9.0"); //$NON-NLS-1$
    }

    @Override
    public String getConnectString() {
        return getString("ProgressV9.1"); //$NON-NLS-1$
    }

    @Override
    public String getAliasString() {
        return getString("ProgressV9.2"); //$NON-NLS-1$
    }

    @Override
    public String getDBAliasString() {
        return getString("ProgressV9.15"); //$NON-NLS-1$
    }

    public String getPropathString() {
        return getString("ProgressV9.3"); //$NON-NLS-1$
    }

    @Override
    public String getSuperInitString() {
        return getString("ProgressV9.13");
    }

    @Override
    public String getRunString() {
        return getString("ProgressV9.4"); //$NON-NLS-1$
    }

    @Override
    public String getReturnProc() {
        return getString("ProgressV9.5"); //$NON-NLS-1$
    }

    @Override
    public String getParameterString() {
        return getString("ProgressV9.6"); //$NON-NLS-1$
    }

    @Override
    public String getAfterRun() {
        return getString("ProgressV9.10"); //$NON-NLS-1$
    }

    @Override
    public String getOutputParameterCall() {
        return getString("ProgressV9.11"); //$NON-NLS-1$
    }

    @Override
    public String getOutputParameterDeclaration() {
        return getString("ProgressV9.8"); //$NON-NLS-1$
    }

    @Override
    public String getOutputParameterProc() {
        return getString("ProgressV9.9"); //$NON-NLS-1$
    }

    @Override
    public String getQuit() {
        return getString("ProgressV9.12"); //$NON-NLS-1$
    }

    @Override
    public String getCallbackString() {
        return "";
    }

    public String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
