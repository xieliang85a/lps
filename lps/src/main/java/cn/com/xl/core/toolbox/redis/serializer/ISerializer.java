/**
 * Copyright (c) 2011-2016, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.xl.core.toolbox.redis.serializer;

/**
 * ISerializer.
 */
public interface ISerializer {
	
    byte[] keyToBytes(String key);
    String keyFromBytes(byte[] bytes);
    
    byte[] fieldToBytes(Object field);
    Object fieldFromBytes(byte[] bytes);
    
	byte[] valueToBytes(Object value);
    Object valueFromBytes(byte[] bytes);
    
    public byte[] serialize(Object value);
    public Object deserialize(byte[] bytes);
    
    byte[] mergeBytes(final byte[] array1, final byte... array2);
}


