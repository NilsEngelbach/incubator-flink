/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.hadoopcompatibility.mapred.wrapper;

import java.util.Iterator;

import org.apache.flink.api.java.operators.translation.TupleUnwrappingIterator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.runtime.WritableSerializer;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


/**
 * Wraps a Flink Tuple2 (key-value-pair) iterator into an iterator over the second (value) field.
 */
@SuppressWarnings("rawtypes")
public class HadoopTupleUnwrappingIterator<KEY extends WritableComparable, VALUE extends Writable> 
									extends TupleUnwrappingIterator<VALUE, KEY> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Iterator<Tuple2<KEY,VALUE>> iterator;
	
	private final WritableSerializer<KEY> keySerializer;
	
	private boolean atFirst = false;
	private KEY curKey = null;
	private VALUE firstValue = null;
	
	public HadoopTupleUnwrappingIterator(Class<KEY> keyClass) {
		this.keySerializer = new WritableSerializer<KEY>(keyClass);
	}
	
	/**
	* Set the Flink iterator to wrap.
	* 
	* @param iterator The Flink iterator to wrap.
	*/
	@Override()
	public void set(final Iterator<Tuple2<KEY,VALUE>> iterator) {
		this.iterator = iterator;
		if(this.hasNext()) {
			final Tuple2<KEY, VALUE> tuple = iterator.next();
			this.curKey = keySerializer.copy(tuple.f0);
			this.firstValue = tuple.f1;
			this.atFirst = true;
		} else {
			this.atFirst = false;
		}
	}
	
	@Override
	public boolean hasNext() {
		if(this.atFirst) {
			return true;
		}
		return iterator.hasNext();
	}
	
	@Override
	public VALUE next() {
		if(this.atFirst) {
			this.atFirst = false;
			return firstValue;
		}
		
		final Tuple2<KEY, VALUE> tuple = iterator.next();
		return tuple.f1;
	}
	
	public KEY getCurrentKey() {
		return this.curKey;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
