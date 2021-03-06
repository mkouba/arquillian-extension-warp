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
package org.jboss.arquillian.warp.client.proxy;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.warp.client.execution.RequestEnrichmentFilter;
import org.jboss.arquillian.warp.client.execution.ResponseFilterMap;
import org.littleshoot.proxy.DefaultHttpProxyServer;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpRequestFilter;

/**
 * The holder for instantiated proxies.
 *
 * @author Lukas Fryc
 *
 */
public class ProxyHolder {

    private Map<URL, HttpProxyServer> servers = new HashMap<URL, HttpProxyServer>();

    public void startProxyForUrl(URL proxyUrl, URL realUrl) {

        if (servers.containsKey(proxyUrl)) {
            return;
        }

        String hostPort = realUrl.getHost() + ":" + realUrl.getPort();
        HttpRequestFilter requestFilter = new RequestEnrichmentFilter();
        ResponseFilterMap responseFilter = new ResponseFilterMap(hostPort);

        HttpProxyServer server = new DefaultHttpProxyServer(proxyUrl.getPort(), responseFilter, hostPort, null, requestFilter);
        server.start();

        servers.put(proxyUrl, server);
    }

    public void freeAllProxies() {
        for (HttpProxyServer server : servers.values()) {
            server.stop();
        }
        servers.clear();
    }
}
