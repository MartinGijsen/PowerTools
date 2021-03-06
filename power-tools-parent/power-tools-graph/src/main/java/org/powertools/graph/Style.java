/* Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools.
 *
 * The PowerTools are free software: you can redistribute them and/or
 * modify them under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.graph;


public enum Style {
    DEFAULT   ("<default>"),
    SOLID     ("solid"),
    DASHED    ("dashed"),
    DOTTED    ("dotted"),
    BOLD      ("bold"),
    ROUNDED   ("rounded"),
    DIAGONALS ("diagonals"),
    INVISIBLE ("invis"),
    FILLED    ("filled");

    private final String text;

    private Style (String text) {
        this.text = text;
    }

    @Override
    public String toString () {
        return this.text;
    }
}
