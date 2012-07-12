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
package org.jboss.arquillian.warp.extension.cdi;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

import org.jboss.arquillian.core.spi.Manager;

/**
 * Regular CDI bean that observes {@link WarpEvent} and fires
 * {@link OnEventLifecycleEvent}.
 * 
 * @author Martin Kouba
 */
@RequestScoped
public class WarpEventObserver {

	private Manager manager;

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	/**
	 * 
	 * @param event
	 */
	public void observeWarpEvent(@Observes WarpEvent event) {
		manager.fire(new OnEventLifecycleEvent(event.getEventType()));
	}

}
