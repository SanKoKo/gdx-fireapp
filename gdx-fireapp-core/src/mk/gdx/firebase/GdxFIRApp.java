/*
 * Copyright 2017 mk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mk.gdx.firebase;

import mk.gdx.firebase.distributions.AppDistribution;

/**
 * Gets access to Firebase App API in multi-modules.
 *
 * @see AppDistribution
 * @see PlatformDistributor
 */
public class GdxFIRApp extends PlatformDistributor<AppDistribution> implements AppDistribution {

    private volatile static GdxFIRApp instance;

    /**
     * GdxFIRApp protected constructor.
     * <p>
     * Instance of this class should be getting by {@link #instance()}
     * <p>
     * {@link PlatformDistributor#PlatformDistributor()}
     */
    private GdxFIRApp() {
    }


    /**
     * @return Thread-safe singleton instance of this class.
     */
    public static GdxFIRApp instance() {
        GdxFIRApp result = instance;
        if (result == null) {
            synchronized (GdxFIRApp.class) {
                result = instance;
                if (result == null) {
                    instance = result = new GdxFIRApp();
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure() {
        platformObject.configure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getIOSClassName() {
        return "mk.gdx.firebase.ios.App";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAndroidClassName() {
        return "mk.gdx.firebase.android.App";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWebGLClassName() {
        return "mk.gdx.firebase.html.App";
    }
}
