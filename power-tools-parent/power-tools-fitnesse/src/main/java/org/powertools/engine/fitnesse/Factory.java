/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.fitnesse;

import fitnesse.testsystems.Descriptor;
import fitnesse.testsystems.TestSystem;
import fitnesse.testsystems.TestSystemFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class Factory implements TestSystemFactory {
    @Override
    public TestSystem create (Descriptor descriptor) throws IOException {
        URLClassLoader classLoader = new URLClassLoader (getUrlsFromClassPath (descriptor), getClass ().getClassLoader ());
        return new PowerToolsTestSystem (classLoader);
    }

    private URL[] getUrlsFromClassPath (Descriptor descriptor) throws MalformedURLException {
        String[] paths = descriptor.getClassPath ().split (System.getProperty ("path.separator"));
        URL[] urls     = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
            urls[i] = new File (paths[i]).toURI ().toURL ();
        }
        return urls;
    }
}
