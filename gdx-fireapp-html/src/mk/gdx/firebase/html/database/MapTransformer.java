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

package mk.gdx.firebase.html.database;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Transforms {@link java.util.Map} to JSON string.
 */
public class MapTransformer {

    private MapTransformer() {
        //
    }

    /**
     * @param map Map, not null
     * @return JSON representation of given map
     */
    public static String mapToJSON(Map<String, Object> map) {
        Json json = new Json();
        json.setTypeName(null);
        json.setQuoteLongValues(true);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);
        return json.toJson(map, HashMap.class);
    }
}
