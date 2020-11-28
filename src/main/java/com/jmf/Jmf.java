package com.jmf;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class Jmf {

    private JSONObject jsonObject;
    private List<Object> listOfJsonValues;
    private List<String> listOfJsonKeys;
    private JSONArray jsonArrayValues;
    private JSONObject jsonObjectValues;

    private BufferedReader getFileFromResources(URL file) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(Objects.requireNonNull(file).getFile())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bufferedReader;
    }

    public Jmf setJsonObject(URL file) {
        JSONTokener jsonTokener = null;
        try {
            jsonTokener = new JSONTokener(Objects.requireNonNull(getFileFromResources(file)));
        } catch (final NullPointerException ex) {
            log.error("Unable to read json file '{}'", file);
        }
        assert jsonTokener != null;
        jsonObject = new JSONObject(jsonTokener);
        return this;
    }

    public Jmf setJsonObject(String jsonObject) {
        this.jsonObject = new JSONObject(jsonObject);
        return this;
    }

    public Jmf updateJsonValueByKey(String concurrentKey, Object concurrentValue, Object newValue) {
        if (jsonObject != null) {
            updateJson(jsonObject, concurrentKey, concurrentValue, newValue);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return this;
    }

    /**
     * This method can update json array, but be sure that:
     * variable `concurrentValue` need to be a List converted to Object !!!
     * variable 'newValue' need to be List converted to Object !!!
     *
     * @param newValue = List<Object/>
     */
    public Jmf updateJsonArrayByKey(String concurrentKey, Object concurrentValue, Object newValue) {
        if (jsonObject != null) {
            updateJsonArray(jsonObject, concurrentKey, concurrentValue, newValue);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return this;
    }

    public Jmf updateJsonValueByBulkKeys(List<String> bulkOfKeys, Object concurrentValue, Object newValue) {
        bulkOfKeys.forEach(key -> updateJsonValueByKey(key, concurrentValue, newValue));
        return this;
    }

    public Jmf updateJsonValuesWithMap(List<Object[]> listOfKeyValues) {
        listOfKeyValues.forEach((val) -> updateJsonValueByKey(val[0].toString(), val[1], val[2]));
        return this;
    }

    public Jmf removeJsonAttributeByKeyValue(String concurrentKey, Object concurrentValue) {
        if (jsonObject != null) {
            removeJsonAttr(jsonObject, concurrentKey, concurrentValue);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return this;
    }

    public Jmf removeJsonAttributeWithObjectValue(String concurrentKey) {
        if (jsonObject != null) {
            removeJsonAttrWithJsonObjectValue(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return this;
    }

    public Jmf removeJsonAttributeWithArrayValue(String concurrentKey) {
        if (jsonObject != null) {
            removeJsonAttrWithJsonArrayValue(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return this;
    }

    public Jmf addJsonAttribute(String concurrentAttr, String newAttr, Object value) {
        if (jsonObject != null) {
            addJsonAttr(jsonObject, concurrentAttr, newAttr, value);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return this;
    }

    public Jmf addJsonAttributeToBaseAttr(String concurrentAttr, String newGroup, String newAttr, Object value) {
        if (jsonObject != null) {
            addJsonAttrToBaseAttr(jsonObject, concurrentAttr, newGroup, newAttr, value);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return this;
    }

    public String getJsonString() {
        if (jsonObject != null) {
            return jsonObject.toString(4);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
    }

    public Object getJsonValueByKey(String concurrentKey) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        //getting first value from json
        Object jsonValue;
        try {
            jsonValue = listOfJsonValues.get(0);
        } catch (IndexOutOfBoundsException e) {
            log.error("Can not be found json value by key {}", concurrentKey);
            throw new IndexOutOfBoundsException(String.format("Can not be found json value by key \"%s\" \n\r", concurrentKey)
                    .concat("From response: \n\r").concat(jsonObject.toString(4)));
        }
        return jsonValue;
    }

    public Object getJsonValueByKey(JSONObject jsonObject, String concurrentKey) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        //getting first value from json
        Object jsonValue;
        try {
            jsonValue = listOfJsonValues.get(0);
        } catch (IndexOutOfBoundsException e) {
            log.error("Can not be found json value by key {}", concurrentKey);
            throw new IndexOutOfBoundsException(String.format("Can not be found json value by key \"%s\" \n\r", concurrentKey)
                    .concat("From response: \n\r").concat(jsonObject.toString(4)));
        }
        return jsonValue;
    }

    public Object getJsonValueByKeyWithIndex(String concurrentKey, int index) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        //getting  value from json
        Object jsonValue;
        try {
            jsonValue = listOfJsonValues.get(index);
        } catch (IndexOutOfBoundsException e) {
            log.error("Can not be found json value by key {}", concurrentKey);
            throw new IndexOutOfBoundsException(String.format("Can not be found json value by key \"%s\" \n\r", concurrentKey)
                    .concat("From response: \n\r").concat(jsonObject.toString(4)));
        }
        return jsonValue;
    }

    public List<Object> getListOfJsonValuesByKey(String concurrentKey) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return listOfJsonValues;
    }

    public JSONArray getJsonArrayByKey(String key) {
        if (jsonObject != null) {
            jsonArrayValues = new JSONArray();
            getJsonArrayByKey(jsonObject, key);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return jsonArrayValues;
    }

    public JSONArray getJsonArrayByKey(JSONObject jsonObject, String key) {
        jsonArrayValues = new JSONArray();
        jsonArrayByKey(jsonObject, key);
        return jsonArrayValues;
    }

    public JSONObject getJsonObjectByKey(String key) {
        if (jsonObject != null) {
            jsonObjectValues = new JSONObject();
            getJsonObjectByKey(jsonObject, key);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        return jsonObjectValues;
    }

    public JSONObject getJsonObjectByKey(JSONObject jsonObject, String key) {
        jsonObjectValues = new JSONObject();
        jsonObjectByKey(jsonObject, key);
        return jsonObjectValues;
    }

    public Jmf assertJsonEqualsValueByKey(String concurrentKey, Object expectedValue) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        assertThat(listOfJsonValues.get(0)).as("Json key \"" + concurrentKey + "\" has not required value").isEqualTo(expectedValue);
        return this;
    }

    public Jmf assertJsonEqualsValueByKey(String concurrentKey, Object expectedValue, int index) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        assertThat(listOfJsonValues.get(index)).as("Json key \"" + concurrentKey + "\" has not required value").isEqualTo(expectedValue);
        return this;
    }

    /**
     * This assert compare values that should NOT be equals;
     * !!! @jsonObject need to be specified before !!!
     *
     * @param concurrentKey  = key to find value
     * @param expectedValues = values to compare
     */
    public Jmf assertJsonIsNotEqualsValueByKey(String concurrentKey, Object... expectedValues) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        Object actualResult = listOfJsonValues.get(0);
        for (Object res : expectedValues) {
            assertThat(actualResult).as("Json key \"" + concurrentKey + "\" has not required value").isNotEqualTo(res);
        }
        return this;
    }

    /**
     * This assert verifying if @jsonObject string value contains required string;
     * !!! @jsonObject need to be specified before !!!
     *
     * @param concurrentKey = key to find value
     * @param expectedValue = value to compare
     */
    public Jmf assertJsonContainsStringValueByKey(String concurrentKey, String expectedValue) {
        if (jsonObject != null) {
            listOfJsonValues = new ArrayList<>();
            listOfJsonValuesByKey(jsonObject, concurrentKey);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }
        assertThat(((String) listOfJsonValues.get(0))).contains(expectedValue);
        return this;
    }

    /**
     * This assert compares all keys in @jsonObject and @comparableJsonObject, that contain @concurrentValue;
     * !!! @jsonObject need to be specified before !!!
     *
     * @param comparableJsonObject = second json object that need to be compared with @jsonObject
     * @param concurrentValue      = keys comparison value
     * @param ignoreKeys           = specify keys that u want to be ignored for comparison
     */
    public Jmf assertJsonsMatchKeys(JSONObject comparableJsonObject, Object concurrentValue,
                                    List<String> ignoreKeys) {
        if (jsonObject != null) {
            listOfJsonKeys = new ArrayList<>();
            listOfJsonKeysByValue(jsonObject, concurrentValue);
            List<String> listOfKeysFromJsonObject = listOfJsonKeys;
            removeItemsFromList(ignoreKeys, listOfKeysFromJsonObject);
            Collections.sort(listOfKeysFromJsonObject);

            listOfJsonKeys = new ArrayList<>();
            listOfJsonKeysByValue(comparableJsonObject, concurrentValue);
            List<String> listOfKeysFromFirstJson = listOfJsonKeys;
            removeItemsFromList(ignoreKeys, listOfKeysFromFirstJson);
            Collections.sort(listOfKeysFromFirstJson);

            assertThat(listOfKeysFromFirstJson).as("List of keys from Response do not match with Json from string")
                    .isEqualTo(listOfKeysFromJsonObject);
        } else {
            throw new NullPointerException("!!! Specify @jsonObject !!!");
        }

        return this;
    }

    /**
     * This assert compares all keys in @firstJsonObject and @secondJsonObject, that contain @concurrentValue;
     *
     * @param firstJsonObject  = first json object that need to be compared
     * @param secondJsonObject = second json object that need to be compared with @jsonObject
     * @param concurrentValue  = keys comparison value
     * @param ignoreKeys       = specify keys that u want to be ignored for comparison
     */
    public Jmf assertJsonsMatchKeys(JSONObject firstJsonObject, JSONObject secondJsonObject, Object concurrentValue,
                                    List<String> ignoreKeys) {
        listOfJsonKeys = new ArrayList<>();
        listOfJsonKeysByValue(firstJsonObject, concurrentValue);
        List<String> listOfKeysFromResponse = listOfJsonKeys;
        removeItemsFromList(ignoreKeys, listOfKeysFromResponse);
        Collections.sort(listOfKeysFromResponse);

        listOfJsonKeys = new ArrayList<>();
        listOfJsonKeysByValue(secondJsonObject, concurrentValue);
        List<String> listOfKeysFromPayload = listOfJsonKeys;
        removeItemsFromList(ignoreKeys, listOfKeysFromPayload);
        Collections.sort(listOfKeysFromPayload);


        assertThat(listOfKeysFromPayload).as("List of keys from Response do not match with Json from string")
                .isEqualTo(listOfKeysFromResponse);
        return this;
    }

    public Object getValueFromJsonArrayByConcurrentKeyValue(JSONArray jsonArray, String desiredKey, String concurrentKey, Object concurrentValue) {
        Object requestedObject = "";
        for (Object s : jsonArray) {
            if (setJsonObject(s.toString()).getJsonValueByKey(concurrentKey).equals(concurrentValue)) {
                requestedObject = setJsonObject(s.toString()).getJsonValueByKey(desiredKey);
            }
        }
        return requestedObject;
    }

    public Boolean isAttributeAbsent(String concurrentKey) {
        return getListOfJsonValuesByKey(concurrentKey).size() == 0;
    }

    private JSONObject updateJson(JSONObject jsonObject, String concurrentKey, Object concurrentValue, Object newValue) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if ((jsonObject.optJSONObject(key) == null) && (jsonObject.optJSONArray(key) == null)) {
                if ((key.equals(concurrentKey)) && (jsonObject.get(key).equals(concurrentValue))) {
                    jsonObject.put(key, newValue);
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                updateJson(jsonObject.getJSONObject(key), concurrentKey, concurrentValue, newValue);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        updateJson(jsonArray.getJSONObject(i), concurrentKey, concurrentValue, newValue);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject listOfJsonValuesByKey(JSONObject jsonObject, String concurrentKey) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if ((jsonObject.optJSONObject(key) == null) && (jsonObject.optJSONArray(key) == null)) {
                if (key.equals(concurrentKey)) {
                    listOfJsonValues.add(jsonObject.get(key));
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                listOfJsonValuesByKey(jsonObject.getJSONObject(key), concurrentKey);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        listOfJsonValuesByKey(jsonArray.getJSONObject(i), concurrentKey);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject listOfJsonKeysByValue(JSONObject jsonObject, Object concurrentValue) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if ((jsonObject.optJSONObject(key) == null) && (jsonObject.optJSONArray(key) == null)) {
                if (jsonObject.get(key).equals(concurrentValue)) {
                    listOfJsonKeys.add(key);
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                listOfJsonKeysByValue(jsonObject.getJSONObject(key), concurrentValue);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        listOfJsonKeysByValue(jsonArray.getJSONObject(i), concurrentValue);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject updateJsonArray(JSONObject jsonObject, String concurrentKey, Object concurrentValue, Object newValue) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if ((jsonObject.optJSONObject(key) == null) && (jsonObject.optJSONArray(key) != null)) {
                if ((key.equals(concurrentKey))) {
                    if (convertJsonArrayToList((JSONArray) jsonObject.get(key)).equals(concurrentValue))
                        jsonObject.put(key, newValue);
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                updateJsonArray(jsonObject.getJSONObject(key), concurrentKey, concurrentValue, newValue);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        updateJsonArray(jsonArray.getJSONObject(i), concurrentKey, concurrentValue, newValue);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject jsonArrayByKey(JSONObject jsonObject, String concurrentKey) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (jsonObject.optJSONObject(key) == null) {
                if ((key.equals(concurrentKey)) && (jsonObject.optJSONArray(key) != null)) {
                    jsonArrayValues = (JSONArray) jsonObject.get(key);
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                jsonArrayByKey(jsonObject.getJSONObject(key), concurrentKey);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        jsonArrayByKey(jsonArray.getJSONObject(i), concurrentKey);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject jsonObjectByKey(JSONObject jsonObject, String concurrentKey) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (jsonObject.optJSONArray(key) == null) {
                if ((key.equals(concurrentKey)) && (jsonObject.optJSONObject(key) != null)) {
                    jsonObjectValues = (JSONObject) jsonObject.get(key);
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                jsonObjectByKey(jsonObject.getJSONObject(key), concurrentKey);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        jsonObjectByKey(jsonArray.getJSONObject(i), concurrentKey);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject removeJsonAttr(JSONObject jsonObject, String concurrentKey, Object concurrentValue) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if ((jsonObject.optJSONObject(key) == null) && (jsonObject.optJSONArray(key) == null)) {
                if ((key.equals(concurrentKey)) && (jsonObject.get(key).equals(concurrentValue))) {
                    jsonObject.remove(key);
                    iterator = jsonObject.keys();
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                removeJsonAttr(jsonObject.getJSONObject(key), concurrentKey, concurrentValue);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        removeJsonAttr(jsonArray.getJSONObject(i), concurrentKey, concurrentValue);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject removeJsonAttrWithJsonObjectValue(JSONObject jsonObject, String concurrentKey) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (jsonObject.optJSONArray(key) == null) {
                if ((key.equals(concurrentKey)) && (jsonObject.optJSONObject(key) != null)) {
                    jsonObject.remove(key);
                    iterator = jsonObject.keys();
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                removeJsonAttrWithJsonObjectValue(jsonObject.getJSONObject(key), concurrentKey);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        removeJsonAttrWithJsonObjectValue(jsonArray.getJSONObject(i), concurrentKey);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject removeJsonAttrWithJsonArrayValue(JSONObject jsonObject, String concurrentKey) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (jsonObject.optJSONObject(key) == null) {
                if ((key.equals(concurrentKey)) && (jsonObject.optJSONArray(key) != null)) {
                    jsonObject.remove(key);
                    iterator = jsonObject.keys();
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                removeJsonAttrWithJsonArrayValue(jsonObject.getJSONObject(key), concurrentKey);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        removeJsonAttrWithJsonArrayValue(jsonArray.getJSONObject(i), concurrentKey);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject addJsonAttr(JSONObject jsonObject, String concurrentAttr, String newAttr, Object value) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (key.equals(concurrentAttr)) {
                if (jsonObject.optJSONObject(key) != null) {
                    jsonObject.optJSONObject(key).put(newAttr, value);
                }
                if (jsonObject.optJSONArray(key) != null) {
                    jsonObject.optJSONArray(key).getJSONObject(0).put(newAttr, value);
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                addJsonAttr(jsonObject.getJSONObject(key), concurrentAttr, newAttr, value);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        addJsonAttr(jsonArray.getJSONObject(i), concurrentAttr, newAttr, value);
                    }
                }
            }
        }
        return jsonObject;
    }

    private JSONObject addJsonAttrToBaseAttr(JSONObject jsonObject, String concurrentAttr, String newGroup, String newAttr, Object value) {
        Iterator iterator = jsonObject.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if (key.equals(concurrentAttr)) {
                if (jsonObject.optJSONObject(key) != null) {
                    jsonObject.optJSONObject(key).put(newGroup, value);
                }
                if (jsonObject.optJSONObject(key) != null) {
                    jsonObject.optJSONObject(key).put(newAttr, value);
                }
                if (jsonObject.optJSONArray(key) != null) {
                    jsonObject.optJSONArray(key).getJSONObject(0).put(newAttr, value);
                }
            }

            if (jsonObject.optJSONObject(key) != null) {
                addJsonAttr(jsonObject.getJSONObject(key), concurrentAttr, newAttr, value);
            }

            if (jsonObject.optJSONArray(key) != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        addJsonAttr(jsonArray.getJSONObject(i), concurrentAttr, newAttr, value);
                    }
                }
            }
        }
        return jsonObject;
    }

    private void addJsonAttrToRoot(JSONObject jsonObject, String newAttr, Object value) {
        jsonObject.put(newAttr, value);
    }

    private void removeItemsFromList(List<String> keysToDelete, List<String> targetList) {
        keysToDelete.forEach(key -> {
            if (targetList.contains(key)) {
                targetList.remove(key);
            }
        });
    }

    private List<Object> convertJsonArrayToList(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        jsonArray.forEach(list::add);
        return list;
    }
}
