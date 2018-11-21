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

package mk.gdx.firebase.html.auth;

import com.badlogic.gdx.utils.Timer;

import mk.gdx.firebase.auth.GdxFirebaseUser;
import mk.gdx.firebase.auth.UserInfo;
import mk.gdx.firebase.distributions.AuthDistribution;
import mk.gdx.firebase.functional.BiConsumer;
import mk.gdx.firebase.functional.Consumer;
import mk.gdx.firebase.html.firebase.ScriptRunner;
import mk.gdx.firebase.promises.FuturePromise;
import mk.gdx.firebase.promises.Promise;

/**
 * @see AuthDistribution
 */
public class Auth implements AuthDistribution {

    /**
     * {@inheritDoc}
     */
    @Override
    public GdxFirebaseUser getCurrentUser() {
        FirebaseUserJS userFromApi = AuthJS.firebaseUser();
        if (userFromApi.isNULL()) return null;
        UserInfo.Builder builder = new UserInfo.Builder();
        builder.setDisplayName(userFromApi.getDisplayName())
                .setPhotoUrl(userFromApi.getPhotoURL())
                .setProviderId(userFromApi.getProviderId())
                .setUid(userFromApi.getUID())
                .setIsEmailVerified(userFromApi.isEmailVerified())
                .setIsAnonymous(userFromApi.isAnonymous())
                .setEmail(userFromApi.getEmail());
        return GdxFirebaseUser.create(builder.build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Promise<GdxFirebaseUser> createUserWithEmailAndPassword(final String email, final char[] password) {
        return FuturePromise.of(new Consumer<FuturePromise<GdxFirebaseUser>>() {
            @Override
            public void accept(final FuturePromise<GdxFirebaseUser> promise) {
                final FuturePromise<GdxFirebaseUser> creationPromise = new FuturePromise<>();
                creationPromise.then(new Consumer<GdxFirebaseUser>() {
                    @Override
                    public void accept(GdxFirebaseUser gdxFirebaseUser) {
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                signInWithEmailAndPassword(email, password).then(new Consumer<GdxFirebaseUser>() {
                                    @Override
                                    public void accept(GdxFirebaseUser gdxFirebaseUser) {
                                        promise.doComplete(gdxFirebaseUser);
                                    }
                                }).fail(new BiConsumer<String, Throwable>() {
                                    @Override
                                    public void accept(String s, Throwable throwable) {
                                        promise.doFail(s, throwable);
                                    }
                                });
                            }
                        }, 0.5f);
                    }
                }).fail(new BiConsumer<String, Throwable>() {
                    @Override
                    public void accept(String s, Throwable throwable) {
                        promise.doFail(s, throwable);
                    }
                });
                ScriptRunner.firebaseScript(new Runnable() {
                    @Override
                    public void run() {
                        AuthJS.createUserWithEmailAndPassword(email, new String(password), creationPromise);
                    }
                });
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Promise<GdxFirebaseUser> signInWithEmailAndPassword(final String email, final char[] password) {
        return FuturePromise.of(new Consumer<FuturePromise<GdxFirebaseUser>>() {
            @Override
            public void accept(final FuturePromise<GdxFirebaseUser> promise) {
                ScriptRunner.firebaseScript(new Runnable() {
                    @Override
                    public void run() {
                        AuthJS.signInWithEmailAndPassword(email, new String(password), promise);
                    }
                });
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Promise<GdxFirebaseUser> signInWithToken(final String token) {
        return FuturePromise.of(new Consumer<FuturePromise<GdxFirebaseUser>>() {
            @Override
            public void accept(final FuturePromise<GdxFirebaseUser> promise) {
                ScriptRunner.firebaseScript(new Runnable() {
                    @Override
                    public void run() {
                        AuthJS.signInWithToken(token, promise);
                    }
                });
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Promise<GdxFirebaseUser> signInAnonymously() {
        return FuturePromise.of(new Consumer<FuturePromise<GdxFirebaseUser>>() {
            @Override
            public void accept(final FuturePromise<GdxFirebaseUser> promise) {
                ScriptRunner.firebaseScript(new Runnable() {
                    @Override
                    public void run() {
                        AuthJS.signInAnonymously(promise);
                    }
                });
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Promise<Void> signOut() {
        return FuturePromise.of(new Consumer<FuturePromise<Void>>() {
            @Override
            public void accept(final FuturePromise<Void> promise) {
                ScriptRunner.firebaseScript(new Runnable() {
                    @Override
                    public void run() {
                        AuthJS.signOut(promise);
                    }
                });
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Promise<Void> sendPasswordResetEmail(final String email) {
        return FuturePromise.of(new Consumer<FuturePromise<Void>>() {
            @Override
            public void accept(final FuturePromise<Void> promise) {
                ScriptRunner.firebaseScript(new Runnable() {
                    @Override
                    public void run() {
                        AuthJS.sendPasswordResetEmail(email, promise);
                    }
                });
            }
        });
    }

}
