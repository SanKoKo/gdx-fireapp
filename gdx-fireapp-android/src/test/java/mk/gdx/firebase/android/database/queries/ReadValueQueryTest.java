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

package mk.gdx.firebase.android.database.queries;

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import mk.gdx.firebase.android.AndroidContextTest;
import mk.gdx.firebase.android.database.Database;
import mk.gdx.firebase.android.database.resolvers.DataCallbackOnDataResolver;
import mk.gdx.firebase.callbacks.DataCallback;
import mk.gdx.firebase.database.pojos.OrderByClause;

@PrepareForTest({GdxNativesLoader.class, DataCallbackOnDataResolver.class, FirebaseDatabase.class})
public class ReadValueQueryTest extends AndroidContextTest {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void setup() throws Exception {
        super.setup();
        PowerMockito.mockStatic(DataCallbackOnDataResolver.class);
        PowerMockito.mockStatic(FirebaseDatabase.class);
        firebaseDatabase = PowerMockito.mock(FirebaseDatabase.class);
        Mockito.when(FirebaseDatabase.getInstance()).thenReturn(firebaseDatabase);
        databaseReference = Mockito.mock(DatabaseReference.class);
        Mockito.when(firebaseDatabase.getReference(Mockito.anyString())).thenReturn(databaseReference);
    }

    @Test
    public void run_ok() {
        // Given
        Database database = Mockito.spy(Database.class);
        final DataSnapshot dataSnapshot = Mockito.mock(DataSnapshot.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ValueEventListener) invocation.getArgument(0)).onDataChange(dataSnapshot);
                return null;
            }
        }).when(databaseReference).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));
        database.inReference("test");
        ReadValueQuery query = new ReadValueQuery(database);

        // When
        ((ReadValueQuery) query.withArgs(String.class, Mockito.mock(DataCallback.class))).execute();

        // Then
        PowerMockito.verifyStatic(DataCallbackOnDataResolver.class);
        DataCallbackOnDataResolver.resolve(Mockito.any(Class.class), Mockito.nullable(OrderByClause.class), Mockito.nullable(DataSnapshot.class), Mockito.any(DataCallback.class));
    }

    @Test
    public void run_fail() {
        // Given
        Database database = Mockito.spy(Database.class);
        final DatabaseError databaseError = Mockito.mock(DatabaseError.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ValueEventListener) invocation.getArgument(0)).onCancelled(databaseError);
                return null;
            }
        }).when(databaseReference).addListenerForSingleValueEvent(Mockito.any(ValueEventListener.class));
        database.inReference("test");
        ReadValueQuery query = new ReadValueQuery(database);
        DataCallback callback = Mockito.mock(DataCallback.class);

        // When
        ((ReadValueQuery) query.withArgs(String.class, callback)).execute();

        // Then
        Mockito.verify(callback, VerificationModeFactory.times(1)).onError(Mockito.nullable(Exception.class));
    }
}