/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.deployer.spi;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * {@code AppDeploymentId} is a representation constructed by a deployer based on a
 * {@link AppDeploymentRequest}. {@code AppDeploymentId} contains everything what
 * deployer needs to know order to either un-deploy or get a status of an app.
 *
 * Contract between {@link AppDeploymentRequest#getDeploymentProperties()} and
 * {{@link AppDeploymentId#getProperties()} is up to the deployer implementation to decide
 * and no other component should modify properties in this instance. For example, deployer
 * may use deployment properties passed via {@link AppDeploymentRequest} as a hint to do
 * something more clever and actual information needed for un-deploy or status would then
 * be available from properties in this class.
 *
 * @author Patrick Peralta
 * @author Mark Fisher
 * @author Janne Valkealahti
 */
public class AppDeploymentId implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The name of the associated group this app belongs to.
	 */
	private final String group;

	/**
	 * The name provided to uniquely identify the app within a group.
	 */
	private final String name;

	/**
	 * The runtime deployment properties for app.
	 */
	private final Map<String, String> properties;

	/**
	 * Instantiates a new app deployment id.
	 *
	 * @param group the group
	 * @param name the name
	 */
	public AppDeploymentId(String group, String name) {
		this(group, name, null);
	}

	/**
	 * Instantiates a new app deployment id.
	 *
	 * @param group the group
	 * @param name the name
	 * @param properties the properties
	 */
	public AppDeploymentId(String group, String name, Map<String, String> properties) {
		Assert.hasText(group);
		Assert.hasText(name);
		Assert.doesNotContain(group, ".");
		Assert.doesNotContain(name, ".");
		this.group = group;
		this.name = name;
		this.properties = properties == null
				? Collections.<String, String>emptyMap()
				: Collections.unmodifiableMap(properties);
	}

	/**
	 * Gets the group name.
	 *
	 * @return the group name
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Gets the app name.
	 *
	 * @return the app name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the deployment properties.
	 *
	 * @return the deployment properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AppDeploymentId that = (AppDeploymentId) o;
		return this.group.equals(that.group)
				&& this.name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = group.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	/**
	 * Return a string containing the ID fields separated by
	 * periods. This string may be used as a key in a database
	 * to uniquely identify an app.
	 *
	 * @return string representation of this ID
	 */
	@Override
	public String toString() {
		if (group == null) {
			return name;
		}
		return String.format("%s.%s", this.group, this.name);
	}
}
