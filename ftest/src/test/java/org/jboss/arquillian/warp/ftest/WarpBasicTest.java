/**
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

import java.io.File;
import java.net.URL;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.ClientAction;
import org.jboss.arquillian.warp.ServerAssertion;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.extension.cdi.OnEvent;
import org.jboss.arquillian.warp.extension.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

/**
 * @author Lukas Fryc
 */
@RunWith(Arquillian.class)
@WarpTest
public class WarpBasicTest {

	@Drone
	WebDriver browser;

	@ArquillianResource
	URL contextPath;

	@Deployment
	public static WebArchive createDeployment() {

		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(TestingServlet.class, Foo.class)
				.addAsWebResource(new File("src/main/webapp/index.html"))
				.addAsWebInfResource("beans.xml");
	}

	@Test
	@RunAsClient
	public void test() {

		Warp.execute(new ClientAction() {
			public void action() {
				browser.navigate().to(contextPath + "index.html");
			}
		}).verify(new InitialRequestAssertion());

		final WebElement sendAjax = new WebDriverWait(browser, 60)
				.until(new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver input) {
						return browser.findElement(By.id("sendAjax"));
					}
				});

		Warp.execute(new ClientAction() {
			public void action() {
				sendAjax.click();
			}
		}).verify(new AjaxRequestAssertion());
	}

	public static class InitialRequestAssertion extends ServerAssertion {

		private static final long serialVersionUID = 1L;

		@BeforeServlet
		public void beforeServlet() {
			System.out.println("Hi server, here is my initial request!");
		}

	}

	public static class AjaxRequestAssertion extends ServerAssertion {

		private static final long serialVersionUID = 1L;

		@Inject
		BeanManager beanManager;

		@Inject
		Foo foo;

		@BeforeServlet
		public void beforeServlet() {
			System.out.println("Hi server, here is AJAX request!");
		}

		@OnEvent("first")
		public void observeFirstFooEvent() {
			System.out
					.println("Hi server, we're observing CDI event! Foo score: "
							+ foo.getScore());
		}
		
		@OnEvent("second")
		public void observeSecondFooEvent() {
			System.out
					.println("Hi server, we're observing CDI event! Foo score: "
							+ foo.getScore());
		}
	}

}
