/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.warp.ftest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.arquillian.warp.extension.cdi.WarpEvent;

@RequestScoped
public class Foo {
	
	@Inject
	private Event<WarpEvent> event;
	
	private int score = 0;
	
	public int getScore() {
		return score;
	}

	public void ping() {
		System.out.println("Fire new Warp event");
		score = 5;
		event.fire(new WarpEvent("first"));
		score = 10;
		event.fire(new WarpEvent("second"));
	}
	
}
