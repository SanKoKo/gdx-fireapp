/*
 * Copyright 2018 mk
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

package mk.gdx.firebase.html.database.queries;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.util.Map;

import mk.gdx.firebase.callbacks.CompleteCallback;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.database.validators.UpdateChildrenValidator;
import mk.gdx.firebase.html.database.Database;
import mk.gdx.firebase.html.database.DatabaseReference;
import mk.gdx.firebase.html.firebase.ScriptRunner;

@PrepareForTest({ScriptRunner.class, DatabaseReference.class})
public class UpdateChildrenQueryTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(DatabaseReference.class);
        PowerMockito.mockStatic(ScriptRunner.class);
        PowerMockito.when(ScriptRunner.class, "firebaseScript", Mockito.any(Runnable.class)).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Runnable) invocation.getArgument(0)).run();
                return null;
            }
        });
    }

    @Test(expected = UnsatisfiedLinkError.class)
    public void runJS() {
        // Given
        Database database = Mockito.spy(Database.class);
        database.inReference("/test");
        UpdateChildrenQuery query = new UpdateChildrenQuery(database);

        // When
        ((UpdateChildrenQuery) query.withArgs(Mockito.mock(Map.class))).execute();

        // Then
        Assert.fail("Native method should be run");
    }


    @Test(expected = UnsatisfiedLinkError.class)
    public void runJS2() {
        // Given
        Database database = Mockito.spy(Database.class);
        database.inReference("/test");
        UpdateChildrenQuery query = new UpdateChildrenQuery(database);
        CompleteCallback callback = Mockito.mock(CompleteCallback.class);

        // When
        ((UpdateChildrenQuery) query.withArgs(Mockito.mock(Map.class), callback)).execute();

        // Then
        Assert.fail("Native method should be run");
    }

    @Test
    public void createArgumentsValidator() {
        // Given
        UpdateChildrenQuery query = new UpdateChildrenQuery(Mockito.mock(Database.class));

        // When
        ArgumentsValidator argumentsValidator = query.createArgumentsValidator();

        // Then
        Assert.assertTrue(argumentsValidator instanceof UpdateChildrenValidator);
    }
}